package com.beanit.iec61850bean.integrationtests;

import com.beanit.iec61850bean.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrcbParsingTest {

  private static final String B431_CID_PATH = "src/test/resources/B431_F87T.cid";

  @Test
  public void testUrcbCountAndDataSetLink() throws SclParseException {
    List<ServerModel> serverModelList = SclParser.parse(B431_CID_PATH);
    assertEquals(1, serverModelList.size());
    
    ServerModel serverModel = serverModelList.get(0);
    
    // Find LLN0
    ModelNode lln0 = null;
    for (ModelNode ld : serverModel.getChildren()) {
        for (ModelNode ln : ld.getChildren()) {
             if (ln.getName().contains("LLN0")) {
                 lln0 = ln;
                 break;
             }
        }
        if (lln0 != null) break;
    }
    
    assertNotNull(lln0, "LLN0 not found");
    
    int urcbCount = 0;
    
    for (ModelNode child : lln0.getChildren()) {
        if (child instanceof Urcb) {
            urcbCount++;
            Urcb urcb = (Urcb) child;
            
            // Validate DataSet Linking
            assertNotNull(urcb.getDataSet(), "URCB DataSet should not be null: " + urcb.getName());
            
            if (urcb.getName().equals("URep01")) {
                 // URep01 uses URDSet01
                 // URDSet01 has 14 FCDAs (checked via file content lines 454-469)
                 // But wait, line 454 to 469 is 15 lines? No, 455 to 468?
                 // Let's check lines 455 to 468 is 14 lines.
                 assertEquals(14, urcb.getDataSet().getMembers().size(), "URep01 DataSet should have 14 members");
            }
        }
    }
    
    assertEquals(7, urcbCount, "Should find exactly 7 URCBs");
  }
}
