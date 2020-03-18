#version 330 core

in vec3 vertices;
in vec2 textures;

out mediump vec2 tex_coords;

uniform mat4 projection;

void main() {
	tex_coords = textures;
	gl_Position = projection * vec4(vertices, 1);
}