#version 150

uniform vec2 Size;
uniform float Radius;
uniform vec4 FillColor;
in vec2 texCoord;
out vec4 fragColor;

float roundedBox(vec2 p, vec2 halfSize, float radius) {
    vec2 q = abs(p) - halfSize + radius;
    return length(max(q, vec2(0.0))) + min(max(q.x, q.y), 0.0) - radius;
}

void main() {
    vec2 p = texCoord * Size - Size * 0.5;
    float d = roundedBox(p, Size * 0.5, Radius);
    float alpha = 1.0 - smoothstep(-1.0, 1.0, d);
    fragColor = vec4(FillColor.rgb, FillColor.a * alpha);
}
