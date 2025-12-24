#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D TranslucentSampler;
uniform sampler2D TranslucentDepthSampler;
uniform sampler2D ParticlesSampler;
uniform sampler2D ParticlesDepthSampler;
uniform sampler2D WeatherSampler;
uniform sampler2D WeatherDepthSampler;
uniform sampler2D CloudsSampler;
uniform sampler2D CloudsDepthSampler;
uniform sampler2D ItemEntitySampler;
uniform sampler2D ItemEntityDepthSampler;

in vec2 texCoord;

out vec4 fragColor;

// Improved transparency shader that renders weather, clouds, and particles behind translucent blocks
void main() {
    vec4 diffuseColor = texture(DiffuseSampler, texCoord);
    vec4 translucentColor = texture(TranslucentSampler, texCoord);
    float translucentDepth = texture(TranslucentDepthSampler, texCoord).r;

    vec4 particlesColor = texture(ParticlesSampler, texCoord);
    float particlesDepth = texture(ParticlesDepthSampler, texCoord).r;

    vec4 weatherColor = texture(WeatherSampler, texCoord);
    float weatherDepth = texture(WeatherDepthSampler, texCoord).r;

    vec4 cloudsColor = texture(CloudsSampler, texCoord);
    float cloudsDepth = texture(CloudsDepthSampler, texCoord).r;

    vec4 itemEntityColor = texture(ItemEntitySampler, texCoord);
    float itemEntityDepth = texture(ItemEntityDepthSampler, texCoord).r;

    // Initialize final color with diffuse
    fragColor = diffuseColor;

    // Layer translucent blocks
    if (translucentColor.a > 0.0) {
        fragColor = mix(fragColor, translucentColor, translucentColor.a);
    }

    // Layer weather behind translucent blocks (experimental)
    if (weatherColor.a > 0.0 && weatherDepth > translucentDepth) {
        fragColor = mix(fragColor, weatherColor, weatherColor.a * 0.8);
    }

    // Layer clouds behind translucent blocks (experimental)
    if (cloudsColor.a > 0.0 && cloudsDepth > translucentDepth) {
        fragColor = mix(fragColor, cloudsColor, cloudsColor.a * 0.6);
    }

    // Layer particles behind translucent blocks (experimental)
    if (particlesColor.a > 0.0 && particlesDepth > translucentDepth) {
        fragColor = mix(fragColor, particlesColor, particlesColor.a);
    }

    // Layer item entities
    if (itemEntityColor.a > 0.0) {
        fragColor = mix(fragColor, itemEntityColor, itemEntityColor.a);
    }
}