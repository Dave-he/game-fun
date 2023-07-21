package com.hyx.domain;

import com.hyx.Manager;
import com.hyx.util.ClickUtil;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Screen {

    private Rectangle baseRect;
    private Robot robot;
    private List<Card> cardList;
    private List<Region> regionList;

    public Screen(Robot robot) {
        this.robot = robot;
        init(robot);
    }

    @Override
    public String toString() {
        return baseRect.toString();
    }

    private void clickCard(Card card) {
        ClickUtil.click(robot, card.getX() + baseRect.x, card.getY() + baseRect.y);
    }

    /**
     * 第一关所有点击2遍
     */
    public void processFirst() {
        for (int i = 0; i < 2; i++) {
            cardList.forEach(this::clickCard);
            robot.delay(1000);
        }
        robot.delay(2000);
    }


    /**
     * 第二关按照区域分值排序点击
     */
    public void processSecond() {
        Map<String, Integer> map = new HashMap<>();
        boolean click = false;
        for (Card card : cardList) {
            Integer num = map.getOrDefault(card.getType(), 0);
            num++;
            map.put(card.getType(), num);
            if (num == 3) {
                for (Card card1 : cardList) {

                    if (card1.getType().equals(card.getType())) {
                        clickCard(card1);
                        num--;
                    }
                    if (num == 0 || card1.equals(card)) {
                        map.put(card.getType(), 0);
                        click = true;
                        break;
                    }
                }
            }
            if(click){
                break;
            }
        }
    }

    /**
     * 从右边区域获取游戏界面
     */
    public void init(Robot robot) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int location = 2 * d.width / 3;
        baseRect = new Rectangle(location, 0, location / 2, d.height);
        BufferedImage sc = robot.createScreenCapture(baseRect);
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (int x = 0; x < sc.getWidth(); x++) {
            for (int y = 0; y < sc.getHeight(); y++) {
                int rgb = sc.getRGB(x, y);
                if (rgb == 0) continue;
                if (!isBackground(rgb)) {
                    continue;
                }
                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
            }
        }
        baseRect.setLocation(location + minX, minY);
        baseRect.setSize(maxX - minX, maxY - minY);
        if (baseRect.width < 500 && baseRect.height < 500) {
            throw new RuntimeException("未获取到游戏界面区域");
        }
    }

    /**
     * 判断颜色
     */
    private static boolean isBackground(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        return 170 < r && r < 210 && 230 < g && 110 < b && b < 140;
    }

    /**
     * 裁剪
     *
     * @return
     * @throws Exception
     */
    public List<Card> getScreen() {
        System.out.printf("屏幕位置:%d,%d\n", baseRect.x, baseRect.y);
        BufferedImage screenCapture = robot.createScreenCapture(baseRect);
        List<Card> cards = Manager.numIslands(screenCapture);

        this.cardList = cards;
        this.save(screenCapture);
        return cards;
    }

    public void save(BufferedImage sc) {
        try {
            ImageIO.write(sc, "png", new File("./img/b_" + System.currentTimeMillis() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 重新开局
     */
    public void processRestart() {
        ClickUtil.click(robot, (baseRect.width / 2) + baseRect.x, 765 + baseRect.y);
        robot.delay(2000);
        ClickUtil.click(robot, (baseRect.width / 2) + baseRect.x, 880 + baseRect.y);
        robot.delay(3000);
    }
}
