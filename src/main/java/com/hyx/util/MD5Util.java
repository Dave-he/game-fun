package com.hyx.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MD5Util {

    public static String md5(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] bs = digest.digest(bytes);
            StringBuilder hexString = new StringBuilder();
            for (byte b : bs) {
                int temp = b & 255;
                if (temp < 16) {
                    hexString.append("0").append(Integer.toHexString(temp));
                } else {
                    hexString.append(Integer.toHexString(temp));
                }
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 转换BufferedImage 数据为String
     */
    public static String imageToBytes(BufferedImage bImage) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "png", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return md5(out.toByteArray());
    }

    /**
     * 按照高度进行缩放
     *
     * @param bufferedImage 内存图片
     * @param targetHeight  目标高度
     * @return 内存图片
     */
    public static BufferedImage zoom(BufferedImage bufferedImage, int targetHeight) {
        // 获取原始图片的宽度和高度
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // 计算目标图片的宽度
        int targetWidth = width * targetHeight / height;
        // 创建新的内存图片
        BufferedImage newBufferedImage = new BufferedImage(targetWidth, targetHeight, bufferedImage.getType());
        // 创建画图对象
        Graphics2D graphics = newBufferedImage.createGraphics();
        // 获取缩略图片并画到画布上
        graphics.drawImage(bufferedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH), 0, 0, null);
        // 关闭画图对象
        graphics.dispose();
        return newBufferedImage;
    }

    /**
     * 可选值为：1,2,4,8,16,32
     * 当值为64时会抛出异常，此时需要实现64位转10进制
     * radix 64 greater than Character.MAX_RADIX
     */
    public static int compareLevel = 4;
    public static int DIFF = 20;

//    public static void main(String[] args) throws IOException {
//        final String pic1Path = Objects.requireNonNull(Calculate.class.getClassLoader().getResource("pic1.jpeg")).getPath();
//        final String pic2Path = Objects.requireNonNull(Calculate.class.getClassLoader().getResource("pic2.jpeg")).getPath();
//        final List<Double> origin = getPicArrayData(pic1Path);
//        System.out.println(origin);
//        final List<Double> after = getPicArrayData(pic2Path);
//        System.out.println(after);
//        System.out.println(PearsonDemo.getPearsonBydim(origin, after));
//    }

    public static double getPearsonDim(List<Double> ratingOne, List<Double> ratingTwo) {
        try {
            if (ratingOne.size() != ratingTwo.size()) {//两个变量的观测值是成对的，每对观测值之间相互独立。
                if (ratingOne.size() > ratingTwo.size()) {//保留小的处理大
                    List<Double> temp = ratingOne;
                    ratingOne = new ArrayList<>();
                    for (int i = 0; i < ratingTwo.size(); i++) {
                        ratingOne.add(temp.get(i));
                    }
                } else {
                    List<Double> temp = ratingTwo;
                    ratingTwo = new ArrayList<>();
                    for (int i = 0; i < ratingOne.size(); i++) {
                        ratingTwo.add(temp.get(i));
                    }
                }
            }
            double sim = 0D;//最后的皮尔逊相关度系数
            double commonItemsLen = ratingOne.size();//操作数的个数
            double oneSum = 0D;//第一个相关数的和
            double twoSum = 0D;//第二个相关数的和
            double oneSqSum = 0D;//第一个相关数的平方和
            double twoSqSum = 0D;//第二个相关数的平方和
            double oneTwoSum = 0D;//两个相关数的乘积和
            for (int i = 0; i < ratingOne.size(); i++) {//计算
                double oneTemp = ratingOne.get(i);
                double twoTemp = ratingTwo.get(i);
                //求和
                oneSum += oneTemp;
                twoSum += twoTemp;
                oneSqSum += Math.pow(oneTemp, 2);
                twoSqSum += Math.pow(twoTemp, 2);
                oneTwoSum += oneTemp * twoTemp;
            }
            double num = (commonItemsLen * oneTwoSum) - (oneSum * twoSum);
            double den = Math.sqrt((commonItemsLen * oneSqSum - Math.pow(oneSum, 2)) * (commonItemsLen * twoSqSum - Math.pow(twoSum, 2)));
            sim = (den == 0) ? 1 : num / den;
            return sim;
        } catch (Exception e) {
            return 0D;
        }
    }


    public static List<Double> getPicArrayData(BufferedImage image) {

        //初始化集合
        final List<Double> picFingerprint = new ArrayList<>(compareLevel * compareLevel * compareLevel);
        IntStream.range(0, compareLevel * compareLevel * compareLevel).forEach(i -> {
            picFingerprint.add(i, 0.0);
        });
        //遍历像素点
        for (int i = DIFF; i < image.getWidth() - DIFF; i++) {
            for (int j = DIFF; j < image.getHeight() - DIFF; j++) {
                int rgb = image.getRGB(i, j);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                //对像素点进行计算
                putIntoFingerprintList(picFingerprint, r, g, b);
            }
        }

        return picFingerprint;
    }

    /**
     * 放入像素的三原色进行计算，得到List的位置
     *
     * @param picFingerprintList picFingerprintList
     * @param r                  r
     * @param g                  g
     * @param b                  b
     * @return
     */
    public static List<Double> putIntoFingerprintList(List<Double> picFingerprintList, int r, int g, int b) {
        //比如r g b是126, 153, 200 且 compareLevel为16进制，得到字符串：79c ,然后转10进制，这个数字就是List的位置
        final Integer index = Integer.valueOf(getBlockLocation(r) + getBlockLocation(g) + getBlockLocation(b), compareLevel);
        final Double origin = picFingerprintList.get(index);
        picFingerprintList.set(index, origin + 1);
        return picFingerprintList;
    }

    /**
     * w
     * 计算 当前原色应该分在哪个区块
     *
     * @param colorPoint colorPoint
     * @return
     */
    public static String getBlockLocation(int colorPoint) {
        return IntStream.range(0, compareLevel)
                //以10进制计算分在哪个区块
                .filter(i -> {
                    int areaStart = (256 / compareLevel) * i;
                    int areaEnd = (256 / compareLevel) * (i + 1) - 1;
                    return colorPoint >= areaStart && colorPoint <= areaEnd;
                })
                //如果compareLevel大于10则转为对应的进制的字符串
                .mapToObj(location -> compareLevel > 10 ? Integer.toString(location, compareLevel) : String.valueOf(location))
                .findFirst()
                .orElseThrow(null);
    }

}
