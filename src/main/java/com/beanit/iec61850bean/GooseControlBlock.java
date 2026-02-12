package com.beanit.iec61850bean;

public final class GooseControlBlock extends ControlBlock {

  private final String destinationMacAddress;
  private final String applicationId;
  private final String gooseId;
  private final String vlanId;
  private final String vlanPriority;
  private final String needCommissioning;
  private final String configurationRevision;

  public GooseControlBlock(
      boolean enabled,
      String controlBlockReference,
      String destinationMacAddress,
      String applicationId,
      String gooseId,
      String dataSetReference,
      String vlanId,
      String vlanPriority,
      String needCommissioning,
      String configurationRevision) {
    super(enabled, controlBlockReference, dataSetReference);
    this.destinationMacAddress = destinationMacAddress;
    this.applicationId = applicationId;
    this.gooseId = gooseId;
    this.vlanId = vlanId;
    this.vlanPriority = vlanPriority;
    this.needCommissioning = needCommissioning;
    this.configurationRevision = configurationRevision;
  }

  public String getDestinationMacAddress() {
    return destinationMacAddress;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public String getGooseId() {
    return gooseId;
  }

  public String getVlanId() {
    return vlanId;
  }

  public String getVlanPriority() {
    return vlanPriority;
  }

  public String getNeedCommissioning() {
    return needCommissioning;
  }

  public String getConfigurationRevision() {
    return configurationRevision;
  }
}
