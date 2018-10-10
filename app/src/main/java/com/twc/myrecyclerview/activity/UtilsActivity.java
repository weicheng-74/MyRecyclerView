package com.twc.myrecyclerview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.twc.myrecyclerview.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UtilsActivity extends AppCompatActivity {

    @BindView(R.id.libaiedit)
    EditText libaiedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils);
        ButterKnife.bind(this);
    }
}
