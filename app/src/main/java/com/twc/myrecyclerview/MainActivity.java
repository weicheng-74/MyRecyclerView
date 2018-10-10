package com.twc.myrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.twc.myrecyclerview.adapter.CodeAdapter;
import com.twc.myrecyclerview.bean.CodeInfo;
import com.twc.myrecyclerview.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.relativewlayout)
    RelativeLayout relativewlayout;
    private CodeAdapter codeAdapter;
    private List<CodeInfo> codeInfos = new ArrayList<>();
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            relativewlayout.setFocusable(true);
            relativewlayout.setFocusableInTouchMode(true);
            relativewlayout.requestFocus();
            DeviceUtil.hideKeyboard(MainActivity.this);
            return false;
        }
    };

    public void setTouchListener(View... views) {
        for (int i = 0; i < views.length; i++) {
            views[i].setOnTouchListener(onTouchListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
        codeAdapter.setListener(new CodeAdapter.CloseFoceListener() {

            @Override
            public void close() {

            }
        });
        setTouchListener(recyclerview, relativewlayout);
    }

    /**
     * 初始化
     */
    private void initView() {
        codeAdapter = new CodeAdapter(this, codeInfos);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerview.setAdapter(codeAdapter);
    }

    /**
     * 模拟数据
     */
    private void initData() {
        for (int i = 0; i < 10; i++) {
            codeInfos.add(new CodeInfo(0, "xx" + i));
        }
        codeAdapter.notifyDataSetChanged();
    }
}
