#version 150

in vec3 Position;
in vec2 UV0;
in vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 CameraPos;
uniform float GameTime;

out vec2 texCoord;
out vec4 vertexColor;
out float vertexDistance;
out vec3 worldPos;

void main() {
    vec4 worldPosition = vec4(Position + CameraPos, 1.0);
    worldPos = worldPosition.xyz;
    
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    
    vertexDistance = length((ModelViewMat * vec4(Position, 1.0)).xyz);
    
    texCoord = UV0;
    vertexColor = Color;
}
