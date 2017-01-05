package com.safecode.andcloud.util.screentouch;

import java.util.List;

/**
 * 屏幕控制信息从前端到模拟器转换
 *
 * @author sumy
 */
public interface InstructionParser {
    List<String> parse(String instruction);
}
