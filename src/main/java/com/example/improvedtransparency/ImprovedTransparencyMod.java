package com.example.improvedtransparency;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImprovedTransparencyMod implements ModInitializer {
    public static final String MOD_ID = "improvedtransparency";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Improved Transparency Mod - Experimental shader-based transparency rendering");
        LOGGER.warn("This mod may impact GPU performance. Monitor your frame rates and GPU usage.");
    }
}