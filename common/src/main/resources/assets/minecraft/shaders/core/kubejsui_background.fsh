#version 150

uniform vec2 resolution;
uniform float time;
uniform float tick;
uniform vec2 mouse;

out vec4 fragColor;

vec3 hue2rgb(float hue) {
	vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
	vec3 p = abs(fract(vec3(hue + K.x, hue + K.y, hue + K.z)) * 6.0 - K.www);
	return mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), 1.0);
}

void main() {
	vec2 m = (gl_FragCoord.xy - mouse * resolution) / max(resolution.x, resolution.y);
	float d = sqrt(m.x * m.x + m.y * m.y);
	float d1 = 1.0 - clamp(d * 10.0 / (sin(time) + 1.5), 0.0, 1.0);

	if (d1 <= 0.0) {
		fragColor = vec4(0.0, 0.0, 0.0, 1.0);
	} else {
		fragColor = vec4(hue2rgb(mouse.x) * d1, 1.0);
	}
}