




import com.zscat.mallplus.elias.CertificateDownloader;
import com.zscat.mallplus.elias.config.CertPathConfig;
import com.zscat.mallplus.encrypt.EncryptSensitive;
import org.junit.Test;

import picocli.CommandLine;

import java.io.IOException;


/**

 * @author: chen

 * @date: 2019/7/25

 **/



public class CertificateDownloaderTest {

    //用于证书解密的密钥

    private String apiV3key = "65fdd8532fbsc784936909f1a7r5872f";// X1FVcXWgzK47dSopvSvMovzcwg321WGz

    // 商户号

    private static String mchId = "1527256251";

    // 商户证书序列号 7D3CAA68D21E6FDDFDC8B1B8D441EBE5E1CA1CB2

    private static String mchSerialNo;//1999B3A90DAAE4D81C1807832ABC88D94BD3EA39

    static {
        try {
            mchSerialNo = EncryptSensitive.getSerialNo(CertPathConfig.apiclient_cert);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 商户私钥

    private static String mchPrivateKeyFilePath = CertPathConfig.privateKeyPath;

    // 微信支付平台证书

    private static String wechatpayCertificateFilePath = "";

    //下载成功后保存证书的路径

    private static String outputFilePath = CertPathConfig.publicKeyPath;



    @Test

    public void testCertDownload() {

        String[] args = {"-k", apiV3key, "-m", mchId, "-f", mchPrivateKeyFilePath,

                "-s", mchSerialNo, "-o", outputFilePath, "-c", wechatpayCertificateFilePath};

        System.out.println("-k:"+ apiV3key+ "-m:"+ mchId+ "-f:"+ mchPrivateKeyFilePath+

                "-s:"+ mchSerialNo+ "-o:"+ outputFilePath+ "-c:"+ wechatpayCertificateFilePath);
//        CommandLine.run(new CertificateDownloader(), args);

    }



}