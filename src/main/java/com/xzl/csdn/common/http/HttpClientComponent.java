package com.xzl.csdn.common.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
public class HttpClientComponent {

    @Autowired
    private HttpConnectionManager connManager;

    public String doHttpPostRequest(String url, Map<String, String> paramsMap) {
        String returnStr = "";
        /* 创建HttpClient实例 */
        // HttpClient httpclient = HttpClientBuilder.create().build();
        CloseableHttpClient httpclient = connManager.getHttpClient();

        /* 创建一个Post方法 */
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if (paramsMap != null) {
            for (Iterator<String> it = paramsMap.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();
                String value = paramsMap.get(key);
                log.info("{}={}", key, value);
                params.add(new BasicNameValuePair(key, value));
            }
        }

        /* 设置重连次数 */
        int reConnTimes = 1;

        /* 设置连接超时时间 */
        int connTimeoutSecond = 10;
        int socketTimeoutSecond = 30;

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeoutSecond * 1000)
                .setConnectTimeout(connTimeoutSecond * 1000).build();
        httpPost.setConfig(requestConfig);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }

        boolean success = false;
        for (int i = 0; i < reConnTimes; i++) {
            CloseableHttpResponse response = null;
            try {
                log.info("POST " + url);
                response = httpclient.execute(httpPost);
                success = response.getStatusLine().getStatusCode() == 200;
                log.info("status code:" + response.getStatusLine().getStatusCode());
                if (success) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instreams = entity.getContent();
                        returnStr = convertStreamToString(instreams);
                        if (returnStr.length() > 500) {
                            log.info("return str:It's too long to print");
                        } else {
                            log.info("return str:" + returnStr);
                        }
                        instreams.close();
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                httpPost.releaseConnection();
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            if (success) {
                break;
            }
        }
        return returnStr;
    }

    public String doHttpGetRequest(String url, Map<String, String> headers) {
        String returnStr = "";
        /* 创建HttpClient实例 */
        CloseableHttpClient httpclient = connManager.getHttpClient();

        /* 创建一个Get方法 */
        HttpGet httpGet = new HttpGet(url);

        //        httpGet.abort();

        /* 设置重连次数 */
        int reConnTimes = 1;

        /* 设置连接超时时间 */
        int connTimeoutSecond = 10;
        int socketTimeoutSecond = 10;

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeoutSecond * 1000)
                .setConnectTimeout(connTimeoutSecond * 1000).build();
        httpGet.setConfig(requestConfig);
        if (headers != null) {
            for (String headerKey : headers.keySet()) {
                httpGet.setHeader(headerKey, headers.get(headerKey));
            }
        }
        boolean success = false;
        for (int i = 0; i < reConnTimes; i++) {
            CloseableHttpResponse response = null;
            try {
                log.info("GET " + url);
                response = httpclient.execute(httpGet);
                success = response.getStatusLine().getStatusCode() == 200;
                log.info("status code:" + response.getStatusLine().getStatusCode());
                if (success) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instreams = entity.getContent();
                        returnStr = convertStreamToString(instreams);
                        log.info("return str:" + returnStr);
                    }
                } else {
                    httpGet.abort();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                httpGet.releaseConnection();
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            if (success) {
                break;
            }
        }
        return returnStr;

    }

    /**
     * post调用方式
     *
     * @param url
     *            请求地址
     * @param paramJson
     *            请求的json参数
     * @return
     */
    public String doHttpPostRequest(String url, String paramJson, RequestHeader... headers) {
        Map<String, Object> paramMap = JSONObject.parseObject(paramJson, Map.class);
        Map<String, String> headerMap = new HashMap<>();
        if (headers != null) {
            for (RequestHeader header : headers) {
                headerMap.put(header.getKey(), header.getValue());
            }
        }
        return doHttpPostRequest(url, paramMap, headerMap);
    }


    /**
     *  post调用方式
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     */
    public String doHttpPostRequest(String url, Map<String, Object> paramMap, Map<String, String> headerMap) {
        log.debug(JSONObject.toJSONString(paramMap));
        String returnStr = "";
        CloseableHttpClient httpclient = connManager.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        /* 设置重连次数 */
        int reConnTimes = 1;

        /* 设置连接超时时间 */
        int connTimeoutSecond = 10;
        int socketTimeoutSecond = 10;

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeoutSecond * 1000)
                .setConnectTimeout(connTimeoutSecond * 1000).build();
        httpPost.setConfig(requestConfig);
        // 添加http headers
        if (headerMap != null && headerMap.size() > 0) {
            for (String key : headerMap.keySet()) {
                httpPost.setHeader(key, headerMap.get(key));
            }
        }
        try {
            StringEntity se = new StringEntity(JSONObject.toJSONString(paramMap), "UTF-8");
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
            httpPost.setEntity(se);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        boolean success = false;
        for (int i = 0; i < reConnTimes; i++) {
            CloseableHttpResponse response = null;
            try {
                log.info("POST " + url);
                response = httpclient.execute(httpPost);
                success = response.getStatusLine().getStatusCode() == 200;
                log.info("status code:" + response.getStatusLine().getStatusCode());
                //兼容社区治理平台接口
                if(response.getStatusLine().getStatusCode() == 401){
                    return String.valueOf(response.getStatusLine().getStatusCode());
                }
                if (success) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instreams = entity.getContent();
                        returnStr = convertStreamToString(instreams);
                        if (returnStr.length() > 500) {
                            log.info("return str:It's too long to print");
                        } else {
                            log.info("return str:" + returnStr);
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                httpPost.releaseConnection();
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            if (success) {
                break;
            }
        }
        return returnStr;
    }


    public String convertStreamToString(InputStream is) {
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((line = reader.readLine()) != null) {
                if (sb.length() == 0) {
                    sb.append(line);
                } else {
                    sb.append("\n" + line);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return sb.toString();
    }

    /**
     * 描述：将json转化成get的请求参数
     * @author ljf
     * @date 2018/10/25 15:19
     */
    public static String changeJsonToArguments(JSONObject argument) {
        Set<String> keys = argument.keySet();
        for (String key : keys) {
            String value = argument.getString(key);
            argument.put(key, urlEncoderText(value));
        }
        String jsonStr = argument.toString();
        String paramStr =
                "?" + jsonStr.substring(1, jsonStr.length() - 1).replace(",", "&").replace(":", "=").replace("\"", "");
        return paramStr;
    }

    /**
     * 描述：url转码,常用与网络
     * @author ljf
     * @date 2018/10/25 15:21
     */
    public static String urlEncoderText(String text) {
        String result = "";
        try {
            result = java.net.URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }


    //	private static PoolingHttpClientConnectionManager connMgr;
    //	private static RequestConfig requestConfig;
    //	private static final int MAX_TIMEOUT = 20000;
    //
    //	static {
    //		// 设置连接池
    //		connMgr = new PoolingHttpClientConnectionManager();
    //		// 设置连接池大小
    //		connMgr.setMaxTotal(100);
    //		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
    //
    //		RequestConfig.Builder configBuilder = RequestConfig.custom();
    //		// 设置连接超时
    //		configBuilder.setConnectTimeout(MAX_TIMEOUT);
    //		// 设置读取超时
    //		configBuilder.setSocketTimeout(MAX_TIMEOUT);
    //		// 设置从连接池获取连接实例的超时
    //		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
    //		// 在提交请求之前 测试连接是否可用
    //		configBuilder.setStaleConnectionCheckEnabled(true);
    //		requestConfig = configBuilder.build();
    //	}

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     *
     * @param url
     * @param headers
     *            需要添加的httpheader参数
     * @return
     */
    public String doGet(String url, Map<String, String> headers, Map<String, Object> params) {

        if (url.startsWith("https://")) {
            return doGetSSL(url, headers, params);
        } else {
            return doGetHttp(url, headers, params);
        }

    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url
     * @param headers
     *            需要添加的httpheader参数
     * @param params
     * @return
     */
    public String doGetHttp(String url, Map<String, String> headers, Map<String, Object> params) {
        //		HttpClient httpclient = new DefaultHttpClient();
        HttpClient httpclient = HttpClientBuilder.create().build();
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if(url.contains("?")){
                param.append("&");
                param.append(key).append("=").append(params.get(key));
                i++;
            }else {
                if (i == 0) {
                    param.append("?");
                } else {
                    param.append("&");
                }
                param.append(key).append("=").append(params.get(key));
                i++;
            }
        }
        apiUrl += param;
        String result = null;
        apiUrl = apiUrl.replaceAll(" ", "%20");
        log.info("url: " + apiUrl);
        try {
            HttpGet httpGet = new HttpGet(apiUrl);
            // 添加http headers
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httpGet.addHeader(key, headers.get(key));
                }
            }

            HttpResponse response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            log.info("code : " + statusCode);
            //兼容社区治理平台接口
            if(response.getStatusLine().getStatusCode() == 401){
                return String.valueOf(response.getStatusLine().getStatusCode());
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送 SSL Get 请求（HTTPS），K-V形式
     *
     * @param
     * @param headers
     * @param params
     *            参数map
     * @return
     */
    public String doGetSSL(String url, Map<String, String> headers, Map<String, Object> params) {
        //		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
        //				.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        CloseableHttpClient httpClient = connManager.getHttpClient();
        CloseableHttpResponse response = null;
        String apiUrl = url;
        String httpStr = null;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if(url.contains("?")){
                param.append("&");
                param.append(key).append("=").append(params.get(key));
                i++;
            }else {
                if (i == 0) {
                    param.append("?");
                } else {
                    param.append("&");
                }
                param.append(key).append("=").append(params.get(key));
                i++;
            }
        }
        apiUrl += param;
        apiUrl = apiUrl.replaceAll(" ", "%20");
        log.info("url: " + apiUrl);

        try {
            HttpGet httpGet = new HttpGet(apiUrl);

            /* 设置连接超时时间 */
            int connTimeoutSecond = 10;
            int socketTimeoutSecond = 30;

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeoutSecond * 1000)
                    .setConnectTimeout(connTimeoutSecond * 1000).build();

            httpGet.setConfig(requestConfig);
            // 添加http headers
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("code : " + statusCode);
            //兼容社区治理平台接口
            if(response.getStatusLine().getStatusCode() == 401){
                return String.valueOf(response.getStatusLine().getStatusCode());
            }
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;

    }

    public String uploadFile(String url, MultipartFile file, String fileParamName, Map<String, Object> paramMap,
                             Map<String, String> headerParams) {
        String flag = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(10000)
                    .setConnectTimeout(5000).build();
            HttpPost httpPost = new HttpPost(url);
//			String boundary = "--------------4585696313564699";
//			httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
            //添加header
            for (Map.Entry<String, String> e : headerParams.entrySet()) {
                if("content-type".equals(e.getKey())){
                    continue;
                }
                httpPost.addHeader(e.getKey(), e.getValue());
            }

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            // 解决中文文件名乱码问题
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.setCharset(Consts.UTF_8);
            entityBuilder.addBinaryBody(fileParamName, file.getInputStream(), ContentType.DEFAULT_BINARY,
                    file.getOriginalFilename());
            if (paramMap != null) {
                for (Map.Entry<String, Object> e : paramMap.entrySet()) {
                    entityBuilder.addTextBody(e.getKey(), e.getValue().toString());// 类似浏览器表单提交，对应input的name和value
                }
            }
            httpPost.setEntity(entityBuilder.build());
            httpPost.setConfig(requestConfig);
            HttpResponse execute = httpclient.execute(httpPost);
            flag = EntityUtils.toString(execute.getEntity());
            return flag;
        } catch (IOException e) {
            log.error("上传失败：" + e);
        }
        return flag;
    }
}
