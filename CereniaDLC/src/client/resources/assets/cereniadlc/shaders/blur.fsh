#version 150

uniform sampler2D InputSampler;
uniform vec2 Direction;
uniform vec2 TexelSize;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 color = texture(InputSampler, texCoord) * 0.227027;
    color += texture(InputSampler, texCoord + Direction * TexelSize * 1.384615) * 0.316216;
    color += texture(InputSampler, texCoord - Direction * TexelSize * 1.384615) * 0.316216;
    color += texture(InputSampler, texCoord + Direction * TexelSize * 3.230769) * 0.070270;
    color += texture(InputSampler, texCoord - Direction * TexelSize * 3.230769) * 0.070270;
    fragColor = color;
}
