package cn.ztuo.bitrade.vendor.provider.support;

import cn.ztuo.bitrade.util.MessageResult;
import cn.ztuo.bitrade.vendor.provider.SMSProvider;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author tansitao
 * @time 2018-04-05
 * 云片短信验证码发送类
 */
@Slf4j
public class FakeSMSProvider implements SMSProvider {

    private String gateway;
    private String apikey;

    public FakeSMSProvider(String gateway, String apikey) {
        this.gateway = gateway;
        this.apikey = apikey;
    }

    private static Pattern RESPONSE_PATTERN = Pattern.compile("<response><error>(-?\\d+)</error><message>(.*[\\\\u4e00-\\\\u9fa5]*)</message></response>");


    public static String getName() {
        return "fake";
    }

    @Override
    public MessageResult sendSingleMessage(String mobile, String content) throws UnirestException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("text", content);
        params.put("mobile", mobile);
        log.info("fakeParameters====", params.toString());
        MessageResult result = new MessageResult(0, "发送成功");
        return result;
    }


    @Override
    public MessageResult sendInternationalMessage(String mobile, String content) throws IOException, DocumentException {
        content = String.format("【coinmany】Your verification code is %s", content);
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", apikey);
        params.put("text", content);
        params.put("mobile", mobile);
        log.info("fakeParameters====", params.toString());
        MessageResult result = new MessageResult(0, "发送成功");
        return result;
    }

    /**
     * 获取验证码信息格式
     *
     * @param code
     * @return
     */
    @Override
    public String formatVerifyCode(String code) {
        return String.format("【coinmany】您的验证码为：%s，请按页面提示填写，切勿泄露于他人。", code);
    }

    @Override
    public MessageResult sendVerifyMessage(String mobile, String verifyCode) throws Exception {
        String content = formatVerifyCode(verifyCode);
        return sendSingleMessage(mobile, content);
    }

    @Override
    public MessageResult sendLoginMessage(String ip, String phone) throws Exception {
        String content=sendLoginMessage(ip);
        return sendSingleMessage(content,phone);
    }

}
