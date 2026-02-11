/*
 * Copyright 2011 The IEC61850bean Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.beanit.iec61850bean;

import com.beanit.iec61850bean.internal.mms.asn1.AlternateAccessSelection;
import com.beanit.iec61850bean.internal.mms.asn1.ObjectName;
import com.beanit.iec61850bean.internal.mms.asn1.ObjectName.DomainSpecific;
import com.beanit.iec61850bean.internal.mms.asn1.VariableDefs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ServerModel extends ModelNode {

  private final Map<String, DataSet> dataSets = new LinkedHashMap<>();

  private final Map<String, Urcb> urcbs = new HashMap<>();
  private final Map<String, Brcb> brcbs = new HashMap<>();
  private final Map<String, Goose> gooses = new HashMap<>();
  private final Map<String, SettingGroup> settingGroups = new HashMap<>();

  public ServerModel(List<LogicalDevice> logicalDevices, Collection<DataSet> dataSets) {
    this(null, logicalDevices, dataSets);
  }

  public ServerModel(ObjectReference objectReference, List<LogicalDevice> logicalDevices, Collection<DataSet> dataSets) {
    children = new LinkedHashMap<>();
    this.objectReference = objectReference;
    for (LogicalDevice logicalDevice : logicalDevices) {
      children.put(logicalDevice.getReference().getName(), logicalDevice);
      logicalDevice.setParent(this);
    }

    if (dataSets != null) {
      addDataSets(dataSets);
    }

    for (LogicalDevice ld : logicalDevices) {
      for (ModelNode ln : ld.getChildren()) {
        for (Urcb urcb : ((LogicalNode) ln).getUrcbs()) {
          urcbs.put(urcb.getReference().toString(), urcb);
          urcb.dataSet = getDataSet(urcb.getDataSetRef().getStringValue().replace('$', '.'));
        }
        for (Brcb brcb : ((LogicalNode) ln).getBrcbs()) {
          brcbs.put(brcb.getReference().toString(), brcb);
          brcb.dataSet = getDataSet(brcb.getDataSetRef().getStringValue().replace('$', '.'));
        }
      }
    }
  }

  @Override
  public ServerModel copy() {
    List<LogicalDevice> childCopies = new ArrayList<>(children.size());
    for (ModelNode childNode : children.values()) {
      childCopies.add((LogicalDevice) childNode.copy());
    }

    List<DataSet> dataSetCopies = new ArrayList<>(dataSets.size());
    for (DataSet dataSet : dataSets.values()) {
      dataSetCopies.add(dataSet);
    }

    return new ServerModel(childCopies, dataSetCopies);
  }

  public ObjectReference getObjectReference() {
    return objectReference;
  }

  /**
   * Get the data set with the given reference. Return null if none is found.
   *
   * @param reference the reference of the requested data set.
   * @return the data set with the given reference.
   */
  public DataSet getDataSet(String reference) {
    return dataSets.get(reference);
  }

  void addDataSet(DataSet dataSet) {
    dataSets.put(dataSet.getReferenceStr().replace('$', '.'), dataSet);
    updateControlBlockDataSets();
  }

  void addDataSets(Collection<DataSet> dataSets) {
    for (DataSet dataSet : dataSets) {
      addDataSet(dataSet);
    }
    updateControlBlockDataSets();
  }

  List<String> getDataSetNames(String ldName) {
    // TODO make thread save
    List<String> dataSetNames = new ArrayList<>();
    for (String dataSetRef : dataSets.keySet()) {
      if (dataSetRef.startsWith(ldName)) {
        dataSetNames.add(dataSetRef.substring(dataSetRef.indexOf('/') + 1).replace('.', '$'));
      }
    }
    return dataSetNames;
  }

  /**
   * Get a collection of all data sets that exist in this model.
   *
   * @return a collection of all data sets
   */
  public Collection<DataSet> getDataSets() {
    return dataSets.values();
  }

  /**
   * @param dataSetReference the data set reference
   * @return returns the DataSet that was removed, null if no DataSet with the
   *         given reference was
   *         found or the data set is not deletable.
   */
  DataSet removeDataSet(String dataSetReference) {
    DataSet dataSet = dataSets.get(dataSetReference);
    if (dataSet == null || !dataSet.isDeletable()) {
      return null;
    }
    DataSet removedDataSet = dataSets.remove(dataSetReference);
    updateControlBlockDataSets();
    return removedDataSet;
  }

  private void updateControlBlockDataSets() {
    for (ModelNode ld : children.values()) {
      for (ModelNode ln : ld.getChildren()) {
        LogicalNode logicalNode = (LogicalNode) ln;
        for (Urcb urcb : logicalNode.getUrcbs()) {
          urcb.dataSet = getDataSet(urcb.getDataSetRef().getStringValue().replace('$', '.'));
        }
        for (Brcb brcb : logicalNode.getBrcbs()) {
          brcb.dataSet = getDataSet(brcb.getDataSetRef().getStringValue().replace('$', '.'));
        }
      }
    }
    updateGooseDataSets();
  }

  private void updateGooseDataSets() {
    for (Goose goose : gooses.values()) {
      String dataSetReference = goose.getDataSetReference();
      if (dataSetReference != null) {
        goose.setDataSet(getDataSet(dataSetReference.replace('$', '.')));
      } else {
        goose.setDataSet(null);
      }
    }
  }

  void addUrcb(Urcb urcb) {
    urcbs.put(urcb.getReference().getName(), urcb);
  }

  /**
   * Get the unbuffered report control block (URCB) with the given reference.
   *
   * @param reference the reference of the requested URCB.
   * @return the reference to the requested URCB or null if none with the given
   *         reference is found.
   */
  public Urcb getUrcb(String reference) {
    return urcbs.get(reference);
  }

  /**
   * Get a collection of all unbuffered report control blocks (URCB) that exist in
   * this model.
   *
   * @return a collection of all unbuffered report control blocks (URCB)
   */
  public Collection<Urcb> getUrcbs() {
    return urcbs.values();
  }

  /**
   * Get the buffered report control block (BRCB) with the given reference.
   *
   * @param reference the reference of the requested BRCB.
   * @return the reference to the requested BRCB or null if none with the given
   *         reference is found.
   */
  public Brcb getBrcb(String reference) {
    return brcbs.get(reference);
  }

  /**
   * Get a collection of all buffered report control blocks (BRCB) that exist in
   * this model.
   *
   * @return a collection of all buffered report control blocks (BRCB)
   */
  public Collection<Brcb> getBrcbs() {
    return brcbs.values();
  }

  void addGoose(Goose goose) {
    String dataSetReference = goose.getDataSetReference();
    if (dataSetReference != null) {
      goose.setDataSet(getDataSet(dataSetReference.replace('$', '.')));
    }
    gooses.put(goose.getReference().toString(), goose);
  }

  /**
   * Get the GOOSE control block with the given reference.
   *
   * @param reference the reference of the requested GOOSE control block.
   * @return the reference to the requested GOOSE control block or null if none
   *         with the given reference is found.
   */
  public Goose getGoose(String reference) {
    return gooses.get(reference);
  }

  /**
   * Get a collection of all GOOSE control blocks that exist in this model.
   *
   * @return a collection of all GOOSE control blocks
   */
  public Collection<Goose> getGooses() {
    return gooses.values();
  }

  void addSettingGroup(SettingGroup settingGroup) {
    settingGroups.put(settingGroup.getReference().toString(), settingGroup);
  }

  public SettingGroup getSettingGroup (String reference) {
    return settingGroups.get(reference);
  }

  public Collection<SettingGroup> getSettingGroups() {
    return settingGroups.values();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (ModelNode logicalDevice : children.values()) {
      sb.append(logicalDevice.toString());
    }
    sb.append("\n\n\n---------------------\nURCBs:");
    for (Urcb urcb : getUrcbs()) {
      sb.append("\n\n").append(urcb);
    }

    sb.append("\n\n\n---------------------\nBRCBs:");
    for (Brcb brcb : getBrcbs()) {
      sb.append("\n\n").append(brcb);
    }

    sb.append("\n\n\n---------------------\nData sets:");
    for (DataSet dataSet : getDataSets()) {
      sb.append("\n\n").append(dataSet);
    }

    sb.append("\n\n\n---------------------\nGOOSE:");
    for (Goose gcb : getGooses()) {
      sb.append("\n\n").append(gcb);
    }

    sb.append("\n\n\n---------------------\nSetting groups:");
    for (SettingGroup stg : getSettingGroups()) {
      sb.append("\n\n").append(stg);
    }

    return sb.toString();
  }

  /**
   * Searches and returns the model node with the given object reference and FC.
   * If searching for
   * Logical Devices and Logical Nodes the given fc parameter may be
   * <code>null</code>.
   *
   * @param objectReference the object reference of the node that is being
   *                        searched for. It has a
   *                        syntax like "ldname/ln.do....".
   * @param fc              the functional constraint of the requested model node.
   *                        May be null for Logical Device
   *                        and Logical Node references.
   * @return the model node if it was found or null otherwise
   */
  public ModelNode findModelNode(ObjectReference objectReference, Fc fc) {

    ModelNode currentNode = this;
    Iterator<String> searchedNodeReferenceIterator = objectReference.iterator();

    while (searchedNodeReferenceIterator.hasNext()) {
      currentNode = currentNode.getChild(searchedNodeReferenceIterator.next(), fc);
      if (currentNode == null) {
        return null;
      }
    }
    return currentNode;
  }

  /**
   * Searches and returns the model node with the given object reference and FC.
   * If searching for
   * Logical Devices and Logical Nodes the given fc parameter may be
   * <code>null</code>.
   *
   * @param objectReference the object reference of the node that is being
   *                        searched for. It has a
   *                        syntax like "ldname/ln.do....".
   * @param fc              the functional constraint of the requested model node.
   *                        May be null for Logical Device
   *                        and Logical Node references.
   * @return the model node if it was found or null otherwise
   */
  public ModelNode findModelNode(String objectReference, Fc fc) {
    return findModelNode(new ObjectReference(objectReference), fc);
  }

  /**
   * Returns the subModelNode that is referenced by the given VariableDef. Return
   * null in case the
   * referenced ModelNode is not found.
   *
   * @param variableDef the variableDef
   * @return the subModelNode that is referenced by the given VariableDef
   * @throws ServiceError if an error occurs
   */
  FcModelNode getNodeFromVariableDef(VariableDefs.SEQUENCE variableDef) throws ServiceError {

    ObjectName objectName = variableDef.getVariableSpecification().getName();

    if (objectName == null) {
      throw new ServiceError(
          ServiceError.FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT,
          "name in objectName is not selected");
    }

    DomainSpecific domainSpecific = objectName.getDomainSpecific();

    if (domainSpecific == null) {
      throw new ServiceError(
          ServiceError.FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT,
          "domain_specific in name is not selected");
    }

    ModelNode modelNode = getChild(domainSpecific.getDomainID().toString());

    if (modelNode == null) {
      return null;
    }

    String mmsItemId = domainSpecific.getItemID().toString();
    int index1 = mmsItemId.indexOf('$');

    if (index1 == -1) {
      throw new ServiceError(
          ServiceError.FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT,
          "invalid mms item id: " + domainSpecific.getItemID());
    }

    LogicalNode ln = (LogicalNode) modelNode.getChild(mmsItemId.substring(0, index1));

    if (ln == null) {
      return null;
    }

    int index2 = mmsItemId.indexOf('$', index1 + 1);

    if (index2 == -1) {
      throw new ServiceError(
          ServiceError.FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT, "invalid mms item id");
    }

    Fc fc = Fc.fromString(mmsItemId.substring(index1 + 1, index2));

    if (fc == null) {
      throw new ServiceError(
          ServiceError.FAILED_DUE_TO_COMMUNICATIONS_CONSTRAINT,
          "unknown functional constraint: " + mmsItemId.substring(index1 + 1, index2));
    }

    index1 = index2;

    index2 = mmsItemId.indexOf('$', index1 + 1);

    if (index2 == -1) {
      if (fc == Fc.RP) {
        return ln.getUrcb(mmsItemId.substring(index1 + 1));
      }
      if (fc == Fc.BR) {
        return ln.getBrcb(mmsItemId.substring(index1 + 1));
      }
      return (FcModelNode) ln.getChild(mmsItemId.substring(index1 + 1), fc);
    }

    if (fc == Fc.RP) {
      modelNode = ln.getUrcb(mmsItemId.substring(index1 + 1, index2));
    } else if (fc == Fc.BR) {
      modelNode = ln.getBrcb(mmsItemId.substring(index1 + 1, index2));
    } else {
      modelNode = ln.getChild(mmsItemId.substring(index1 + 1, index2), fc);
    }

    index1 = index2;
    index2 = mmsItemId.indexOf('$', index1 + 1);
    while (index2 != -1) {
      modelNode = modelNode.getChild(mmsItemId.substring(index1 + 1, index2));
      index1 = index2;
      index2 = mmsItemId.indexOf('$', index1 + 1);
    }

    modelNode = modelNode.getChild(mmsItemId.substring(index1 + 1));

    if (variableDef.getAlternateAccess() == null) {
      // no array is in this node path
      return (FcModelNode) modelNode;
    }

    AlternateAccessSelection altAccIt = variableDef.getAlternateAccess().getCHOICE().get(0).getUnnamed();

    if (altAccIt.getSelectAlternateAccess() != null) {
      // path to node below an array element
      modelNode = ((Array) modelNode)
          .getChild(
              altAccIt.getSelectAlternateAccess().getAccessSelection().getIndex().intValue());

      String mmsSubArrayItemId = altAccIt
          .getSelectAlternateAccess()
          .getAlternateAccess()
          .getCHOICE()
          .get(0)
          .getUnnamed()
          .getSelectAccess()
          .getComponent()
          .getBasic()
          .toString();

      index1 = -1;
      index2 = mmsSubArrayItemId.indexOf('$');
      while (index2 != -1) {
        modelNode = modelNode.getChild(mmsSubArrayItemId.substring(index1 + 1, index2));
        index1 = index2;
        index2 = mmsItemId.indexOf('$', index1 + 1);
      }

      return (FcModelNode) modelNode.getChild(mmsSubArrayItemId.substring(index1 + 1));
    } else {
      // path to an array element
      return (FcModelNode) ((Array) modelNode).getChild(altAccIt.getSelectAccess().getIndex().intValue());
    }
  }
}
