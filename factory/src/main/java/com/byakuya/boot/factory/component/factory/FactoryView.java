package com.byakuya.boot.factory.component.factory;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by ganzl on 2021/1/6.
 */
@Data
class FactoryView {
    FactoryView(LocalDateTime startTime, LocalDateTime endTime, List<MachineStatus> list, int[] totalArr) {
        this.list = list;
        this.timeInterval = initTimeInterval(startTime, endTime);
        this.totalArr = totalArr;
    }

    private String initTimeInterval(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日HH时mm分");
        return String.format("%s  -  %s", formatter.format(startTime), formatter.format(endTime));
    }

    private List<MachineStatus> list;
    private String timeInterval;
    private int[] totalArr;
}
