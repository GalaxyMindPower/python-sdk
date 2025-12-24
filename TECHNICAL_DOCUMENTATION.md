# Technical Documentation - Improved Transparency Mod

## Architecture Overview

### Rendering Pipeline

The mod implements a multi-pass rendering system that separates translucent effects into distinct layers:

```
Frame Start
    ↓
Setup Transparency Buffers
    ↓
Render Opaque Geometry → Main Framebuffer
    ↓
Capture Depth Buffer
    ↓
Render Weather → Weather Buffer
    ↓
Render Clouds → Cloud Buffer
    ↓
Render Particles → Particle Buffer
    ↓
Render Translucent Blocks → Main Framebuffer
    ↓
Composite All Layers → Final Output
    ↓
Frame End
```

## Framebuffer System

### Buffer Allocation

The mod creates four separate framebuffers:

1. **Weather Buffer** (RGBA16F + Depth24)
   - Stores rain and snow effects
   - Alpha channel for transparency
   - Depth for proper sorting

2. **Cloud Buffer** (RGBA16F + Depth24)
   - Volumetric cloud rendering
   - High precision for smooth gradients
   - Separate depth for layering

3. **Particle Buffer** (RGBA16F + Depth24)
   - All particle effects
   - Soft particle blending
   - Per-particle depth values

4. **Depth Buffer** (R32F)
   - Scene depth information
   - Used for depth comparisons
   - Enables proper occlusion

### Memory Management

```java
// Buffer initialization
weatherBuffer = new Framebuffer(width, height, true, IS_SYSTEM_MAC);
weatherBuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

// Automatic resizing on window resize
if (weatherBuffer.viewportWidth != newWidth || 
    weatherBuffer.viewportHeight != newHeight) {
    weatherBuffer.resize(newWidth, newHeight, IS_SYSTEM_MAC);
}
```

## Shader System

### Transparency Composite Shader

**Purpose**: Combines all rendering layers with proper depth sorting

**Inputs**:
- `DiffuseSampler`: Main scene color
- `WeatherSampler`: Weather effects
- `CloudSampler`: Cloud layer
- `ParticleSampler`: Particle effects
- `DepthSampler`: Scene depth
- `TranslucentDepthSampler`: Translucent block depth

**Algorithm**:
```glsl
1. Sample all input textures
2. Linearize depth values for accurate comparison
3. For each layer (clouds, weather, particles):
   a. Compute depth weight based on translucent depth
   b. Apply atmospheric scattering if needed
   c. Blend with base color using alpha compositing
4. Apply quality-based post-processing
5. Output final color
```

**Depth Weight Calculation**:
```glsl
float computeDepthWeight(float objectDepth, float translucentDepth) {
    float linearObject = linearizeDepth(objectDepth);
    float linearTranslucent = linearizeDepth(translucentDepth);
    
    if (linearObject < linearTranslucent) {
        return 1.0; // Object is in front, fully visible
    } else {
        float depthDiff = linearObject - linearTranslucent;
        return smoothstep(0.0, 0.1, depthDiff); // Smooth transition
    }
}
```

### Weather Shader

**Features**:
- Procedural rain generation using FBM noise
- Snow particle simulation with wind
- Dynamic movement based on game time
- Atmospheric blending

**Rain Algorithm**:
```glsl
vec3 computeRainEffect(vec2 uv, float time) {
    // Stretch UV for vertical streaks
    vec2 rainUV = uv * vec2(0.5, 2.0);
    rainUV.y += time * 2.0; // Downward movement
    
    // Generate rain pattern with noise
    float rainPattern = fbm(rainUV * 10.0);
    rainPattern = smoothstep(0.4, 0.6, rainPattern);
    
    // Add vertical streaks
    float streaks = fbm(vec2(rainUV.x * 20.0, rainUV.y * 0.5));
    rainPattern *= streaks;
    
    return vec3(0.7, 0.8, 0.9) * rainPattern;
}
```

**Snow Algorithm**:
```glsl
vec3 computeSnowEffect(vec2 uv, float time) {
    vec2 snowUV = uv * 5.0;
    snowUV.y += time * 0.3; // Slower fall speed
    snowUV.x += sin(snowUV.y * 2.0 + time) * 0.1; // Wind drift
    
    // Base snow pattern
    float snowPattern = fbm(snowUV * 3.0);
    snowPattern = smoothstep(0.5, 0.7, snowPattern);
    
    // Individual flakes
    float flakes = hash(floor(snowUV * 10.0));
    flakes = step(0.95, flakes);
    
    return vec3(1.0) * (snowPattern * 0.5 + flakes * 0.5);
}
```

