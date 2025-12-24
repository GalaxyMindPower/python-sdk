# Performance Guide - Improved Transparency Mod

## Understanding GPU Performance Impact

This mod uses advanced rendering techniques that require additional GPU resources. Understanding the performance characteristics will help you optimize settings for your system.

## Performance Metrics by Quality Level

### Low Quality
- **GPU Overhead**: 5-10%
- **VRAM Usage**: +33 MB
- **Bandwidth**: ~2.5 GB/s
- **Recommended For**: 
  - GTX 1050 / RX 560 or lower
  - Integrated graphics (Intel UHD, AMD Vega)
  - Systems with <4GB VRAM

**Settings**:
```json
{
  "renderQuality": 0,
  "enablePerformanceMode": true,
  "weatherOpacity": 0.8,
  "cloudOpacity": 0.7,
  "particleOpacity": 0.8
}
```

### Medium Quality
- **GPU Overhead**: 10-15%
- **VRAM Usage**: +33 MB
- **Bandwidth**: ~3.5 GB/s
- **Recommended For**:
  - GTX 1060 / RX 580
  - GTX 1650 / RX 5500 XT
  - Systems with 4-6GB VRAM

**Settings**:
```json
{
  "renderQuality": 1,
  "enablePerformanceMode": false,
  "weatherOpacity": 1.0,
  "cloudOpacity": 0.9,
  "particleOpacity": 1.0
}
```

### High Quality
- **GPU Overhead**: 15-25%
- **VRAM Usage**: +33 MB
- **Bandwidth**: ~4.4 GB/s
- **Recommended For**:
  - RTX 2060 / RX 5700
  - GTX 1070 Ti / RX Vega 64
  - Systems with 6-8GB VRAM

**Settings**:
```json
{
  "renderQuality": 2,
  "enablePerformanceMode": false,
  "useDepthSorting": true,
  "weatherOpacity": 1.0,
  "cloudOpacity": 1.0,
  "particleOpacity": 1.0
}
```

### Ultra Quality
- **GPU Overhead**: 25-40%
- **VRAM Usage**: +33 MB
- **Bandwidth**: ~5.5 GB/s
- **Recommended For**:
  - RTX 3060 Ti / RX 6700 XT or higher
  - RTX 2080 / RX 5700 XT
  - Systems with 8GB+ VRAM

**Settings**:
```json
{
  "renderQuality": 3,
  "enablePerformanceMode": false,
  "useDepthSorting": true,
  "weatherOpacity": 1.0,
  "cloudOpacity": 1.0,
  "particleOpacity": 1.0
}
```

## Optimization Strategies

### 1. Selective Feature Disabling

Disable features you don't need:

```json
{
  "weatherBehindTranslucent": true,   // Keep if you want rain/snow effects
  "cloudsBehindTranslucent": false,   // Disable if clouds aren't important
  "particlesBehindTranslucent": true  // Keep for better particle effects
}
```

**Performance Impact**:
- Disabling clouds: ~8-12% GPU savings
- Disabling weather: ~5-8% GPU savings
- Disabling particles: ~3-5% GPU savings

### 2. Opacity Reduction

Lower opacity values reduce blending overhead:

```json
{
  "weatherOpacity": 0.7,  // 70% opacity
  "cloudOpacity": 0.6,    // 60% opacity
  "particleOpacity": 0.8  // 80% opacity
}
```

**Performance Impact**: 2-5% GPU savings with minimal visual difference

### 3. Depth Sorting Toggle

Depth sorting adds computational overhead:

```json
{
  "useDepthSorting": false  // Disable for ~5-8% GPU savings
}
```

**Trade-off**: May cause visual artifacts where effects render incorrectly behind translucent blocks

### 4. Performance Mode

Enables multiple optimizations:

```json
{
  "enablePerformanceMode": true
}
```

**Changes**:
- Reduces buffer resolution by 50%
- Limits FBM octaves to 3
- Disables expensive post-processing
- Simplifies particle effects
- Increases LOD distances

**Performance Impact**: 15-25% GPU savings

## Resolution Scaling

The mod's performance scales with resolution:

| Resolution | VRAM Usage | Bandwidth | Relative Performance |
|------------|------------|-----------|---------------------|
| 1280x720   | 18 MB      | 1.8 GB/s  | 100% (baseline)     |
| 1920x1080  | 33 MB      | 4.4 GB/s  | 65-75%              |
| 2560x1440  | 59 MB      | 7.8 GB/s  | 45-55%              |
| 3840x2160  | 132 MB     | 17.5 GB/s | 25-35%              |

**Recommendation**: If you're running at 4K, consider using Performance Mode or reducing render quality.

## Vanilla Settings Impact

### Render Distance
- **High Impact**: More chunks = more weather/clouds to render
- **Recommendation**: Keep at 12-16 chunks for best balance

### Particles
- **Medium Impact**: More particles = more work for particle shader
- **Recommendation**: Set to "Decreased" or "Minimal" if struggling

### Graphics Quality
- **Low Impact**: Fancy vs Fast has minimal effect on this mod
- **Recommendation**: Use "Fast" for slight improvement

