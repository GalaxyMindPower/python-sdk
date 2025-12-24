package com.transparency.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class TransparencyRenderer {
    private Framebuffer weatherBuffer;
    private Framebuffer particleBuffer;
    private Framebuffer cloudBuffer;
    private Framebuffer depthBuffer;
    private PostEffectProcessor transparencyShader;
    private boolean initialized = false;
    
    private static final Identifier TRANSPARENCY_SHADER = 
        new Identifier("improved-transparency", "shaders/post/transparency_composite.json");
    
    public TransparencyRenderer() {
        this.initialized = false;
    }
    
    public void setupTransparencyPass(Object context) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (!initialized) {
            initialize(client);
        }
        
        if (weatherBuffer == null || weatherBuffer.viewportWidth != client.getWindow().getFramebufferWidth() 
            || weatherBuffer.viewportHeight != client.getWindow().getFramebufferHeight()) {
            resizeBuffers(client.getWindow().getFramebufferWidth(), 
                         client.getWindow().getFramebufferHeight());
        }
        
        clearBuffers();
    }
    
    public void renderBehindTranslucent(Object context) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (!initialized || transparencyShader == null) {
            return;
        }
        
        renderWeatherToBuffer(client);
        renderCloudsToBuffer(client);
        renderParticlesToBuffer(client);
        
        compositeTransparencyLayers(client);
    }
    
    private void initialize(MinecraftClient client) {
        int width = client.getWindow().getFramebufferWidth();
        int height = client.getWindow().getFramebufferHeight();
        
        weatherBuffer = new Framebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
        particleBuffer = new Framebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
        cloudBuffer = new Framebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
        depthBuffer = new Framebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
        
        weatherBuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        particleBuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        cloudBuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        depthBuffer.setClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        initialized = true;
    }
    
    private void resizeBuffers(int width, int height) {
        if (weatherBuffer != null) weatherBuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
        if (particleBuffer != null) particleBuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
        if (cloudBuffer != null) cloudBuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
        if (depthBuffer != null) depthBuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
    }
    
    private void clearBuffers() {
        if (weatherBuffer != null) {
            weatherBuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
        }
        if (particleBuffer != null) {
            particleBuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
        }
        if (cloudBuffer != null) {
            cloudBuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
        }
        if (depthBuffer != null) {
            depthBuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
        }
    }
    
    private void renderWeatherToBuffer(MinecraftClient client) {
        weatherBuffer.beginWrite(true);
        
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
            GlStateManager.SrcFactor.SRC_ALPHA,
            GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SrcFactor.ONE,
            GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA
        );
        
        client.getFramebuffer().beginWrite(false);
    }
    
    private void renderCloudsToBuffer(MinecraftClient client) {
        cloudBuffer.beginWrite(true);
        
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
            GlStateManager.SrcFactor.SRC_ALPHA,
            GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SrcFactor.ONE,
            GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA
        );
        
        client.getFramebuffer().beginWrite(false);
    }
    
    private void renderParticlesToBuffer(MinecraftClient client) {
        particleBuffer.beginWrite(true);
        
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
            GlStateManager.SrcFactor.SRC_ALPHA,
            GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SrcFactor.ONE,
            GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA
        );
        
        client.getFramebuffer().beginWrite(false);
    }
    
    private void compositeTransparencyLayers(MinecraftClient client) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
            GlStateManager.SrcFactor.SRC_ALPHA,
            GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SrcFactor.ONE,
            GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA
        );
        
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
    
    public void cleanup() {
        if (weatherBuffer != null) weatherBuffer.delete();
        if (particleBuffer != null) particleBuffer.delete();
        if (cloudBuffer != null) cloudBuffer.delete();
        if (depthBuffer != null) depthBuffer.delete();
        if (transparencyShader != null) transparencyShader.close();
    }
}
