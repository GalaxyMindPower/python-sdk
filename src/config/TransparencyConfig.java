package com.transparency.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TransparencyConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("transparency-config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir().resolve("improved-transparency.json");
    
    private boolean enabled = true;
    private boolean weatherBehindTranslucent = true;
    private boolean cloudsBehindTranslucent = true;
    private boolean particlesBehindTranslucent = true;
    private boolean useDepthSorting = true;
    private float weatherOpacity = 1.0f;
    private float cloudOpacity = 1.0f;
    private float particleOpacity = 1.0f;
    private int renderQuality = 2;
    private boolean enablePerformanceMode = false;
    
    public static TransparencyConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                return GSON.fromJson(json, TransparencyConfig.class);
            } catch (IOException e) {
                LOGGER.error("Failed to load config, using defaults", e);
            }
        }
        
        TransparencyConfig config = new TransparencyConfig();
        config.save();
        return config;
    }
    
    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String json = GSON.toJson(this);
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isWeatherBehindTranslucent() {
        return weatherBehindTranslucent;
    }
    
    public void setWeatherBehindTranslucent(boolean weatherBehindTranslucent) {
        this.weatherBehindTranslucent = weatherBehindTranslucent;
    }
    
    public boolean isCloudsBehindTranslucent() {
        return cloudsBehindTranslucent;
    }
    
    public void setCloudsBehindTranslucent(boolean cloudsBehindTranslucent) {
        this.cloudsBehindTranslucent = cloudsBehindTranslucent;
    }
    
    public boolean isParticlesBehindTranslucent() {
        return particlesBehindTranslucent;
    }
    
    public void setParticlesBehindTranslucent(boolean particlesBehindTranslucent) {
        this.particlesBehindTranslucent = particlesBehindTranslucent;
    }
    
    public boolean isUseDepthSorting() {
        return useDepthSorting;
    }
    
    public void setUseDepthSorting(boolean useDepthSorting) {
        this.useDepthSorting = useDepthSorting;
    }
    
    public float getWeatherOpacity() {
        return weatherOpacity;
    }
    
    public void setWeatherOpacity(float weatherOpacity) {
        this.weatherOpacity = Math.max(0.0f, Math.min(1.0f, weatherOpacity));
    }
    
    public float getCloudOpacity() {
        return cloudOpacity;
    }
    
    public void setCloudOpacity(float cloudOpacity) {
        this.cloudOpacity = Math.max(0.0f, Math.min(1.0f, cloudOpacity));
    }
    
    public float getParticleOpacity() {
        return particleOpacity;
    }
    
    public void setParticleOpacity(float particleOpacity) {
        this.particleOpacity = Math.max(0.0f, Math.min(1.0f, particleOpacity));
    }
    
    public int getRenderQuality() {
        return renderQuality;
    }
    
    public void setRenderQuality(int renderQuality) {
        this.renderQuality = Math.max(0, Math.min(3, renderQuality));
    }
    
    public boolean isEnablePerformanceMode() {
        return enablePerformanceMode;
    }
    
    public void setEnablePerformanceMode(boolean enablePerformanceMode) {
        this.enablePerformanceMode = enablePerformanceMode;
    }
}
