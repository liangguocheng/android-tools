package com.lange.tool.events;

/**
 * Created by liangguocheng on 2018/12/20.
 */

public class EventBusBean<T> {

    public static final int TAG_TEST=10001;




    private int tag;
    private T model;

    public EventBusBean(int tag) {
        this.tag = tag;
    }

    public EventBusBean(int tag, T model) {
        this.tag = tag;
        this.model = model;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
