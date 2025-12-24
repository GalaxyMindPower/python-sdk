# Installation Guide - Improved Transparency Mod

## Prerequisites

Before installing the Improved Transparency Mod, ensure you have:

1. **Minecraft Java Edition** (Version 1.19.x - 1.20.x)
2. **Fabric Loader** (Version 0.14.0 or higher)
3. **Fabric API** (Latest version for your Minecraft version)
4. **Java 17 or higher**

## Step-by-Step Installation

### 1. Install Fabric Loader

If you haven't already installed Fabric:

1. Download the Fabric installer from [fabricmc.net](https://fabricmc.net/use/)
2. Run the installer
3. Select your Minecraft version
4. Click "Install"
5. Launch Minecraft and select the Fabric profile

### 2. Install Fabric API

1. Download Fabric API from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/fabric-api) or [Modrinth](https://modrinth.com/mod/fabric-api)
2. Place the downloaded JAR file in your `.minecraft/mods` folder

### 3. Install Improved Transparency Mod

1. Download the latest release of Improved Transparency Mod
2. Place the JAR file in your `.minecraft/mods` folder
3. Launch Minecraft with the Fabric profile

### 4. Verify Installation

1. Launch Minecraft
2. Check the mods list (Mods button on main menu)
3. Look for "Improved Transparency Mod" in the list
4. Check the game log for: `[improved-transparency] Initializing Improved Transparency Mod`

## Directory Structure

After installation, your Minecraft directory should look like this:

```
.minecraft/
├── mods/
│   ├── fabric-api-x.x.x.jar
│   └── improved-transparency-1.0.0.jar
├── config/
│   └── improved-transparency.json (created on first launch)
└── ...
```

## Configuration

### First Launch

On first launch, the mod will create a default configuration file at:
```
.minecraft/config/improved-transparency.json
```

### Default Configuration

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

### Accessing Settings In-Game

Currently, settings must be edited manually in the config file. A future update will add an in-game settings screen.

To edit settings:
1. Close Minecraft
2. Open `.minecraft/config/improved-transparency.json` in a text editor
3. Modify values as desired
4. Save the file
5. Launch Minecraft

## System Requirements

### Minimum Requirements
- **GPU**: GTX 1050 / RX 560 or equivalent
- **VRAM**: 2GB
- **RAM**: 8GB (4GB allocated to Minecraft)
- **Java**: Java 17

### Recommended Requirements
- **GPU**: GTX 1660 / RX 5600 XT or better
- **VRAM**: 4GB
- **RAM**: 16GB (6-8GB allocated to Minecraft)
- **Java**: Java 17 or 21

### Optimal Requirements
- **GPU**: RTX 3060 / RX 6700 XT or better
- **VRAM**: 8GB
- **RAM**: 16GB+ (8GB allocated to Minecraft)
- **Java**: Java 21

## Performance Optimization

### For Low-End Systems

Edit your config file:
```json
{
  "renderQuality": 0,
  "enablePerformanceMode": true,
  "cloudsBehindTranslucent": false,
  "weatherOpacity": 0.7,
  "cloudOpacity": 0.6
}
```

### For Mid-Range Systems

Edit your config file:
```json
{
  "renderQuality": 1,
  "enablePerformanceMode": false,
  "useDepthSorting": true
}
```

### For High-End Systems

Edit your config file:
```json
{
  "renderQuality": 3,
  "enablePerformanceMode": false,
  "useDepthSorting": true
}
```

## Compatibility

### Compatible Mods
- ✅ Sodium
- ✅ Iris Shaders
- ✅ Lithium
- ✅ Phosphor
- ✅ Mod Menu
- ✅ Most optimization mods

### Incompatible Mods
- ❌ OptiFine (use Iris + Sodium instead)
- ❌ Custom weather mods that override rendering
- ❌ Mods that heavily modify WorldRenderer

### Shader Pack Compatibility

The mod works with most Iris shader packs, but may have visual conflicts with:
- Shader packs that implement custom transparency
- Shader packs with custom weather systems
- Shader packs that heavily modify particle rendering

**Recommendation**: Test your shader pack with the mod. If you experience issues, try disabling specific features in the config.

## Troubleshooting

### Mod Doesn't Load

**Symptoms**: Mod not in mods list, no log messages

**Solutions**:
1. Verify Fabric Loader is installed
2. Ensure Fabric API is in the mods folder
3. Check that you're using the correct Minecraft version
4. Verify Java 17+ is installed

### Game Crashes on Launch

**Symptoms**: Crash report mentioning transparency mod

**Solutions**:
1. Update Fabric API to the latest version
2. Remove conflicting mods (especially OptiFine)
3. Allocate more RAM to Minecraft (6-8GB recommended)
4. Update graphics drivers

### Low FPS

**Symptoms**: Significant FPS drop with mod enabled

**Solutions**:
1. Enable Performance Mode in config
2. Lower render quality
3. Disable individual features (clouds, weather, particles)
4. Reduce vanilla render distance
5. See [PERFORMANCE_GUIDE.md](PERFORMANCE_GUIDE.md) for detailed optimization

### Visual Artifacts

**Symptoms**: Flickering, incorrect rendering, z-fighting

**Solutions**:
1. Enable depth sorting in config
2. Disable conflicting shader packs
3. Update graphics drivers
4. Try different render quality settings

### Config File Not Found

**Symptoms**: Config file doesn't exist after launch

**Solutions**:
1. Ensure the mod loaded successfully (check logs)
2. Manually create the config file with default values
3. Check file permissions on the config directory

## Uninstallation

To remove the mod:

1. Close Minecraft
2. Delete `improved-transparency-1.0.0.jar` from `.minecraft/mods`
3. (Optional) Delete `.minecraft/config/improved-transparency.json`
4. Launch Minecraft

## Updating

To update to a new version:

1. Close Minecraft
2. Delete the old version from `.minecraft/mods`
3. Download the new version
4. Place the new JAR in `.minecraft/mods`
5. Launch Minecraft

**Note**: Your config file will be preserved. New settings will use default values.

## Getting Help

If you encounter issues:

1. Check this installation guide
2. Read [PERFORMANCE_GUIDE.md](PERFORMANCE_GUIDE.md)
3. Review [TECHNICAL_DOCUMENTATION.md](TECHNICAL_DOCUMENTATION.md)
4. Check the game logs in `.minecraft/logs/latest.log`
5. Report bugs on the GitHub repository

## Advanced Installation

### MultiMC / Prism Launcher

1. Create a new instance with Minecraft 1.20.1
2. Click "Edit Instance"
3. Go to "Version" tab
4. Click "Install Fabric"
5. Go to "Loader mods" tab
6. Click "Add" and select Fabric API
7. Click "Add" and select Improved Transparency Mod
8. Launch the instance

### Server Installation

**Note**: This is a client-side mod. It does not need to be installed on servers.

However, if you want to install it on a server (for testing):

1. Install Fabric Loader on the server
2. Install Fabric API
3. Place the mod JAR in the server's `mods` folder
4. Start the server

**Warning**: The mod will have no effect on the server as it only modifies client-side rendering.

## Building from Source

If you want to build the mod yourself:

### Prerequisites
- Java 17 JDK
- Gradle 8.0+
- Node.js 22+ (optional, for build scripts)

### Build Steps

```bash
# Clone the repository
git clone https://github.com/your-repo/improved-transparency.git
cd improved-transparency

# Build with Gradle
./gradlew build

# Or build with Node.js
npm install
npm run build

# Output will be in build/libs/
```

## License

This mod is released under the MIT License. See [LICENSE](LICENSE) for details.

## Credits

- **Fabric Team**: For the Fabric modding framework
- **Minecraft Community**: For shader development resources
- **Contributors**: See GitHub repository for full list

## Support the Project

If you enjoy this mod:
- ⭐ Star the repository on GitHub
- 🐛 Report bugs and issues
- 💡 Suggest new features
- 📝 Contribute code or documentation
- 📢 Share with friends

Thank you for using Improved Transparency Mod!
