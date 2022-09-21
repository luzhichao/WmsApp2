package org.dajia.demo.jiaboprinter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.gprinter.command.CpclCommand;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.BluetoothPort;
import com.gprinter.io.PortManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Gprintersdkv {
    private Context context;

    private PortManager mPort;
    private boolean isOpenPort;

    public Gprintersdkv(){
    }

    /**
     * @param mac  MAC地址-DC:0D:30:A2:E3:4B
     * @param code 物料编号-TX52010210-01
     * @param name 物料名称-连杆
     * @param lot  批次号-202008260001
     * @param num  数量-120
     * @param con  容器编码-S2012010001
     * @return
     */
    public String printerData(String mac, String code, String name, String lot, String num, String con) {
        String result;
        initPort(mac);
        if (isOpenPort) {
            result = sendDataImmediately(buildData(code, name, lot, num, con));
        } else {
            result = "打印机未连接";
        }

        closePort();
        return result;
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

//        tsc.addBar(10, 1, 600, 1);
//        tsc.addBar(10, 70, 600, 1);
//        tsc.addBar(10, 140, 600, 1);
//        tsc.addBar(10, 210, 600, 1);
//        tsc.addBar(10, 280, 600, 1);
//        tsc.addBar(10, 420, 600, 1);

        tsc.addText(10, 10, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "物料编码");
        tsc.addText(10, 40, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "Material Code");
//        tsc.addText(180, 25, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, code);
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
//        tsc.addText(180, 195, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, lot);
        tsc.add1DBarcode(180, 190, LabelCommand.BARCODETYPE.CODE128, 50, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, lot);

        tsc.addText(10, 270, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "数量 ");
        tsc.addText(10, 300, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "Quantity");
        tsc.addText(180, 280, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_1, num);

        tsc.addText(10, 340, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "容器编号");
        tsc.addText(10, 370, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "Container No.");
        tsc.add1DBarcode(180, 330, LabelCommand.BARCODETYPE.CODE128, 50, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, con);

        tsc.addPrint(1, 1);
//        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        return tsc.getCommand();
    }

    /**
     * 按长度将字符串分割
     *
     * @param inputString
     * @param length
     * @return
     */
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
