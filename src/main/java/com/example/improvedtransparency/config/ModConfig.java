package com.example.improvedtransparency.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "improvedtransparency")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean enableExperimentalTransparency = true;

    @ConfigEntry.Gui.Tooltip
    public boolean showPerformanceWarning = true;

    @ConfigEntry.Gui.Tooltip
    public int maxRenderDistance = 16;
}