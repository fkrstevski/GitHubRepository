#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform float u_alpha;

void main() {
        vec4 c = v_color * texture2D(u_texture, v_texCoords);
        //20 / 255.0f, 201 / 255.0f, 113 / 255.0f
        gl_FragColor = vec4(0.078, 0.788, 0.443, c.a * u_alpha);
}