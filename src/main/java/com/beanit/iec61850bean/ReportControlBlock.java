package com.beanit.iec61850bean;

public final class ReportControlBlock {

  private final boolean enabled;
  private final boolean reserved;
  private final String controlBlockReference;
  private final String reportId;
  private final String dataSetReference;
  private final String triggerOptions;
  private final long bufferTimeMs;
  private final long configurationRevision;
  private final long integrityPeriodMs;
  private final String owner;

  public ReportControlBlock(
      boolean enabled,
      boolean reserved,
      String controlBlockReference,
      String reportId,
      String dataSetReference,
      String triggerOptions,
      long bufferTimeMs,
      long configurationRevision,
      long integrityPeriodMs,
      String owner) {
    this.enabled = enabled;
    this.reserved = reserved;
    this.controlBlockReference = controlBlockReference;
    this.reportId = reportId;
    this.dataSetReference = dataSetReference;
    this.triggerOptions = triggerOptions;
    this.bufferTimeMs = bufferTimeMs;
    this.configurationRevision = configurationRevision;
    this.integrityPeriodMs = integrityPeriodMs;
    this.owner = owner;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public boolean isReserved() {
    return reserved;
  }

  public String getControlBlockReference() {
    return controlBlockReference;
  }

  public String getReportId() {
    return reportId;
  }

  public String getDataSetReference() {
    return dataSetReference;
  }

  public String getTriggerOptions() {
    return triggerOptions;
  }

  public long getBufferTimeMs() {
    return bufferTimeMs;
  }

  public long getConfigurationRevision() {
    return configurationRevision;
  }

  public long getIntegrityPeriodMs() {
    return integrityPeriodMs;
  }

  public String getOwner() {
    return owner;
  }
}
