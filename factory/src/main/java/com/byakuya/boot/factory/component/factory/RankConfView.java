package com.byakuya.boot.factory.component.factory;

import com.byakuya.boot.factory.component.factory.machine.Machine;
import lombok.Data;

import java.util.List;

/**
 * Created by ganzl on 2021/4/26.
 */
@Data
public class RankConfView {

    @Data
    public static class GroupItem implements Comparable<GroupItem> {
        @Override
        public int compareTo(GroupItem o) {
            return this.name.compareTo(o.name);
        }

        private List<ConfItem> items;
        private String name;
    }

    @Data
    public static class ConfItem {
        public ConfItem(Machine machine, String conf) {
            this.id = machine.getId();
            this.name = machine.getOrdering();
            //noinspection ConstantConditions
            this.checked = (conf != null && conf.contains(machine.getId()));
        }

        private boolean checked;
        private String id;
        private String name;
    }
}
