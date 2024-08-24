package core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class GraphicalInterface {
    static final int SMALLFONT = 15;
    static final int BIGFONT = 30;
    static final int SMALLWAIT = 50;
    static final float HALF = (float) 1 / 2;
    static final float HALFF = (float) 4 / 10;
    static final float HALFM = (float) 6 / 10;
    static final float TITLE = (float) 4 / 5;
    static final int CANVASSIZE = 16;
    public static void drawMainMenu() {
        Font small = new Font("Monaco", Font.BOLD, SMALLFONT);
        Font big = new Font("Monaco", Font.BOLD, BIGFONT);
        StdDraw.setFont(small);
        StdDraw.setCanvasSize(Main.WIDTH * CANVASSIZE, Main.HEIGHT * CANVASSIZE);
        StdDraw.setXscale(0, Main.WIDTH);
        StdDraw.setYscale(0, Main.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();
        StdDraw.text(Main.WIDTH / 2, Main.HEIGHT * HALFM, "New Game (N)");
        StdDraw.text(Main.WIDTH / 2, Main.HEIGHT * HALF, "Load Game (L)");
        StdDraw.text(Main.WIDTH / 2, Main.HEIGHT * HALFF, "Quit (Q)");
        StdDraw.setFont(big);
        StdDraw.text(Main.WIDTH / 2, Main.HEIGHT * TITLE, "CS61BL: The Game");
        StdDraw.show();
    }
    public static void drawMiddle(String s) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, BIGFONT);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(Main.WIDTH / 2, Main.HEIGHT / 2, s);
        StdDraw.show();
    }
    public static String waitForInput() {
        while (true) {
            char c;
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                c = Character.toLowerCase(c);
                if (c == 'n') {
                    StdDraw.clear();
                    return gatherSeed();
                } else if (c == 'l') {
                    return "load";
                } else if (c == 'q') {
                    System.exit(0);
                }
            }
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                System.out.println("x: " + x + " y: " + y);
                if (Math.abs(Main.WIDTH / 2 - x) < 5) {
                    //It is centered enough
                    if (Math.abs(Main.HEIGHT * HALFM - y) < 1) {
                        StdDraw.clear();
                        return gatherSeed();
                    } else if (Math.abs(Main.HEIGHT * HALF - y) < 1) {
                        return "load";
                    } else if (Math.abs(Main.HEIGHT * HALFF - y) < 1 && Math.abs(Main.WIDTH / 2 - x) < 2) {
                        System.exit(0);
                    }
                }
            }
            StdDraw.pause(SMALLWAIT);
        }


    }
    private static String gatherSeed() {
        String answer = "";
        while (true) {
            char c;
            if (StdDraw.hasNextKeyTyped()) {
                c = StdDraw.nextKeyTyped();
                c = Character.toLowerCase(c);
                if (c == 's' || c == '\n') {
                    break;
                } else if (c == '\b') {
                    if (!answer.isEmpty()) {
                        answer = answer.substring(0, answer.length() - 1);
                    }
                } else {
                    answer += c;
                }
            }
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                System.out.println("x: " + x + " y: " + y);
                if (Math.abs(Main.WIDTH / 2 - x) < 2 + answer.length()) {
                    //It is centered enough
                    if (Math.abs(Main.HEIGHT * HALF - y) < 1) {
                        break;
                    }
                }
            }
            drawMiddle("Seed: " + answer);
            StdDraw.pause(SMALLWAIT);
        }
        return answer;
    }
}
