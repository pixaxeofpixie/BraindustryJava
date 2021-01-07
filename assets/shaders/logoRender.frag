#define HIGHP
uniform sampler2D u_texture;
uniform float u_time;
varying vec2 v_texCoords;
varying lowp vec4 v_color;
uniform vec2 iResolution;
vec4 getPix(vec2 fragCoord )
{
    float time=u_time;
    vec2 uv = fragCoord/(iResolution.xy*2.);

    vec3 col = 0.60+0.40*cos(time+uv.xyx+vec3(0,2,4));
    col = 0.70+0.30*cos(time/2.+uv.xyx+vec3(-2,2,-0));

    // Output to screen
    return vec4(col,1.0);
}

float rand(vec2 co){return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);}
float rand (vec2 co, float l) {return rand(vec2(rand(co), l));}
float rand (vec2 co, float l, float t) {return rand(vec2(rand(co, l), t));}

float perlin(vec2 p, float dim, float time) {
    float M_PI=3.141592;
 	vec2 pos = floor(p * dim);
	vec2 posx = pos + vec2(1.0, 0.0);
	vec2 posy = pos + vec2(0.0, 1.0);
	vec2 posxy = pos + vec2(1.0);

	float c = rand(pos, dim, time);
	float cx = rand(posx, dim, time);
	float cy = rand(posy, dim, time);
	float cxy = rand(posxy, dim, time);

	vec2 d = fract(p * dim);
	d = -0.5 * cos(d * M_PI) + 0.5;

	float ccx = mix(c, cx, d.x);
	float cycxy = mix(cy, cxy, d.x);
	float center = mix(ccx, cycxy, d.y);

	return center * 2.0 - 1.0;
}
float perlin(vec2 p, float dim) {
	return perlin(p, dim, 0.0);
}
void main(){


    vec2 r=vec2(rand(vec2(v_texCoords.x),u_time),v_texCoords.y);
    vec4 rainbow=vec4(getPix(gl_FragCoord.xy));
    vec4 color = texture2D(u_texture,r);
    gl_FragColor = vec4(rainbow.rgb*color.rgb, color.a);
}