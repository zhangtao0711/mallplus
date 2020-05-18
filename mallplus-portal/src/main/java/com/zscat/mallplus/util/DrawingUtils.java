package com.zscat.mallplus.util;
 
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
 
import javax.imageio.ImageIO;
 
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

 
/**
 * 把两张图片合并
 * 
 * @author WuHao
 * @version $Id: Pic.java, v 0.1 2019-10-26 下午3:21:23 1111 Exp $
 */
public class DrawingUtils {
 
	private Font font = new Font("宋体", Font.BOLD, 40); // 添加字体的属性设置
 
	private Graphics2D g = null;
	private Graphics2D J = null;
 
	private int fontsize = 0;
 
	private int x = 0;
 
	private int y = 0;
 
	/**
	 * 导入本地图片到缓冲区
	 */
	public BufferedImage loadImageLocal(String imgName) {
		try {
			return ImageIO.read(new File(imgName));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
 
	/**
	 * 导入网络图片到缓冲区
	 */
	public BufferedImage loadImageUrl(String imgName) {
		try {
			URL url = new URL(imgName);
			return ImageIO.read(url);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
 
	/**
	 * 生成新图片到本地
	 */
	public void writeImageLocal(String newImage, BufferedImage img) {
		if (newImage != null && img != null) {
			try {
				File outputfile = new File(newImage);
				ImageIO.write(img, "jpg", outputfile);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
 
	/**
	 * 设定文字的字体等
	 */
	public void setFont(String fontStyle, int fontSize) {
		this.fontsize = fontSize;
		this.font = new Font(fontStyle, Font.PLAIN, fontSize);
	}
 
	/**
	 * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
	 */
	public BufferedImage modifyImage(BufferedImage img, Object content, int x, int y, Font font, Color color) {
 
		try {
			g = img.createGraphics();
			g.setBackground(Color.WHITE);
			g.setColor(color);// 设置字体颜色
			if (font != null)
				g.setFont(font);
			// 验证输出位置的纵坐标和横坐标
			if (content != null) {
				g.drawString(content.toString(), x, y);
			}
			g.dispose();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
 
		return img;
	}
 
	/**
	 * 修改图片,返回修改后的图片缓冲区（输出多个文本段） xory：true表示将内容在一行中输出；false表示将内容多行输出
	 */
	public BufferedImage modifyImage(BufferedImage img, Object[] contentArr, int x, int y, boolean xory) {
		try {
			int w = img.getWidth();
			int h = img.getHeight();
			g = img.createGraphics();
			g.setBackground(Color.WHITE);
			g.setColor(Color.RED);
			if (this.font != null)
				g.setFont(this.font);
			// 验证输出位置的纵坐标和横坐标
			if (x >= h || y >= w) {
				this.x = h - this.fontsize + 2;
				this.y = w;
			} else {
				this.x = x;
				this.y = y;
			}
			if (contentArr != null) {
				int arrlen = contentArr.length;
				if (xory) {
					for (int i = 0; i < arrlen; i++) {
						g.drawString(contentArr[i].toString(), this.x, this.y);
						this.x += contentArr[i].toString().length() * this.fontsize / 2 + 5;// 重新计算文本输出位置
					}
				} else {
					for (int i = 0; i < arrlen; i++) {
						g.drawString(contentArr[i].toString(), this.x, this.y);
						this.y += this.fontsize + 2;// 重新计算文本输出位置
					}
				}
			}
			g.dispose();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
 
		return img;
	}
 
	/**
	 * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
	 * 
	 * 时间:2007-10-8
	 * 
	 * @param img
	 * @return
	 */
	public BufferedImage modifyImageYe(BufferedImage img) {
 
		try {
			int w = img.getWidth();
			int h = img.getHeight();
			g = img.createGraphics();
			g.setBackground(Color.WHITE);
			g.setColor(Color.blue);// 设置字体颜色
			if (this.font != null)
				g.setFont(this.font);
			g.drawString("www.hi.baidu.com?xia_mingjian", w - 85, h - 5);
			g.dispose();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
 
		return img;
	}
 
	public BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d, int X, int Y) {
 
		try {
			int w = b.getWidth();
			int h = b.getHeight();
 
			g = d.createGraphics();
			g.drawImage(b, X, Y, w, h, null);
			g.dispose();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
 
		return d;
	}
 
	/**
	 * 从url中读取图片
	 * 
	 * @param urlHttp
	 * @param path
	 */
	public static String getPicture(String urlHttp, String path) {
 
		int machineId = 1;// 最大支持1-9个集群机器部署
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		if (hashCodeV < 0) {// 有可能是负数
			hashCodeV = -hashCodeV;
		}
 
		String file = path + machineId + String.format("%015d", hashCodeV) + ".jpg";
		try {
			URL url = new URL(urlHttp);
			BufferedImage img = ImageIO.read(url);
			ImageIO.write(img, "jpg", new File(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
 
	public static void pichead(String url, String path) throws MalformedURLException, IOException {
 
		BufferedImage avatarImage = ImageIO.read(new URL(url));
		int width = 84;// 如果剪切出来的图片画质模糊，请将width 调大点.
		// 透明底的图片
		BufferedImage formatAvatarImage = new BufferedImage(width, width, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = formatAvatarImage.createGraphics();
		// 把图片切成一个圓
 
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// 留一个像素的空白区域，这个很重要，画圆的时候把这个覆盖
		int border = 1;
		// 图片是一个圆型
		Ellipse2D.Double shape = new Ellipse2D.Double(border, border, width - border * 2, width - border * 2);
		// 需要保留的区域
		graphics.setClip(shape);
		graphics.drawImage(avatarImage, border, border, width - border * 2, width - border * 2, null);
		graphics.dispose();
 
		// 在圆图外面再画一个圆
 
		// 新创建一个graphics，这样画的圆不会有锯齿
		graphics = formatAvatarImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int border1 = 3;
		// 画笔是4.5个像素，BasicStroke的使用可以查看下面的参考文档
		// 使画笔时基本会像外延伸一定像素，具体可以自己使用的时候测试
		Stroke s = new BasicStroke(4.5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		graphics.setStroke(s);
		graphics.setColor(Color.WHITE);
		graphics.drawOval(border1, border1, width - border1 * 2, width - border1 * 2);
		graphics.dispose();
 
		OutputStream os = new FileOutputStream(path);// 发布项目时，如：Tomcat 他会在服务器                
       // 本地tomcat webapps文件下创建此文件名
		ImageIO.write(formatAvatarImage, "PNG", os);
		os.close();
	}
 
	/**
	 * 删除文件
	 * 
	 * @param filedel
	 */
	public static void filedel(String filedel) {
		File file = new File(filedel);
		if (!file.exists()) {
			System.out.println("文件不存在");
		} else {
			System.out.println("存在文件");
			file.delete();// 删除文件
		}
	}
 
	public static void main(String[] args) throws MalformedURLException, IOException {
 
		
		DrawingUtils tt = new DrawingUtils();
 
		BufferedImage j = tt.loadImageUrl("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1589340510&di=b729cc1573b4a958bdd0ef2af1748a10&src=http://a3.att.hudong.com/14/75/01300000164186121366756803686.jpg");
		BufferedImage k = tt.loadImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589349375229&di=96d25d3721626d467e3dc30d3d8d3c9d&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F36%2F48%2F19300001357258133412489354717.jpg");
		tt.writeImageLocal("E:\\shenzhuan\\com\\c1.jpg", tt.modifyImagetogeter(k, j, 175, 140));

        //此处可重复多次合成多张图片
        //完成后即可上传对象存储		
 
        //filedel("C:\\Users\\Waitforyou\\Desktop\\img\\c1.jpg");//完成后删除本地文件
 
		System.out.println("success");

		BufferedImage j1 = tt.loadImageLocal("E:\\shenzhuan\\com\\1.png");
		BufferedImage k1 = tt.loadImageLocal("E:\\shenzhuan\\com\\c1.jpg");
		tt.writeImageLocal("E:\\shenzhuan\\com\\c2.jpg", tt.modifyImagetogeter(k1, j1, 175, 140));

	}
 
}