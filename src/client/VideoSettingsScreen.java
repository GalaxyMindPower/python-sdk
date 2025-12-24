package com.transparency.client.gui;

import com.transparency.client.TransparencyClientMod;
import com.transparency.client.config.TransparencyConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.client.util.math.MatrixStack;

public class VideoSettingsScreen extends Screen {
    private final Screen parent;
    private final TransparencyConfig config;
    
    public VideoSettingsScreen(Screen parent) {
        super(Text.literal("Improved Transparency Settings"));
        this.parent = parent;
        this.config = TransparencyClientMod.getConfig();
    }
    
    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 40;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 25;
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Transparency: " + (config.isEnabled() ? "ON" : "OFF")),
            button -> {
                config.setEnabled(!config.isEnabled());
                button.setMessage(Text.literal("Transparency: " + (config.isEnabled() ? "ON" : "OFF")));
                config.save();
            })
            .dimensions(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight)
            .build());
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Weather Behind Translucent: " + (config.isWeatherBehindTranslucent() ? "ON" : "OFF")),
            button -> {
                config.setWeatherBehindTranslucent(!config.isWeatherBehindTranslucent());
                button.setMessage(Text.literal("Weather Behind Translucent: " + (config.isWeatherBehindTranslucent() ? "ON" : "OFF")));
                config.save();
            })
            .dimensions(centerX - buttonWidth / 2, startY + spacing, buttonWidth, buttonHeight)
            .build());
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Clouds Behind Translucent: " + (config.isCloudsBehindTranslucent() ? "ON" : "OFF")),
            button -> {
                config.setCloudsBehindTranslucent(!config.isCloudsBehindTranslucent());
                button.setMessage(Text.literal("Clouds Behind Translucent: " + (config.isCloudsBehindTranslucent() ? "ON" : "OFF")));
                config.save();
            })
            .dimensions(centerX - buttonWidth / 2, startY + spacing * 2, buttonWidth, buttonHeight)
            .build());
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Particles Behind Translucent: " + (config.isParticlesBehindTranslucent() ? "ON" : "OFF")),
            button -> {
                config.setParticlesBehindTranslucent(!config.isParticlesBehindTranslucent());
                button.setMessage(Text.literal("Particles Behind Translucent: " + (config.isParticlesBehindTranslucent() ? "ON" : "OFF")));
                config.save();
            })
            .dimensions(centerX - buttonWidth / 2, startY + spacing * 3, buttonWidth, buttonHeight)
            .build());
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Depth Sorting: " + (config.isUseDepthSorting() ? "ON" : "OFF")),
            button -> {
                config.setUseDepthSorting(!config.isUseDepthSorting());
                button.setMessage(Text.literal("Depth Sorting: " + (config.isUseDepthSorting() ? "ON" : "OFF")));
                config.save();
            })
            .dimensions(centerX - buttonWidth / 2, startY + spacing * 4, buttonWidth, buttonHeight)
            .build());
        
        String[] qualityLevels = {"Low", "Medium", "High", "Ultra"};
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Render Quality: " + qualityLevels[config.getRenderQuality()]),
            button -> {
                int newQuality = (config.getRenderQuality() + 1) % 4;
                config.setRenderQuality(newQuality);
                button.setMessage(Text.literal("Render Quality: " + qualityLevels[newQuality]));
                config.save();
            })
            .dimensions(centerX - buttonWidth / 2, startY + spacing * 5, buttonWidth, buttonHeight)
            .build());
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Performance Mode: " + (config.isEnablePerformanceMode() ? "ON" : "OFF")),
            button -> {
                config.setEnablePerformanceMode(!config.isEnablePerformanceMode());
                button.setMessage(Text.literal("Performance Mode: " + (config.isEnablePerformanceMode() ? "ON" : "OFF")));
                config.save();
            })
            .dimensions(centerX - buttonWidth / 2, startY + spacing * 6, buttonWidth, buttonHeight)
            .build());
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Done"),
            button -> {
                if (this.client != null) {
                    this.client.setScreen(parent);
                }
            })
            .dimensions(centerX - buttonWidth / 2, this.height - 30, buttonWidth, buttonHeight)
            .build());
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        
        int centerX = this.width / 2;
        int warningY = this.height - 50;
        drawCenteredText(matrices, this.textRenderer, 
            Text.literal("Warning: May impact GPU performance"), 
            centerX, warningY, 0xFFAA00);
        
        super.render(matrices, mouseX, mouseY, delta);
    }
    
    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
