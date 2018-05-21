package com.example.hp.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.fastaccess.datetimepicker.DatePickerFragmentDialog;
import com.fastaccess.datetimepicker.DateTimeBuilder;
import com.fastaccess.datetimepicker.callback.DatePickerCallback;
import com.fastaccess.datetimepicker.callback.TimePickerCallback;
import com.necer.ndialog.NDialog;

import java.util.Calendar;

import static com.example.hp.todolist.SampleHelper.getDateAndTime;

public class DetailsActivity extends AppCompatActivity implements DatePickerCallback, TimePickerCallback, View.OnClickListener {
    private MyDB myDB;
    private CheckBox finishCB;
    private Button dateBtn;
    private EditText titleET;
    private EditText descriptionET;
    private String originalTitle;
    private String newTitle;
    private String description;
    private String date;

    //
    private String repeat_type;
    private String priority;

    private Button repeatBtn,priorityBtn;
    private String[] repeatList={"每天重复","每周重复","每月重复"};//重复方式列表
    private String[] priorityList={"高优先级","中优先级","低优先级"};
    //private TextView tv_repeatMenuContext;
  //  tv_repeatMenuContext=(TextView) findViewById(R.id.tv_repeatMenuContext);
   // registerForContextMenu(tv_repeatMenuContext);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getId();//获取控件id
        click();//将按钮绑定点击事件

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarInDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new MyOnMenuItemClickListener());

        finishCB = (CheckBox) findViewById(R.id.finishCBinDetails);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        titleET = (EditText) findViewById(R.id.titleET);
        descriptionET = (EditText) findViewById(R.id.descriptionET);
        //引入重复按钮
        repeatBtn = (Button) findViewById(R.id.repeatBtn);
        priorityBtn=(Button) findViewById(R.id.priorityBtn);

        myDB = new MyDB(this);

        // 用传入的title检索数据库，据此填充详情页信息
        Intent intent = getIntent();
        originalTitle = intent.getStringExtra("title");
        if (!originalTitle.equals("")) {
            newTitle = originalTitle;
            titleET.setText(originalTitle);
            String[] info = myDB.findInfo(originalTitle);
            description = info[0];
            date = info[1];
            if (description != null) descriptionET.setText(description);
            if (date != null) {
                dateBtn.setText(date);
                dateBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            if (info[2] != null && info[2].equals("true")) {
                finishCB.setChecked(true);
            }
        }

        finishCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newTitle = titleET.getText().toString();
                if (newTitle.equals("")) {
                    Toast.makeText(DetailsActivity.this, R.string.empty_title, Toast.LENGTH_SHORT).show();
                    buttonView.setChecked(!isChecked);
                }
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar minDate = Calendar.getInstance();
               DatePickerFragmentDialog.newInstance(
                        DateTimeBuilder.get()
                                .withTime(true)
                                .with24Hours(true)
                                .withCurrentHour(12)
                                .withCurrentMinute(30)
                                .withTheme(R.style.PickersTheme))
                        .show(getSupportFragmentManager(), "DatePickerFragmentDialog");

            }


        });





    }

    //获取控件id
    private void getId() {
        repeatBtn=(Button)findViewById(R.id.repeatBtn);
        priorityBtn=(Button)findViewById(R.id.priorityBtn);
    }
    //将按钮绑定点击事件
    private void click(){
        repeatBtn.setOnClickListener(this);
        priorityBtn.setOnClickListener(this);
    }

  //  @Override
    public void onDateSet(long m) {
        dateBtn.setText(String.valueOf(m));
    }

    /**
     * @param timeOnly
     *         time only
     * @param dateWithTime
     *         full date with time
     */
//    @Override
    //选定日期后更改时间
    public void onTimeSet(long timeOnly, long dateWithTime) {
        dateBtn.setText(String.format("%s", getDateAndTime(dateWithTime)));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
     public void onClick(View view) {
        switch (view.getId()){
            case R.id.repeatBtn:
                showRepeatDialog();//选择重复类型的对话框
                break;
            case R.id.priorityBtn:
                showPriorityDialog();//选择优先级的对话框
                break;
        }
    }
    //重复类型选择对话框
    private void showRepeatDialog(){
        new NDialog(this)
                .setItems(repeatList)
                .setItemGravity(Gravity.LEFT)
                .setItemColor(Color.parseColor("#AEAAA5"))
                .setItemHeigh(50)
                .setItemSize(16)
                .setDividerHeigh(1)
                .setAdapter(null)
                .setDividerColor(Color.parseColor("#E5E5E5"))
                .setHasDivider(true)
                .setOnChoiceListener(new NDialog.OnChoiceListener() {
                    @Override
                    public void onClick(String item, int which) {
                        Toast.makeText(DetailsActivity.this, "选择了：：" + item, Toast.LENGTH_SHORT).show();
                        repeatBtn.setText(String.format("%s", item));
                    }
                }).create(NDialog.CHOICE).show();
    }
    //优先级选择对话框
    private void showPriorityDialog(){

        new NDialog(this)
                .setItems(priorityList)
                .setItemGravity(Gravity.LEFT)
                .setItemColor(Color.parseColor("#AEAAA5"))
                .setItemHeigh(50)
                .setItemSize(16)
                .setDividerHeigh(1)
                .setAdapter(null)
                .setDividerColor(Color.parseColor("#112321"))
                .setHasDivider(true)
                .setOnChoiceListener(new NDialog.OnChoiceListener() {
                    @Override
                    public void onClick(String item, int which) {
                        Toast.makeText(DetailsActivity.this, "选择了：：" + item, Toast.LENGTH_SHORT).show();
                        priorityBtn.setText(String.format("%s", item));
                    }
                }).create(NDialog.CHOICE).show();
    }

    private class MyOnMenuItemClickListener implements Toolbar.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_save_in_details:
                    date = dateBtn.getText().toString();
                    newTitle = titleET.getText().toString();
                    description = descriptionET.getText().toString();
                    //优先级和重复类型
                    repeat_type=repeatBtn.getText().toString();
                    priority=priorityBtn.getText().toString();
                    if(priority.equals("高优先级")){

                    }

                    if (newTitle.equals("")) {
                        Toast.makeText(DetailsActivity.this, R.string.empty_title, Toast.LENGTH_SHORT).show();
                    } else if (!originalTitle.equals(newTitle) && myDB.ifExists(newTitle)) {
                        Toast.makeText(DetailsActivity.this, R.string.same_title, Toast.LENGTH_SHORT).show();
                    } else {
                        if (originalTitle.equals("")) {
                            myDB.insert(newTitle, description, date);
                        } else {
                            myDB.update(originalTitle, newTitle, description, date);
                        }
                        if (finishCB.isChecked()) {
                            myDB.finishThis(newTitle);
                        } else {
                            myDB.unfinishThis(newTitle);
                        }
                        sendBroadcast(new Intent(NewAppWidget.ACTION_UPDATE_ALL));
                        finish();
                        overridePendingTransition(R.anim.fin_anim, R.anim.fout_anim);
                    }
                    break;
                case R.id.action_delete:
                    if (myDB.ifExists(originalTitle)) {
                        myDB.delete(originalTitle);
                    }
                    sendBroadcast(new Intent(NewAppWidget.ACTION_UPDATE_ALL));
                    finish();
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}
