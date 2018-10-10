package com.twc.myrecyclerview.bean;

public class CodeInfo {
    private int isSelect;
    private String msg;

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CodeInfo(int isSelect, String msg) {
        this.isSelect = isSelect;
        this.msg = msg;
    }
}
