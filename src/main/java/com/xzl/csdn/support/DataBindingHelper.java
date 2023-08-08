package com.xzl.csdn.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import javax.servlet.ServletRequest;
import java.util.*;

public class DataBindingHelper {

    private ConversionService conversionService;

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public DataBinderBuilder initBinder(Object target) {
        return new DataBinderBuilder(target);
    }

    public class DataBinderBuilder {
        private DataBinder dataBinder;

        DataBinderBuilder(Object target) {
            dataBinder = new DataBinder(target);
            if (conversionService != null) {
                dataBinder.setConversionService(conversionService);
            }
        }

        private boolean denyDisallowed = false;
        private boolean requiredOnly = false;
        private String prefix;

        public boolean isRequiredOnly() {
            return requiredOnly;
        }

        /**
         * 是否为只需要 require 指定的字段
         *
         * @param requiredOnly
         */
        public DataBinderBuilder setRequiredOnly(boolean requiredOnly) {
            this.requiredOnly = requiredOnly;
            return this;
        }

        public String getPrefix() {
            return prefix;
        }

        public boolean isDenyDisallowed() {
            return denyDisallowed;
        }

        public DataBinder getDataBinder() {
            return dataBinder;
        }

        public DataBinderBuilder setAllowedFields(String... fields) {
            dataBinder.setAllowedFields(fields);
            return this;
        }

        public DataBinderBuilder setDisallowedFields(String... fields) {
            dataBinder.setDisallowedFields(fields);
            return this;
        }

        public DataBinderBuilder setRequiredFields(String... fields) {
            dataBinder.setRequiredFields(fields);
            return this;
        }

        public DataBinderBuilder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public DataBinderBuilder setValidator(Validator validator) {
            dataBinder.setValidator(validator);
            return this;
        }

        public DataBinderBuilder setDenyDisallowed() {
            this.denyDisallowed = true;
            return this;
        }

        private RequestDataBinder build() {
            return new RequestDataBinder(this);
        }

        public void doBind(ServletRequest request) {
            build().doBind(request);
        }
    }


    private class RequestDataBinder {
        private boolean denyDisallowed;
        private boolean requiredOnly;
        private String prefix;
        private DataBinder dataBinder;

        private List<String> requiredFields;

        RequestDataBinder(DataBinderBuilder dataBinderBuilder) {
            this.denyDisallowed = dataBinderBuilder.isDenyDisallowed();
            this.requiredOnly = dataBinderBuilder.isRequiredOnly();
            this.prefix = dataBinderBuilder.getPrefix();

            this.dataBinder = dataBinderBuilder.getDataBinder();
            this.requiredFields = Arrays.asList(dataBinder.getRequiredFields());
        }

        public void doBind(ServletRequest request) {

            MutablePropertyValues propertyValues = getProperties(prefix, request);


            String[] dsafs = dataBinder.getDisallowedFields();

            if (denyDisallowed && dsafs.length > 0) {
                for (String name : dsafs) {
                    if (propertyValues.contains(name)) {
                        request.setAttribute("dataBinding.status", ApiStatus.INTERNAL_DATA_BINGING.getStatus());
                        request.setAttribute("dataBinding.error.message",
                                String.format("Param [%s%s] is not allowed",
                                        (prefix == null ? "" : prefix), name));
                        throw new RuntimeException();// go to errorPage
                    }
                }
            }

            dataBinder.bind(propertyValues);

            if (dataBinder.getValidator() != null) {
                dataBinder.validate();
            }

            BindingResult bindingResult = dataBinder.getBindingResult();
            if (bindingResult.hasErrors()) {
                request.setAttribute("dataBinding.status", ApiStatus.INTERNAL_DATA_BINGING.getStatus());
                request.setAttribute("dataBinding.error.message", getErrorMessage(bindingResult));
                throw new RuntimeException(bindingResult.getAllErrors().toString());
            }
        }

        private String getErrorMessage(BindingResult errors) {
            if (errors == null) {
                return null;
            }
            List<ObjectError> allErrors = errors.getAllErrors();
            if (allErrors.isEmpty()) {
                return null;
            } else {
                ObjectError e = allErrors.get(0);

                String errorMessage = "Data binding error!";
                switch (e.getCode()) {
                    case "required":
                        errorMessage = String.format(
                                "Param [%s%s] is needed",
                                (prefix == null ? "" : prefix),
                                getField(e, "arguments[0].code"));
                        break;

                    case "typeMismatch":
                        errorMessage = String.format(
                                "Param [%s%s] with value [%s] can not convert to type %s",
                                (prefix == null ? "" : prefix),
                                getField(e, "arguments[0].code"),
                                getField(e, "rejectedValue"),
                                getField(e, "codes[2].substring(13)")
                        );
                        break;
                }

                return errorMessage;
            }
        }


        private MutablePropertyValues getProperties(String prefix, ServletRequest request) {
            Map<String, String> all = getPropertyMap(request);
            MutablePropertyValues values = new MutablePropertyValues();

            for (String name : all.keySet()) {
                String newName = name;
                if (StringUtils.isNotEmpty(prefix)) {
                    if (name.startsWith(prefix)) {
                        newName = name.replace(prefix, "");
                    }
                }

                if (!(requiredOnly && !requiredFields.contains(newName))) {
                    values.add(newName, all.get(name));
                }
            }

            return values;
        }

        private Map<String, String> getPropertyMap(ServletRequest request) {
            Map<String, String[]> paraMap = request.getParameterMap();
            Map<String, String> result = new HashMap<>();
            Enumeration<String> attrName = request.getAttributeNames();
            while (attrName.hasMoreElements()) {
                String name = attrName.nextElement();
                result.put(name, request.getAttribute(name).toString());
            }

            for (String k : paraMap.keySet()) {
                String[] vs = paraMap.get(k);
                result.put(k, vs.length > 0 ? vs[0] : null);
            }


            return result;
        }
    }


    /**
     * 在对象上执行 spring el-express 以拿到字段值
     *
     * @param target    对象实例
     * @param elExpress
     * @return
     */
    public static Object getField(Object target, String elExpress) {
        //System.out.println(JSONObject.toJSONString(target));
        //System.out.println(target.getClass().getTypeName());
        if (target instanceof Map) return ((Map) target).get(elExpress);

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(elExpress);
        EvaluationContext context = new StandardEvaluationContext(target);

        return exp.getValue(context);
    }

}
