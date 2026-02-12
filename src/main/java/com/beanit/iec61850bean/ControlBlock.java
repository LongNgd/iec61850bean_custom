package com.beanit.iec61850bean;

public abstract class ControlBlock {

  private final boolean enabled;
  private final String controlBlockReference;
  private final String dataSetReference;

  protected ControlBlock(boolean enabled, String controlBlockReference, String dataSetReference) {
    this.enabled = enabled;
    this.controlBlockReference = controlBlockReference;
    this.dataSetReference = dataSetReference;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getControlBlockReference() {
    return controlBlockReference;
  }

  public String getDataSetReference() {
    return dataSetReference;
  }
}
