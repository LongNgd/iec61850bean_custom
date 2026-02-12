package com.beanit.iec61850bean;

public final class ReportControlBlock extends ControlBlock {

  private final boolean reserved;
  private final String reportId;
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
    super(enabled, controlBlockReference, dataSetReference);
    this.reserved = reserved;
    this.reportId = reportId;
    this.triggerOptions = triggerOptions;
    this.bufferTimeMs = bufferTimeMs;
    this.configurationRevision = configurationRevision;
    this.integrityPeriodMs = integrityPeriodMs;
    this.owner = owner;
  }

  public boolean isReserved() {
    return reserved;
  }

  public String getReportId() {
    return reportId;
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
