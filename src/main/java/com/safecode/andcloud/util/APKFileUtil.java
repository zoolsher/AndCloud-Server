package com.safecode.andcloud.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * 操作文件
 *
 * @author sumy
 */
public class APKFileUtil {

    /**
     * 将IMG文件转换成base64编码表示方式
     *
     * @param subfix 扩展名，文件格式
     * @param image  图片输入流
     * @return Base64+格式表示的图片编码
     */
    public static String imgToBase64Code(String subfix, InputStream image) {
        try {
            byte[] imagedata = IOUtils.toByteArray(image);
            String base64string = Base64.encodeBase64String(imagedata);
            return "data:image/" + subfix.toLowerCase() + ";base64," + base64string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        File file = new File("/home/sumy/application_icon.png");
        String subfix = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1);
        try {
            InputStream inputStream = new FileInputStream(file);
            System.out.println(imgToBase64Code(subfix, inputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
