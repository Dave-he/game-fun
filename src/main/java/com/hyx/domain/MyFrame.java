package com.hyx.domain;

import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class MyFrame extends JFrame {
    public static void main(String[] args) {
        new MyFrame();
    }


    private MyPanel myPanel;
    private static Point lastPoint = null;
    private static StringBuilder keyScore = new StringBuilder();

    private LinkedList<Region> regionList = new LinkedList<>();
    public MyFrame() {
        this.setSize(500, 800);
        this.setLocationRelativeTo(null);//窗口置于屏幕中央
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击窗口的关闭按钮时，程序关闭
        //否则程序仍然在后台运行
        this.setLayout(new BorderLayout());//创建一个新的流布局管理器，
//        setUndecorated(true);
//        setBackground(new Color(255, 255, 255, 125));


        addClearButton();
        addConfirmButton();
        mousePress();
        keyPress();
        myPanel = new MyPanel();
        add(myPanel, BorderLayout.CENTER);
        this.setVisible(true);
        this.setFocusable(true);
    }

    private void addClearButton(){
        JButton b = new JButton("清空");
        b.setSize(10,5);
        b.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regionList.clear();
            }
        });
        this.add(b, BorderLayout.NORTH);
    }

    private void addConfirmButton(){
        JButton b = new JButton("确认");
        b.setSize(10,5);
        b.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Region region : regionList) {
                    System.out.println(region);
                }

                JButton source = (JButton) e.getSource();
                myPanel = new MyPanel();
                source.add(myPanel);
                System.out.println("已保存");
            }
        });
        this.add(b, BorderLayout.SOUTH);
    }

    private void mousePress(){
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (lastPoint == null) {
                    lastPoint = e.getPoint();
                    return;
                }
                regionList.add(new Region(lastPoint, e.getPoint()));

                lastPoint = null;
                System.out.println(e.getPoint());
            }
        });
    }

    private void keyPress(){
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                System.out.println(keyChar);
                if(keyScore.length() > 3){
                    keyScore.setLength(0);
                }
                if (keyChar <= '9' && keyChar >= '0') {
                    keyScore.append(keyChar);
                    System.out.println(keyScore);
                }
                if (keyChar == '\n') {
                    regionList.getLast().setScore(keyScore.length() >0 ? Integer.parseInt(keyScore.toString()): null);
                    keyScore.setLength(0);

                    System.out.println(regionList.getLast());
                    myPanel.drawReact(regionList);
                }
            }
        });
    }



}

@Data
class MyPanel extends JPanel{

    private LinkedList<Region> regionList = new LinkedList<>();

    public MyPanel() {
        this.setSize(500, 700);
        this.setBackground(new Color(128,255,128, 30));
        this.setLocation(0,0);
        this.setVisible(true);
    }

    public void drawReact(LinkedList<Region> regionList) {
        this.regionList = regionList;
        this.repaint();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.red);
        for (Region region : regionList) {
            Rectangle r = region.getRectangle();
            g.drawRect(r.x, r.y, r.width, r.height);
            g.drawString(String.valueOf(region.getScore()), r.x + r.width / 2, r.y + r.height / 2);
        }
    }
}
