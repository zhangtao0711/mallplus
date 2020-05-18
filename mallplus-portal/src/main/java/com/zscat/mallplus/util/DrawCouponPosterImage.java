package com.zscat.mallplus.util;
import com.alibaba.fastjson.JSONObject;
import com.zscat.mallplus.pms.entity.PmsProduct;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
@Slf4j
public class DrawCouponPosterImage {
    /**
     * 绘制海报文字（默认微软雅黑/PLAIN/32）
     *
     * @param graphics
     *            画笔
     * @param text
     *            文本
     * @param width
     *            坐标 x
     * @param height
     *            坐标 y
     * @param color
     *            颜色
     * @param font
     *            字体
     * @return
     */
    public static Graphics2D drawText(Graphics2D graphics, String text, int width, int height, Color color, Font font) {
        // 抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (font == null) {
            font = new Font("微软雅黑", Font.PLAIN, 32);
        }
        graphics.setFont(font);
        graphics.setPaint(new Color(0, 0, 0, 64));
        // 先绘制一遍底色
        graphics.drawString(text, width, height);
        graphics.setPaint(color);
        // 再绘制一遍文字
        // 由于部分情况会出现文字模糊的情况，保险起见才出此对策。
        graphics.drawString(text, width, height);
        return graphics;
    }
    /**
     * 绘制海报文字(换行)
     *
     * @param graphics
     *            画笔
     * @param text
     *            文本
     * @param width
     *            位置：x
     * @param height
     *            位置：y
     * @param lineHeight
     *            单行行高
     * @param lineWidth
     *            单行行宽
     * @param color
     *            文本颜色
     * @param textSize
     *            文本字体大小
     * @param limitLineNum
     *            限制行数
     * @param backgroundWidth
     *            底背位置（多行文字绘制时，出现为单行时居中的区域宽度。）
     * @return
     */
    public static int drawTextNewLine(Graphics2D graphics, String text, int width, int height, int lineHeight,
                                      int lineWidth, Color color, int textSize, int limitLineNum, int backgroundWidth) {
        Font font = new Font("微软雅黑", Font.PLAIN, textSize);
        graphics.setFont(font);
        graphics.setPaint(color);
        FontRenderContext frc = graphics.getFontRenderContext();
        graphics.getFontRenderContext();
        Rectangle2D stringBounds = font.getStringBounds(text, frc);
        double fontWidth = stringBounds.getWidth();

        java.util.List<String> lineList = new ArrayList<String>();
        int lineCharCountSub = 0;

        // 不满一行
        if (fontWidth <= lineWidth) {
            lineList.add(text);
            if (backgroundWidth > 0) {
                width = (backgroundWidth - Double.valueOf(fontWidth).intValue()) / 2;
            }
        } else {
            if (backgroundWidth > 0) {
                width = (backgroundWidth - lineWidth) / 2;
            }
            // 输出文本宽度,这里就以画布宽度作为文本宽度测试
            int textWidth = lineWidth;
            // 文本长度是文本框长度的倍数
            double bs = fontWidth / textWidth;
            // 每行大概字数
            int lineCharCount = (int)Math.ceil(text.length() / bs);
            lineCharCountSub = lineCharCount;
            int beginIndex = 0;
            while (beginIndex < text.length()) {
                int endIndex = beginIndex + lineCharCount;
                if (endIndex >= text.length()) {
                    endIndex = text.length();
                }
                String lineStr = text.substring(beginIndex, endIndex);
                Rectangle2D tempStringBounds = font.getStringBounds(lineStr, frc);
                int tzzs = 1;
                while (tempStringBounds.getWidth() > textWidth) {
                    lineStr = lineStr.substring(0, lineStr.length() - tzzs);
                    tempStringBounds = font.getStringBounds(lineStr, frc);
                }
                lineList.add(lineStr);
                beginIndex = beginIndex + lineStr.length();
            }
        }

        // Color.BLACK 。字体颜色
        graphics.setPaint(color);
        if (lineHeight == 0) {
            lineHeight = 35;
        }
        // 限行
        int lineNum = lineList.size();
        if (limitLineNum != 0 && lineNum > limitLineNum) {
            lineNum = limitLineNum;
        }
        // 绘制 换行文字
        for (int i = 0; i < lineNum; i++) {
            String lineStr = lineList.get(i);
            if (lineNum >= 2 && i == lineNum - 1) {
                if (lineStr.length() >= lineCharCountSub - 3) {
                    lineStr = lineStr.substring(0, lineStr.length() - 2) + "...";
                }
            }
            graphics.drawString(lineStr, width, height + (i + 1) * lineHeight);
            graphics.drawString(lineStr, width, height + (i + 1) * lineHeight);
        }
        return lineNum;
    }
    /**
     * 绘制 头像
     *
     * @param graphics
     * @param avatarUrl
     * @param bufferImage
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    public static Graphics2D drawAvatar(Graphics2D graphics, String avatarUrl, BufferedImage bufferImage, int x, int y)
            throws Exception {
        // 绘制头像 。
        BufferedImage logoBufferImage = null;
        if (avatarUrl.startsWith("http")) {
            logoBufferImage = Thumbnails.of(new URL(avatarUrl)).size(70, 70).asBufferedImage();
        } else {
            logoBufferImage = Thumbnails.of(new File(avatarUrl)).size(70, 70).asBufferedImage();
        }
        int height2 = logoBufferImage.getHeight();
        // 图片变圆
        int border = 2;
        Ellipse2D.Double shapeEll =
                new Ellipse2D.Double(x + border, y + border, height2 - border * 2, height2 - border * 2);
        // 抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setClip(shapeEll);
        // TODO 绘制头像
        graphics.drawImage(logoBufferImage, x, y, null);

        // 为了防止 头像圆角之后锯齿问题。需要在头像周围画一个白框覆盖即可。
        // 需要重新获取画笔对象。
        graphics = bufferImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int border2 = 2;
        Stroke s = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        graphics.setStroke(s);
        graphics.setColor(Color.WHITE);
        // TODO 绘制头像边框
        graphics.drawOval(x + border, y + border, height2 - border2 * 2, height2 - border2 * 2);
        return graphics;
    }
    /**
     * 绘制 图片
     *
     * @param graphics
     *            画笔
     * @param bufferImage
     *            图片资源
     * @param x
     *            坐标 x
     * @param y
     *            坐标 y
     * @return
     * @throws Exception
     */
    public static Graphics2D drawImage(Graphics2D graphics, BufferedImage bufferImage, int x, int y) throws Exception {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(bufferImage, x, y, null);
        return graphics;
    }
}
