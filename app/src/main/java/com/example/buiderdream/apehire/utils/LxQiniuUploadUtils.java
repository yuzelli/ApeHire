package com.example.buiderdream.apehire.utils;

import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;

import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * 七牛上传工具类
 *
 * @author  李秉龙
 * @version V1.0
 * @Title: ${FILE_NAME}
 * @Package cn.haodehaode.utils
 * @Description: ${todo}
 * @date 15/10/31 15:32
 */
public class LxQiniuUploadUtils {
    //七牛后台的key
    private static String AccessKey = ConstantUtils.QN_ACCESSKEY;
    //七牛后台的secret
    private static String SecretKey = ConstantUtils.QN_SECRETKEY;

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    //unix时间戳:2065-12-31 00:00:00
    private static long delayTimes = 3029414400l;

    /**
     * 上传
     *
     * @param domain bucketName的名字
     * @param path   上传文件的路径地址
     */
    public static void uploadPic(final String domain, final String path, final String keys, final UploadCallBack callBack) {
        try {
            // 1:第一种方式 构造上传策略
            JSONObject _json = new JSONObject();
            _json.put("deadline", delayTimes);// 有效时间为一个小时
            _json.put("scope", domain);
            String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
                    .toString().getBytes());
            byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, SecretKey);
            String _encodedSign = UrlSafeBase64.encodeToString(_sign);
            final String _uploadToken = AccessKey + ':' + _encodedSign + ':'
                    + _encodedPutPolicy;
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(path, keys, _uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info,
                                             JSONObject response) {
                            if (info.isOK()) {
                                String urls = getFileUrl(domain, keys);
                                callBack.sucess(urls);
                            } else
                                callBack.fail(key,info);
                        }
                    }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
            throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }

    /**
     * 通过key获取上传的资源文件的全路径
     *
     * @param key
     * @param domain
     * @return
     */
    public static String getFileUrl(String domain, String key) {
     //   String url = HdUtils.transDomai2Zone(domain);
       String url = "http://ojterpx44.bkt.clouddn.com"+domain;
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        try {
            //1:构造URL
            String encode = URLEncoder.encode(key, "UTF-8");
            sb.append(encode);
            //2:为url加上过期时间  unix时间
            sb.append("?e=" + delayTimes);//delayTimes = 1451491200
            //3:对1 2 操作后的url进行hmac-sha1签名 secrect
            String s = sb.toString();
            byte[] bytes = HmacSHA1Encrypt(s, SecretKey);
            String sign = UrlSafeBase64.encodeToString(bytes);
            //4:将accsesskey 连接起来
            sb.append("&token=" + AccessKey + ":" + sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public interface UploadCallBack {
        void sucess(String url);
        void fail(String key, ResponseInfo info);
    }

}
