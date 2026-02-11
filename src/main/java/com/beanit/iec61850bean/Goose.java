package com.beanit.iec61850bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public final class Goose extends ModelNode {

  private final boolean enabled;
  private final String controlBlockReference;
  private final String destinationMacAddress;
  private final String applicationId;
  private final String gooseId;
  private final String dataSetReference;
  @EqualsAndHashCode.Exclude private DataSet dataSet;
  private final String vlanId;
  private final String vlanPriority;
  private final String needCommissioning;
  private final String configurationRevision;

  public Goose(
      ObjectReference objectReference,
      boolean enabled,
      String controlBlockReference,
      String destinationMacAddress,
      String applicationId,
      String gooseId,
      String dataSetReference,
      DataSet dataSet,
      String vlanId,
      String vlanPriority,
      String needCommissioning,
      String configurationRevision) {
    this.objectReference = objectReference;
    this.enabled = enabled;
    this.controlBlockReference = controlBlockReference;
    this.destinationMacAddress = destinationMacAddress;
    this.applicationId = applicationId;
    this.gooseId = gooseId;
    this.dataSetReference = dataSetReference;
    this.dataSet = dataSet;
    this.vlanId = vlanId;
    this.vlanPriority = vlanPriority;
    this.needCommissioning = needCommissioning;
    this.configurationRevision = configurationRevision;
  }

  void setDataSet(DataSet dataSet) {
    this.dataSet = dataSet;
  }

  public String getDataSetReference() {
    return dataSetReference;
  }

  public DataSet getDataSet() {
    return dataSet;
  }

  @Override
  public ModelNode copy() {
    return new Goose(
        this.objectReference,
        this.enabled,
        this.controlBlockReference,
        this.destinationMacAddress,
        this.applicationId,
        this.gooseId,
        this.dataSetReference,
        this.dataSet,
        this.vlanId,
        this.vlanPriority,
        this.needCommissioning,
        this.configurationRevision);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getReference().toString());
    sb.append("\n");
    sb.append("enabled: ").append(enabled);
    sb.append("\n");
    sb.append("controlBlockReference: ").append(controlBlockReference);
    sb.append("\n");
    sb.append("destinationMacAddress: ").append(destinationMacAddress);
    sb.append("\n");
    sb.append("applicationId: ").append(applicationId);
    sb.append("\n");
    sb.append("gooseId: ").append(gooseId);
    sb.append("\n");
    sb.append("dataSetReference: ").append(dataSetReference);
    sb.append("\n");
    sb.append("dataSet: ").append(dataSet != null ? dataSet.getReferenceStr() : null);
    sb.append("\n");
    sb.append("vlanId: ").append(vlanId);
    sb.append("\n");
    sb.append("vlanPriority: ").append(vlanPriority);
    sb.append("\n");
    sb.append("needCommissioning: ").append(needCommissioning);
    sb.append("\n");
    sb.append("configurationRevision: ").append(configurationRevision);

    return sb.toString();
  }
}
