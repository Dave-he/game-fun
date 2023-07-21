package com.hyx;


import com.hyx.domain.Card;
import com.hyx.domain.Screen;
import com.hyx.util.ClickUtil;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    /**
     * 主入口
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        Screen screen = new Screen(robot);
        Map<String, Integer> map = new HashMap<String, Integer>();
        Manager.init("./img/card");
        boolean first = true;
        while (true) {
            List<Card> cards = screen.getScreen();
            if (cards.isEmpty()) {
                System.out.println("====== 重新开始 ======");
                screen.processRestart();
                first = true;
                continue;
            }

            if (first && cards.size() == 9) {
                System.out.println("====== 第一关 ======");
                first = false;
                screen.processFirst();
            } else {
                screen.processSecond();
            }

            Thread.sleep(2000);
        }
    }

}
