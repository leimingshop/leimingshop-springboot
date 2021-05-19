/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 滑块验证工具类
 * @Date: 2019/7/15 18:02
 * @Version: V1.0
 */
@Slf4j
public class VerifyImageUtil {

    private static int ORI_WIDTH = 480;  //源文件宽度
    private static int ORI_HEIGHT = 240;  //源文件高度
    private static int X;  //抠图坐标x
    private static int Y;  //抠图坐标y
    private static int WIDTH;  //模板图宽度
    private static int HEIGHT;  //模板图高度

    private VerifyImageUtil() {
    }
//    private static float xPercent;  //X位置移动百分比
//    private static float yPercent;  //Y位置移动百分比
//
//    public static int getX() {
//        return X;
//    }
//
//    public static int getY() {
//        return Y;
//    }
//
//    public static float getxPercent() {
//        return xPercent;
//    }
//
//    public static float getyPercent() {
//        return yPercent;
//    }

    /**
     * 根据模板切图
     *
     * @param templateFile
     * @param targetFile
     * @param templateType
     * @param targetType
     * @return
     * @throws Exception
     */
    public static Map<String, Object> pictureTemplatesCut(byte[] templateFile, byte[] targetFile, String templateType, String targetType) throws Exception {
        Map<String, Object> pictureMap = new HashMap<>();
        // 文件类型
        String templateFiletype = templateType;
        String targetFiletype = targetType;
        if (StringUtils.isEmpty(templateFiletype) || StringUtils.isEmpty(targetFiletype)) {
            throw new RuntimeException("file type is empty");
        }
        // 源文件流
        //File Orifile = targetFile;
        //InputStream oriis = new FileInputStream(Orifile);

        // 滑块图
        BufferedImage imageTemplate = ImageIO.read(new ByteArrayInputStream(templateFile));
        WIDTH = imageTemplate.getWidth();
        HEIGHT = imageTemplate.getHeight();
        // 模板图
        BufferedImage imageTarget = ImageIO.read(new ByteArrayInputStream(targetFile));
        ORI_WIDTH = imageTarget.getWidth();
        ORI_HEIGHT = imageTarget.getHeight();

        generateCutoutCoordinates();
        // 最终图像
        BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, imageTemplate.getType());
        Graphics2D graphics = newImage.createGraphics();
        graphics.setBackground(Color.white);

        int bold = 5;
        // 获取感兴趣的目标区域
        BufferedImage targetImageNoDeal = getTargetArea(X, Y, WIDTH, HEIGHT, new ByteArrayInputStream(targetFile), targetFiletype);

        // 根据模板图片抠图
        newImage = DealCutPictureByTemplate(targetImageNoDeal, imageTemplate, newImage);
        // 滑块添加边框
        newImage = imagesFrameSlider(newImage);
        // 增加高斯模糊
        newImage = simpleBlur(newImage, null);

