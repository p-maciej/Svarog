#version 120

uniform lowp sampler2D sampler;
uniform lowp vec4 tint;
uniform lowp vec4 sharpness;
uniform lowp vec4 texture_size;

varying mediump vec2 tex_coords;

float sharpen(float pix_coord) {
    float norm = (fract(pix_coord) - 0.5) * 2.0;
    float norm2 = norm * norm;
    return floor(pix_coord) + norm * pow(norm2, sharpness.x) / 2.0 + 0.5;
}

void main() {
 	//vec2 vres = textureSize(sampler, 0);
	vec2 vres = vec2(texture_size.x, texture_size.y);
	lowp vec4 tint_pm = vec4(tint.xyz * tint.w, tint.w);
	gl_FragColor = texture2D(sampler, vec2(
        sharpen(tex_coords.x * vres.x) / vres.x,
        sharpen(tex_coords.y * vres.y) / vres.y
    )) * tint_pm;
	//gl_FragColor = texture2D(sampler, tex_coords);
}