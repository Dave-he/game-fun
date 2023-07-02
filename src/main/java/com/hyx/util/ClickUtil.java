package com.hyx.util;

import com.hyx.domain.Screen;

import java.awt.*;
import java.awt.event.InputEvent;

public class ClickUtil {


    public static void click(Robot robot, int x, int y) {
        robot.mouseMove(x, y);
        robot.delay(40);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(30);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(200);
    }

}
