package com.xzl.csdn.auth.codeVerification;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

/**
 * @author: liupu
 * @description:
 * @date: 2021/8/9
 */
@Data
public class ImageCode implements Serializable {
    private static final long serialVersionUID = -7092816917239421679L;
    private transient BufferedImage image;
    private String code;
    static Random random = new Random();
    /**
     * @param image
     * @param code
     */
    public ImageCode(BufferedImage image, String code) {
        this.image = image;
        this.code = code;
    }


    public static ImageCode createImageCode() {
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();


        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        StringBuilder code =new StringBuilder(64);
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            code.append(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }

        g.dispose();
        return new ImageCode(image, code.toString());
    }

    /**y
     * 生成随机背景条纹
     *
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandColor(int fc, int bc) {

        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}