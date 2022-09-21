package com.linkingwin.education.studyonline.student.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseActivity;
import com.linkingwin.education.studyonline.common.http.Response;
import com.linkingwin.education.studyonline.common.http.DefaultHttpCallback;
import com.linkingwin.education.studyonline.common.utils.ToastUtils;
import com.linkingwin.education.studyonline.student.api.Api;
import com.linkingwin.education.studyonline.student.vo.OrderInfo;
import com.linkingwin.education.studyonline.student.vo.PayResult;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.alipay.sdk.app.AuthTask;
//import com.alipay.sdk.app.PayTask;
//import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;

@ContentView (R.layout.activity_pay)
public class PayActivity extends BaseActivity implements IWXAPIEventHandler {


    private static final int SDK_PAY_FLAG = 1;

    IWXAPI msgApi;

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                ToastUtils.show(getString(R.string.pay_success) + payResult);
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                ToastUtils.show(getString(R.string.pay_failed) + payResult);
            }
        };
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        msgApi = WXAPIFactory.createWXAPI(this, null);
        msgApi.handleIntent(getIntent(), this);
        super.onCreate (savedInstanceState);
    }

    /**
     * 获取权限使用的 RequestCode
     */
    private static final int PERMISSIONS_REQUEST_CODE = 1002;

    /**
     * 检查支付宝 SDK 所需的权限，并在必要的时候动态获取。
     * 在 targetSDK = 23 以上，READ_PHONE_STATE 和 WRITE_EXTERNAL_STORAGE 权限需要应用在运行时获取。
     * 如果接入支付宝 SDK 的应用 targetSdk 在 23 以下，可以省略这个步骤。
     */
    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSIONS_REQUEST_CODE);

        } else {
            ToastUtils.show (getString(R.string.permission_already_granted));
        }
    }

    @Event(value =R.id.payV2,type = View.OnClickListener.class)
    private void pay(View view) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setMerchantId("ROOT");
        orderInfo.setChannel("ali");
        orderInfo.setTradeNo(System.currentTimeMillis() + "");
        orderInfo.setSubject("test");
        orderInfo.setTradeType("APP");
        orderInfo.setPrice(1.01);
        Api.APP_PAY.request (orderInfo, new DefaultHttpCallback () {
            @Override
            public void onSucceed(Response response) {
                 payV2((String) response.getResult ());
            }

            @Override
            public void onFail(Response response) {
                ToastUtils.show (response.getErrorMsg ());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show (ex.getMessage ());
            }
        });
    }


    /**
     * 支付宝支付业务示例
     */
    public void payV2(final String orderInfo) {
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Event(value =R.id.payWx,type = View.OnClickListener.class)
    private void wxPay(View view) {
        HashMap<String, Object> orderinfo = new HashMap<> ();
        orderinfo.put ("merchantId", "ROOT");
        orderinfo.put ("channel", "wx");
        orderinfo.put ("tradeNo", System.currentTimeMillis() + "");
        orderinfo.put ("subject", "test");
        orderinfo.put ("tradeType", "APP");
        orderinfo.put ("price", 1.01);
        Api.APP_PAY.request (orderinfo, new DefaultHttpCallback () {
            @Override
            public void onSucceed(Response response) {
                JSONObject json = ((JSONObject) response.getResult());
                PayReq req = json.toJavaObject(PayReq.class);
                msgApi.registerApp(req.appId);
                msgApi.sendReq(req);
            }

            @Override
            public void onFail(Response response) {
                ToastUtils.show (response.getErrorMsg ());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show (ex.getMessage ());
            }
        });
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if(resp.getType()==ConstantsAPI.COMMAND_PAY_BY_WX){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
        }
    }

    public static class WXPayEvent {

        private int errCode;

        public WXPayEvent(int errCode) {
            this.errCode = errCode;
        }

        public int getErrCode() {
            return errCode;
        }
    }



    /*
     * 转换成xml
     */
    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<"+params.get(i).getName()+">");
            sb.append(params.get(i).getValue());
            sb.append("</"+params.get(i).getName()+">");
        }
        sb.append("</xml>");
        Log.e("Simon",">>>>"+sb.toString());
        return sb.toString();
    }
}
