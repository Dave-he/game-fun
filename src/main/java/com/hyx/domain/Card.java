package com.hyx.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.awt.*;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Card {
    /**
     * 类型
     */
    private String type;

    /**
     * 原始值
     */
    private List<Double> origin;

    /**
     * 相对水平坐标
     */
    private Integer x;

    /**
     * 相对垂直坐标
     */
    private Integer y;

    /**
     * 图上区域
     */
    private Rectangle rectangle;

    /**
     * 分数
     */
    private Integer score;

    /**
     * hash
     */
    private String hash;


    public Card() {
        rectangle = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
    }

    public Card(String type, Integer x, Integer y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public Card(String type, List<Double> picArrayData) {
        this.type = type;
        this.origin = picArrayData;
    }

    /**
     * 判断颜色
     */
    public static boolean inArea(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return b < r && b < g - 25 && (190 < r && 195 < g && 160 < b && b < 215);
    }

    public void updateArea(int r, int c) {
        rectangle.setLocation(Math.min(rectangle.x, r), Math.min(rectangle.y, c));
        rectangle.setSize(Math.max(rectangle.width, r - rectangle.x), Math.max(rectangle.height, c - rectangle.y));
        x = rectangle.x + rectangle.width / 2;
        y = rectangle.y + rectangle.height / 2;
    }

    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
