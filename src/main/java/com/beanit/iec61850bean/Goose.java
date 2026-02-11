package com.beanit.iec61850bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public final class Goose extends ModelNode {

  private final GooseControlBlock controlBlock;
  @EqualsAndHashCode.Exclude private DataSet dataSet;

  public Goose(ObjectReference objectReference, GooseControlBlock controlBlock, DataSet dataSet) {
    this.objectReference = objectReference;
    this.controlBlock = controlBlock;
    this.dataSet = dataSet;
  }

  void setDataSet(DataSet dataSet) {
    this.dataSet = dataSet;
  }

  @Override
  public ModelNode copy() {
    return new Goose(this.objectReference, this.controlBlock, this.dataSet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getReference().toString());
    sb.append("\n");
    sb.append("enabled: ").append(controlBlock.isEnabled());
    sb.append("\n");
    sb.append("controlBlockReference: ").append(controlBlock.getControlBlockReference());
    sb.append("\n");
    sb.append("destinationMacAddress: ").append(controlBlock.getDestinationMacAddress());
    sb.append("\n");
    sb.append("applicationId: ").append(controlBlock.getApplicationId());
    sb.append("\n");
    sb.append("gooseId: ").append(controlBlock.getGooseId());
    sb.append("\n");
    sb.append("dataSetReference: ").append(controlBlock.getDataSetReference());
    sb.append("\n");
    sb.append("dataSet: ").append(dataSet != null ? dataSet.getReferenceStr() : null);
    sb.append("\n");
    sb.append("vlanId: ").append(controlBlock.getVlanId());
    sb.append("\n");
    sb.append("vlanPriority: ").append(controlBlock.getVlanPriority());
    sb.append("\n");
    sb.append("needCommissioning: ").append(controlBlock.getNeedCommissioning());
    sb.append("\n");
    sb.append("configurationRevision: ").append(controlBlock.getConfigurationRevision());

    return sb.toString();
  }
}
