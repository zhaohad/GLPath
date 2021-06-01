uniform mat4 u_Matrix;
uniform float u_Time;

attribute vec3 a_Position;
// attribute vec3 a_Color;
attribute vec3 a_DirectionVector;
attribute float a_ParticleStartTime;

varying float v_ElapsedTime;

void main() {
    v_ElapsedTime = u_Time - a_ParticleStartTime;
    float gravityFactor = v_ElapsedTime * v_ElapsedTime / 3.0;

    vec3 pos = a_Position + (a_DirectionVector * v_ElapsedTime);
    pos.y -= gravityFactor;

    gl_Position = u_Matrix * vec4(pos, 1);
    gl_PointSize = 10.0;
}   
