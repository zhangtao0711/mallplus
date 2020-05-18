package com.zscat.mallplus.util;


import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.UUID;

public class CommonUtil {
    private static Logger log = LoggerFactory.getLogger(CommonUtil.class);


    /**
     * 发送https请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("连接超时：{}", ce);
        } catch (Exception e) {
            log.error("https请求异常：{}", e);
        }
        return jsonObject;
    }


    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
    public void share() throws Exception {
        //创建图片
        BufferedImage img = new BufferedImage(750, 1334, BufferedImage.TYPE_INT_RGB);
        //开启画图
        Graphics g = img.getGraphics();
        //背景 -- 读取互联网图片
        BufferedImage back  = ImageIO.read(new URL("填写海报的背景图片链接"));
        g.drawImage(back.getScaledInstance(750, 1334, Image.SCALE_DEFAULT), 0, 0, null); // 绘制缩小后的图
        //商品  banner图
        //读取互联网图片
        BufferedImage priductUrl = ImageIO.read(new URL("填写自己的banner图，线上链接")); //TODO
        g.drawImage(priductUrl.getScaledInstance(690,516,Image.SCALE_DEFAULT),29,61,null);
        //文案标题
        g.setFont(new Font("微软雅黑", Font.BOLD, 34));
        g.setColor(new Color(29,29,29));
        //绘制文字
        g.drawString("填写文案标题", 31, 638);//TODO
        //文案
        g.setFont(new Font("微软雅黑", Font.PLAIN, 30));
        g.setColor(new Color(47,47,47));
        int fontlen = getWatermarkLength("填写文内容", g);//TODO
        //文字长度相对于图片宽度应该有多少行
        int line = fontlen / (back.getWidth() - 90);
        //高度
        int y = back.getHeight() - (line + 1) * 30 - 500;
        //文字叠加,自动换行叠加
        int tempX = 32;
        int tempY = y;
        //单字符长度
        int tempCharLen = 0;
        //单行字符总长度临时计算
        int tempLineLen = 0;
        StringBuffer sb =new StringBuffer();
        for(int i=0; i < "填写文内容".length(); i++) {//TODO
            char tempChar = "填写文内容".charAt(i);//TODO
            tempCharLen = getCharLen(tempChar, g);
            tempLineLen += tempCharLen;
            if(tempLineLen >= (back.getWidth()-90)) {
                //长度已经满一行,进行文字叠加
                g.drawString(sb.toString(), tempX, tempY + 50);
                //清空内容,重新追加
                sb.delete(0, sb.length());
                //每行文字间距50
                tempY += 50;
                tempLineLen =0;
            }
            //追加字符
            sb.append(tempChar);
        }
        //最后叠加余下的文字
        g.drawString(sb.toString(), tempX, tempY + 50);

        //价格背景
        //读取互联网图片
        BufferedImage bground  = ImageIO.read(new URL("填写自己的价格背景图，七牛图片链接"));//TODO
        // 绘制缩小后的图
        g.drawImage(bground.getScaledInstance(160, 40, Image.SCALE_DEFAULT), 30, 1053, null);

        //限时促销价
        g.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        g.setColor(new Color(255,255,255));
        g.drawString("限时促销价", 50, 1080);

        //价格
        g.setFont(new Font("微软雅黑", Font.PLAIN, 50));
        g.setColor(new Color(249,64,64));
        g.drawString("¥" + "填写商品的价格", 29, 1162);//TODO

        //原价
        g.setFont(new Font("微软雅黑", Font.PLAIN, 36));
        g.setColor(new Color(171,171,171));
        String price = "¥" + "填写商品的原价";//TODO
        g.drawString(price, 260, 1160);
        g.drawLine(250,1148,260+150,1148);

        //商品名称
        g.setFont(new Font("微软雅黑", Font.PLAIN, 32));
        g.setColor(new Color(29,29,29));
        g.drawString("填写商品名称", 30, 1229);//TODO

        //生成二维码返回链接
        String url = "";//TODO
        //读取互联网图片
        BufferedImage qrCode  = ImageIO.read(new URL(url));
        // 绘制缩小后的图
        g.drawImage(qrCode.getScaledInstance(174, 174, Image.SCALE_DEFAULT), 536, 1057, null);

        //二维码字体
        g.setFont(new Font("微软雅黑", Font.PLAIN, 25));
        g.setColor(new Color(171,171,171));
        //绘制文字
        g.drawString("扫描或长按小程序码", 515, 1260);

        g.dispose();
        //保存到本地 生成文件名字
        String iconKey = UUID.randomUUID().toString() +".png"; //TODO
        //先将画好的海报写到本地
        String picUrl = "填写自己电脑的路径" + iconKey; // TODO
        File file = new File(picUrl);
        ImageIO.write(img, "jpg",file);
        //再将file上传至七牛返回链接存入数据库
        //end
        // TODO
    }

    /**
     * 获取水印文字总长度
     *@paramwaterMarkContent水印的文字
     *@paramg
     *@return水印文字总长度
     */
    public int getWatermarkLength(String waterMarkContent, Graphics g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(),0, waterMarkContent.length());
    }
    public int getCharLen(char c, Graphics g) {
        return g.getFontMetrics(g.getFont()).charWidth(c);
    }
}
