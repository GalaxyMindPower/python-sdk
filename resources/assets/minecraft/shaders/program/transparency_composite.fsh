#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D WeatherSampler;
uniform sampler2D CloudSampler;
uniform sampler2D ParticleSampler;
uniform sampler2D DepthSampler;
uniform sampler2D TranslucentDepthSampler;

uniform vec2 InSize;
uniform float WeatherOpacity;
uniform float CloudOpacity;
uniform float ParticleOpacity;
uniform int UseDepthSorting;
uniform int RenderQuality;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

float linearizeDepth(float depth) {
    float near = 0.05;
    float far = 1000.0;
    return (2.0 * near) / (far + near - depth * (far - near));
}

vec4 sampleWithBilinear(sampler2D tex, vec2 uv) {
    if (RenderQuality >= 2) {
        vec2 texelSize = oneTexel;
        vec2 f = fract(uv / texelSize);
        
        vec4 tl = texture(tex, uv);
        vec4 tr = texture(tex, uv + vec2(texelSize.x, 0.0));
        vec4 bl = texture(tex, uv + vec2(0.0, texelSize.y));
        vec4 br = texture(tex, uv + texelSize);
        
        vec4 tA = mix(tl, tr, f.x);
        vec4 tB = mix(bl, br, f.x);
        return mix(tA, tB, f.y);
    } else {
        return texture(tex, uv);
    }
}

float computeDepthWeight(float weatherDepth, float translucentDepth) {
    float linearWeather = linearizeDepth(weatherDepth);
    float linearTranslucent = linearizeDepth(translucentDepth);
    
    if (linearWeather < linearTranslucent) {
        return 1.0;
    } else {
        float depthDiff = linearWeather - linearTranslucent;
        return smoothstep(0.0, 0.1, depthDiff);
    }
}

vec3 applyAtmosphericScattering(vec3 color, float depth) {
    float linearDepth = linearizeDepth(depth);
    float fogAmount = 1.0 - exp(-linearDepth * 0.5);
    vec3 fogColor = vec3(0.6, 0.7, 0.8);
    return mix(color, fogColor, fogAmount * 0.3);
}

void main() {
    vec4 baseColor = texture(DiffuseSampler, texCoord);
    vec4 weatherColor = sampleWithBilinear(WeatherSampler, texCoord);
    vec4 cloudColor = sampleWithBilinear(CloudSampler, texCoord);
    vec4 particleColor = sampleWithBilinear(ParticleSampler, texCoord);
    
    float sceneDepth = texture(DepthSampler, texCoord).r;
    float translucentDepth = texture(TranslucentDepthSampler, texCoord).r;
    
    vec3 finalColor = baseColor.rgb;
    float finalAlpha = baseColor.a;
    
    if (UseDepthSorting == 1) {
        float weatherWeight = computeDepthWeight(
            texture(DepthSampler, texCoord).r, 
            translucentDepth
        );
        weatherColor.a *= weatherWeight;
        
        float cloudWeight = computeDepthWeight(
            texture(DepthSampler, texCoord).r, 
            translucentDepth
        );
        cloudColor.a *= cloudWeight;
        
        float particleWeight = computeDepthWeight(
            texture(DepthSampler, texCoord).r, 
            translucentDepth
        );
        particleColor.a *= particleWeight;
    }
    
    if (cloudColor.a > 0.001) {
        cloudColor.rgb = applyAtmosphericScattering(cloudColor.rgb, sceneDepth);
        cloudColor.a *= CloudOpacity;
        finalColor = mix(finalColor, cloudColor.rgb, cloudColor.a);
    }
    
    if (weatherColor.a > 0.001) {
        weatherColor.a *= WeatherOpacity;
        finalColor = mix(finalColor, weatherColor.rgb, weatherColor.a);
    }
    
    if (particleColor.a > 0.001) {
        particleColor.a *= ParticleOpacity;
        finalColor = mix(finalColor, particleColor.rgb, particleColor.a);
    }
    
    if (RenderQuality >= 3) {
        vec3 blurred = vec3(0.0);
        float totalWeight = 0.0;
        
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                vec2 offset = vec2(float(x), float(y)) * oneTexel;
                float weight = 1.0 / (1.0 + length(vec2(x, y)));
                blurred += texture(DiffuseSampler, texCoord + offset).rgb * weight;
                totalWeight += weight;
            }
        }
        blurred /= totalWeight;
        
        float edgeFactor = length(finalColor - baseColor.rgb);
        finalColor = mix(finalColor, blurred, edgeFactor * 0.1);
    }
    
    fragColor = vec4(finalColor, finalAlpha);
}
