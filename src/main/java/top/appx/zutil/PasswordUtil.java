package top.appx.zutil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lhzxd on 2017/3/5.
 */
public class PasswordUtil {
    private PasswordUtil(){}
    /**
     * 获取字符串md5值
     * @param msg
     * @return md5
     * @throws Exception
     */
    public static byte[] md5(String msg) {
        return md5(msg.getBytes());
    }
    public static byte[] md5(byte[] bs) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(bs);
        return md5.digest();
    }


}
