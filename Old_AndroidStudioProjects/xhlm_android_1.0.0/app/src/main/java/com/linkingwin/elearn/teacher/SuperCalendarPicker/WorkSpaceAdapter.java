package com.linkingwin.elearn.teacher.SuperCalendarPicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.teacher.model.OrderVO;

import java.util.List;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

public class WorkSpaceAdapter extends RecyclerView.Adapter<WorkSpaceAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
//    private String[] titles;
    private List<OrderVO> orderVOS;

    public WorkSpaceAdapter(Context context, List<OrderVO> orderVOS) {
//        titles = context.getResources().getStringArray(R.array.titles);
        this.orderVOS = orderVOS;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public WorkSpaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.items, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String courseId = orderVOS.get(position).getCourseId();
        String courseName = orderVOS.get(position).getCourseName();
        String startTime = orderVOS.get(position).getSchoolStartTime().split(" ")[1];
        String endTime = orderVOS.get(position).getSchoolEndTime().split(" ")[1];
        String roomId = orderVOS.get(position).getRoomId();
        String courseStatus = orderVOS.get(position).getCourseStatus();

        holder.textView.setText(courseName);
        holder.tvTime.setText(String.format("%s~%s", startTime, endTime));
        holder.tvCourseId.setText(courseId);
        //0:未开始,1:进行中,2:已完成
        switch (courseStatus){
            case "0":
                holder.roomCodeText.setText("申请课程码");
                holder.roomCodeText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestParams params = RequestParamsBuilder.buildRequestParams( (HttpCycleContext) context );
                        if (holder.roomCodeInfo.getText() != "" && holder.roomCodeText.getText() == "进入房间"){
                            String room_id = holder.roomCodeInfo.getText().toString();
                            params.addFormDataPart("roomId", room_id);
                            params.addFormDataPart("courseId", courseId);
                            params.addFormDataPart("teachId", ElearnApplication.teachBusinessInfo.getUserId());
                            clientTeachEnterRoom(params);
                        } else {
                            Log.d("ViewHolder", String.format("startTime: %s, startTime: %s, startTime: %s", startTime, endTime, courseName));
                            //点击获取直播房间号
                            params.addFormDataPart("courseId", courseId);
                            params.addFormDataPart("startTime", orderVOS.get(position).getSchoolStartTime());
                            params.addFormDataPart("endTime", orderVOS.get(position).getSchoolEndTime());
                            params.addFormDataPart("roomName", courseName);
                            createRoomByCourse(holder, params);
                        }
                    }
                });
                break;
            case "1":
//                holder.roomCodeText.setText(String.format("课程码：%s", roomId));
                holder.roomCodeText.setText("进入房间");
                holder.roomCodeInfo.setText(roomId);
                break;
            case "2":
                holder.roomCodeText.setText("已完课");
                holder.roomCodeInfo.setText(roomId);
                break;
        }

    }

    @Override
    public int getItemCount() {
//        return titles == null ? 0 : titles.length;
        return orderVOS == null ? 0 : orderVOS.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView tvTime;
        TextView roomCodeText;
        TextView roomCodeInfo;
        TextView tvCourseId;

        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text_view);
            tvTime = view.findViewById(R.id.tv_time);
            roomCodeText = view.findViewById(R.id.tv_room_code_text);
            roomCodeInfo = view.findViewById(R.id.tv_room_code_info);
            tvCourseId = view.findViewById(R.id.tv_course_id);
        }
    }

    /**
     * 获取房间号
     * @param holder
     * @param params
     */
    private void createRoomByCourse(final ViewHolder holder, final RequestParams params){
        HttpRequest.post(Api.POST_CREATE_ROOM_BY_COURSE, params, new JsonHttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                if (jsonObject.getBoolean("success")){
                    JSONObject result = JSONObject.parseObject(jsonObject.getString("result"));
                    String room_id = result.getString("room_id");
                    holder.roomCodeText.setText("进入房间");
                    holder.roomCodeInfo.setText(room_id);
                } else {
                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    Log.d("CREATE_ROOM", jsonObject.getString("code") + ":" + jsonObject.getString("msg"));
                }
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Toast.makeText(context, "网络异常，登陆失败", Toast.LENGTH_SHORT).show();
                Log.d("CREATE_ROOM", errorCode + ":" + msg);
            }
            //请求网络结束
            @Override
            public void onFinish() {
                Log.d("CREATE_ROOM", "请求结束");
            }
        });
    }

    /**
     * 老师客户端进入直播间
     * @param params
     */
    private void  clientTeachEnterRoom(final RequestParams params){
        HttpRequest.post(Api.POST_CLIENT_TEACH_ENTER_ROOM, params, new JsonHttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                //TODO 此处逻辑需确定
                if (jsonObject.getBoolean("success")){
                    JSONObject result = JSONObject.parseObject(jsonObject.getString("result"));
                } else {
                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    Log.d("ENTER_ROOM", jsonObject.getString("code") + ":" + jsonObject.getString("msg"));
                }
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Toast.makeText(context, "网络异常，登陆失败", Toast.LENGTH_SHORT).show();
                Log.d("ENTER_ROOM", errorCode + ":" + msg);
            }
            //请求网络结束
            @Override
            public void onFinish() {
                Log.d("ENTER_ROOM", "请求结束");
            }
        });
    }
}
