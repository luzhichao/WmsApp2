package com.linkingwin.elearn.teacher.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;

import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.widget.BaseHomeActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.SuperCalendarPicker.ThemeDayView;
import com.linkingwin.elearn.teacher.SuperCalendarPicker.WorkSpaceAdapter;
import com.linkingwin.elearn.teacher.adapter.StudentAdapter;
import com.linkingwin.elearn.teacher.model.OrderVO;
import com.linkingwin.elearn.teacher.model.StudentVO;
import com.linkingwin.elearn.teacher.widget.BottomBar;
import com.linkingwin.elearn.teacher.widget.CustomDayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.linkingwin.elearn.common.http.Api.BOTTOM_BAR_WORKSPACE;


public class WorkSpace extends BaseHomeActivity {
    //????????????
    @BindView(R.id.show_year_view)
    TextView tvYear;
    @BindView(R.id.show_month_view)
    TextView tvMonth;
    @BindView(R.id.back_today_button)
    TextView backToday;
    @BindView(R.id.content)
    CoordinatorLayout content;
    @BindView(R.id.calendar_view)
    MonthPager monthPager;
    @BindView(R.id.list)
    RecyclerView rvToDoList;
    @BindView(R.id.next_month)
    TextView nextMonthBtn;
    @BindView(R.id.last_month)
    TextView lastMonthBtn;

    @BindView(R.id.bottom_bar)
    BottomNavigationBar bt_bottomBar;
    @BindView(R.id.tv_title)
    TitleView titleView;

    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;
    private List<OrderVO> orderVOS = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_work_space );
        //????????????????????????????????????????????????
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        ElearnApplication.bottomBar = new BottomBar( getContext() );
        ElearnApplication.bottomBar.initBar( bt_bottomBar, BOTTOM_BAR_WORKSPACE );

        titleView.setTitleText( "?????????", R.color.titleWhite ).hideBackIcon();
        //????????????setViewHeight??????????????????????????????????????????

        context = this;
        //????????????setViewHeight??????????????????????????????????????????
        monthPager.setViewHeight( Utils.dpi2px( context, 270 ) );
        rvToDoList.setHasFixedSize( true );
        //????????????????????? ?????????listview
        rvToDoList.setLayoutManager( new LinearLayoutManager( this ) );
