/*
 * Created by 石茜 on 2018/5/18.
 */
package com.example.hp.todolist;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class FragmentCalendar extends Fragment implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnDateLongClickListener,
        View.OnClickListener{

    TextView mTextMonthDay;

    //TextView mTextToDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;

    private int mYear;
    CalendarLayout mCalendarLayout;

    MyDB  myDB;

    AddFloatingActionButton addButton;

    public FragmentCalendar() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_bar_calendar, container, false);
        mTextMonthDay=(TextView)view.findViewById(R.id.tv_month_day);
        //mTextToDay=(TextView)view.findViewById(R.id.current_day);
        mTextYear = (TextView) view.findViewById(R.id.tv_year);
        mTextLunar = (TextView) view.findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) view.findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) view.findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        view.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = (CalendarLayout) view.findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnDateLongClickListener(this, true);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        //mTextToDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        myDB=new MyDB(getActivity());
        //先从数据库中全部取出来？
        Map<String, Vector<String>> allTask = myDB.queryAll(MyDB.ALL);
        List<Calendar> schemes = new ArrayList<>();


        for (int i = 0; i < allTask.get("dates").size(); i++) {
            String taskDate=allTask.get("dates").get(i);
            int year=Integer.parseInt(taskDate.split("年")[0]);
            int month=Integer.parseInt(taskDate.split("年")[1].split("月")[0]);
            String extra=taskDate.split("年")[1].split("月")[1];
            int day=Integer.parseInt(extra.split("日")[0]);
            Log.d("information",year+"...."+month+"....."+day);
            schemes.add(getSchemeCalendar(year, month, day, Color.parseColor("#FDE8AA"), "事"));
        }
        mCalendarView.setSchemeDate(schemes);

        //添加任务
        addButton = (AddFloatingActionButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("title", "");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_anim, R.anim.out_anim);
            }
        });
        //获取当前日期，显示当前日期的任务
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        String currentDate="";
        if ((calendar.get(java.util.Calendar.MONTH) + 1) < 10) {
            currentDate = calendar.get(java.util.Calendar.YEAR) + "年0" + (calendar.get(java.util.Calendar.MONTH) + 1) + "月" + calendar.get(java.util.Calendar.DAY_OF_MONTH) + "日";
            Log.d("today = ", currentDate);
        } else {
            currentDate = calendar.get(java.util.Calendar.YEAR) + "年" + (calendar.get(java.util.Calendar.MONTH) + 1) + "月" + calendar.get(java.util.Calendar.DAY_OF_MONTH) + "日";
            Log.d("today = ", currentDate);
        }

        ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.the_day_task, calendarFragment.newInstance(currentDate)).commit();

        return view;
    }
    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }
    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        //进入任务详情页
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();

        //选择的日期
        String selectDate="";
        if(calendar.getMonth()<10&&calendar.getDay()<10){
            selectDate=calendar.getYear()+"年0"+calendar.getMonth()+"月0"+calendar.getDay()+"日";
            Log.d("today = ", selectDate);
        }
        else{
            if(calendar.getMonth()<10&&calendar.getDay()>=10)
            {
                selectDate=calendar.getYear()+"年0"+calendar.getMonth()+"月"+calendar.getDay()+"日";
            }
            else{
                if(calendar.getMonth()>=10&&calendar.getDay()<10)
                {
                    selectDate=calendar.getYear()+"年"+calendar.getMonth()+"月0"+calendar.getDay()+"日";
                }
                else{
                    selectDate=calendar.getYear()+"年"+calendar.getMonth()+"月"+calendar.getDay()+"日";
                }
            }
            Log.d("today = ", selectDate);
        }
        if (isClick) {
            //Toast.makeText(this, getCalendarText(calendar), Toast.LENGTH_SHORT).show();
            //这里是需要下面显示当日的任务
            ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.the_day_task, calendarFragment.newInstance(selectDate)).commit();
        }
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onDateLongClick(Calendar calendar) {

    }

    @Override
    public void onMonthChange(int year, int month) {

    }
}
