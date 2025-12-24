package com.transparency.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import com.transparency.client.render.TransparencyRenderer;
import com.transparency.client.config.TransparencyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransparencyClientMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("improved-transparency-client");
    private static TransparencyRenderer renderer;
    private static TransparencyConfig config;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Improved Transparency Client");
        
        config = TransparencyConfig.load();
        renderer = new TransparencyRenderer();
        
        WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
            if (config.isEnabled()) {
                renderer.setupTransparencyPass(context);
            }
        });
        
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            if (config.isEnabled()) {
                renderer.renderBehindTranslucent(context);
            }
        });
        
        LOGGER.info("Transparency rendering hooks registered");
    }
    
    public static TransparencyConfig getConfig() {
        return config;
    }
    
    public static TransparencyRenderer getRenderer() {
        return renderer;
    }
}
