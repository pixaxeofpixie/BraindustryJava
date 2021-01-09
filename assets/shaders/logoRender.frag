#define HIGHP
varying vec2 v_texCoords;
varying lowp vec4 v_color;
varying vec4 v_pos;
varying vec2 v_viewportInverse;


uniform sampler2D u_texture;
uniform float u_time;
uniform vec2 iResolution;
uniform vec2 u_texsize;
uniform vec2 u_size;
uniform float u_timeMul;
uniform float u_mul1;
uniform float u_scl;
uniform vec4 colorFrom;
uniform vec4 colorTo;
vec4 getPix(vec2 fragCoord )
{
    float time=u_time;
    vec2 uv = fragCoord/(iResolution.xy*2.);

    vec3 col = 0.60+0.40*cos(time+uv.xyx+vec3(0,2,4));
    col = 0.70+0.30*cos(time/2.+uv.xyx+vec3(-2,2,-0));

    // Output to screen
    return vec4(col,1.0);
}
void main(){

    float iTime=u_time;
    vec2 texCoord=vec2(v_texCoords.xy);
    vec2 fragCoord=vec2(gl_FragCoord.xy);
    texCoord.x+=cos(fragCoord.y+u_timeMul)/(iResolution.y*u_mul1);
    vec4 rainbow=vec4(getPix(fragCoord.xy));
    vec4 color = texture2D(u_texture,texCoord);


    vec3 mulColor=mix(colorFrom.rgb,colorTo.rgb,v_texCoords.y*u_scl);
    if (u_texsize.x<=texCoord.x){
       color.a=0.0;
    }
    gl_FragColor = vec4(mix(mulColor.rgb*color.rgb,color.rgb,0.3), color.a);
}