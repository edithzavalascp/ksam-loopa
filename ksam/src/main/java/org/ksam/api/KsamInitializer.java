package org.ksam.api;

import org.ksam.logic.autonomicmanager.SimpleLoopManager;
import org.ksam.logic.me.configuration.MEConfigurationManager;

public class KsamInitializer {
    private final static SimpleLoopManager loop = new SimpleLoopManager("simpleLoop");
    public static MEConfigurationManager meConfigM = new MEConfigurationManager(loop);
}
