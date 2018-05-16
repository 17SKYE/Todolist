package com.example.hp.todolist;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 石茜 on 2018/5/15.
 */

public class MyFragment extends Fragment {
    private static Bundle bundle=new Bundle();

    public static  MyFragment newInstance(String content){
        MyFragment myFragment=new MyFragment();
        bundle.putString("content",content); //压入数据
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content);
        txt_content.setText(bundle.getString("content"));
        return view;
    }
}
