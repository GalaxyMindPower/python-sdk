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
in float cloudHeight;

out vec4 fragColor;

float hash(vec3 p) {
    p = fract(p * vec3(443.897, 441.423, 437.195));
    p += dot(p, p.yzx + 19.19);
    return fract((p.x + p.y) * p.z);
}

float noise3D(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    
    float n000 = hash(i);
    float n100 = hash(i + vec3(1.0, 0.0, 0.0));
    float n010 = hash(i + vec3(0.0, 1.0, 0.0));
    float n110 = hash(i + vec3(1.0, 1.0, 0.0));
    float n001 = hash(i + vec3(0.0, 0.0, 1.0));
    float n101 = hash(i + vec3(1.0, 0.0, 1.0));
    float n011 = hash(i + vec3(0.0, 1.0, 1.0));
    float n111 = hash(i + vec3(1.0, 1.0, 1.0));
    
    float x00 = mix(n000, n100, f.x);
    float x10 = mix(n010, n110, f.x);
    float x01 = mix(n001, n101, f.x);
    float x11 = mix(n011, n111, f.x);
    
    float y0 = mix(x00, x10, f.y);
    float y1 = mix(x01, x11, f.y);
    
    return mix(y0, y1, f.z);
}

float fbm3D(vec3 p) {
    float value = 0.0;
    float amplitude = 0.5;
    float frequency = 1.0;
    
    for (int i = 0; i < 5; i++) {
        value += amplitude * noise3D(p * frequency);
        frequency *= 2.0;
        amplitude *= 0.5;
    }
    
    return value;
}

vec3 computeVolumetricClouds(vec3 pos, float time) {
    vec3 cloudPos = pos * 0.001;
    cloudPos.x += time * 0.01;
    cloudPos.z += time * 0.005;
    
    float density = fbm3D(cloudPos * 2.0);
    
    float detail = fbm3D(cloudPos * 8.0) * 0.3;
    density = density * 0.7 + detail;
    
    density = smoothstep(0.3, 0.7, density);
    
    float heightFalloff = smoothstep(0.0, 50.0, cloudHeight - 128.0) * 
                          smoothstep(200.0, 150.0, cloudHeight - 128.0);
    density *= heightFalloff;
    
    vec3 lightDir = normalize(vec3(0.5, 1.0, 0.3));
    float lighting = max(0.0, dot(lightDir, vec3(0.0, 1.0, 0.0)));
    
    vec3 cloudColor = mix(vec3(0.6, 0.65, 0.7), vec3(1.0, 1.0, 1.0), lighting);
    
    float ambientOcclusion = 1.0 - density * 0.5;
    cloudColor *= ambientOcclusion;
    
    return cloudColor * density;
}

vec3 computeSimpleClouds(vec2 uv, float time) {
    vec2 cloudUV = uv * 0.5;
    cloudUV.x += time * 0.01;
    
    float cloudPattern = fbm3D(vec3(cloudUV * 3.0, time * 0.1));
    cloudPattern = smoothstep(0.4, 0.6, cloudPattern);
    
    vec3 cloudColor = vec3(0.9, 0.95, 1.0);
    
    return cloudColor * cloudPattern;
}

void main() {
    vec4 texColor = texture(Sampler0, texCoord);
    vec4 color = texColor * vertexColor * ColorModulator;
    
    float time = GameTime * 1200.0;
    
    vec3 cloudEffect = computeVolumetricClouds(worldPos, time);
    
    if (vertexDistance > 200.0) {
        vec2 screenUV = gl_FragCoord.xy / vec2(1920.0, 1080.0);
        vec3 simpleCloud = computeSimpleClouds(screenUV, time);
        cloudEffect = mix(cloudEffect, simpleCloud, smoothstep(200.0, 400.0, vertexDistance));
    }
    
    color.rgb = mix(color.rgb, cloudEffect, 0.5);
    
    float fogFactor = smoothstep(FogStart, FogEnd, vertexDistance);
    color.rgb = mix(color.rgb, FogColor.rgb, fogFactor * FogColor.a * 0.5);
    
    float distanceFade = 1.0 - smoothstep(100.0, 500.0, vertexDistance);
    color.a *= distanceFade;
    
    color.a *= 0.8;
    
    if (color.a < 0.01) {
        discard;
    }
    
    fragColor = color;
}
