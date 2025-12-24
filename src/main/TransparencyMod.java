package com.transparency;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransparencyMod implements ModInitializer {
    public static final String MOD_ID = "improved-transparency";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Improved Transparency Mod");
        LOGGER.info("Screen shader-based transparency rendering enabled");
    }
}
