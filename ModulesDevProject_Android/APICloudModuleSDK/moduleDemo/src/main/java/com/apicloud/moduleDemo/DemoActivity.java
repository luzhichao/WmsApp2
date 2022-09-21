package com.apicloud.moduleDemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.apicloud.sdk.moduledemo.R;
import com.example.tscdll.TSCActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * 原生activity
 */
public class DemoActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mo_demo_main_activity);
        Intent data = getIntent();
        String appParam = data.getStringExtra("appParam");
        if (null != appParam) {
            TextView text = (TextView) findViewById(R.id.text);
            text.setText("JS传入的参数为：" + appParam);
        }
        final boolean needResult = data.getBooleanExtra("needResult", false);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = "赵清超";
                //				TextPaint textPaint = new TextPaint();
                //				textPaint.setAntiAlias(true);
                //				textPaint.setTextSize(15);
                //				Bitmap txtBitmap = Bitmap.createBitmap((int)textPaint.measureText(txt),200, Bitmap.Config.ARGB_8888);
                //				Canvas canvas = new Canvas(txtBitmap);
                //				canvas.drawBitmap(txtBitmap, 0, 0, null);
                //				StaticLayout sl= new StaticLayout(txt, textPaint, txtBitmap.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                //				sl.draw(canvas);

                copyAssetAndWrite("futur_b.ttf");

                File dataFile = new File(getCacheDir(), "futur_b.ttf");
                Log.d("DemoActivity", "filePath:" + dataFile.getAbsolutePath());

                TSCActivity BT = new TSCActivity();
                BT.openport("DC:1D:30:4E:FA:6D");
                //				BT.sendcommand("!0 200 200 500 1\r\n");
                ////				BT.sendcommand("CENTER\r\n");
                ////				BT.sendcommand( "SETMAG 1 1\r\n");
                ////				BT.sendcommand( "LEFT\r\n");
                //				BT.sendcommand("TEXT 4 0 0 195 " + txt +"\r\n");
                ////				BT.sendcommand("FORM\r\n");
                //				BT.sendcommand("PRINT\r\n");


                BT.sendcommand("SIZE 100 mm, 100 mm\r\n");
                BT.sendcommand("GAP 2 mm, 0 mm\r\n");
                BT.sendcommand("SPEED 4\r\n");
                BT.sendcommand("DENSITY 10\r\n");
                BT.sendcommand("DIRECTION 0\r\n");
                BT.clearbuffer();
                BT.sendcommand("SET TEAR ON\r\n");

//                BT.sendcommand("TEXT 30,60,\"3\",0,1,1,\"Material\"\n");
//                BT.windowsfont(20, 50, 20, dataFile.getAbsolutePath(), "物料编码：");
//                BT.barcode(20, 80, "128", 60, 1, 0, 3, 3, "M000001841935");
//
//                BT.windowsfont(20, 200, 20, dataFile.getAbsolutePath(), "物料名称：");
//                BT.windowsfont(20, 230, 20, dataFile.getAbsolutePath(), "赵清超超超超超超超超超超超超超超超超超超");
//
//                BT.windowsfont(20, 290, 20, dataFile.getAbsolutePath(), "物料批次：");
//                BT.barcode(20, 320, "128", 60, 1, 0, 1, 1, "M000001841935-3E0GH-11J510001");

                BT.windowsfont(20, 170, 20, dataFile.getAbsolutePath(), "容器编码：");
                BT.barcode(20, 200, "128", 60, 1, 0, 3, 3, "1234567890123456789012");


//                String font = "TST24.BF2";
//                try {
//                    String content = new String("中文987654321".getBytes("gb2312"));
//                    BT.sendcommand("TEXT 30,310,\"TSS24.BF2\",0,1,1," + "\"" + content + "\"" + "\n");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

                BT.printlabel(1, 1);
                BT.closeport(700);
            }
        });
    }

    public Bitmap getNewBitMap(String text) {
        Bitmap newBitmap = Bitmap.createBitmap(120, 150, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(newBitmap, 0, 0, null);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(16.0F);
        StaticLayout sl = new StaticLayout(text, textPaint, newBitmap.getWidth() - 8, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        canvas.translate(6, 40);
        sl.draw(canvas);
        return newBitmap;
    }

    public void addInitializePrinter(int height, int qty) {
        String str = "! 0 200 200 " + height + " " + qty + "\r\n";
    }

    /**
     * 将asset文件写入缓存
     */
    private boolean copyAssetAndWrite(String fileName) {
        try {
            File cacheDir = getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return false;
                }
            } else {
                if (outFile.length() > 10) {//表示已经写入一次
                    return true;
                }
            }
            InputStream is = getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
