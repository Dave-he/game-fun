package com.hyx.domain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Region {

    private Rectangle rectangle;

    private Integer score;

    private Boolean enable;

    /**
     * 测试鼠标位置
     */
    public static void main(String[] args) throws Exception {
        while (true){
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            System.out.println(pointerInfo.getLocation());
            Thread.sleep(2000);
        }

    }

}
