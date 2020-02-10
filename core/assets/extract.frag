#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec2 u_dir;

vec4 filter(vec4 c, float thresh) {
    float maxC = max(c.r, max(c.g, c.b));
    float minC = min(c.r, min(c.g, c.b));

    float lum = (maxC + minC) * 0.5;

    if (lum > thresh) {
        return c;
    } else
    {
        return vec4(0.0, 0.0, 0.0, 1.0);
    }

}

vec4 sampleFilter(vec2 v)
{
    return filter(v_color * texture2D(u_texture, v), 0.2);
}

void main()
{
	//this will be our RGBA sum
	vec4 sum = vec4(0.0);

	//our original texcoord for this fragment
	vec2 tc = v_texCoords;

	//the amount to blur, i.e. how far off center to sample from
	//1.0 -> blur by one pixel
	//2.0 -> blur by two pixels, etc.
	float blur = 1.0/170.0;

	//the direction of our blur
	//(1.0, 0.0) -> x-axis blur
	//(0.0, 1.0) -> y-axis blur

	float hstep = u_dir.x;
	float vstep = u_dir.y;

	//apply blurring, using a 9-tap filter with predefined gaussian weights

	sum += sampleFilter(vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.0162162162;
	sum += sampleFilter(vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.0540540541;
	sum += sampleFilter(vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.1216216216;
	sum += sampleFilter(vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.1945945946;

	vec4 center = sampleFilter(vec2(tc.x, tc.y)) * 0.2270270270;
	sum += center;

	sum += sampleFilter(vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.1945945946;
	sum += sampleFilter(vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.1216216216;
	sum += sampleFilter(vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.0540540541;
	sum += sampleFilter(vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.0162162162;

	//discard alpha for our simple demo, multiply by vertex color and return
	gl_FragColor = 1.0 * vec4(sum.rgb, 1.0);
}