package com.hyx.util;

import java.awt.*;
import java.awt.image.BufferedImage;


public class ImageUtil {
    /**
     * 计算dHash方法
     *
     * @return hash
     */
    public static String getDHash(BufferedImage srcImage) {
        BufferedImage buffImg = new BufferedImage(25, 4, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(srcImage.getScaledInstance(25, 24, Image.SCALE_SMOOTH), 0, 0, null);

        int width = buffImg.getWidth();
        int height = buffImg.getHeight();
        int[][] grayPix = new int[width][height];
        StringBuffer figure = new StringBuffer();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //图片灰度化
                int rgb = buffImg.getRGB(x, y);
                int r = rgb >> 16 & 0xff;
                int g = rgb >> 8 & 0xff;
                int b = rgb & 0xff;
                int gray = (r * 30 + g * 59 + b * 11) / 100;
                grayPix[x][y] = gray;

                //开始计算dHash 总共有9*8像素 每行相对有8个差异值 总共有 8*8=64 个
                if (x != 0) {
                    long bit = grayPix[x - 1][y] > grayPix[x][y] ? 1 : 0;
                    figure.append(bit);
                }
            }
        }

        return figure.toString();
    }

    /**
     * 计算海明距离
     * <p>
     * 原本用于编码的检错和纠错的一个算法
     * 现在拿来计算相似度，如果差异值小于一定阈值则相似，一般经验值小于5为同一张图片
     *
     * @param str1
     * @param str2
     * @return 距离
     */
    public static long getHammingDistance(String str1, String str2) {
        int distance;
        if (str1 == null || str2 == null || str1.length() != str2.length()) {
            distance = -1;
        } else {
            distance = 0;
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) != str2.charAt(i)) {
                    distance++;
                }
            }
        }
        return distance;
    }

}
