uniform mat4 u_projTrans;
uniform float u_time;
uniform float u_timeMul;

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_pos;
varying vec2 v_viewportInverse;

uniform vec2 u_viewportInverse;


uniform vec2 iResolution;
uniform float u_mul1;
uniform float u_scl;
void main(){
    gl_Position =u_projTrans * a_position;
    v_pos=a_position;
    v_texCoords = a_texCoord0;
    v_viewportInverse=u_viewportInverse;
    v_color = a_color;
}
