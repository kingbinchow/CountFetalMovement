package com.king.countfetalmovement;

/**
 * Created by king.zhou on 2016/6/23.
 */
public class MsgShakeEvent {

    private String msg;

    private int value;

    private int amount;

    private long startTime;

    public MsgShakeEvent(String msg) {
        super();
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
