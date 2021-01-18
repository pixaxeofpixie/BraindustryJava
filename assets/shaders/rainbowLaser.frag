#define HIGHP
uniform sampler2D u_texture;
varying vec2 v_texCoords;
varying lowp vec4 v_color;
uniform vec2 iResolution;
uniform float u_time;
uniform vec3 u_offset;
uniform vec2 u_grow;
uniform vec2 u_pos;
uniform vec2 u_vecRot;
uniform vec2 u_screenPos;
uniform float u_length;
uniform float u_scl;
float dst(vec2  to,vec2 from){
    float b_x=from.x-to.x;
    float b_y=from.y-to.y;
    return sqrt(b_x*b_x+b_y*b_y);
}
vec4 getPix(in vec2 fragCoord )
{
    vec4 color = texture2D(u_texture, v_texCoords);
    if (color.a==0)return vec4(0);
    float time=(u_time);
    fragCoord=fragCoord.xy;
    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv =fragCoord/ (iResolution.xy*2.);
    float dis=dst(uv,u_screenPos);
    uv*=1.;
    // Time varying pixel color

    vec3 disVec=vec3((dis)/u_length);
    vec3 col = 0.50+0.50*cos(u_time/2.+disVec.x+u_offset);

    // Output to screen
    return vec4(col,1.0);
}
void main(){
    vec4 rainbow=vec4(getPix(vec2(gl_FragCoord.x,gl_FragCoord.y)));
    vec4 color = texture2D(u_texture, v_texCoords);
    gl_FragColor = vec4(rainbow.rgb*color.rgb, color.a*v_color.a);
}