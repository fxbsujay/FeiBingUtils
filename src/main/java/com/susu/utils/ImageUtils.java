package com.susu.utils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * <p>Description: Image generation and recognition</p>
 * <p>图片生成和识别</p>
 * @author sujay
 * @version 21:43 2022/1/25
 * @since JDK1.8 <br/>
 */
public class ImageUtils {

    /**
     * Constructs a <code>BufferedImage</code> of one of the predefined
     * image types.  The <code>ColorSpace</code> for the image is the
     * default sRGB space.
     * <p>构造一个预定义图像类型之一的 BufferedImage。图像的 ColorSpace 是默认的 sRGB 空间</p>
     * @param width 图片宽带
     * @param height 图片高度
     * @return 图片操作缓冲区
     */
    public static BufferedImage getBufImg(Integer width,Integer height) {
        return new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
    }

    /**
     * @param buffer 图片缓冲区
     * @return 画笔
     */
    public static Graphics getPen(BufferedImage buffer) {
        Graphics2D graphics = buffer.createGraphics();

        // 抗锯齿
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics.setRenderingHints(rh);

        return graphics;
    }


    /**
     * 导出
     * @param bufImg 缓冲区对象
     * @param path 生成路径
     * @return 是否成功
     */
    public static boolean writer(BufferedImage bufImg,String path ) {

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
        ImageWriter writer = writers.next();
        ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
        File file = com.meta.tools.FileUtils.getFile(path);

        ImageOutputStream outStream = null;
        IIOImage iioImage = new IIOImage(bufImg, null, null);
        try {
            outStream = ImageIO.createImageOutputStream(file);
            writer.setOutput(outStream);
            writer.write(null,iioImage,imageWriteParam);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    public static void main(String[] args) {
        BufferedImage bufImg = getBufImg(820, 600);
        Graphics pen = getPen(bufImg);
        // 设置背景颜色
        pen.setColor(Color.WHITE);
        // 填充整张图片(其实就是设置背景颜色)
        pen.fillRect(0, 0, 820, 600);
        // 设置字体颜色
        pen.setColor(Color.black);
        // 边框加粗
        pen.setFont(new Font("宋体", Font.BOLD, 20));
        pen.drawString("Hello Word !!! Sujay", 33, 125);
        writer(bufImg,"D:\\文档\\API文档\\out.png");
    }


}
