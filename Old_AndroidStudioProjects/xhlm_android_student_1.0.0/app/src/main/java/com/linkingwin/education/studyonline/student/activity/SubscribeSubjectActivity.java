package com.linkingwin.education.studyonline.student.activity;

import android.os.Bundle;
import android.view.View;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseActivity;
import com.linkingwin.education.studyonline.common.base.view.BottomWheelSelectDialogUi;
import com.linkingwin.education.studyonline.common.utils.ToastUtils;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import jsc.kit.wheel.base.WheelItem;

@ContentView(R.layout.activity_subscribe_subject)
public class SubscribeSubjectActivity extends BaseActivity {

    @ViewInject( R.id.list_item_contact )
    QMUIGroupListView groupListContact;

    private WheelItem[] buildNumberOfSubject(int minNumber, int maxNumber) {
        final WheelItem[] items = new WheelItem[maxNumber - minNumber];
        for (int index = 0, i = minNumber; i < maxNumber; i++, index++) {
            items[index] = new WheelItem(i + "");
        }
        return items;
    }


    private WheelItem[] buildDurationOfSubject() {
        double minHour = 1.5, maxHour = 6.0, frequency = 0.5;
        int size  = (int)((maxHour - minHour) / frequency);
        WheelItem[] items = new WheelItem[size];
        for (int i = 0; i < size; i++) {
            items[i] = new WheelItem((minHour + (i * frequency)) + "小时");
        }
        return items;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final QMUICommonListItemView chooseNumberOfSubject = groupListContact.createItemView("购买课次");
        chooseNumberOfSubject.setDetailText("26次");
        chooseNumberOfSubject.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUIGroupListView.newSection(this)
                .setTitle("为保证教学质量，购课26次起")
                .addItemView(chooseNumberOfSubject, new View.OnClickListener(){
            public void onClick(View v) {
                BottomWheelSelectDialogUi<WheelItem, WheelItem, WheelItem, WheelItem, WheelItem> dialog = BottomWheelSelectDialogUi.newInstance();
                dialog.initDialog(SubscribeSubjectActivity.this)
                    .setTitle("选择课次")
                    .setItemData1(buildNumberOfSubject(26, 50), 0)
                    .setCancelButton("取消", null)
                    .setOKButton("确定", new BottomWheelSelectDialogUi.OnClickListener<WheelItem, WheelItem, WheelItem, WheelItem, WheelItem>() {
                        @Override
                        public boolean onClick(WheelItem item1, WheelItem item2, WheelItem item3, WheelItem item4, WheelItem item5) {
                            ToastUtils.show(item1.getShowText());
                            return false;
                        }
                }).show();
            }
        }).addTo(groupListContact);

        final QMUICommonListItemView choosedDurationOfSubject = groupListContact.createItemView("每次课时长");
        choosedDurationOfSubject.setDetailText("2小时");
        choosedDurationOfSubject.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUIGroupListView.newSection(this).setTitle("选择单次课时长").addItemView(choosedDurationOfSubject, new View.OnClickListener(){
            public void onClick(View v) {
                BottomWheelSelectDialogUi<WheelItem, WheelItem, WheelItem, WheelItem, WheelItem> dialog = BottomWheelSelectDialogUi.newInstance();
                dialog.initDialog(SubscribeSubjectActivity.this)
                .setTitle("每次课时长")
                .setItemData1(buildDurationOfSubject(), 0)
                .setCancelButton("取消", null)
                .setOKButton("确定", new BottomWheelSelectDialogUi.OnClickListener<WheelItem, WheelItem, WheelItem, WheelItem, WheelItem>() {
                    @Override
                    public boolean onClick(WheelItem item1, WheelItem item2, WheelItem item3, WheelItem item4, WheelItem item5) {
                        ToastUtils.show(item1.getShowText());
                        return false;
                    }
                }).show();
            }
        }).addTo(groupListContact);
    }
}
