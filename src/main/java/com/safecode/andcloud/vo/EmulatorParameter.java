package com.safecode.andcloud.vo;

/**
 * 存储模拟器的基本参数
 *
 * @author sumy
 */
public class EmulatorParameter {

    private int screenwidth;
    private int screenheight;
    private int wmwidth;
    private int wmheight;

    private int maxxpixel;
    private int maxypixel;

    public EmulatorParameter(int screenwidth, int screenheight, int wmwidth, int wmheight, int maxxpixel, int maxypixel) {
        this.screenwidth = screenwidth;
        this.screenheight = screenheight;
        this.wmwidth = wmwidth;
        this.wmheight = wmheight;
        this.maxxpixel = maxxpixel;
        this.maxypixel = maxypixel;
    }

    public int getScreenwidth() {
        return screenwidth;
    }

    public int getScreenheight() {
        return screenheight;
    }

    public int getWmwidth() {
        return wmwidth;
    }

    public int getWmheight() {
        return wmheight;
    }

    public int getMaxxpixel() {
        return maxxpixel;
    }

    public int getMaxypixel() {
        return maxypixel;
    }
}
