#version 330 core

layout (location = 0) in vec3 a_position;

uniform mat4 u_projectionMatrix;
uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;

out vec3 v_worldPos;

void main() {
    // Calculer la position mondiale
    vec4 worldPos = u_viewMatrix * u_modelMatrix * vec4(a_position, 1.0);
    v_worldPos = worldPos.xyz;

    // Appliquer la projection isométrique
    gl_Position = u_projectionMatrix * worldPos;
}