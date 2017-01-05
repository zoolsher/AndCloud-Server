package com.safecode.andcloud.util.screentouch;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认转换器，不进行转换
 *
 * @author sumy
 */
public class DefaultInstructionParser implements InstructionParser {
    @Override
    public List<String> parse(String instruction) {
        List<String> instructions = new ArrayList<>();
        instructions.add(instruction);
        return instructions;
    }
}
