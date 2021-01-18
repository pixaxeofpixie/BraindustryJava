#define HIGHP
uniform sampler2D u_texture;
varying vec2 v_texCoords;
varying lowp vec4 v_color;
uniform vec2 iResolution;
uniform float u_time;
float dst(vec2  to,vec2 from){
    float b_x=from.x-to.x;
    float b_y=from.y-to.y;
    return sqrt(b_x*b_x+b_y*b_y);
}
void main(){
    vec2 uv =gl_FragCoord.xy/ (iResolution.xy);
    vec3 gray=vec3(uv.x*uv.y);
    vec4 color = texture2D(u_texture, v_texCoords);
    gl_FragColor = vec4(gray.rgb*color.rgb, color.a*v_color.a);
}