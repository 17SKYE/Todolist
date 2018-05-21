package com.example.hp.todolist;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 石茜 on 2018/5/16.
 */

public class FragmentRemind extends Fragment {
    Toolbar toolbar;
    MyDB  myDB;
    DrawerLayout mDrawerLayout;
    TextView head_iv;
    public FragmentRemind(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //调用父类方法
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_bar_remind, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbarInRemind);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        myDB=new MyDB(getActivity());
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.RemindLayout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();//初始化状态
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        NavigationView mNavigationView = (NavigationView) view.findViewById(R.id.navigation_view);
        ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.remind_content, new taskFragment()).commit();

        // 获取头部控件
        // 点击之后跳转到登陆注册界面
        View headview = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        head_iv = (TextView) headview.findViewById(R.id.id_username);
        head_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!head_iv.getText().toString().equals("guest")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("注意!")
                            .setMessage("当前用户为：" + head_iv.getText().toString() + ",是否需要重新登录或切换用户？")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent1);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
                else {
                    Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent1);
                }
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.search:
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.collection:
                        ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.remind_content, new collectionFragment()).commit();
                        toolbar.setTitle(R.string.collect_box);
                        break;
                    case R.id.today_task:
                        ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.remind_content, new taskFragment()).commit();
                        toolbar.setTitle(R.string.today);
                        break;
                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        return view;
    }

}
