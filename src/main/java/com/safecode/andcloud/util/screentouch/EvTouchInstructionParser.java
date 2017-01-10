package com.safecode.andcloud.util.screentouch;

import java.util.ArrayList;
import java.util.List;

/**
 * EvTouch设备指令转换
 *
 * @author sumy
 */
public class EvTouchInstructionParser implements InstructionParser {

    private int screenwidth;
    private int screenheight;
    private int wmwidth;
    private int wmheight;
    private int maxxpixel;
    private int maxypixel;

    private int pixelwidth;
    private int pixelheight;
    private int pixelstartx;
    private int pixelstarty;

    public EvTouchInstructionParser(int screenwidth, int screenheight, int wmwidth, int wmheight, int maxxpixel, int maxypixel) {
        this.screenwidth = screenwidth;
        this.screenheight = screenheight;
        this.wmwidth = wmwidth;
        this.wmheight = wmheight;
        this.maxxpixel = maxxpixel;
        this.maxypixel = maxypixel;

        if (1.0 * this.screenwidth / this.screenheight > 1.0 * this.wmwidth / this.wmheight) {
            // height 充满
            this.pixelheight = maxypixel;
            this.pixelstarty = 0;
            this.pixelwidth = maxxpixel * this.screenwidth / this.wmwidth;
            this.pixelstartx = (maxxpixel - this.pixelwidth) / 2;
        } else {
            // width 充满
            this.pixelwidth = maxxpixel;
            this.pixelstartx = 0;
            this.pixelheight = maxypixel * this.screenheight / this.wmheight;
            this.pixelstarty = (maxypixel - this.pixelheight) / 2;
        }
    }

    @Override
    public List<String> parse(String instruction) {
        List<String> instructions = new ArrayList<String>();
        String[] insts = instruction.split(",");
        double x = Double.parseDouble(insts[1]);
        double y = Double.parseDouble(insts[2]);
        // 3 0 x 移动坐标x
        // 3 1 y 移动坐标y
        instructions.add("3 0 " + (int) (x * this.pixelwidth + this.pixelstartx));
        instructions.add("3 1 " + (int) (y * this.pixelheight + this.pixelstarty));
        switch (insts[0]) {
            case "DOWN":
                // 1 272 1 鼠标左键按下
                instructions.add("1 272 1");
                break;
            case "UP":
                // 1 272 0 鼠标左键松开
                instructions.add("1 272 0");
                break;
            case "MOVE":
                // pass
                break;
            default:
                return instructions;
        }
        // 0 0 0 提交
        instructions.add("0 0 0");
        return instructions;
    }
}
