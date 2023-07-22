package com.hyx.domain;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {

    private Rectangle rectangle;

    private Integer score;

    public Region(Point start, Point end) {
        this.rectangle = new Rectangle(
                Math.min(start.getLocation().x, end.getLocation().x),
                Math.min(start.getLocation().y, end.getLocation().y),
                Math.abs(start.getLocation().x - end.getLocation().x),
                Math.abs(start.getLocation().y - end.getLocation().y)
        );
    }

}
