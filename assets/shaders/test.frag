#define HIGHP
uniform sampler2D u_texture;
uniform sampler2D region;
varying vec2 v_texCoords;
varying lowp vec4 v_color;
uniform vec2 u_resolution,u_pos;
uniform float u_time,u_dscl,u_scl,u_delta;
float sd=0.;
bool isIn(vec2 to,vec2 i,float offset){
    if (to.x>i.x-offset && to.x<i.x+offset){

        if (to.y>i.y-offset && to.y<i.y+offset)return true;
    }
    return false;
}
float dst(vec2  to,vec2 from){
    return distance(to,from);
}
void main(){
    vec2 uv =gl_FragCoord.xy/ (u_resolution.xy);
    vec3 gray=vec3(uv.x*uv.y);
    if (isIn(gl_FragCoord.xy,u_pos,8.*u_dscl)){
        gray=vec3(1);
    } else{
        gray=vec3(0);
    }
    if (dst(u_pos,gl_FragCoord.xy)<8.*u_dscl){
        gray.r=1;
    } else{
        gray.r=0.;
    }
    vec4 color = texture2D(u_texture, v_texCoords);
    color=texture2D(region,uv);
    gl_FragColor = vec4(gray.rgb*color.rgb, color.a*v_color.a);
}