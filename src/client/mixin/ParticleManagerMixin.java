package com.transparency.mixin;

import com.transparency.client.TransparencyClientMod;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    
    @Inject(method = "renderParticles", at = @At("HEAD"))
    private void onBeforeParticleRender(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers,
                                        LightmapTextureManager lightmapTextureManager, Camera camera, 
                                        float tickDelta, CallbackInfo ci) {
        if (TransparencyClientMod.getConfig().isEnabled() && 
            TransparencyClientMod.getConfig().isParticlesBehindTranslucent()) {
        }
    }
}
