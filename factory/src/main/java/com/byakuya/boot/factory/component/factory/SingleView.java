package com.byakuya.boot.factory.component.factory;

import com.byakuya.boot.factory.component.device.log.TriColorLedLog;
import lombok.Data;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by ganzl on 2021/2/3.
 */
@Data
class SingleView {
    void addDuration(long red, long yellow, long green) {
        redDurations.add(red);
        yellowDurations.add(yellow);
        greenDurations.add(green);
        if (redDurations.size() > 1000) {
            redDurations.removeFirst();
            yellowDurations.removeFirst();
            greenDurations.removeFirst();
        }
    }

    void addLog(TriColorLedLog log) {
        durationArray[TriColorLedLog.Status.NONE.ordinal()] -= log.getDuration();
        durationArray[log.getStatus().ordinal()] += log.getDuration();
        logs.add(new Item(log));
        if (logs.size() > 1000) {
            logs.removeFirst();
        }
    }

    void setTotalSecond(long total) {
        durationArray = new long[TriColorLedLog.Status.values().length];
        durationArray[TriColorLedLog.Status.NONE.ordinal()] = total;
    }

    private long[] durationArray;
    private LinkedList<Long> greenDurations = new LinkedList<>();
    private LinkedList<Item> logs = new LinkedList<>();
    private LinkedList<Long> redDurations = new LinkedList<>();
    private String[] statusArray = Arrays.stream(TriColorLedLog.Status.values()).map(TriColorLedLog.Status::getName).toArray(String[]::new);
    private LinkedList<Long> yellowDurations = new LinkedList<>();

    @Data
    public static class Item {
        Item(TriColorLedLog log) {
            this.name = log.getStatus().getName();
            this.value = new long[2];
            this.value[0] = log.getTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
            this.value[1] = log.getDuration();
        }

        private String name;
        private long[] value;
    }
}
