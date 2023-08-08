package com.xzl.csdn.domain.query;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.*;

@Data
public class WxbErrorTrack implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private String errorNo;

    private byte[] geo;  //List<String>  lon,lat

    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getErrorNo() {
        return errorNo;
    }

    public void setErrorNo(String errorNo) {
        this.errorNo = errorNo == null ? null : errorNo.trim();
    }

    public byte[] getGeo() {
        return geo;
    }

    public void setGeo(byte[] geo) {
        this.geo = geo;
    }

    public WxbErrorTrack(String errorNo) {
        this.errorNo = errorNo;
    }

    public WxbErrorTrack() {
    }

    /**
     * 将对象转化为字节数组
     */
    public byte[] objectToBytes(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        byte[] bytes = baos.toByteArray();
        baos.close();
        oos.close();
        return bytes;
    }

    /**
     * 将字节数组转化为对象
     */
    @SuppressWarnings("unchecked")
    public <T> T bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object object = ois.readObject();
        bais.close();
        ois.close();
        return (T) object;
    }
}