package com.twc.myrecyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twc.myrecyclerview.MainActivity;
import com.twc.myrecyclerview.R;
import com.twc.myrecyclerview.bean.CodeInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.ViewHolder> {


    private Context context;
    private List<CodeInfo> codInfos = new ArrayList<>();
    private CloseFoceListener listener;

    public CodeAdapter(MainActivity mainActivity, List<CodeInfo> codeInfos) {
        this.context = mainActivity;
        this.codInfos = codeInfos;
    }

    private int TYPE_LAST = 1000;
    private int TYPE_COMMAND = 1;
    private int TYPE_EDIT = 2;


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_LAST;
        } else if (codInfos.get(position).getMsg() == null || codInfos.get(position).getMsg().length() == 0) {
            return TYPE_EDIT;
        } else {
            return TYPE_COMMAND;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LAST) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_add, parent, false);
            return new AddViewHolder(view);
        } else if (viewType == TYPE_COMMAND) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_command, parent, false);
            return new CommandViewHolder(view);
        } else if (viewType == TYPE_EDIT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_edit, parent, false);
            return new EditViewHolder(view);
        }
        return null;
    }

    public void setListener(CloseFoceListener listener) {
        this.listener = listener;
    }

    public interface CloseFoceListener {
        void close();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (holder instanceof AddViewHolder) {
            final AddViewHolder addViewHolder = (AddViewHolder) holder;
            addViewHolder.lineadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    codInfos.add(new CodeInfo(0, null));
                    listener.close();
                    notifyDataSetChanged();
                }
            });


        } else if (holder instanceof CommandViewHolder) {
            //系统默认的数据
            if (codInfos.get(position).getMsg().equals("") || codInfos.get(position).getMsg() == null) {
                ((CommandViewHolder) holder).addmsg.setText("");
            } else {
                ((CommandViewHolder) holder).addmsg.setText(codInfos.get(position).getMsg());
            }
            ((CommandViewHolder) holder).commandline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (codInfos.get(position).getIsSelect() == 0) {

                    } else if (codInfos.get(position).getIsSelect() == 1) {

                    }

                }
            });
        } else if (holder instanceof EditViewHolder) {
            final EditViewHolder editViewHolder = (EditViewHolder) holder;
            editViewHolder.editmsg.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return codInfos.size() + 1;
    }

    public class EditViewHolder extends ViewHolder {
        @BindView(R.id.editmsg)
        EditText editmsg;
        @BindView(R.id.linearedit)
        LinearLayout linearedit;

        public EditViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CommandViewHolder extends ViewHolder {
        @BindView(R.id.addmsg)
        TextView addmsg;
        @BindView(R.id.commandline)
        LinearLayout commandline;

        public CommandViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public class AddViewHolder extends ViewHolder {
        @BindView(R.id.textadd)
        TextView textadd;
        @BindView(R.id.lineadd)
        LinearLayout lineadd;

        public AddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
