package com.linkingwin.elearn.common.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linkingwin.elearn.R;

import static com.linkingwin.elearn.common.util.Tools.isMobileNO;

public class LoginVerify implements View.OnFocusChangeListener {
    private EditText et_num;
    private TextView tv_Tip;
    public LoginVerify(EditText et_num, TextView tv_Tip){
        this.et_num=et_num;
        this.tv_Tip=tv_Tip;
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String num=et_num.getText().toString();

        if (hasFocus) {
//                Toast.makeText(LoginActivity.this, "获取焦点验证,"+isMobileNO(num)+","+num, Toast.LENGTH_SHORT).show();
            // 此处为获取焦点时的处理内容
            if (isMobileNO(num)){
                et_num.setText(num);
            }else{
                et_num.setText("");
            }
            tv_Tip.setVisibility(View.GONE);
        } else {
//                Toast.makeText(LoginActivity.this, "失去焦点验证"+isMobileNO(num)+","+num, Toast.LENGTH_SHORT).show();
            // 此处为失去焦点时的处理内容
            if(!isMobileNO(num)){
                tv_Tip.setText(R.string.tv_TipNum);
                tv_Tip.setVisibility(View.VISIBLE);
            }
        }
    }


}
