#version 150

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 CameraPos;
uniform float GameTime;

out vec2 texCoord;
out vec4 vertexColor;
out float vertexDistance;
out vec3 worldPos;
out vec3 normal;
out float particleDepth;

void main() {
    vec4 worldPosition = vec4(Position + CameraPos, 1.0);
    worldPos = worldPosition.xyz;
    
    vec4 viewPos = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * viewPos;
    
    vertexDistance = length(viewPos.xyz);
    particleDepth = -viewPos.z;
    
    texCoord = UV0;
    vertexColor = Color;
    normal = Normal;
}
