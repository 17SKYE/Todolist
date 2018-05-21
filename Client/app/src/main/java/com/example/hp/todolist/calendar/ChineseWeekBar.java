package com.example.hp.todolist.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;

import com.example.hp.todolist.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekBar;

/**
 * Created by 石茜 on 2018/5/20.
 */

public class ChineseWeekBar extends WeekBar {
    private int mPreSelectedIndex;
    public ChineseWeekBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.chinese_week_bar, this, true);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDateSelected(Calendar calendar, int weekStart, boolean isClick) {
        getChildAt(mPreSelectedIndex).setSelected(false);
        int viewIndex = getViewIndexByCalendar(calendar, weekStart);
        getChildAt(viewIndex).setSelected(true);
        mPreSelectedIndex = viewIndex;
    }
}
