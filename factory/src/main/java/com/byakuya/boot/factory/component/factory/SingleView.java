package com.byakuya.boot.factory.component.factory;

import com.byakuya.boot.factory.component.device.log.TriColorLedLog;
import lombok.Data;

import java.util.Arrays;

/**
 * Created by ganzl on 2021/2/3.
 */
@Data
class SingleView {
    void setTotalSecond(long total) {
        durationArray = new long[TriColorLedLog.Status.values().length];
        durationArray[TriColorLedLog.Status.NONE.ordinal()] = total;
    }

    void visit(TriColorLedLog log) {
        durationArray[TriColorLedLog.Status.NONE.ordinal()] -= log.getDuration();
        durationArray[log.getStatus().ordinal()] += log.getDuration();
    }

    private long[] durationArray;
    private String[] statusArray = Arrays.stream(TriColorLedLog.Status.values()).map(TriColorLedLog.Status::getName).toArray(String[]::new);
}
