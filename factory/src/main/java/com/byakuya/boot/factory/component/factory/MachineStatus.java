package com.byakuya.boot.factory.component.factory;

import lombok.Data;

/**
 * Created by ganzl on 2021/1/6.
 */
@Data
class MachineStatus {
    private String detail;
    private String deviceId;
    private String duration;
    private String name;
    private String operator;
    private String serialNumber;
    private String status;
    private String type;
}
