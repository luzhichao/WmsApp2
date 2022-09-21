package com.linkingwin.education.studyonline.common.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;

import com.linkingwin.education.studyonline.common.base.ViewHolder;
import com.linkingwin.education.studyonline.common.base.adapter.WidgetCheckboxAdapter;
import com.linkingwin.education.studyonline.common.base.model.CheckAbleBean;
import com.linkingwin.education.studyonline.common.utils.Lists;

import java.util.List;
import java.util.Map;

/**
 * 复选按钮AdapterView
 *
 * @param <B>
 */
public class MultCheckButtonListAdapterUi<B extends CheckAbleBean> {

    private Context context;

    private AdapterView contentView;

    private WidgetCheckboxAdapter<B> checkboxAdapter;

    private List<B> dataList;

    private Integer checkBoxId;

    public MultCheckButtonListAdapterUi(@NonNull Context context, @NonNull AdapterView contentView) {
        this.context = context;
        this.contentView = contentView;
    }

    public MultCheckButtonListAdapterUi<B> setDataList(@NonNull List<B> dataList) {
        this.dataList = dataList;
        return this;
    }

    public MultCheckButtonListAdapterUi<B> setCheckBoxId(@NonNull Integer checkBoxId) {
        this.checkBoxId = checkBoxId;
        return this;
    }

    public MultCheckButtonListAdapterUi<B> init() {
        checkboxAdapter = new WidgetCheckboxAdapter<B>(context, dataList);
        contentView.setAdapter(checkboxAdapter);
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder holder = (ViewHolder) view.getTag();
                CheckBox checkBox = holder.getView(checkBoxId);
                B b = dataList.get(position);
                b.setChecked(!b.isChecked());
                checkboxAdapter.getSelected().put(position, b.isChecked());
                checkBox.setChecked(b.isChecked());
            }
        });
        return this;
    }

    /**
     * 获得所有选中的对象
     * @return
     */
    public List<B> getCheckedData() {
        Map<Integer, Boolean> selected = checkboxAdapter.getSelected();
        List<B> checked = Lists.newArrayList();
        for (Map.Entry<Integer, Boolean> entry : selected.entrySet()) {
            if (entry.getValue()) {
                checked.add(dataList.get(entry.getKey()));
            }
        }
        return checked;
    }

}
