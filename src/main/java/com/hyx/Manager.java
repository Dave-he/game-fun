package com.hyx;

import com.hyx.domain.Card;
import com.hyx.util.ImageUtil;
import com.hyx.util.MD5Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Manager {

    private static List<Card> allCards = new ArrayList<>();
    private static final double RATE = 0.988;

    public static void main(String[] args) {
        init("./img/card");
        try {
            BufferedImage read = ImageIO.read(new File("./img/1.png"));
            List<Double> picArrayData = MD5Util.getPicArrayData(read);
//            String dHash = ImageUtil.getDHash(read);
            for (Card curr : allCards) {
//                long hammingDistance = ImageUtil.getHammingDistance(dHash, curr.getHash());
//                System.out.println(curr + "------------ " + dHash + "haming :" + hammingDistance);
//

                double pearsonDim = MD5Util.getPearsonDim(curr.getOrigin(), picArrayData);

                if (RATE < pearsonDim) {
                    System.out.println(pearsonDim);
                    System.out.println(curr);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init(String path) {
        File file = new File(path);
        file.mkdirs();
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File file1 : files) {
            try {
                BufferedImage read = ImageIO.read(file1);
                Card card = new Card(file1.getName().split("\\.")[0], MD5Util.getPicArrayData(read));
//                card.setHash(ImageUtil.getDHash(read));
                allCards.add(card);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public static List<Card> numIslands(BufferedImage grid) {
        if (grid == null || grid.getWidth() == 0 || grid.getHeight() == 0) {
            return new ArrayList<>();
        }
        int num = 0;
        List<Card> cards = new ArrayList<>();
        for (int r = 0; r < grid.getWidth(); ++r) {
            for (int c = 0; c < grid.getHeight(); ++c) {
                if (Card.inArea(grid.getRGB(r, c))) {
                    Card card = new Card();
                    long dfs = dfs(grid, r, c, card);
                    if (1000 < dfs && dfs < 3500) {
                        ++num;
                        Rectangle rc = card.getRectangle();
                        BufferedImage image = grid.getSubimage(rc.x, rc.y, rc.width, rc.height);
                        image = MD5Util.zoom(image, 150);
//                        String s = null;
//                        try{
//                            s = instance.doOCR(image).trim();
//                        }catch (TesseractException e){
//                            e.printStackTrace();
//                        }
//                        String s = MD5Util.imageToBytes(image);
                        card.setOrigin(MD5Util.getPicArrayData(image));
//                        card.setHash(ImageUtil.getDHash(image));
                        boolean find = false;
                        for (Card curr : allCards) {
                            double pearsonDim = MD5Util.getPearsonDim(curr.getOrigin(), card.getOrigin());
//                            long hammingDistance = ImageUtil.getHammingDistance(curr.getHash(), card.getHash());
//                            if(hammingDistance < 10){
                            if (RATE < pearsonDim) {
                                find = true;
                                card.setType(curr.getType());
                                break;
                            }
                        }
                        if (!find) {
                            card.setType(String.valueOf(System.currentTimeMillis()));
                            System.out.println("未找到，新增card" + card);
                            allCards.add(card);
                            BufferedImage finalImage = image;
                            new Thread(() -> {
                                try {
                                    boolean png = ImageIO.write(finalImage, "png", new File(String.format("./img/card/%s.png", card.getType())));

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }).start();

                        }
                        cards.add(card);
                    }
                }
            }
        }
        System.out.printf("卡片数量:%d\n", num);
        return cards;
    }


    private static long dfs(BufferedImage grid, int r, int c, Card card) {
        int nr = grid.getWidth();
        int nc = grid.getHeight();

        if (r < 0 || c < 0 || r >= nr || c >= nc || !Card.inArea(grid.getRGB(r, c))) {
            return 0;
        }

        card.updateArea(r, c);
        grid.setRGB(r, c, 0);
        return 1 +
                dfs(grid, r - 1, c, card) +
                dfs(grid, r + 1, c, card) +
                dfs(grid, r, c - 1, card) +
                dfs(grid, r, c + 1, card) +
                dfs(grid, r - 1, c - 1, card) +
                dfs(grid, r + 1, c + 1, card) +
                dfs(grid, r - 1, c - 1, card) +
                dfs(grid, r + 1, c - 1, card) +
                dfs(grid, r - 1, c + 1, card);
    }
}
