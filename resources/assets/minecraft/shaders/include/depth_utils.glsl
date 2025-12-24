#version 150

float linearizeDepth(float depth, float near, float far) {
    return (2.0 * near) / (far + near - depth * (far - near));
}

float getLinearDepth(sampler2D depthSampler, vec2 uv, float near, float far) {
    float depth = texture(depthSampler, uv).r;
    return linearizeDepth(depth, near, far);
}

float compareDepths(float depth1, float depth2, float threshold) {
    float diff = abs(depth1 - depth2);
    return smoothstep(0.0, threshold, diff);
}

float computeDepthWeight(float objectDepth, float referenceDepth, float softness) {
    if (objectDepth < referenceDepth) {
        return 1.0;
    } else {
        float depthDiff = objectDepth - referenceDepth;
        return smoothstep(0.0, softness, depthDiff);
    }
}

vec3 reconstructWorldPos(vec2 uv, float depth, mat4 invProjMat, mat4 invViewMat) {
    vec4 clipSpacePos = vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    vec4 viewSpacePos = invProjMat * clipSpacePos;
    viewSpacePos /= viewSpacePos.w;
    vec4 worldSpacePos = invViewMat * viewSpacePos;
    return worldSpacePos.xyz;
}
