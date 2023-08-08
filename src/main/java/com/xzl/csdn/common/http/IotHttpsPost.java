package com.xzl.csdn.common.http;

import com.alibaba.fastjson.JSONObject;
import com.xzl.csdn.common.http.response.AqtResponse;
import com.xzl.csdn.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 物联网
 *
 * @author tfDeng
 * @description:
 * @date 2020/12/17
 **/
@Slf4j
@Component
public class IotHttpsPost {

    private static String systemName;

    @Value("${csdn.system.name}")
    public void setSystemName(String systemName) {
        IotHttpsPost.systemName = systemName;
    }

    /**
     *      * 获得KeyStore.
     *      * @param keyStorePath
     *      *            密钥库路径
     *      * @param password
     *      *            密码
     *      * @return 密钥库
     *
     * @throws Exception
     */
    public static KeyStore getKeyStore(String password, String keyStorePath)
            throws Exception {
        log.info("keyStorePath = {}", keyStorePath);
        // 实例化密钥库
        KeyStore ks = KeyStore.getInstance("JKS");

        InputStream is = FileUtil.class.getClassLoader().getResourceAsStream(keyStorePath);

        // 加载密钥库
        ks.load(is, password.toCharArray());
        // 关闭密钥库文件流
        is.close();
        return ks;
    }

    /**
     *      * 获得SSLSocketFactory.      * @param password      *           
     * 密码      * @param keyStorePath      *            密钥库路径      * @param
     * trustStorePath      *            信任库路径      * @return
     * SSLSocketFactory      * @throws Exception     
     */
    public static SSLContext getSSLContext(String password) throws Exception {
        // 实例化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        // 获得密钥库
        KeyStore keyStore = getKeyStore(password, "config/" + systemName + "/ssl_client.jks");
        // 初始化密钥工厂
        keyManagerFactory.init(keyStore, password.toCharArray());

        // 实例化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // 获得信任库
        KeyStore trustStore = getKeyStore(password, "config/" + systemName + "/ssl2_client.jks");
        // 初始化信任库
        trustManagerFactory.init(trustStore);
        // 实例化SSL上下文
        SSLContext ctx = SSLContext.getInstance("TLS");
        // 初始化SSL上下文
        ctx.init(keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(), null);
        // 获得SSLSocketFactory
        return ctx;
    }

    /**
     *      * 初始化HttpsURLConnection.
     *      * @param password
     *      *            密码
     *      * @param keyStorePath
     *      *            密钥库路径
     *      * @param trustStorePath
     *      *            信任库路径
     *      * @throws Exception
     *     
     */
    public static SSLContext initHttpsURLConnection(String password) throws Exception {
        // 声明SSL上下文
        SSLContext sslContext = null;

        try {
            sslContext = getSSLContext(password);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    /**
     *      * 发送请求.
     *
     * @param      * @param httpsUrl
     *                *            请求的地址
     *                * @param param
     *                *            请求的数据
     *               
     */
    public static String post(String httpsUrl, String jsonStr, String token) {
        // 密码
        String password = "MyPassword";
        // 传输文本
        StringBuffer sb = null;
        try {
            HttpURLConnection urlCon = null;

            if (httpsUrl.startsWith("https")) {
                SSLContext sslContext = initHttpsURLConnection(password);
                HttpsURLConnection urlConHttps = (HttpsURLConnection) (new URL(httpsUrl)).openConnection();
                if (sslContext != null) {
                    urlConHttps.setSSLSocketFactory(sslContext.getSocketFactory());
                }
                urlCon = urlConHttps;
            } else {
                urlCon = (HttpURLConnection) new URL(httpsUrl).openConnection();
            }

            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-Length", Objects.isNull(jsonStr) ? "0" :
                    String.valueOf(jsonStr.getBytes().length));
            urlCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (StringUtils.isNotBlank(token)) {
                urlCon.setRequestProperty("Authorization", "Bearer " + token);
            }
            urlCon.setUseCaches(false);
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            //设置为gbk可以解决服务器接收时读取的数据中文乱码问题
            if (Objects.nonNull(jsonStr)) {
                urlCon.getOutputStream().write(jsonStr.getBytes("utf-8"));
            }
            urlCon.getOutputStream().flush();
            urlCon.getOutputStream().close();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlCon.getInputStream()));
            String line;
            sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            log.error("调用物联网接口失败", e);
        }
        log.info("调用物联网接口:" + sb.toString());
        return sb.toString();
    }

    public static String get(String httpsUrl, List<String> fieldNames, List<String> jsonStrs, String token, int count) {
        // 密码
        String password = "MyPassword";
        // 传输文本
        StringBuffer sb = null;
        HttpsURLConnection urlCon = null;
        BufferedReader in = null;
        InputStream is = null;
        try {
            SSLContext sslContext = initHttpsURLConnection(password);
            List<String> urlParams = new ArrayList<>();
            for (int i = 0; i < fieldNames.size(); i++) {
                String tempParam = fieldNames.get(i) + "=" + URLEncoder.encode(jsonStrs.get(i), "UTF-8");
                urlParams.add(tempParam);
            }
            String usrParam = String.join("&", urlParams);
            log.info("iot get url = {}", httpsUrl + "?" + usrParam);
            urlCon = (HttpsURLConnection) (new URL(httpsUrl + "?" + usrParam).openConnection());
            if (sslContext != null) {
                urlCon.setSSLSocketFactory(sslContext.getSocketFactory());
            }
            urlCon.setRequestMethod("GET");

            /** 设置通用的请求属性 */
            urlCon.setRequestProperty("accept", "*/*");
            urlCon.setRequestProperty("connection", "Keep-Alive");
            urlCon.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式
            urlCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置连接超时时间s
            urlCon.setConnectTimeout(15000);
            //设置读取超时时间
            urlCon.setReadTimeout(15000);
            if (StringUtils.isNotBlank(token)) {
                urlCon.setRequestProperty("Authorization", "Bearer " + token);
            }
            urlCon.connect();
            is = urlCon.getInputStream();
            in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            if ("connect timed out".equals(e.getMessage())) {
                if (count == 2) {
                    log.info("调用物联网接口超时!");
                    AqtResponse aqtResponse = new AqtResponse();
                    aqtResponse.setCode(500);
                    aqtResponse.setMessage("调用物联网接口超时!");
                    return JSONObject.toJSONString(aqtResponse);
                }
                get(httpsUrl, fieldNames, jsonStrs, token, 2);
            }
            log.error("调用物联网接口失败{}", e.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (urlCon != null) {
                //请求结束后关闭HTTP链接
                urlCon.disconnect();
            }
        }
        log.info("调用物联网接口:" + sb.toString());
        return sb.toString();
    }
}
