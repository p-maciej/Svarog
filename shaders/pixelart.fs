#version 330 core

in mediump vec2 tex_coords;
uniform sampler2D sampler;
uniform float sharpness;

out vec4 frag_color;

float sharpen(float pix_coord) {
    float norm = (fract(pix_coord) - 0.5) * 2.0;
    float norm2 = norm * norm;
    return floor(pix_coord) + norm * pow(norm2, sharpness) / 2.0 + 0.5;
}


void main() {
 	vec2 vres = textureSize(sampler, 0);
    frag_color = texture(sampler, vec2(
        sharpen(tex_coords.x * vres.x) / vres.x,
        sharpen(tex_coords.y * vres.y) / vres.y
    ));
}