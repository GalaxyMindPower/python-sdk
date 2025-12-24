# Improved Transparency Mod

An experimental Minecraft mod that uses screen shaders for drawing weather, clouds, and particles behind translucent blocks and water.

## Features

### Core Rendering System
- **Screen Shader-Based Transparency**: Uses multiple framebuffers to render weather, clouds, and particles separately before compositing them behind translucent blocks
- **Depth-Aware Rendering**: Implements proper depth sorting to ensure correct rendering order
- **Multi-Pass Rendering**: Separates rendering into distinct passes for optimal performance and quality

### Weather Effects
- **Enhanced Rain Rendering**: Procedural rain with realistic streaks and patterns
- **Volumetric Snow**: Soft, floating snowflakes with wind simulation
- **Atmospheric Scattering**: Weather effects blend naturally with the environment
- **Behind Translucent Blocks**: Weather renders correctly behind glass, water, and other transparent surfaces

### Cloud System
- **Volumetric Clouds**: 3D noise-based cloud generation with realistic density
- **Dynamic Movement**: Clouds move naturally across the sky
- **Height-Based Falloff**: Clouds fade appropriately based on altitude
- **Lighting Integration**: Clouds respond to directional lighting and ambient occlusion

### Particle System
- **Soft Particles**: Particles blend smoothly with the scene using depth-based softness
- **Enhanced Lighting**: Particles receive proper lighting from the environment
- **Depth Sorting**: Particles render correctly behind translucent surfaces
- **Visual Effects**: Sparkle, pulse, and edge glow effects for enhanced visuals

### Video Settings Integration
- **Enable/Disable Toggle**: Turn the entire system on or off
- **Individual Component Control**: 
  - Weather behind translucent blocks
  - Clouds behind translucent blocks
  - Particles behind translucent blocks
- **Depth Sorting Toggle**: Enable or disable depth-based sorting
- **Render Quality Levels**:
  - Low: Basic rendering, minimal GPU impact
  - Medium: Standard quality with bilinear filtering
  - High: Enhanced quality with better sampling
  - Ultra: Maximum quality with blur and post-processing
- **Performance Mode**: Optimized settings for lower-end GPUs
- **Opacity Controls**: Adjust transparency of weather, clouds, and particles independently

## Technical Details

### Shader Architecture
```
Main Framebuffer
├── Weather Buffer (RGBA + Depth)
├── Cloud Buffer (RGBA + Depth)
├── Particle Buffer (RGBA + Depth)
└── Composite Shader (Combines all layers)
```

### Shader Files
- `transparency_composite.vsh/fsh`: Main compositing shader
- `weather_render.vsh/fsh`: Weather effect rendering
- `cloud_render.vsh/fsh`: Volumetric cloud rendering
- `particle_render.vsh/fsh`: Enhanced particle rendering
- `depth_utils.glsl`: Depth calculation utilities
- `noise_utils.glsl`: Procedural noise functions

### Performance Considerations

**GPU Impact**: This mod uses multiple framebuffers and screen-space shaders, which can impact GPU performance:
- **Low Quality**: ~5-10% GPU overhead
- **Medium Quality**: ~10-15% GPU overhead
- **High Quality**: ~15-25% GPU overhead
- **Ultra Quality**: ~25-40% GPU overhead

**Optimization Features**:
- Adaptive quality based on distance
- Frustum culling for off-screen effects
- LOD system for distant particles and clouds
- Performance mode for budget GPUs

## Installation

1. Install Fabric Loader for Minecraft 1.20.1+
2. Install Fabric API
3. Place the mod JAR in your `mods` folder
4. Launch Minecraft

## Configuration

Configuration file location: `.minecraft/config/improved-transparency.json`

```json
{
  "enabled": true,
  "weatherBehindTranslucent": true,
  "cloudsBehindTranslucent": true,
  "particlesBehindTranslucent": true,
  "useDepthSorting": true,
  "weatherOpacity": 1.0,
  "cloudOpacity": 1.0,
  "particleOpacity": 1.0,
  "renderQuality": 2,
  "enablePerformanceMode": false
}
```

## Building from Source

### Prerequisites
- Java 17 or higher
- Gradle 8.0+
- Node.js 22+ (for build scripts)

### Build Commands
```bash
# Using Gradle
./gradlew build

# Using Node.js build script
npm run build

# Development mode with auto-rebuild
npm run dev
```

## Compatibility

- **Minecraft Version**: 1.19.x - 1.20.x
- **Fabric Loader**: 0.14.0+
- **Fabric API**: Required
- **OptiFine**: Not compatible (use Iris + Sodium instead)
- **Iris Shaders**: Compatible with most shader packs

## Known Issues

1. **Performance**: High-quality settings may cause FPS drops on older GPUs
2. **Shader Packs**: Some shader packs may conflict with the transparency system
3. **Resource Packs**: Custom weather/particle textures may not render correctly

## Troubleshooting

### Low FPS
- Enable Performance Mode in video settings
- Lower Render Quality to Medium or Low
- Disable individual features (clouds, weather, or particles)
- Reduce vanilla render distance

### Visual Artifacts
- Ensure depth sorting is enabled
- Check for conflicting mods (especially other rendering mods)
- Try disabling shader packs temporarily
- Update graphics drivers

### Crashes
- Check that you have enough VRAM (2GB+ recommended)
- Verify Fabric API is installed
- Check logs for specific error messages

## Credits

- **Shader Development**: Advanced GLSL techniques for transparency rendering
- **Noise Functions**: Procedural generation algorithms
- **Fabric API**: Mod framework and rendering hooks

## License

MIT License - See LICENSE file for details

## Support

For bug reports and feature requests, please open an issue on the GitHub repository.

## Changelog

### Version 1.0.0
- Initial release
- Screen shader-based transparency system
- Weather, cloud, and particle rendering
- Video settings integration
- Performance optimization modes
- Configurable opacity and quality settings
