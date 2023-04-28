package com.example;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TessactOCR {
    @Test
    public void Test1() throws TesseractException, IOException {
        Tesseract iTesseract = new Tesseract();
        //设置tessdata训练库语言包地址，项目根目录下为默认地址可不设置
        iTesseract.setDatapath("D:\\1_Java-Total package-after10_27\\various-App\\NotOftenUse\\Tesseract\\Tess4J-3.4.8-src\\Tess4J\\tessdata");
        //默认识别英文
        //如果需要识别英文之外的语种，需要指定识别语种，并且需要将对应的语言包放进项目中
        iTesseract.setLanguage("chi_sim");

        //iTesseract.setLanguage("ghy");

        // 指定本地图片
        BufferedImage img = ImageIO.read(new File("D:\\1_Java-Total package-after10_27\\various-App\\NotOftenUse\\Tesseract\\Tarin\\Train_image\\1680271358284.png"));

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                int gray = (int) (0.299 * ((rgb >> 16) & 0xFF) + 0.587 * ((rgb >> 8) & 0xFF) + 0.114 * (rgb & 0xFF));
                img.setRGB(x, y, (gray << 16) | (gray << 8) | gray);
            }
        }

        int threshold = 128;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                int gray = (int) (0.299 * ((rgb >> 16) & 0xFF) + 0.587 * ((rgb >> 8) & 0xFF) + 0.114 * (rgb & 0xFF));
                if (gray > threshold) {
                    img.setRGB(x, y, 0xFFFFFF);
                } else {
                    img.setRGB(x, y, 0x000000);
                }
            }
        }
        ImageIO.write(img, "png", new File("D:\\1_Java-Total package-after10_27\\java-note\\图片资源\\图片处理结果\\1680271388700.jpg"));


        //转为png文件
        //convertPng(img);
        //开始识别时间
        long startTime = System.currentTimeMillis();
        //识别结果
        String ocrResult = iTesseract.doOCR(img);

        String dictFilePath = "D:\\1_Java-Total package-after10_27\\various-App\\NotOftenUse\\Tesseract\\Tarin\\dictionary.txt";
        Set<Character> dictSet = new HashSet<>();
        try (FileReader reader = new FileReader(dictFilePath)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                char[] chars = line.toCharArray();
                for (char c : chars) {
                    dictSet.add(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String ocrResult1 = "Tesseract OCR 输出的结果字符串";
        StringBuffer buffer = new StringBuffer(ocrResult.length());
        for (int i = 0; i < ocrResult.length(); i++) {
            char c = ocrResult.charAt(i);
            if (dictSet.contains(c)) {
                buffer.append(c);
            }
        }
        String resultWithoutRare = buffer.toString();

        // 假设“output”是Tesseract的结果字符串
        //String result = ocrResult.replaceAll("[^(\\u4e00-\\u9fa5)]", ""); // 只保留汉字
        // 输出识别结果
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("识别结果: \n" + resultWithoutRare);
    }

//    //图片灰度化
//
//    public static void Image() throws IOException {
//        BufferedImage image = ImageIO.read(new File("D:\\1_Java-Total package-after10_27\\java-note\\图片资源\\test7.jpg"));
//        for (int y = 0; y < image.getHeight(); y++) {
//            for (int x = 0; x < image.getWidth(); x++) {
//                int rgb = image.getRGB(x, y);
//                int gray = (int) (0.299 * ((rgb >> 16) & 0xFF) + 0.587 * ((rgb >> 8) & 0xFF) + 0.114 * (rgb & 0xFF));
//                image.setRGB(x, y, (gray << 16) | (gray << 8) | gray);
//            }
//        }
//        ImageIO.write(image, "jpg", new File("D:\\1_Java-Total package-after10_27\\java-note\\图片资源\\图片处理结果\\test7-HDH.jpg"));
//    }
//
//    @Test
//    public void Image2() throws IOException {
//        BufferedImage image = ImageIO.read(new File("D:\\1_Java-Total package-after10_27\\java-note\\图片资源\\test8.png"));
//        int threshold = 128;
//        for (int y = 0; y < image.getHeight(); y++) {
//            for (int x = 0; x < image.getWidth(); x++) {
//                int rgb = image.getRGB(x, y);
//                int gray = (int) (0.299 * ((rgb >> 16) & 0xFF) + 0.587 * ((rgb >> 8) & 0xFF) + 0.114 * (rgb & 0xFF));
//                if (gray > threshold) {
//                    image.setRGB(x, y, 0xFFFFFF);
//                } else {
//                    image.setRGB(x, y, 0x000000);
//                }
//            }
//        }
//        ImageIO.write(image, "png", new File("D:\\1_Java-Total package-after10_27\\java-note\\图片资源\\图片处理结果\\test8-EZH.png"));
//    }

}
