package com.example.hp.todolist;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;

import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    MyDB myDB;
    DrawerLayout mDrawerLayout;
    MySensor mySensor;
    TextView head_iv;

    //17 add
    //UI Object
    private TextView txt_remind;
    private TextView txt_calendar;
    private TextView txt_timeBlock;
    private TextView txt_setting;
    private FrameLayout frame_content;
    //Fragment Object
    private  FragmentRemind fragmentRemind;
    private MyFragment fg1,fg2,fg3,fg4;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //调用父类方法
        super.onCreate(savedInstanceState);
        //状态栏透明
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //加载布局文件
        setContentView(R.layout.activity_main);

        //17 add
        fragmentManager=getFragmentManager();
        bindViews();
        txt_remind.performClick();
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#f8bE12"));

        myDB = new MyDB(this);
        mySensor = new MySensor(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mainDrawerLayout);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // 首先看看当前是不是用用户登录了
        SharedPreferences preferences = getSharedPreferences("UserNameData", MODE_PRIVATE);
        String id = preferences.getString("id", "guest");
        mySensor.registerSensor();
        sendBroadcast(new Intent(NewAppWidget.ACTION_UPDATE_ALL));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySensor.unRegisterSensor();
        sendBroadcast(new Intent(NewAppWidget.ACTION_UPDATE_ALL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                String toShare = "";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                Map<String, Vector<String>> map = myDB.queryAll(1);
                Vector<String> titles = map.get("titles");
                int q = titles.size();
                Log.i("map_size", Integer.toString(q));
                if (q == 0) {
                    toShare = "爸爸的任务全部完成啦！";
                } else {
                    toShare = "还有任务没有完成啊...\n……………………\n好吧我就是想要测试一下分享出去没^_^\n";
                    Vector<String> dates = map.get("dates");
                    for (int i = 0; i < titles.size(); i++) {
                        toShare = toShare + "Task" + Integer.toString(i + 1) + ": " + titles.get(i).toString() +
                                "\nDDL: " + dates.get(i).toString() + "\n";
                    }
                }
                intent.putExtra(Intent.EXTRA_TEXT, toShare);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "share"));
                break;
            default:
                break;
        }
        return true;
    }

    //17 add
    //UI组件初始化与事件绑定
    private void bindViews(){
        txt_remind=(TextView)findViewById(R.id.bar_remind);
        txt_calendar=(TextView)findViewById(R.id.bar_calendrier);
        txt_timeBlock=(TextView)findViewById(R.id.bar_timeBlock);
        txt_setting=(TextView)findViewById(R.id.bar_setter);
        frame_content=(FrameLayout)findViewById(R.id.frame_content);

        txt_remind.setOnClickListener(this);
        txt_calendar.setOnClickListener(this);
        txt_timeBlock.setOnClickListener(this);
        txt_setting.setOnClickListener(this);
    }
    //重置所有文本的选中状态
    private void setSelected(){
        txt_remind.setSelected(false);
        txt_calendar.setSelected(false);
        txt_timeBlock.setSelected(false);
        txt_setting.setSelected(false);
    }
    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fragmentRemind != null)fragmentTransaction.hide(fragmentRemind);
        if(fg2 != null)fragmentTransaction.hide(fg2);
        if(fg3 != null)fragmentTransaction.hide(fg3);
        if(fg4 != null)fragmentTransaction.hide(fg4);
    }
    @Override
    public void onClick(View v){
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch(v.getId()){
            case R.id.bar_remind:
                setSelected();
                txt_remind.setSelected(true);
                if(fragmentRemind==null)
                {
                    fragmentRemind= new FragmentRemind();
                    fragmentTransaction.add(R.id.frame_content,fragmentRemind);
                }
                else{
                    fragmentTransaction.show(fragmentRemind);
                }
                break;
            case R.id.bar_calendrier:
                setSelected();
                txt_calendar.setSelected(true);
                if(fg2==null)
                {
                    fg2= MyFragment.newInstance("第二个Fragment");
                    fragmentTransaction.add(R.id.frame_content,fg2);
                }
                else{
                    fragmentTransaction.show(fg2);
                }
                break;
            case R.id.bar_timeBlock:
                setSelected();
                txt_timeBlock.setSelected(true);
                if(fg3==null)
                {
                    fg3= MyFragment.newInstance("第三个Fragment");
                    fragmentTransaction.add(R.id.frame_content,fg3);
                }
                else{
                    fragmentTransaction.show(fg3);
                }
                break;
            case R.id.bar_setter:
                setSelected();
                txt_setting.setSelected(true);
                if(fg4==null)
                {
                    fg4= MyFragment.newInstance("第四个Fragment");
                    fragmentTransaction.add(R.id.frame_content,fg4);
                }
                else{
                    fragmentTransaction.show(fg4);
                }
                break;
        }
        fragmentTransaction.commit();
    }
}
