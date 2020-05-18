package com.zscat.mallplus.util;

/**
 * 测试启动类
 *
 * @author Angzk
 * @date 2019年10月23日
 */
public class AngzkRun {

    /**
     * 注: png 格式图片可能会导致 出现黑色背景.
     *
     * @param args main
     */
    public static void main(String[] args) {
        // 获取Resource下 images 的目录
        String folderPath = GraphicsUtils.class.getResource("/images").getPath();

        String spuName = "澳洲风味小麦白11啤（APA）";
        String linkUrl = "https://gitee.com/zscat/mallplus/wikis/pages";
        String logoPath =  "https://images.gitee.com/uploads/images/2020/0109/102351_cfd0b0c7_134431.png";
        String backgroundUrl =  "https://images.gitee.com/uploads/images/2020/0513/134511_35be9915_134431.jpeg";
        String spuPicUrl =  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1589349375229&di=96d25d3721626d467e3dc30d3d8d3c9d&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F36%2F48%2F19300001357258133412489354717.jpg";
        String memberPrice = "会员福利￥99.80";
        String price = "直购价￥188.90";


        GraphicsUtils.createPosterByRedTemplate(linkUrl, true, logoPath, backgroundUrl, spuPicUrl,
                memberPrice, price, spuName);
    }

}
