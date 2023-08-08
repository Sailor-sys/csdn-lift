package com.xzl.csdn.utils;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

/**
 * @author：lianp
 * @description：
 * @date：11:17 2018/8/8
 */
public class FileUtil {

    public static String getPostfix(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return null;
        }
        String postfix = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
        return postfix.toLowerCase();
    }

    public static boolean checkIsImage(String fileType) {
        switch (fileType) {
            case "jpg":
                return true;
            case "JPG":
                return true;
            case "jpeg":
                return true;
            case "JPEG":
                return true;
            case "png":
                return true;
            case "PNG":
                return true;
            case "gif":
                return true;
            default:
                return false;
        }
    }

    public static String readFile(String uri) {
        String result = "";
        String wrap = getSystem();
        InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(uri);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(in, "GB2312"));

            for (String read = null; (read = br.readLine()) != null; result = result + read + wrap) {
                ;
            }
        } catch (Exception var14) {
            //log.err("readFile error", var14);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException var13) {
                //log.error("readFile error", var13);
            }

        }

        return result;
    }

    public static String getSystem() {
        String wrap = "";
        String system = System.getProperty("os.name");
        if (system.contains("Windows")) {
            wrap = "\r\n";
        } else if (system.contains("Linux")) {
            wrap = "\n";
        } else {
            wrap = "\r";
        }

        return wrap;
    }

    /**
     * 服务器下载文件
     * @param strUrl
     * @return
     */
    public static InputStream getInputStreamByUrl(String strUrl) throws Exception {
		return new ByteArrayInputStream(getByteByUrl(strUrl));
    }

	public static byte[] getByteByUrl(String strUrl) throws Exception {
		HttpURLConnection conn = null;
		try {
			strUrl = strUrl.replace(" ", "%20");
			URL url = new URL(strUrl);
			if("https".equalsIgnoreCase(url.getProtocol())){
				trustAllHttpsCertificates();
			}
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(20 * 1000);
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			IOUtils.copy(conn.getInputStream(), output);
			return output.toByteArray();
		} finally {
			try {
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Exception e) {
			}
		}
	}

	private static void trustAllHttpsCertificates() throws Exception {
		SSLContext sc = SSLContext.getInstance("SSL");
		TrustManager tm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		sc.init(null, new TrustManager[]{tm}, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}
}
