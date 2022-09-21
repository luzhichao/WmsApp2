package com.linkingwin.education.studyonline.common.base.adapter;

import android.content.Context;
import android.widget.CheckBox;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseViewAdapter;
import com.linkingwin.education.studyonline.common.base.ViewHolder;
import com.linkingwin.education.studyonline.common.base.model.CheckAbleBean;

import org.xutils.view.annotation.ContentView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.widget_tag)
public class WidgetCheckboxAdapter<E  extends CheckAbleBean> extends BaseViewAdapter<E> {

    private Map<Integer,Boolean> selected;

    public WidgetCheckboxAdapter(Context context, List<E> dataList) {
        super(context, dataList);
        initData(dataList);
    }

    private void initData(List<E> dataList) {
        selected = new HashMap<>();
        for (int position = 0; position < dataList.size(); position++) {
            selected.put(position, dataList.get(position).isChecked());
        }
    }

    @Override
    public void bindView(ViewHolder holder, E e) {
        CheckBox tag = holder.getCheckBox(R.id.checkbox_tag);
        tag.setText(e.getCheckText());
    }

    public Map<Integer, Boolean> getSelected() {
        return selected;
    }
}
