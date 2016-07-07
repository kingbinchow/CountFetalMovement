package com.king.countfetalmovement;

/**
 * Created by king.zhou on 2016/6/23.
 */
public class MsgUpdateEvent {

    private String msg;

    public MsgUpdateEvent(String msg) {
        super();
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
}