### Cloud Shader

**Volumetric Cloud Rendering**:

The cloud shader implements a simplified volumetric rendering approach:

```glsl
vec3 computeVolumetricClouds(vec3 pos, float time) {
    // Transform to cloud space
    vec3 cloudPos = pos * 0.001;
    cloudPos.x += time * 0.01; // Wind movement
    
    // Multi-octave noise for cloud density
    float density = fbm3D(cloudPos * 2.0);
    
    // Add fine detail
    float detail = fbm3D(cloudPos * 8.0) * 0.3;
    density = density * 0.7 + detail;
    
    // Shape clouds
    density = smoothstep(0.3, 0.7, density);
    
    // Height-based falloff
    float heightFalloff = smoothstep(0.0, 50.0, cloudHeight - 128.0) * 
                          smoothstep(200.0, 150.0, cloudHeight - 128.0);
    density *= heightFalloff;
    
    // Lighting
    vec3 lightDir = normalize(vec3(0.5, 1.0, 0.3));
    float lighting = max(0.0, dot(lightDir, vec3(0.0, 1.0, 0.0)));
    vec3 cloudColor = mix(vec3(0.6, 0.65, 0.7), vec3(1.0), lighting);
    
    // Ambient occlusion
    float ao = 1.0 - density * 0.5;
    cloudColor *= ao;
    
    return cloudColor * density;
}
```

**Performance Optimization**:
- LOD system switches to simpler 2D clouds at distance
- Frustum culling for off-screen clouds
- Adaptive sampling based on render quality

### Particle Shader

**Soft Particle Implementation**:

```glsl
float computeSoftParticle(float particleDepth, vec2 screenUV) {
    // Sample scene depth
    float sceneDepth = texture(DepthSampler, screenUV).r;
    
    // Linearize both depths
    float linearScene = linearizeDepth(sceneDepth);
    float linearParticle = linearizeDepth(particleDepth);
    
    // Compute depth difference
    float depthDiff = linearScene - linearParticle;
    
    // Smooth fade based on proximity
    return smoothstep(0.0, 0.5, depthDiff);
}
```

**Particle Lighting**:
```glsl
vec3 computeParticleLighting(vec3 color, vec3 normal) {
    vec3 lightDir = normalize(vec3(0.5, 1.0, 0.3));
    float diffuse = max(0.0, dot(normal, lightDir));
    
    float ambient = 0.4;
    float lighting = ambient + diffuse * 0.6;
    
    return color * lighting;
}
```

**Visual Effects**:
- Sparkle effect using noise
- Pulse animation based on game time
- Edge glow for enhanced visibility

## Depth Sorting System

### Linearization

Depth values are stored in a non-linear format for better precision near the camera. The mod linearizes these values for accurate comparisons:

```glsl
float linearizeDepth(float depth) {
    float near = 0.05;
    float far = 1000.0;
    return (2.0 * near) / (far + near - depth * (far - near));
}
```

### Comparison Logic

```glsl
if (UseDepthSorting == 1) {
    // Compute weight for each layer
    float weatherWeight = computeDepthWeight(
        weatherDepth, 
        translucentDepth
    );
    weatherColor.a *= weatherWeight;
    
    // Repeat for clouds and particles
    // ...
}
```

## Noise Functions

### Hash Functions

```glsl
// 2D hash for random values
float hash2D(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);
}

// 3D hash for volumetric effects
float hash3D(vec3 p) {
    p = fract(p * vec3(443.897, 441.423, 437.195));
    p += dot(p, p.yzx + 19.19);
    return fract((p.x + p.y) * p.z);
}
```

### Perlin Noise

```glsl
float noise3D(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f); // Smoothstep interpolation
    
    // Sample 8 corners of cube
    float n000 = hash3D(i);
    float n100 = hash3D(i + vec3(1.0, 0.0, 0.0));
    // ... (6 more corners)
    
    // Trilinear interpolation
    float x00 = mix(n000, n100, f.x);
    float x10 = mix(n010, n110, f.x);
    // ... (continue interpolation)
    
    return mix(y0, y1, f.z);
}
```

### Fractional Brownian Motion (FBM)

```glsl
float fbm3D(vec3 p, int octaves) {
    float value = 0.0;
    float amplitude = 0.5;
    float frequency = 1.0;
    
    for (int i = 0; i < octaves; i++) {
        value += amplitude * noise3D(p * frequency);
        frequency *= 2.0;  // Double frequency each octave
        amplitude *= 0.5;  // Halve amplitude each octave
    }
    
    return value;
}
```

