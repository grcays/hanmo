package com.example;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class 篆刻测试 {

    public static void main(String[] args) throws IOException, FontFormatException {
        int x = 8,y = 110;
        int flag = 2;
        boolean isZhuWen = true;
        int x_pyl = 112;
        int y_pyl = 110;
        Font font = null;
        // 加载篆刻字体库
        //Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/AaMeiRenZhuan-2.ttf"));
        if(flag == 1){
            //这个的话x_pyl
            x_pyl -= 20;
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/汉仪篆书繁.ttf"));
        }else {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/MLFFZ.TTF"));
        }
        // 设置用户输入的汉字
        String chineseCharacter = "高槐玉印";
        int length = chineseCharacter.length();

        // 创建空白的画布
        BufferedImage image = new BufferedImage(240, 250, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        if (isZhuWen) {
            graphics.setColor(Color.RED);
            graphics.fillRect(5,10,230,230);
        }else {
            graphics.setColor(Color.red);
            graphics.setStroke(new BasicStroke(4));
            graphics.drawRect(5, 10,230, 230);
        }
            for (int i = 0; i < length; i++) {
                if (i >= 2) {
                    if (i == 2) {
                        x = x - (2*x_pyl);
                        y = y + y_pyl;
                    }
                    if(isZhuWen) {
                        graphics.setColor(Color.WHITE);
                    }else {
                        graphics.setColor(Color.red);
                    }
                    graphics.setFont(font.deriveFont(120.0f));
                    graphics.drawString(String.valueOf(chineseCharacter.charAt(i)), x, y);
                    x = x + x_pyl;
                } else {
                    // 绘制篆刻字体并调整
                    if(isZhuWen) {
                        graphics.setColor(Color.WHITE);
                    }else {
                        graphics.setColor(Color.red);
                    }                    graphics.setFont(font.deriveFont(120.0f));
                    graphics.drawString(String.valueOf(chineseCharacter.charAt(i)), x, y);
                    x = x + x_pyl;
                }
            }

        // 绘制篆刻字体并调整
//        graphics.setColor(Color.BLACK);
//        graphics.setFont(font.deriveFont(120.0f));
//        graphics.drawString(chineseCharacter, 200, 340);

        // 对生成的图像进行必要的后处理和调整，比如调整亮度、对比度、锐度等等
        // ...
        //BufferedImage image1 = flipImage(image, true, false);
        // 将生成的篆刻字体图像保存或者展示给用户
        ImageIO.write(image, "png", new File("seal.png"));
    }

    /**
     * 实现水平或垂直反转
     * @param originalImage 原图像
     * @param flipHorizontal 是否水平反转
     * @param flipVertical 是否垂直反转
     * @return 反转后的图像
     */
    private static BufferedImage flipImage(BufferedImage originalImage, boolean flipHorizontal, boolean flipVertical) {
        // 创建AffineTransform对象，实现反转变换
        AffineTransform flipTransform = new AffineTransform();
        if (flipHorizontal) {
            flipTransform.concatenate(AffineTransform.getScaleInstance(-1, 1));
            flipTransform.concatenate(AffineTransform.getTranslateInstance(-originalImage.getWidth(), 0));
        }
        if (flipVertical) {
            flipTransform.concatenate(AffineTransform.getScaleInstance(1, -1));
            flipTransform.concatenate(AffineTransform.getTranslateInstance(0, -originalImage.getHeight()));
        }

        // 创建AffineTransformOp对象，并应用变换
        AffineTransformOp flipOperation = new AffineTransformOp(flipTransform, AffineTransformOp.TYPE_BICUBIC);
        return flipOperation.filter(originalImage, null);
    }


}
class Company{
        private String name = "CVTE";
public String getName() {
    return name;
}
public static void main(String[] args)throws Exception{
        Company company = new Company();
        Field field = company.getClass().getField("name");
        String nameFromField =(String)field.get(company);
        Method method =  company.getClass().getMethod("getName");
        String nameFromMethod =(String)method.invoke(company);
        System.out.println(nameFromField.equals(nameFromMethod));
}
}