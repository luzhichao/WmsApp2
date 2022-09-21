package org.dajia.demo.gprinter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.dajia.demo.jiaboprinter.Gprintersdkv;

public class MainActivity extends AppCompatActivity {

    protected Button btnPrintTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mac_text = (EditText) this.findViewById(R.id.mac);
        final EditText code_text = (EditText) this.findViewById(R.id.code);
        final EditText name_text = (EditText) this.findViewById(R.id.name);
        final EditText lot_text = (EditText) this.findViewById(R.id.lot);
        final EditText num_text = (EditText) this.findViewById(R.id.num);
        final EditText con_text = (EditText) this.findViewById(R.id.con);
        btnPrintTest = this.findViewById(R.id.btnPrintTest);
        btnPrintTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mac = mac_text.getText().toString();
                String code = code_text.getText().toString();
                String name = name_text.getText().toString();
                String lot = lot_text.getText().toString();
                String num = num_text.getText().toString();
                String con = con_text.getText().toString();
                final String mrs = testPrinterData(mac, code, name, lot, num, con);
                Toast.makeText(MainActivity.this, mrs, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String testPrinterData(String mac, String code, String name, String lot, String num, String con) {
        String result;
//        String mac = "DC:0D:30:A2:E3:4B";
//        String code = "TX52010210-01";
//        String name = "1001682  2919010-DY020一汽青岛前下反作用杆带橡胶接头总成";
//        String lot = "202008260001";
//        String num = "120";
//        String con = "S2012010001";

        Gprintersdkv prt = new Gprintersdkv();
        result = prt.printerData(mac, code, name, lot, num, con);

        return result;
    }

}
