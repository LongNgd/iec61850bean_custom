package com.beanit.iec61850bean.integrationtests;

import com.beanit.iec61850bean.GooseControlBlock;
import com.beanit.iec61850bean.SclParseException;
import com.beanit.iec61850bean.SclParser;
import com.beanit.iec61850bean.ServerModel;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GooseParsingTest {

    private static final String F650_FILE = "src/test/resources/F650.icd";
    private static final String B431_FILE = "src/test/resources/B431_F87T.cid";

    @Test
    public void testGooseParsingF650() throws SclParseException {
        List<ServerModel> models = SclParser.parse(F650_FILE);
        assertNotNull(models);
        assertFalse(models.isEmpty());

        ServerModel model = models.get(0);
        Collection<GooseControlBlock> gooseBlocks = model.getGooseControlBlocks();

        System.out.println("\n=== F650.icd GOOSE Control Blocks ===");
        System.out.println("Found " + gooseBlocks.size() + " GOOSE control blocks\n");

        for (GooseControlBlock gcb : gooseBlocks) {
            System.out.println("GOOSE Control Block:");
            System.out.println("  Reference: " + gcb.getControlBlockReference());
            System.out.println("  Application ID: " + gcb.getApplicationId());
            System.out.println("  GOOSE ID: " + gcb.getGooseId());
            System.out.println("  Dataset: " + gcb.getDataSetReference());
            System.out.println("  MAC Address: " + gcb.getDestinationMacAddress());
            System.out.println("  VLAN ID: " + gcb.getVlanId());
            System.out.println("  VLAN Priority: " + gcb.getVlanPriority());
            System.out.println("  Config Revision: " + gcb.getConfigurationRevision());
            System.out.println("  Enabled: " + gcb.isEnabled());
            System.out.println();
        }

        // F650.icd should have at least 1 GOOSE control block (gcb01)
        // Note: scb is GSSE type and should be skipped
        assertFalse(gooseBlocks.isEmpty(), "Should have at least 1 GOOSE block");

        // Verify one of the GOOSE blocks
        boolean foundGcb01 = false;
        for (GooseControlBlock gcb : gooseBlocks) {
            if (gcb.getControlBlockReference().contains("gcb01")) {
                foundGcb01 = true;
                assertEquals("F650_GOOSE1", gcb.getApplicationId());
                assertNotNull(gcb.getDataSetReference());
                assertTrue(gcb.getDataSetReference().contains("GOOSE1"));
                assertEquals("1", gcb.getConfigurationRevision());
            }
        }
        assertTrue(foundGcb01, "Should find gcb01 GOOSE control block");
    }

    @Test
    public void testGooseParsingB431() throws SclParseException {
        List<ServerModel> models = SclParser.parse(B431_FILE);
        assertNotNull(models);
        assertFalse(models.isEmpty());

        ServerModel model = models.get(0);
        Collection<GooseControlBlock> gooseBlocks = model.getGooseControlBlocks();

        System.out.println("\n=== B431_F87T.cid GOOSE Control Blocks ===");
        System.out.println("Found " + gooseBlocks.size() + " GOOSE control blocks\n");

        for (GooseControlBlock gcb : gooseBlocks) {
            System.out.println("GOOSE Control Block:");
            System.out.println("  Reference: " + gcb.getControlBlockReference());
            System.out.println("  Application ID: " + gcb.getApplicationId());
            System.out.println("  Dataset: " + gcb.getDataSetReference());
            System.out.println();
        }

        // B431_F87T.cid should have multiple GOOSE control blocks
        assertFalse(gooseBlocks.isEmpty(), "Should have GOOSE blocks");
    }

    @Test
    public void testGooseControlBlockProperties() throws SclParseException {
        List<ServerModel> models = SclParser.parse(F650_FILE);
        ServerModel model = models.get(0);
        Collection<GooseControlBlock> gooseBlocks = model.getGooseControlBlocks();

        for (GooseControlBlock gcb : gooseBlocks) {
            // Verify basic properties are set
            assertNotNull(gcb.getReference(), "Reference should not be null");
            assertNotNull(gcb.getControlBlockReference(), "Control block reference should not be null");

            // Application ID should be set for valid GOOSE blocks
            if (gcb.getControlBlockReference().contains("gcb01")) {
                assertNotNull(gcb.getApplicationId(), "Application ID should not be null");
            }

            // Enabled should default to true
            assertTrue(gcb.isEnabled(), "GOOSE should be enabled by default");
        }
    }

    @Test
    public void testServerModelGetGooseControlBlock() throws SclParseException {
        List<ServerModel> models = SclParser.parse(F650_FILE);
        ServerModel model = models.get(0);

        // Test getting GOOSE block by reference
        Collection<GooseControlBlock> allBlocks = model.getGooseControlBlocks();

        for (GooseControlBlock gcb : allBlocks) {
            String reference = gcb.getReference().toString();
            GooseControlBlock retrieved = model.getGooseControlBlock(reference);
            assertNotNull(retrieved, "Should be able to retrieve GOOSE block by reference");
            assertEquals(gcb.getControlBlockReference(), retrieved.getControlBlockReference());
        }
    }
}
