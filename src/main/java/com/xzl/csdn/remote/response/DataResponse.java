package com.xzl.csdn.remote.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class DataResponse {

    private String zone;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date time;

    private List<DataTransfer> data;

    public void addDataTransfer(DataTransfer transfer){
        data.add(transfer);
    }

    private static DataResponse getDefaultResponse(String areaCode){
        DataResponse dataResponse = new DataResponse();
        dataResponse.setTime(new Date());
        dataResponse.setZone(areaCode);
        dataResponse.setData(new ArrayList<>());
        return dataResponse;
    }
    public static DataResponse getDataResponse(Object obj,String areaCode){
        DataResponse response = getDefaultResponse(areaCode);
        if(obj==null){
            return response;
        }
        if(obj instanceof Map){
            Iterator its = ((Map) obj).keySet().iterator();
            while(its.hasNext()){
                Object key  =its.next();
                response.addDataTransfer(new DataTransfer(key.toString(),((Map)obj).get(key)));
            }

        }else{
            Class userCla = (Class) obj.getClass();
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                // 设置些属性是可以访问的
                f.setAccessible(true);
                Object val = new Object();
                try {
                    val = f.get(obj);
                    if(val!=null){
                        Object tempVal = val;
                        if(val instanceof Date){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            tempVal = sdf.format(val);
                        }
                        response.addDataTransfer(new DataTransfer(f.getName(),tempVal));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
    public static DataResponse getDataResponse(Object obj){
        return getDataResponse(obj,"330100000000");
    }

}