//        rvToDoList.setAdapter( new WorkSpaceAdapter( this, orderVOS ) );
        initCurrentDate();
        initCalendarView();
        initToolbarClickListener();
        Log.e("ldf","OnCreated");
    }

    /**
     * onWindowFocusChanged??????????????????????????????????????????????????????
     * @return void
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged( hasFocus );
        if (hasFocus && !initiated) {
            refreshMonthPager();
            initiated = true;
        }
    }

    /*
     * ???????????????????????????????????????????????????onResume?????????
     * Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
     * calendarAdapter.switchToWeek(monthPager.getRowIndex());
     * */
    @Override
    protected void onResume() {
        super.onResume();
        Utils.scrollTo( content, rvToDoList, monthPager.getCellHeight(), 200 );
        calendarAdapter.switchToWeek( monthPager.getRowIndex() );
    }

    /**
     * ????????????????????????listener
     *
     * @return void
     */
    private void initToolbarClickListener() {
        backToday.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackToDayBtn();
            }
        } );

        nextMonthBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthPager.setCurrentItem( monthPager.getCurrentPosition() + 1 );
            }
        } );
        lastMonthBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthPager.setCurrentItem( monthPager.getCurrentPosition() - 1 );
            }
        } );
    }

    /**
     * ?????????currentDate
     *
     * @return void
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        tvYear.setText( currentDate.getYear() + "???" );
        tvMonth.setText( currentDate.getMonth() + "" );
    }

    /**
     * ?????????CustomDayView????????????CalendarViewAdapter???????????????
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView( context, R.layout.activity_custom_day );
        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.WeekArrayType.Monday,
                customDayView );
        calendarAdapter.setOnCalendarTypeChangedListener( new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                rvToDoList.scrollToPosition( 0 );
            }
        } );
        initMarkData();
        initMonthPager();
    }

    /**
     * ????????????????????????HashMap????????????????????????
     * ????????????????????????????????????setMarkData???????????? calendarAdapter.notifyDataChanged();
     */
    private void initMarkData() {
        HashMap<String, String> markData = new HashMap<>();
//        markData.put( "2017-8-9", "1" );
//        markData.put( "2017-7-9", "0" );
//        markData.put( "2017-6-9", "1" );
//        markData.put( "2017-6-10", "0" );
        calendarAdapter.setMarkData( markData );
        calendarAdapter.notifyDataChanged();
    }

    /**
     * ??????????????????
     */
    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate( date );
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //????????? -1????????????????????????????????? ??? 1?????????????????????????????????
                monthPager.selectOtherMonth( offset );
            }
        };
    }

    /**
     * ????????????????????????
     * @param date
     */
    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        tvYear.setText( date.getYear() + "???" );
        tvMonth.setText( date.getMonth() + "" );
        //????????????
        findCourseByWorkspace(date.toString());
    }

    /**
     * ?????????monthPager???MonthPager?????????ViewPager
     *
     * @return void
     */
    private void initMonthPager() {
        monthPager.setAdapter( calendarAdapter );
        monthPager.setCurrentItem( MonthPager.CURRENT_DAY_INDEX );
        monthPager.setPageTransformer( false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt( 1 - Math.abs( position ) );
                page.setAlpha( position );
            }
        } );
        monthPager.addOnPageChangeListener( new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get( position % currentCalendars.size() ) != null) {
                    CalendarDate date = currentCalendars.get( position % currentCalendars.size() ).getSeedDate();
                    currentDate = date;
                    tvYear.setText( date.getYear() + "???" );
                    tvMonth.setText( date.getMonth() + "" );
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        } );
    }

    public void onClickBackToDayBtn() {
        refreshMonthPager();
    }

    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged( today );
        tvYear.setText( today.getYear() + "???" );
        tvMonth.setText( today.getMonth() + "" );
        refreshClickDate(today);
    }

    /**
     * ????????????????????????
     * @param date
     */
    private void findCourseByWorkspace (String date){
        //????????????ID
        String userID = ElearnApplication.teachBusinessInfo.getUserId();
        RequestParams params = RequestParamsBuilder.buildRequestParams( this );
        params.addFormDataPart("teachId", userID);
        params.addFormDataPart("schoolTime", date);
        HttpRequest.post(Api.POST_COURSE_BY_WORKSPACE, params, new JsonHttpRequestCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                JSONArray result = jsonObject.getJSONArray("result");
                if(result != null && result.size() > 0){
                    List<OrderVO> datas = new ArrayList<>();
                    for(int i=0;i<result.size();i++){
                        OrderVO orderVO = new OrderVO();
                        orderVO.setId(result.getObject(i, OrderVO.class).getId());
                        orderVO.setCourseId(result.getObject(i, OrderVO.class).getCourseId());
                        orderVO.setCourseName(result.getObject(i, OrderVO.class).getCourseName());
                        orderVO.setSchoolStartTime(result.getObject(i, OrderVO.class).getSchoolStartTime());
                        orderVO.setSchoolEndTime(result.getObject(i, OrderVO.class).getSchoolEndTime());
                        orderVO.setCourseStatus(result.getObject(i, OrderVO.class).getCourseStatus());
                        orderVO.setRoomId(result.getObject(i, OrderVO.class).getRoomId());

                        datas.add(orderVO);
                    }
//                    orderVOS = datas;
                    rvToDoList.setAdapter( new WorkSpaceAdapter( WorkSpace.this, datas ) );
                }

            }

            //?????????????????????????????????JSON????????????????????????????????????
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText(WorkSpace.this, "???????????????????????????", LENGTH_SHORT).show();
                Log.d("findCourseByWorkspace", errorCode + ":" + msg);
            }

            //??????????????????
            @Override
            public void onFinish() {
                Log.d("findCourseByWorkspace", "????????????");
            }
        });
    }
}