        // 设置“抗锯齿”的属性
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics.drawImage(newImage, 0, 0, null);
        graphics.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();//新建流。
        ImageIO.write(newImage, templateFiletype, os);//利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
        byte[] newImages = os.toByteArray();
        pictureMap.put("newImage", newImages);
        // 源图生成遮罩
        BufferedImage oriImage = ImageIO.read(new ByteArrayInputStream(targetFile));
        byte[] oriCopyImages = DealOriPictureByTemplate(oriImage, imageTemplate, X, Y, true);
        byte[] baseImage = DealOriPictureByTemplate(oriImage, imageTemplate, 0, 0, false);
        pictureMap.put("baseImage", baseImage);
        pictureMap.put("oriCopyImage", oriCopyImages);
        pictureMap.put("X", X);
        pictureMap.put("Y", Y);
        return pictureMap;
    }

   /* *//**
     * 根据模板切图
     *
     * @param templateFile
     * @param targetFile
     * @param templateType
     * @param targetType
     * @return
     * @throws Exception
     *//*
    public static Map<String, Object> pictureTemplatesCut(File templateFile, File targetFile, String templateType, String targetType) {
        Map<String, Object> pictureMap = new HashMap<>();
        // 文件类型
        String templateFiletype = templateType;
        String targetFiletype = targetType;
        if (StringUtils.isEmpty(templateFiletype) || StringUtils.isEmpty(targetFiletype)) {
            throw new RuntimeException("file type is empty");
        }
        InputStream oriis = null;
        ByteArrayOutputStream os = null;
        try {
            // 源文件流
            File Orifile = targetFile;
            oriis = new FileInputStream(Orifile);

            // 模板图
            BufferedImage imageTemplate = ImageIO.read(templateFile);
            WIDTH = imageTemplate.getWidth();
            HEIGHT = imageTemplate.getHeight();
            // 模板图
            BufferedImage imageTarget = ImageIO.read(Orifile);
            ORI_WIDTH = imageTarget.getWidth();
            ORI_HEIGHT = imageTarget.getHeight();

            generateCutoutCoordinates();
            // 最终图像
            BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, imageTemplate.getType());
            Graphics2D graphics = newImage.createGraphics();
            graphics.setBackground(Color.white);

            int bold = 5;
            // 获取感兴趣的目标区域
            BufferedImage targetImageNoDeal = getTargetArea(X, Y, WIDTH, HEIGHT, oriis, targetFiletype);


            // 根据模板图片抠图
            newImage = DealCutPictureByTemplate(targetImageNoDeal, imageTemplate, newImage);

            // 设置“抗锯齿”的属性
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            graphics.drawImage(newImage, 0, 0, null);
            graphics.dispose();

            os = new ByteArrayOutputStream();//新建流。
            ImageIO.write(newImage, templateFiletype, os);//利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
            byte[] newImages = os.toByteArray();
            pictureMap.put("newImage", newImages);
            // 源图生成遮罩
            BufferedImage oriImage = ImageIO.read(Orifile);
            byte[] oriCopyImages = DealOriPictureByTemplate(oriImage, imageTemplate, X, Y);
            pictureMap.put("oriCopyImage", oriCopyImages);
            pictureMap.put("X", X);
            pictureMap.put("Y", Y);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        } finally {
            try {
                if (oriis != null) {
                    oriis.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.error("错误信息为" + e);
            }
        }
        return pictureMap;
    }*/

    /**
     * 抠图后原图生成
     *
     * @param oriImage
     * @param templateImage
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    private static byte[] DealOriPictureByTemplate(BufferedImage oriImage, BufferedImage templateImage, int x,
                                                   int y, boolean isCut) throws Exception {
        // 源文件备份图像矩阵 支持alpha通道的rgb图像
        BufferedImage ori_copy_image = new BufferedImage(oriImage.getWidth(), oriImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        // 源文件图像矩阵
        int[][] oriImageData = getData(oriImage);
        // 模板图像矩阵
        int[][] templateImageData = getData(templateImage);

        //copy 源图做不透明处理
        for (int i = 0; i < oriImageData.length; i++) {
            for (int j = 0; j < oriImageData[0].length; j++) {
                int rgb = oriImage.getRGB(i, j);
                int r = (0xff & rgb);
                int g = (0xff & (rgb >> 8));
                int b = (0xff & (rgb >> 16));
                //无透明处理
                rgb = r + (g << 8) + (b << 16) + (255 << 24);
                ori_copy_image.setRGB(i, j, rgb);
            }
        }

        // 判断是否需要截取
        if (isCut) {
            for (int i = 0; i < templateImageData.length; i++) {
                for (int j = 0; j < templateImageData[0].length - 5; j++) {
                    int rgb = templateImage.getRGB(i, j);
                    //对源文件备份图像(x+i,y+j)坐标点进行透明处理
                    if (rgb != 16777215 && rgb <= 0) {
                        try {
                            int rgb_ori = ori_copy_image.getRGB(x + i, y + j);
                            int r = (0xff & rgb_ori);
                            int g = (0xff & (rgb_ori >> 8));
                            int b = (0xff & (rgb_ori >> 16));
                            rgb_ori = r + (g << 8) + (b << 16) + (150 << 24);
                            ori_copy_image.setRGB(x + i, y + j, rgb_ori);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // 数组越界错误处理，这样页面就不会返回图像问题。
                            log.error("X:" + X + "||Y:" + Y, e);
                        }

                    } else {
                        //do nothing
                    }
                }
            }

            Graphics graphics = ori_copy_image.getGraphics();
            graphics.setColor(new Color(255, 255, 224));
            graphics.drawRect(x, y, WIDTH - 1, HEIGHT - 5);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();//新建流。
        ImageIO.write(simpleBlur(ori_copy_image, null), "png", os);//利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
        //从流中获取数据数组
        return os.toByteArray();
    }


    /**
     * 给图片增加边框
     *
     * @param sliderImage: 图片
     * @return 增加边框后的图片
     * @date 2019/11/12 10:46
     * @author lixiangx@leimingtech.com
     **/
    private static BufferedImage imagesFrameSlider(BufferedImage sliderImage) {
        int height = sliderImage.getHeight();
        int width = sliderImage.getWidth();
        Graphics g = sliderImage.getGraphics();
        //画笔颜色
        g.setColor(new Color(255, 255, 224));
        //矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
        g.drawRect(0, 0, width - 1, height - 1);
        return sliderImage;
    }

    private static BufferedImage simpleBlur(BufferedImage source, BufferedImage dest) {
        BufferedImageOp op = getGaussianBlurFilter(2, false);
        return op.filter(source, dest);
    }

    /**
     * 高斯模糊图片
     *
     * @param radius
     * @param horizontal
     * @return
     */
    private static ConvolveOp getGaussianBlurFilter(int radius, boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }
        int size = radius * 2 + 1;
        float[] data = new float[size];
        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;
        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }
        Kernel kernel;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }


    /**
     * 对1(滑块)的地方进行上色
     *
     * @param targetImage   目标图片
     * @param templateImage 抠图区域坐标
     * @return
     */
    private static BufferedImage cutByTemplate(BufferedImage targetImage, int[][] templateImage) {
        for (int i = 0; i < targetImage.getWidth(); i++) {
            for (int j = 0; j < targetImage.getHeight(); j++) {
                int rgb = templateImage[j][i];
                /**
                 * 原图中对应位置变色处理
                 */
                int rgb_ori = targetImage.getRGB(i, j);
                if (rgb == 1) {
                    int b = (rgb_ori & 0xff0000) >> 16;
                    int g = (rgb_ori & 0xff00) >> 8;
                    int r = (rgb_ori & 0xff);
                    int color = (int) (b * 0.3 + g * 0.59 + r * 0.11);
                    color = color > 128 ? 255 : color;
                    targetImage.setRGB(i, j, (color << 16) | (color << 8) | color);
                }
            }
        }
        return targetImage;
    }

    /**
     * 根据模板图片抠图
     *
     * @param oriImage
     * @param templateImage
     * @return
     */

    private static BufferedImage DealCutPictureByTemplate(BufferedImage oriImage, BufferedImage templateImage,
                                                          BufferedImage targetImage) throws Exception {
        // 源文件图像矩阵
        int[][] oriImageData = getData(oriImage);
        // 模板图像矩阵
        int[][] templateImageData = getData(templateImage);
        // 模板图像宽度
        try {
            for (int i = 0; i < templateImageData.length; i++) {
                // 模板图片高度
                for (int j = 0; j < templateImageData[0].length; j++) {
                    // 如果模板图像当前像素点不是白色 copy源文件信息到目标图片中
                    int rgb = templateImageData[i][j];
                    if (rgb != 16777215 && rgb <= 0) {
                        targetImage.setRGB(i, j, oriImageData[i][j]);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {/*数组越界错误处理，这样页面就不会返回图像问题。*/
            log.error("X:" + X + "||Y:" + Y, e);
        } catch (Exception e) {
            log.error("X:" + X + "||Y:" + Y, e);
        }
        return targetImage;
    }


    /**
     * 获取目标区域
     *
     * @param x            随机切图坐标x轴位置
     * @param y            随机切图坐标y轴位置
     * @param targetWidth  切图后目标宽度
     * @param targetHeight 切图后目标高度
     * @param ois          源文件输入流
     * @return
     * @throws Exception
     */
    private static BufferedImage getTargetArea(int x, int y, int targetWidth, int targetHeight, InputStream ois,
                                               String filetype) throws Exception {
        Iterator<ImageReader> imageReaderList = ImageIO.getImageReadersByFormatName(filetype);
        ImageReader imageReader = imageReaderList.next();
        // 获取图片流
        ImageInputStream iis = ImageIO.createImageInputStream(ois);
        // 输入源中的图像将只按顺序读取
        imageReader.setInput(iis, true);

        ImageReadParam param = imageReader.getDefaultReadParam();
        Rectangle rec = new Rectangle(x, y, targetWidth, targetHeight);
        param.setSourceRegion(rec);
        return imageReader.read(0, param);
    }

    /**
     * 生成图像矩阵
     *
     * @param
     * @return
     * @throws Exception
     */
    private static int[][] getData(BufferedImage bimg) throws Exception {
        int[][] data = new int[bimg.getWidth()][bimg.getHeight()];
        for (int i = 0; i < bimg.getWidth(); i++) {
            for (int j = 0; j < bimg.getHeight(); j++) {
                data[i][j] = bimg.getRGB(i, j);
            }
        }
        return data;
    }

    /**
     * 随机生成抠图坐标
     */
    private static void generateCutoutCoordinates() {
//        Random random = new Random();
//        int widthDifference = ORI_WIDTH - WIDTH;
//        int heightDifference = ORI_HEIGHT - HEIGHT;
//
//        if (widthDifference <= 0) {
//            X = 5;
//
//        } else {
//            X = random.nextInt(ORI_WIDTH - WIDTH);
//            if (X < WIDTH) {/*@herb 解决切图相对位置问题*/
//                X = WIDTH;
//            }
//        }
//
//        if (heightDifference <= 0) {
//            Y = 5;
//        } else {
//            Y = random.nextInt(ORI_HEIGHT - HEIGHT);
//        }
//        NumberFormat numberFormat = NumberFormat.getInstance();
//        numberFormat.setMaximumFractionDigits(2);
//
//        xPercent = Float.parseFloat(numberFormat.format((float) X / (float) ORI_WIDTH));
//        yPercent = Float.parseFloat(numberFormat.format((float) Y / (float) ORI_HEIGHT));

        // 保证地址控制在后半区域
        X = RandomUtils.nextInt(0, ORI_WIDTH / 2 / 2) + ORI_WIDTH / 2;
        Y = RandomUtils.nextInt(0, ORI_HEIGHT / 2 / 2) + ORI_HEIGHT / 2 / 2;
    }
}
