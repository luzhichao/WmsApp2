package com.linkingwin.education.studyonline.student.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseActivity;
import com.linkingwin.education.studyonline.common.base.annotation.ToolbarContent;
import com.linkingwin.education.studyonline.student.adapter.LvBillDataAdapter;
import com.linkingwin.education.studyonline.student.enums.FilterBillStatus;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView (R.layout.activity_mine_bills)
@ToolbarContent (title = "我的订单")
public class MineBillsActivity extends BaseActivity {

    @ViewInject ( R.id.lv_bill_data )
    ListView lv_bill_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        FilterBillStatus filterBillStatus = getParam ("filterBillStatus");
        Log.d (MineBillsActivity.class.getName (), String.format ("filterBillStatus:%s", filterBillStatus));
        loadBillData(filterBillStatus);
    }

    private void loadBillData(FilterBillStatus filterBillStatus) {
        List<Object> billData = new ArrayList<> ();
        for(int i = 0; i < 10; i++) {
            billData.add (new Object());
        }
        LvBillDataAdapter lvBillDataAdapter = new LvBillDataAdapter (this, billData, R.layout.item_bill);
        lv_bill_data.setAdapter (lvBillDataAdapter);
    }
}