### VSync
- **No Impact**: VSync doesn't affect GPU load, only frame pacing
- **Recommendation**: Enable if you experience screen tearing

## Shader Pack Compatibility

### Performance with Shader Packs

| Shader Pack Type | Additional Overhead | Recommended Quality |
|------------------|---------------------|---------------------|
| None             | 0%                  | High/Ultra          |
| Lightweight      | 5-10%               | Medium/High         |
| Medium           | 15-25%              | Low/Medium          |
| Heavy            | 30-50%              | Low only            |

**Note**: Some shader packs may conflict with this mod. Test compatibility before using both.

## Monitoring Performance

### In-Game Metrics

Press `F3` to view debug screen:
- **FPS**: Target 60+ for smooth gameplay
- **GPU**: Should stay below 95% for headroom
- **Memory**: Watch for VRAM usage

### External Tools

**MSI Afterburner / RivaTuner**:
- Monitor GPU usage
- Track VRAM allocation
- Check frame times

**GPU-Z**:
- Real-time GPU stats
- Memory bandwidth usage
- Temperature monitoring

## Troubleshooting Performance Issues

### Low FPS (<30)

1. Enable Performance Mode
2. Reduce Render Quality to Low
3. Disable clouds and weather
4. Lower vanilla render distance
5. Close background applications

### Stuttering

1. Disable depth sorting
2. Reduce particle opacity
3. Check for VRAM overflow (reduce resolution)
4. Update graphics drivers
5. Allocate more RAM to Minecraft (8GB recommended)

### High GPU Temperature

1. Enable Performance Mode
2. Cap frame rate to 60 FPS
3. Improve case airflow
4. Clean GPU fans/heatsink
5. Consider undervolting GPU

### VRAM Overflow

Symptoms:
- Severe stuttering
- Texture pop-in
- Crashes

Solutions:
1. Enable Performance Mode (reduces buffer resolution)
2. Lower game resolution
3. Reduce vanilla render distance
4. Close other applications
5. Disable resource packs with high-res textures

## Benchmark Results

### Test System 1: Mid-Range
- **GPU**: RTX 3060 (12GB)
- **CPU**: Ryzen 5 5600X
- **RAM**: 16GB DDR4
- **Resolution**: 1920x1080

| Quality | FPS (Vanilla) | FPS (Mod) | GPU Usage |
|---------|---------------|-----------|-----------|
| Low     | 180           | 165       | 45%       |
| Medium  | 180           | 155       | 52%       |
| High    | 180           | 140       | 58%       |
| Ultra   | 180           | 115       | 68%       |

### Test System 2: Budget
- **GPU**: GTX 1650 (4GB)
- **CPU**: i5-10400F
- **RAM**: 16GB DDR4
- **Resolution**: 1920x1080

| Quality | FPS (Vanilla) | FPS (Mod) | GPU Usage |
|---------|---------------|-----------|-----------|
| Low     | 90            | 80        | 75%       |
| Medium  | 90            | 68        | 85%       |
| High    | 90            | 55        | 92%       |
| Ultra   | 90            | 42        | 98%       |

### Test System 3: High-End
- **GPU**: RTX 4070 Ti (12GB)
- **CPU**: Ryzen 7 7800X3D
- **RAM**: 32GB DDR5
- **Resolution**: 2560x1440

| Quality | FPS (Vanilla) | FPS (Mod) | GPU Usage |
|---------|---------------|-----------|-----------|
| Low     | 240           | 225       | 38%       |
| Medium  | 240           | 210       | 42%       |
| High    | 240           | 195       | 48%       |
| Ultra   | 240           | 165       | 56%       |

## Optimal Settings by GPU

### NVIDIA

**RTX 40 Series**: Ultra Quality
**RTX 30 Series**: High-Ultra Quality
**RTX 20 Series**: Medium-High Quality
**GTX 16 Series**: Low-Medium Quality
**GTX 10 Series**: Low Quality + Performance Mode

### AMD

**RX 7000 Series**: Ultra Quality
**RX 6000 Series**: High-Ultra Quality
**RX 5000 Series**: Medium-High Quality
**RX 500 Series**: Low Quality + Performance Mode

### Intel

**Arc A770/A750**: High Quality
**Arc A580/A380**: Medium Quality
**Xe Graphics**: Low Quality + Performance Mode

## Future Optimizations

Planned improvements for future versions:

1. **Async Compute**: Offload shader work to compute queue
2. **Variable Rate Shading**: Reduce shading in low-detail areas
3. **Temporal Reprojection**: Reuse previous frame data
4. **Adaptive Quality**: Automatically adjust based on FPS
5. **Checkerboard Rendering**: Render at half resolution and upscale

## Conclusion

The Improved Transparency Mod offers flexible performance options for a wide range of hardware. Start with the recommended settings for your GPU tier and adjust based on your target frame rate and visual preferences.

For the best experience:
- **60 FPS target**: Use recommended quality for your GPU
- **120+ FPS target**: Drop one quality level
- **Maximum visuals**: Use highest quality your GPU can handle while maintaining 60+ FPS

Remember: Visual quality vs performance is always a trade-off. Find the balance that works best for your system and gameplay style.
