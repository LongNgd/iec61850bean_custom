package com.beanit.iec61850bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Getter
public final class GooseControlBlock extends ModelNode {

    private final boolean enabled;

    /** ldInst/lnClass/lnInst$GSEControl.name */
    private final String controlBlockReference;

    /** MAC-Address */
    private final String destinationMacAddress;

    /** APPID */
    private final String applicationId;

    /** <GSEControl gooseID="..."> */
    private final String gooseId;

    /** DataSet reference */
    private final String dataSetReference;

    /** VLAN-ID */
    private final String vlanId;

    /** VLAN-PRIORITY */
    private final String vlanPriority;

    /** needCommissioning="true|false" */
    private final String needCommissioning;

    /** confRev */
    private final String configurationRevision;

    public GooseControlBlock(
            ObjectReference objectReference,
            boolean enabled,
            String controlBlockReference,
            String destinationMacAddress,
            String applicationId,
            String gooseId,
            String dataSetReference,
            String vlanId,
            String vlanPriority,
            String needCommissioning,
            String configurationRevision
    ) {
        this.objectReference = objectReference;
        this.enabled = enabled;
        this.controlBlockReference = controlBlockReference;
        this.destinationMacAddress = destinationMacAddress;
        this.applicationId = applicationId;
        this.gooseId = gooseId;
        this.dataSetReference = dataSetReference;
        this.vlanId = vlanId;
        this.vlanPriority = vlanPriority;
        this.needCommissioning = needCommissioning;
        this.configurationRevision = configurationRevision;
    }

    @Override
    public ModelNode copy() {
        return new GooseControlBlock(
                this.objectReference,
                this.enabled,
                this.controlBlockReference,
                this.destinationMacAddress,
                this.applicationId,
                this.gooseId,
                this.dataSetReference,
                this.vlanId,
                this.vlanPriority,
                this.needCommissioning,
                this.configurationRevision
        );
    }
}