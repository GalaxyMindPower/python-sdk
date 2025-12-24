import { readdir, readFile, writeFile, mkdir, copyFile } from 'fs/promises';
import { join, dirname } from 'path';
import { fileURLToPath } from 'url';
import { existsSync } from 'fs';

const __dirname = dirname(fileURLToPath(import.meta.url));

async function copyDirectory(src, dest) {
    await mkdir(dest, { recursive: true });
    const entries = await readdir(src, { withFileTypes: true });
    
    for (const entry of entries) {
        const srcPath = join(src, entry.name);
        const destPath = join(dest, entry.name);
        
        if (entry.isDirectory()) {
            await copyDirectory(srcPath, destPath);
        } else {
            await copyFile(srcPath, destPath);
        }
    }
}

async function build() {
    console.log('Building Improved Transparency Mod...');
    
    const buildDir = join(__dirname, 'build');
    const resourcesDir = join(buildDir, 'resources');
    
    await mkdir(buildDir, { recursive: true });
    await mkdir(resourcesDir, { recursive: true });
    
    if (existsSync(join(__dirname, 'resources'))) {
        console.log('Copying shader resources...');
        await copyDirectory(
            join(__dirname, 'resources'),
            join(resourcesDir, 'main')
        );
    }
    
    console.log('Copying mod configuration...');
    await copyFile(
        join(__dirname, 'mod.json'),
        join(buildDir, 'fabric.mod.json')
    );
    
    await copyFile(
        join(__dirname, 'transparency.mixins.json'),
        join(buildDir, 'transparency.mixins.json')
    );
    
    const buildInfo = {
        name: 'improved-transparency',
        version: '1.0.0',
        buildTime: new Date().toISOString(),
        features: [
            'Screen shader-based transparency',
            'Weather rendering behind translucent blocks',
            'Cloud rendering with depth sorting',
            'Particle system with soft particles',
            'Configurable video settings',
            'Performance optimization modes'
        ]
    };
    
    await writeFile(
        join(buildDir, 'build-info.json'),
        JSON.stringify(buildInfo, null, 2)
    );
    
    console.log('Build completed successfully!');
    console.log(`Output directory: ${buildDir}`);
}

build().catch(console.error);
