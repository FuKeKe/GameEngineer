precision mediump float;
uniform sampler2D sTexture;
uniform vec2 vTexCoordMult;
varying vec2 vTextureCoord;
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;
void main()
{
	vec4 finalColor = texture2D(sTexture,vTextureCoord*vTexCoordMult);			//Ã˘Õº
	//vec4 finalColor = vec4(1.0,1.0,1.0,1.0);
	gl_FragColor = finalColor*vAmbient + finalColor*vDiffuse + finalColor*vSpecular;	//π‚”∞
}