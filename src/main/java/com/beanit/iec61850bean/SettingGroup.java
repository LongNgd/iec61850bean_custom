package com.beanit.iec61850bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Getter
public class SettingGroup extends ModelNode {

    private final String controlBlock;
    private final String numberOfSettingGroups;
    private final String activeSettingGroup;
    private final String lastChanged;
    private final String reserveTime;
    private final List<String> affectedLogicalDevices;

    public SettingGroup(
            ObjectReference objectReference,
            String controlBlock,
            String numberOfSettingGroups,
            String activeSettingGroup,
            String lastChanged,
            String reserveTime,
            List<String> affectedLogicalDevices
    ) {
        this.objectReference = objectReference;
        this.controlBlock = controlBlock;
        this.numberOfSettingGroups = numberOfSettingGroups;
        this.activeSettingGroup = activeSettingGroup;
        this.lastChanged = lastChanged;
        this.reserveTime = reserveTime;
        this.affectedLogicalDevices = affectedLogicalDevices;
    }

    @Override
    public ModelNode copy() {
        return new SettingGroup(
                this.objectReference,
                this.controlBlock,
                this.numberOfSettingGroups,
                this.activeSettingGroup,
                this.lastChanged,
                this.reserveTime,
                this.affectedLogicalDevices
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getReference().toString());
        sb.append("\n");
        sb.append("controlBlock: ").append(controlBlock);
        sb.append("\n");
        sb.append("numberOfSettingGroups: ").append(numberOfSettingGroups);
        sb.append("\n");
        sb.append("activeSettingGroup: ").append(activeSettingGroup);
        sb.append("\n");
        sb.append("lastChanged: ").append(lastChanged);
        sb.append("\n");
        sb.append("reserveTime: ").append(reserveTime);
        sb.append("\n");
        sb.append("affectedLogicalDevices: ").append(affectedLogicalDevices);

        return sb.toString();
    }
}