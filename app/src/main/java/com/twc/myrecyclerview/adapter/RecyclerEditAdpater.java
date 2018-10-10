package com.twc.myrecyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.twc.myrecyclerview.R;
import com.twc.myrecyclerview.RecyclerViewEdit;
import com.twc.myrecyclerview.bean.CodeInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerEditAdpater extends RecyclerView.Adapter<RecyclerEditAdpater.ViewHolder> {

    private final Context context;

    private List<CodeInfo> data;

    public RecyclerEditAdpater(RecyclerViewEdit recyclerViewEdit) {
        this.context = recyclerViewEdit;
    }

    public void setData(List<CodeInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.editmsg)
        EditText editmsg;
        @BindView(R.id.linearedit)
        LinearLayout linearedit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.editmsg.setText(data.get(position).getMsg());
        holder.linearedit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.linearedit.setFocusable(true);
                holder.linearedit.setFocusableInTouchMode(true);
                holder.linearedit.requestFocus();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
