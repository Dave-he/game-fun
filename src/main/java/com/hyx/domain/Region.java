package com.hyx.domain;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import lombok.Data;

@Data
public class Region {

    private Rectangle rectangle;

    private Integer score;

    public Region(Point start, Point end, Integer score) {
        this.score = score;

        this.rectangle = new Rectangle(
                Math.min(start.getLocation().x, end.getLocation().x),
                Math.min(start.getLocation().y, end.getLocation().y),
                Math.abs(start.getLocation().x - end.getLocation().x),
                Math.abs(start.getLocation().y - end.getLocation().y)
        );
    }

    private static Point lastPoint = null;
    private static int lastScore = 0;



    /**
     * 测试鼠标位置
     */
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("test");
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        MyJPanel jPanel = new MyJPanel();
        frame.add(jPanel);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (lastPoint == null) {
                    lastPoint = e.getPoint();
                }
                Region region = new Region(lastPoint, e.getPoint(), lastScore++);
                System.out.println(region);
                Rectangle re = region.getRectangle();

                jPanel.paintImmediately(re);
                jPanel.repaint();

            }
        });
        while (true) {
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            System.out.println(pointerInfo.getLocation());
            Thread.sleep(2000);
        }

    }

}

class MyJPanel extends JPanel{
    @Override
    public void paint(Graphics g) {
        super.paint(g);
//        g.setColor(Color.RED);
//        g.drawRect(re.x, re.y, re.width, re.height);
    }
}