## Performance Optimization

### Quality Levels

**Low (0)**:
- No bilinear filtering
- Minimal post-processing
- 3 FBM octaves
- Simple particle rendering

**Medium (1)**:
- Basic bilinear filtering
- Standard post-processing
- 4 FBM octaves
- Soft particles enabled

**High (2)**:
- Full bilinear filtering
- Enhanced sampling
- 5 FBM octaves
- All effects enabled

**Ultra (3)**:
- Maximum quality filtering
- Blur post-processing
- 6 FBM octaves
- All effects + extras

### Performance Mode

When enabled:
- Reduces buffer resolution by 50%
- Disables expensive post-processing
- Limits FBM octaves to 3
- Simplifies particle effects
- Increases LOD distances

### Adaptive Quality

```java
if (vertexDistance > 200.0) {
    // Switch to simpler rendering
    vec3 simpleCloud = computeSimpleClouds(screenUV, time);
    cloudEffect = mix(cloudEffect, simpleCloud, 
                     smoothstep(200.0, 400.0, vertexDistance));
}
```

## Integration Points

### Mixin Injection Points

**WorldRendererMixin**:
```java
@Inject(method = "render", at = @At(
    value = "INVOKE",
    target = "renderWeather",
    shift = At.Shift.BEFORE
))
```

**ParticleManagerMixin**:
```java
@Inject(method = "renderParticles", at = @At("HEAD"))
```

### Fabric API Hooks

```java
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
```

## Configuration System

### Config Structure

```java
public class TransparencyConfig {
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
}
```

### Serialization

Uses Gson for JSON serialization:
```java
private static final Gson GSON = new GsonBuilder()
    .setPrettyPrinting()
    .create();

public static TransparencyConfig load() {
    String json = Files.readString(CONFIG_PATH);
    return GSON.fromJson(json, TransparencyConfig.class);
}
```

## GPU Performance Impact

### Memory Usage

Per frame at 1920x1080:
- Weather Buffer: 8.3 MB (RGBA16F + Depth24)
- Cloud Buffer: 8.3 MB
- Particle Buffer: 8.3 MB
- Depth Buffer: 8.3 MB
- **Total**: ~33 MB additional VRAM

### Shader Complexity

- Transparency Composite: ~150 ALU operations
- Weather Shader: ~200 ALU operations
- Cloud Shader: ~300 ALU operations (volumetric)
- Particle Shader: ~180 ALU operations

### Bandwidth Requirements

Per frame:
- 4 framebuffer reads: ~33 MB
- 4 framebuffer writes: ~33 MB
- Depth buffer access: ~8 MB
- **Total**: ~74 MB per frame

At 60 FPS: ~4.4 GB/s bandwidth

## Future Improvements

1. **Temporal Anti-Aliasing**: Reduce noise in procedural effects
2. **Ray-Marched Clouds**: True volumetric rendering
3. **Screen-Space Reflections**: Reflect weather in water
4. **Async Compute**: Offload shader work to compute queue
5. **Variable Rate Shading**: Reduce shading in low-detail areas

## Debugging

### Shader Debugging

Enable debug output in shaders:
```glsl
// Visualize depth
fragColor = vec4(vec3(linearizeDepth(depth)), 1.0);

// Visualize individual layers
fragColor = vec4(weatherColor.rgb, 1.0); // Weather only
fragColor = vec4(cloudColor.rgb, 1.0);   // Clouds only
```

### Performance Profiling

Use Minecraft's built-in profiler:
```
F3 + L: Start profiling
F3 + L: Stop profiling
```

Check `debug/profiling/` for results.

## Compatibility Notes

### Shader Pack Compatibility

The mod may conflict with shader packs that:
- Override weather rendering
- Modify particle systems
- Use custom framebuffers
- Implement their own transparency

### Mod Compatibility

Compatible with:
- Sodium
- Iris Shaders
- Fabric API
- Most optimization mods

Incompatible with:
- OptiFine (use Iris instead)
- Mods that heavily modify WorldRenderer
- Custom weather mods

## References

- [OpenGL Framebuffer Objects](https://www.khronos.org/opengl/wiki/Framebuffer_Object)
- [GPU Gems: Soft Particles](https://developer.nvidia.com/gpugems/gpugems3/part-iv-image-effects/chapter-23-high-speed-screen-particles)
- [Volumetric Cloud Rendering](https://www.guerrilla-games.com/read/nubis-realtime-volumetric-cloudscapes-in-a-nutshell)
- [Perlin Noise](https://en.wikipedia.org/wiki/Perlin_noise)
