precision mediump float;

uniform sampler2D u_TextureUnit;

varying float v_ElapsedTime;

void main() {
    // gl_FragColor = vec4(v_Color / v_ElapsedTime, 1.0);
    vec3 color = vec3(0.7, 0.2, 0.1);
    gl_FragColor = vec4(color / v_ElapsedTime, 1.0) * texture2D(u_TextureUnit, gl_PointCoord);
    /// gl_FragColor = u_Color;
}
