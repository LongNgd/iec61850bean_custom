package com.beanit.iec61850bean.integrationtests;

import com.beanit.iec61850bean.SclParseException;
import com.beanit.iec61850bean.SclParser;
import com.beanit.iec61850bean.ServerModel;
import com.beanit.iec61850bean.SettingGroup;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SettingGroupParsingTest {

    private static final String SCL_FILE = "src/test/resources/B431_F87T.cid";

    @Test
    public void testSettingGroupParsing() throws SclParseException {
        List<ServerModel> models = SclParser.parse(SCL_FILE);
        assertNotNull(models);
        assertFalse(models.isEmpty());

        ServerModel model = models.get(0);
        Collection<SettingGroup> settingGroups = model.getSettingGroups();

        assertNotNull(settingGroups);
        assertFalse(settingGroups.isEmpty(), "Should have at least 1 SettingGroup");
        assertEquals(1, settingGroups.size());

        SettingGroup sg = settingGroups.iterator().next();

        System.out.println("Parsed SettingGroup:");
        System.out.println(sg);

        // Verification based on B431_F87T.cid content
        // <SettingControl esel:activeGroupLabel="ACTGRP"
        // esel:numberOfSettingGroupsLabel="MAXGRP" numOfSGs="6" />

        assertEquals("6", sg.getNumberOfSettingGroups());
        assertEquals("0", sg.getActiveSettingGroup(), "Default activity setting group should be 0 if not specified");

        // Expected Reference: SEL_487ECFG/LLN0.SGCB
        assertTrue(sg.getControlBlock().contains("LLN0.SGCB"));
    }
}
