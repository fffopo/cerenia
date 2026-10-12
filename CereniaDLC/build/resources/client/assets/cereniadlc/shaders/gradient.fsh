#version 150

uniform vec4 LeftColor;
uniform vec4 RightColor;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    fragColor = mix(LeftColor, RightColor, clamp(texCoord.x, 0.0, 1.0));
}
