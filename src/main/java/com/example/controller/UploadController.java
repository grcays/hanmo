package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.example.dto.Result;
import com.example.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.example.utils.SystemConstants.IMAGE_UPLOAD_DIR2;

@Slf4j
@RestController
@RequestMapping("upload")
//@Api(tags = "发布动态时上传图片接口")
public class UploadController {

    @PostMapping("blog")
//    @ApiOperation(value = "添加图片")
   // @ApiImplicitParam(name = "file",value = "图片上传文件夹")
    public Result uploadImage(@RequestParam("file") MultipartFile image) {
        try {
            // 获取原始文件名称
            String originalFilename = image.getOriginalFilename();
            // 生成新文件名
            String fileName = createNewFileName(originalFilename);
            // 保存文件
            image.transferTo(new File(SystemConstants.IMAGE_UPLOAD_DIR, fileName));
            // 返回结果
            log.debug("文件上传成功，{}", fileName);
            return Result.ok(fileName);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @PostMapping("text")
//    @ApiOperation(value = "添加图片")
    // @ApiImplicitParam(name = "file",value = "图片上传文件夹")
    public Result recognitionImage(@RequestParam("file") MultipartFile file) throws TesseractException, IOException {

        //ImageIO.write(file, "png", new File(IMAGE_UPLOAD_DIR2 + "seal.png"));
            BufferedImage read = ImageIO.read(file.getInputStream());
        file.transferTo(new File(SystemConstants.IMAGE_UPLOAD_DIR2,"identifiedImageUrl.png"));

        //ImageIO.write(read, "png", new File(IMAGE_UPLOAD_DIR2 + "identifiedImageUrl2.png"));
       // Files.delete("//identifiedImageUrl.png");
        String[] split = file.getOriginalFilename().split("\\.");
//            String fileName = file.getOriginalFilename();
            Tesseract iTesseract = new Tesseract();
            //设置tessdata训练库语言包地址，项目根目录下为默认地址可不设置
            iTesseract.setDatapath("D:\\1_Java-Total package-after10_27\\various-App\\NotOftenUse\\Tesseract\\Tess4J-3.4.8-src\\Tess4J\\tessdata");
            //默认识别英文
            //如果需要识别英文之外的语种，需要指定识别语种，并且需要将对应的语言包放进项目中
            iTesseract.setLanguage("chi_sim");

            for (int y = 0; y < read.getHeight(); y++) {
                for (int x = 0; x < read.getWidth(); x++) {
                    int rgb = read.getRGB(x, y);
                    int gray = (int) (0.299 * ((rgb >> 16) & 0xFF) + 0.587 * ((rgb >> 8) & 0xFF) + 0.114 * (rgb & 0xFF));
                    read.setRGB(x, y, (gray << 16) | (gray << 8) | gray);
                }
            }
            int threshold = 128;
            for (int y = 0; y < read.getHeight(); y++) {
                for (int x = 0; x < read.getWidth(); x++) {
                    int rgb = read.getRGB(x, y);
                    int gray = (int) (0.299 * ((rgb >> 16) & 0xFF) + 0.587 * ((rgb >> 8) & 0xFF) + 0.114 * (rgb & 0xFF));
                    if (gray > threshold) {
                        read.setRGB(x, y, 0xFFFFFF);
                    } else {
                        read.setRGB(x, y, 0x000000);
                    }
                }
            }
            //开始识别时间
            long startTime = System.currentTimeMillis();
            //识别结果
            String str = iTesseract.doOCR(read);

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
            StringBuffer buffer = new StringBuffer(str.length());
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (dictSet.contains(c)) {
                    buffer.append(c);
                }
            }
            String resultWithoutRare = buffer.toString();
        // 输出识别结果
        System.out.println("识别结果: \n" + resultWithoutRare);
            String[] str2 = new String[2];
            str2[0] = String.valueOf(resultWithoutRare);
            str2[1] = split[0];

            return Result.ok(str2);
    }

    @GetMapping("/blog/delete")
//    @ApiOperation(value = "删除图片",notes = "在图片有时间有一个x,点击就可以删除")
    public Result deleteBlogImg(@RequestParam("name") String filename) {
        File file = new File(SystemConstants.IMAGE_UPLOAD_DIR, filename);
        if (file.isDirectory()) {
            return Result.fail("错误的文件名称");
        }
        FileUtil.del(file);
        return Result.ok();
    }

    private String createNewFileName(String originalFilename) {
        // 获取后缀
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        // 生成目录
        String name = UUID.randomUUID().toString();
        int hash = name.hashCode();
        int d1 = hash & 0xF;
        int d2 = (hash >> 4) & 0xF;
        // 判断目录是否存在
        File dir = new File(SystemConstants.IMAGE_UPLOAD_DIR, StrUtil.format("/blogs/{}/{}", d1, d2));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成文件名
        return StrUtil.format("/blogs/{}/{}/{}.{}", d1, d2, name, suffix);
    }

    @GetMapping("/zhuanke")
    public Result zhuanKe(@RequestParam("str") String str,@RequestParam("select") String select,
        @RequestParam("transType") String transType,@RequestParam("transMirror") boolean transMirror) throws IOException, FontFormatException {
        int x = 8,y = 110;
        int flag = 2;
        boolean isZhuWen = false;
        if (Objects.equals(select, "白文印")) {
            isZhuWen = false;
        }else {
            isZhuWen = true;
        }
        int x_pyl = 112;
        int y_pyl = 110;
        Font font = null;
        // 加载篆刻字体库
        //Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/AaMeiRenZhuan-2.ttf"));
        switch (transType){
            case "1":
                x_pyl -= 20;
                font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/汉仪篆书繁.ttf"));
                break;
            case "2": font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/MLFFZ.TTF"));break;
            case "3": font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/AaMeiRenZhuan-2.ttf"));break;
            case "4": font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/BeiShiDaShuoWenXiaoZhuan-1.ttf"));break;
            case "5": font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/富汉通细印篆体繁.ttf"));break;
        }
        int length = str.length();

        // 创建空白的画布
        BufferedImage image = new BufferedImage(240, 250, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        if (!isZhuWen) {
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
                if(!isZhuWen) {
                    graphics.setColor(Color.WHITE);
                }else {
                    graphics.setColor(Color.red);
                }
                graphics.setFont(font.deriveFont(120.0f));
                graphics.drawString(String.valueOf(str.charAt(i)), x, y);
                x = x + x_pyl;
            } else {
                // 绘制篆刻字体并调整
                if(!isZhuWen) {
                    graphics.setColor(Color.WHITE);
                }else {
                    graphics.setColor(Color.red);
                }                    graphics.setFont(font.deriveFont(120.0f));
                graphics.drawString(String.valueOf(str.charAt(i)), x, y);
                x = x + x_pyl;
            }
        }

        // 绘制篆刻字体并调整
//        graphics.setColor(Color.BLACK);
//        graphics.setFont(font.deriveFont(120.0f));
//        graphics.drawString(chineseCharacter, 200, 340);

        // 对生成的图像进行必要的后处理和调整，比如调整亮度、对比度、锐度等等
        // ...
        BufferedImage image1 = null;
        if(transMirror) {
            image1 = flipImage(image, true, false);
        }else {
            image1 = flipImage(image, false, false);
        }
        ImageIO.write(image1, "png", new File(IMAGE_UPLOAD_DIR2 + "seal.png"));
//        log.debug("文件上传成功，{}", "seal.png");

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        // 将生成的篆刻字体图像保存或者展示给用户
//        ImageIO.write(image, "png", baos);
//        byte[] imageBytes = baos.toByteArray();
//        HttpHeaders headers = new HttpHeaders();
//        //headers.setContentType(MediaType.IMAGE_PNG);
//        headers.setContentType(MediaType.IMAGE_JPEG);

        return Result.ok("success");
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

    @PostMapping("/Test")
    private String Test(){
        return "ghy+hjy";
    }
}
