#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform float u_red;
uniform float u_green;
uniform float u_blue;
uniform float u_alpha;

void main() {
        vec4 c = v_color * texture2D(u_texture, v_texCoords);
        // 238 / 255.0f, 115 / 255.0f, 98 / 255.0f
        //gl_FragColor = vec4(0.93, 0.45, 0.38, c.a * u_alpha);
        gl_FragColor = vec4(u_red, u_green, u_blue, c.a * u_alpha);
}