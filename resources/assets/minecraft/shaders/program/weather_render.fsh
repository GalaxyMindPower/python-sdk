#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;
uniform vec3 CameraPos;

in vec2 texCoord;
in vec4 vertexColor;
in float vertexDistance;
in vec3 worldPos;

out vec4 fragColor;

float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    
    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));
    
    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;
    float frequency = 1.0;
    
    for (int i = 0; i < 4; i++) {
        value += amplitude * noise(p * frequency);
        frequency *= 2.0;
        amplitude *= 0.5;
    }
    
    return value;
}

vec3 computeRainEffect(vec2 uv, float time) {
    vec2 rainUV = uv * vec2(0.5, 2.0);
    rainUV.y += time * 2.0;
    
    float rainPattern = fbm(rainUV * 10.0);
    rainPattern = smoothstep(0.4, 0.6, rainPattern);
    
    float streaks = fbm(vec2(rainUV.x * 20.0, rainUV.y * 0.5));
    rainPattern *= streaks;
    
    return vec3(0.7, 0.8, 0.9) * rainPattern;
}

vec3 computeSnowEffect(vec2 uv, float time) {
    vec2 snowUV = uv * 5.0;
    snowUV.y += time * 0.3;
    snowUV.x += sin(snowUV.y * 2.0 + time) * 0.1;
    
    float snowPattern = fbm(snowUV * 3.0);
    snowPattern = smoothstep(0.5, 0.7, snowPattern);
    
    float flakes = hash(floor(snowUV * 10.0));
    flakes = step(0.95, flakes);
    
    return vec3(1.0) * (snowPattern * 0.5 + flakes * 0.5);
}

void main() {
    vec4 texColor = texture(Sampler0, texCoord);
    vec4 color = texColor * vertexColor * ColorModulator;
    
    float time = GameTime * 1200.0;
    vec2 screenUV = gl_FragCoord.xy / vec2(1920.0, 1080.0);
    
    vec3 weatherEffect = vec3(0.0);
    
    if (vertexColor.b > vertexColor.r) {
        weatherEffect = computeRainEffect(screenUV, time);
    } else if (vertexColor.r > 0.9 && vertexColor.g > 0.9 && vertexColor.b > 0.9) {
        weatherEffect = computeSnowEffect(screenUV, time);
    }
    
    color.rgb = mix(color.rgb, weatherEffect, 0.3);
    
    float fogFactor = smoothstep(FogStart, FogEnd, vertexDistance);
    color.rgb = mix(color.rgb, FogColor.rgb, fogFactor * FogColor.a);
    
    float depthFade = 1.0 - smoothstep(0.0, 100.0, vertexDistance);
    color.a *= depthFade;
    
    if (color.a < 0.01) {
        discard;
    }
    
    fragColor = color;
}
