package com.transparency.mixin;

import com.transparency.client.TransparencyClientMod;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    
    @Inject(method = "render", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/render/WorldRenderer;renderWeather(Lnet/minecraft/client/render/LightmapTextureManager;FDDD)V",
        shift = At.Shift.BEFORE
    ))
    private void onBeforeWeatherRender(MatrixStack matrices, float tickDelta, long limitTime, 
                                       boolean renderBlockOutline, Camera camera, CallbackInfo ci) {
        if (TransparencyClientMod.getConfig().isEnabled() && 
            TransparencyClientMod.getConfig().isWeatherBehindTranslucent()) {
            TransparencyClientMod.getRenderer().setupTransparencyPass(null);
        }
    }
    
    @Inject(method = "render", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/render/WorldRenderer;renderClouds(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FDDD)V",
        shift = At.Shift.BEFORE
    ))
    private void onBeforeCloudRender(MatrixStack matrices, float tickDelta, long limitTime, 
                                     boolean renderBlockOutline, Camera camera, CallbackInfo ci) {
        if (TransparencyClientMod.getConfig().isEnabled() && 
            TransparencyClientMod.getConfig().isCloudsBehindTranslucent()) {
        }
    }
}
