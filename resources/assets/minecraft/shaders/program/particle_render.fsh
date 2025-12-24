#version 150

uniform sampler2D Sampler0;
uniform sampler2D DepthSampler;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;
uniform vec3 CameraPos;
uniform vec2 ScreenSize;

in vec2 texCoord;
in vec4 vertexColor;
in float vertexDistance;
in vec3 worldPos;
in vec3 normal;
in float particleDepth;

out vec4 fragColor;

float linearizeDepth(float depth) {
    float near = 0.05;
    float far = 1000.0;
    return (2.0 * near) / (far + near - depth * (far - near));
}

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

vec3 computeParticleLighting(vec3 color, vec3 normal) {
    vec3 lightDir = normalize(vec3(0.5, 1.0, 0.3));
    float diffuse = max(0.0, dot(normal, lightDir));
    
    float ambient = 0.4;
    float lighting = ambient + diffuse * 0.6;
    
    return color * lighting;
}

float computeSoftParticle(float particleDepth, vec2 screenUV) {
    float sceneDepth = texture(DepthSampler, screenUV).r;
    float linearSceneDepth = linearizeDepth(sceneDepth);
    float linearParticleDepth = linearizeDepth(particleDepth);
    
    float depthDiff = linearSceneDepth - linearParticleDepth;
    
    float softness = smoothstep(0.0, 0.5, depthDiff);
    return softness;
}

vec3 applyParticleEffects(vec3 color, float time) {
    vec2 effectUV = texCoord * 5.0 + time * 0.1;
    float sparkle = noise(effectUV) * noise(effectUV * 2.0);
    sparkle = pow(sparkle, 3.0);
    
    color += vec3(sparkle) * 0.2;
    
    float pulse = sin(time * 2.0) * 0.5 + 0.5;
    color *= 0.9 + pulse * 0.1;
    
    return color;
}

void main() {
    vec4 texColor = texture(Sampler0, texCoord);
    vec4 color = texColor * vertexColor * ColorModulator;
    
    float time = GameTime * 1200.0;
    
    vec2 screenUV = gl_FragCoord.xy / ScreenSize;
    float softParticle = computeSoftParticle(particleDepth, screenUV);
    color.a *= softParticle;
    
    if (length(normal) > 0.1) {
        color.rgb = computeParticleLighting(color.rgb, normalize(normal));
    }
    
    color.rgb = applyParticleEffects(color.rgb, time);
    
    float fogFactor = smoothstep(FogStart, FogEnd, vertexDistance);
    color.rgb = mix(color.rgb, FogColor.rgb, fogFactor * FogColor.a);
    
    float distanceFade = 1.0 - smoothstep(0.0, 200.0, vertexDistance);
    color.a *= distanceFade;
    
    float edgeGlow = 1.0 - abs(texCoord.x * 2.0 - 1.0);
    edgeGlow *= 1.0 - abs(texCoord.y * 2.0 - 1.0);
    edgeGlow = pow(edgeGlow, 2.0);
    color.rgb += color.rgb * edgeGlow * 0.3;
    
    if (color.a < 0.01) {
        discard;
    }
    
    fragColor = color;
}
