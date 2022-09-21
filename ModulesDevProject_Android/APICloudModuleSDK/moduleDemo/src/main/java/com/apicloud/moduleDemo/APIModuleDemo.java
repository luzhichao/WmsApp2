package com.apicloud.moduleDemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.BluetoothPort;
import com.gprinter.io.PortManager;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 该类映射至Javascript中moduleDemo对象<br><br>
 * <strong>Js Example:</strong><br>
 * var module = api.require('moduleDemo');<br>
 * module.xxx();
 *
 * @author APICloud
 */
public class APIModuleDemo extends UZModule {

    static final int ACTIVITY_REQUEST_CODE_A = 100;

    private AlertDialog.Builder mAlert;
    private Vibrator mVibrator;
    private UZModuleContext mJsCallback;
    private MyTextView mMyTextView;

    private PortManager mPort;
    private boolean isOpenPort;

    public APIModuleDemo(UZWebView webView) {
        super(webView);
    }

    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中moduleDemo对象的showAlert函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.showAlert(argument);
     *
     * @param moduleContext (Required)
     */
    public void jsmethod_showAlert(final UZModuleContext moduleContext) {
        if (null != mAlert) {
            return;
        }
        String showMsg = moduleContext.optString("msg");
        mAlert = new AlertDialog.Builder(context());
        mAlert.setTitle("这是标题");
        mAlert.setMessage(showMsg);
        mAlert.setCancelable(false);
        mAlert.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mAlert = null;
                JSONObject ret = new JSONObject();
                try {
                    ret.put("buttonIndex", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                moduleContext.success(ret, true);
            }
        });
        mAlert.show();
    }

    public void jsmethod_printData(UZModuleContext moduleContext) throws JSONException {
        JSONObject ret = new JSONObject();

        String mac = moduleContext.optString("mac");//"DC:0D:30:A2:E3:4B";
        String code = moduleContext.optString("code");//"TX52010210-01";
        String name = moduleContext.optString("name");//"连杆";
        String lot = moduleContext.optString("lot");//"202008260001";
        String num = moduleContext.optString("num");//"120";
        String con = moduleContext.optString("con");//"S2012010001";


//        final Gprintersdkv printer = new Gprintersdkv();
//        printer.printerData(mac, code, name, lot, num, con);


        String result;
        initPort(mac);
        if (isOpenPort) {
            result = sendDataImmediately(buildData(code, name, lot, num, con));
        } else {
            result = "打印机未连接";
        }

        closePort();
        ret.put("result", result);
        moduleContext.success(ret, true);
    }

    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中moduleDemo对象的startActivity函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.startActivity(argument);
     *
     * @param moduleContext (Required)
     */
    public void jsmethod_startActivity(UZModuleContext moduleContext) {
        Intent intent = new Intent(context(), DemoActivity.class);
        intent.putExtra("appParam", moduleContext.optString("appParam"));
        startActivity(intent);
    }

    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中moduleDemo对象的startActivityForResult函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.startActivityForResult(argument);
     *
     * @param moduleContext (Required)
     */
    public void jsmethod_startActivityForResult(UZModuleContext moduleContext) {
        mJsCallback = moduleContext;
        Intent intent = new Intent(context(), DemoActivity.class);
        intent.putExtra("appParam", moduleContext.optString("appParam"));
        intent.putExtra("needResult", true);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE_A);
    }

    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中moduleDemo对象的vibrate函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.vibrate(argument);
     *
     * @param moduleContext (Required)
     */
    public void jsmethod_vibrate(UZModuleContext moduleContext) {
        try {
            if (null == mVibrator) {
                mVibrator = (Vibrator) context().getSystemService(Context.VIBRATOR_SERVICE);
            }
            mVibrator.vibrate(moduleContext.optLong("milliseconds"));
        } catch (SecurityException e) {
            Toast.makeText(context(), "no vibrate permisson declare", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中moduleDemo对象的stopVibrate函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.stopVibrate(argument);
     *
     * @param moduleContext (Required)
     */
    public void jsmethod_stopVibrate(UZModuleContext moduleContext) {
        if (null != mVibrator) {
            try {
                mVibrator.cancel();
                mVibrator = null;
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中moduleDemo对象的addView函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.addView(argument);
     *
     * @param moduleContext (Required)
     */
    public void jsmethod_addView(UZModuleContext moduleContext) {
        int x = moduleContext.optInt("x");
        int y = moduleContext.optInt("y");
        int w = moduleContext.optInt("w");
        int h = moduleContext.optInt("h");
        if (w == 0) {
            w = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        if (h == 0) {
            h = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        if (null == mMyTextView) {
            mMyTextView = new MyTextView(context());
        }
        int FROM_TYPE = Animation.RELATIVE_TO_PARENT;
        Animation anim = new TranslateAnimation(FROM_TYPE, 1.0f, FROM_TYPE, 0.0f, FROM_TYPE, 0.0f, FROM_TYPE, 0.0f);
        anim.setDuration(500);
        mMyTextView.setAnimation(anim);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(w, h);
        rlp.leftMargin = x;
        rlp.topMargin = y;
        insertViewToCurWindow(mMyTextView, rlp);
    }

    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中moduleDemo对象的removeView函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.removeView(argument);
     *
     * @param moduleContext (Required)
     */
    public void jsmethod_removeView(UZModuleContext moduleContext) {
        if (null != mMyTextView) {

            removeViewFromCurWindow(mMyTextView);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == ACTIVITY_REQUEST_CODE_A) {
            String result = data.getStringExtra("result");
            if (null != result && null != mJsCallback) {
                try {
                    JSONObject ret = new JSONObject(result);
                    mJsCallback.success(ret, true);
                    mJsCallback = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onClean() {
        if (null != mAlert) {
            mAlert = null;
        }
        if (null != mJsCallback) {
            mJsCallback = null;
        }
    }

    class MyTextView extends TextView {

        public MyTextView(Context context) {
            super(context);
            setBackgroundColor(0xFFF0CFD0);
            setText("我是自定义View");
            setTextColor(0xFF000000);
            setGravity(Gravity.CENTER);
        }
    }

    private void initPort(String macCode) {
        mPort = new BluetoothPort(macCode);
        isOpenPort = mPort.openPort();
    }

    private void closePort() {
        if (isOpenPort) {
            mPort.closePort();
        }
    }

    private String sendDataImmediately(final Vector<Byte> data) {
        if (this.mPort == null) {
            return "没有可用打印机";
        }
        try {
            this.mPort.writeDataImmediately(data);
            return "打印完成";
        } catch (Exception e) {
            Log.e("打印异常:", e.getMessage());
            return "打印机异常,请重启设备";
        }
    }

    private Vector<Byte> buildData(String code, String name, String lot, String num, String con) {
        LabelCommand tsc = new LabelCommand(80, 60, 0);
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        tsc.addReference(5, 10);
        tsc.addTear(EscCommand.ENABLE.ON);
        tsc.addCls();

        tsc.addText(10, 10, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "物料编码");
        tsc.addText(10, 40, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "Material Code");
        tsc.add1DBarcode(180, 5, LabelCommand.BARCODETYPE.CODE128, 50, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, code);

        tsc.addText(10, 100, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "名称");
        tsc.addText(10, 130, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "Description");
        name = name != null ? name : "";
        if (name.length() > 18) {
            final List<String> names = getStrList(name, 18);
            int y = 90;
            for (String n : names) {
                tsc.addText(180, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, n);
                y += 30;
            }
        } else {
            tsc.addText(180, 115, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, name);
        }

        tsc.addText(10, 200, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "批次");
        tsc.addText(10, 230, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "Batch No.");
        tsc.add1DBarcode(180, 190, LabelCommand.BARCODETYPE.CODE128, 50, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, lot);

        tsc.addText(10, 270, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "数量 ");
        tsc.addText(10, 300, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "Quantity");
        tsc.addText(180, 280, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_1, num);

        tsc.addText(10, 340, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "容器编号");
        tsc.addText(10, 370, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "Container No.");
        tsc.add1DBarcode(180, 330, LabelCommand.BARCODETYPE.CODE128, 50, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, con);

        tsc.addPrint(1, 1);
        return tsc.getCommand();
    }

    private static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList0(inputString, length, size);
    }

    private static List<String> getStrList0(String inputString, int length, int size) {
        List<String> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length, (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    private static String substring(String str, int f, int t) {
        if (f > str.length()) {
            return null;
        }
        if (t > str.length()) {
            return str.substring(f);
        } else {
            return str.substring(f, t);
        }
    }

}