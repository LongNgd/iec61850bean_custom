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
package com.beanit.iec61850bean.integrationtests;

import com.beanit.iec61850bean.SclParseException;
import com.beanit.iec61850bean.SclParser;
import com.beanit.iec61850bean.ServerModel;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SclTests {

  private static final String B431_CID_PATH = "src/test/resources/B431_F87T.cid";
  private static final String F650_ICD_PATH = "src/test/resources/F650.icd";
  private static final String TECHDAY_SCD_PATH = "src/test/resources/TechDay_160124.scd";

  @Test
  public void testClientServerCom() throws SclParseException {
    List<ServerModel> serverModelList = SclParser.parse(B431_CID_PATH);
    System.out.println(serverModelList);
  }
}
