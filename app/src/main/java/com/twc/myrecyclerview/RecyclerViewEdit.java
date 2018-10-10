package com.twc.myrecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.twc.myrecyclerview.adapter.RecyclerEditAdpater;
import com.twc.myrecyclerview.bean.CodeInfo;
import com.twc.myrecyclerview.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewEdit extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.linemain)
    LinearLayout linemain;
    private RecyclerEditAdpater recyclerEditAdpater;
    private List<CodeInfo> stringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_edit);
        ButterKnife.bind(this);
        recyclerEditAdpater = new RecyclerEditAdpater(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(recyclerEditAdpater);
        stringList.add(new CodeInfo(1, "xxx"));
        stringList.add(new CodeInfo(1, "xxx"));
        stringList.add(new CodeInfo(1, "xxx"));
        stringList.add(new CodeInfo(1, "xxx"));
        stringList.add(new CodeInfo(1, "xxx"));
        stringList.add(new CodeInfo(1, "xxx"));
        recyclerEditAdpater.setData(stringList);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringList.add(new CodeInfo(1, "add"));
            }
        });
        recyclerview.setOnTouchListener(onTouchListener);
        add.setOnTouchListener(onTouchListener);
        linemain.setOnTouchListener(onTouchListener);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            linemain.setFocusable(true);
            linemain.setFocusableInTouchMode(true);
            linemain.requestFocus();
            DeviceUtil.hideKeyboard(RecyclerViewEdit.this);
            return false;
        }
    };




}
