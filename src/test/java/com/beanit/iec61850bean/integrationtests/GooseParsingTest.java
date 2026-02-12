package com.beanit.iec61850bean.integrationtests;

import com.beanit.iec61850bean.Goose;
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
        Collection<Goose> gooseBlocks = model.getGooses();

        System.out.println("\n=== F650.icd GOOSE Control Blocks ===");
        System.out.println("Found " + gooseBlocks.size() + " GOOSE control blocks\n");

        for (Goose gcb : gooseBlocks) {
            GooseControlBlock controlBlock = gcb.getControlBlock();
            System.out.println("GOOSE Control Block:");
            System.out.println("  Reference: " + controlBlock.getControlBlockReference());
            System.out.println("  Application ID: " + controlBlock.getApplicationId());
            System.out.println("  GOOSE ID: " + controlBlock.getGooseId());
            System.out.println("  Dataset: " + controlBlock.getDataSetReference());
            System.out.println(
                "  Dataset object: "
                    + (gcb.getDataSet() != null ? gcb.getDataSet().getReferenceStr() : null));
            System.out.println("  MAC Address: " + controlBlock.getDestinationMacAddress());
            System.out.println("  VLAN ID: " + controlBlock.getVlanId());
            System.out.println("  VLAN Priority: " + controlBlock.getVlanPriority());
            System.out.println("  Config Revision: " + controlBlock.getConfigurationRevision());
            System.out.println("  Enabled: " + controlBlock.isEnabled());
            System.out.println();
        }

        // F650.icd should have at least 1 GOOSE control block (gcb01)
        // Note: scb is GSSE type and should be skipped
        assertFalse(gooseBlocks.isEmpty(), "Should have at least 1 GOOSE block");

        // Verify one of the GOOSE blocks
        boolean foundGcb01 = false;
        for (Goose gcb : gooseBlocks) {
            GooseControlBlock controlBlock = gcb.getControlBlock();
            if (controlBlock.getControlBlockReference().contains("gcb01")) {
                foundGcb01 = true;
                assertEquals("F650_GOOSE1", controlBlock.getApplicationId());
                assertNotNull(controlBlock.getDataSetReference());
                assertTrue(controlBlock.getDataSetReference().contains("GOOSE1"));
                assertNotNull(gcb.getDataSet());
                assertEquals(
                    controlBlock.getDataSetReference().replace('$', '.'), gcb.getDataSet().getReferenceStr());
                assertEquals("1", controlBlock.getConfigurationRevision());
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
        Collection<Goose> gooseBlocks = model.getGooses();

        System.out.println("\n=== B431_F87T.cid GOOSE Control Blocks ===");
        System.out.println("Found " + gooseBlocks.size() + " GOOSE control blocks\n");

        for (Goose gcb : gooseBlocks) {
            GooseControlBlock controlBlock = gcb.getControlBlock();
            System.out.println("GOOSE Control Block:");
            System.out.println("  Reference: " + controlBlock.getControlBlockReference());
            System.out.println("  Application ID: " + controlBlock.getApplicationId());
            System.out.println("  Dataset: " + controlBlock.getDataSetReference());
            System.out.println();
        }

        // B431_F87T.cid should have multiple GOOSE control blocks
        assertFalse(gooseBlocks.isEmpty(), "Should have GOOSE blocks");

        for (Goose goose : gooseBlocks) {
            GooseControlBlock controlBlock = goose.getControlBlock();
            String reference = controlBlock.getControlBlockReference();
            if (reference.contains("GSE_487E_02")) {
                assertEquals("48", controlBlock.getApplicationId());
                assertEquals("487E_GSE02", controlBlock.getGooseId());
                assertEquals("1", controlBlock.getVlanId());
            } else if (reference.contains("GSE_487E_03")) {
                assertEquals("49", controlBlock.getApplicationId());
                assertEquals("487E_GSE03", controlBlock.getGooseId());
                assertEquals("1", controlBlock.getVlanId());
            } else if (reference.contains("GSE_487E_04")) {
                assertEquals("50", controlBlock.getApplicationId());
                assertEquals("GSE_487E_04", controlBlock.getGooseId());
                assertEquals("1", controlBlock.getVlanId());
            }
        }
    }

    @Test
    public void testGooseControlBlockProperties() throws SclParseException {
        List<ServerModel> models = SclParser.parse(F650_FILE);
        ServerModel model = models.get(0);
        Collection<Goose> gooseBlocks = model.getGooses();

        for (Goose gcb : gooseBlocks) {
            GooseControlBlock controlBlock = gcb.getControlBlock();
            // Verify basic properties are set
            assertNotNull(gcb.getReference(), "Reference should not be null");
            assertNotNull(controlBlock.getControlBlockReference(), "Control block reference should not be null");

            if (controlBlock.getDataSetReference() != null) {
                assertNotNull(gcb.getDataSet(), "Dataset object should be linked when datSet is present");
                assertEquals(
                    controlBlock.getDataSetReference().replace('$', '.'), gcb.getDataSet().getReferenceStr());
            }

            // Application ID should be set for valid GOOSE blocks
            if (controlBlock.getControlBlockReference().contains("gcb01")) {
                assertNotNull(controlBlock.getApplicationId(), "Application ID should not be null");
            }

            // Enabled should default to true
            assertTrue(controlBlock.isEnabled(), "GOOSE should be enabled by default");
        }
    }

    @Test
    public void testServerModelGetGooseControlBlock() throws SclParseException {
        List<ServerModel> models = SclParser.parse(F650_FILE);
        ServerModel model = models.get(0);

        // Test getting GOOSE block by reference
        Collection<Goose> allBlocks = model.getGooses();

        for (Goose gcb : allBlocks) {
            String reference = gcb.getReference().toString();
            Goose retrieved = model.getGoose(reference);
            assertNotNull(retrieved, "Should be able to retrieve GOOSE block by reference");
            assertEquals(
                gcb.getControlBlock().getControlBlockReference(),
                retrieved.getControlBlock().getControlBlockReference());
        }
    }
}
