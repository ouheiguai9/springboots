package com.byakuya.boot.factory.component.factory;

import org.springframework.data.util.Pair;

import java.time.LocalDateTime;

/**
 * Created by ganzl on 2021/1/13.
 */
public enum TimeType {
    S("当前班次"), D("当日"), D3("最近三日"), W("本周"), M("当月"), Y("本年"), X("自定义");

    TimeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    private String name;
}
