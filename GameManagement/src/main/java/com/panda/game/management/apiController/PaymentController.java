/***
 * @pName management
 * @name PaymentController
 * @user HongWei
 * @date 2018/9/5
 * @desc
 */
package com.panda.game.management.apiController;

import com.alipay.api.*;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipayEncrypt;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.panda.game.management.controller.BaseController;
import com.panda.game.management.utils.DateUtil;
import com.panda.game.management.utils.JsonUtil;
import com.panda.game.management.utils.OrderUtil;
import com.panda.game.management.utils.StringUtil;
import org.springframework.web.bind.annotation.*;
import com.panda.game.management.entity.AlipayConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
public class PaymentController extends BaseController {

    private Map<String, String> payInfo = new HashMap();

    @GetMapping("/pay")
    public void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String appid =  new String(request.getParameter("appid").getBytes("ISO-8859-1"),"UTF-8");

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String(OrderUtil.get().getBytes("ISO-8859-1"),"UTF-8");
        // 订单名称，必填
        String subject = "consume";
        System.out.println(subject);
        // 付款金额，必填
        String total_amount=new String(request.getParameter("total").getBytes("ISO-8859-1"),"UTF-8");
        // 商品描述，可空
        String body = "cm";
        // 超时时间 可空
        String timeout_express="2m";
        // 销售产品码 必填
        String product_code="QUICK_WAP_WAY";
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(AlipayConfig.notify_url);
        // 设置同步地址
        alipay_request.setReturnUrl(AlipayConfig.return_url);

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = client.pageExecute(alipay_request).getBody();
            response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
            System.out.println(form);
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public String appPay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String outtradeno = new String(OrderUtil.get().getBytes("ISO-8859-1"),"UTF-8");
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID
                , AlipayConfig.RSA_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, "RSA2");
//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest alipayTradeAppPayRequest = new AlipayTradeAppPayRequest();
//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("我是测试数据");
        model.setSubject("App支付测试Java");
        model.setOutTradeNo("IQJZSRC1YMQB5HU");
        model.setTimeoutExpress("30m");
        model.setTotalAmount("0.01");
        model.setSellerId("");
        model.setProductCode("QUICK_MSECURITY_PAY");
        alipayTradeAppPayRequest.setBizModel(model);
        alipayTradeAppPayRequest.setNotifyUrl(AlipayConfig.notify_url);
        String returnBody = null;
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(alipayTradeAppPayRequest);
            returnBody = alipayTradeAppPayResponse.getBody();
            System.out.println(returnBody);//就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return returnBody;
    }

    @PostMapping("/appNotify")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean flag = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET,"RSA2");
        System.out.println(flag == true ? "支付成功" : "支付失败");
    }


    @GetMapping("/appPay")
    @ResponseBody
    public String GetPayInfo(String total)//_amount:付款金额
    {
        String strJson = "";
        try
        {
            String orderInfo = GetOrderInfoWithOutEncode(total);
            // 对订单做RSA 签名
            DefaultSigner defaultSigner = new DefaultSigner(AlipayConfig.RSA_PRIVATE_KEY);
            String sign = defaultSigner.sign(orderInfo, "RSA2", "UTF-8");
            //仅需对sign做URL编码
            sign = URLEncoder.encode(sign,"utf-8");
            String payInfo = GetOrderInfoWithEncode() + "&sign=" + sign;
            strJson = payInfo.replace("+", "%20");//日期那里会有一个空格(2017-01-05 11:11:11)转化为+,所以这里要替换一下
            System.err.println(strJson);
        }
        catch (Exception ex)
        {
            System.err.println(ex.toString());
        }
        return strJson;
    }
    /// <summary>
    /// 不包含Encode的字符串拼接
    /// </summary>
    /// <param name="price"></param>
    /// <returns></returns>
    public String GetOrderInfoWithOutEncode(String price)
    {
        payInfo.clear();
        payInfo.put("app_id", AlipayConfig.RSA_PRIVATE_KEY);
        payInfo.put("biz_content", GetBizContent(price));
        payInfo.put("charset", "utf-8");
        payInfo.put("format", "json");
        payInfo.put("method", "alipay.trade.app.pay");
        payInfo.put("notify_url", "http://wxpay.lmx.ren/ResultNotify");
        payInfo.put("sign_type", "RSA");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        payInfo.put("timestamp", sdf.format(calendar.getTime()));
        payInfo.put("version", "1.0");
        String strUrl = BuildQueryWithOutEncode(payInfo);
        return strUrl;
    }
    /// <summary>
    /// 包含Encode的字符串拼接
    /// </summary>
    /// <param name="price"></param>
    /// <returns></returns>
    public String GetOrderInfoWithEncode()
    {
        String strUrl = BuildQuery(payInfo, "utf-8");
        return strUrl;
    }
    /// <summary>
    /// 获取支付内容详情
    /// </summary>
    /// <param name="total_amount"></param>
    /// <returns></returns>
    public String GetBizContent(String total_amount)
    {
        Map<String, String> biz_content_info = new HashMap<>();
        biz_content_info.put("timeout_express", "30m");//该笔订单允许的最晚付款时间，逾期将关闭交易。
        biz_content_info.put("seller_id", "");//收款支付宝用户ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户ID
        biz_content_info.put("product_code", "QUICK_MSECURITY_PAY");//销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        biz_content_info.put("total_amount", "0.01");//订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        biz_content_info.put("subject", "Iphone7 128G");//商品的标题/交易标题/订单标题/订单关键字等。
        biz_content_info.put("body", "最新款的手机啦");//对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
        biz_content_info.put("out_trade_no", OrderUtil.get());//商户网站唯一订单号
        String strBizContent = JsonUtil.getJsonNotEscape(biz_content_info);
        return strBizContent;
    }


    /// <summary>
    /// 组装普通文本请求参数(带Encode)。
    /// </summary>
    /// <param name="parameters">Key-Value形式请求参数字典</param>
    /// <returns>URL编码后的请求数据</returns>
    public static String BuildQuery(Map<String, String> parameters, String charset)  {
        StringBuilder postData = new StringBuilder();
        boolean hasParam = false;

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (!name.isEmpty() && !value.isEmpty())
            {
                if (hasParam)
                {
                    postData.append("&");
                }

                postData.append(name);
                postData.append("=");

                String encodedValue = null;
                try {
                    encodedValue = URLEncoder.encode(value, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                postData.append(encodedValue);
                hasParam = true;
            }
        }

        return postData.toString();
    }
    /// <summary>
    /// 组装普通文本请求参数(不带Encode)。
    /// </summary>
    /// <param name="parameters">Key-Value形式请求参数字典</param>
    /// <returns>URL编码后的请求数据</returns>
    public static String BuildQueryWithOutEncode(Map<String, String> parameters)
    {
        StringBuilder postData = new StringBuilder();
        boolean hasParam = false;

        for (Map.Entry<String, String> entry : parameters.entrySet()){
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (!name.isEmpty() && !value.isEmpty())
            {
                if (hasParam)
                {
                    postData.append("&");
                }

                postData.append(name);
                postData.append("=");

                String encodedValue = value;
                postData.append(encodedValue);
                hasParam = true;
            }
        }
        return postData.toString();
    }

}
