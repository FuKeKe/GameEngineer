uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uCameraLocation;
uniform vec3 uLightLocation;
uniform vec4 uLightAmbient;
uniform vec4 uLightDiffuse;
uniform vec4 uLightSpecular;

attribute vec3 aPosition;
attribute vec2 aTexCoor;
attribute vec3 aNormal;

varying vec2 vTextureCoord;
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;

void pointLight(
	in vec3 normal,							//法向量
	inout vec4 ambient,						//环境光最终强度
	inout vec4 diffuse,						//散射光最终强度
	inout vec4 specular,						//镜面光最终强度
	in vec3 lightLocation,						//光源位置
	in vec4 lightAmbient,						//环境光强度
	in vec4 lightDiffuse,						//散射光强度
	in vec4 lightSpecular						//镜面光强度
	)
{
	//法向量
	vec3 normalTarget = aPosition+normal;				//计算变换后法向量
	vec3 newNormal = (uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
	newNormal = normalize(newNormal);
	
	//计算要用到的向量
	vec3 eye = normalize(uCameraLocation-(uMMatrix*vec4(aPosition,1)).xyz);	//计算表面点到摄像机的向量
	//计算表面点到光源位置的向量，如果为常值就说明是定向光
	vec3 vp = normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);
	vec3 halfVector = normalize(vp+eye);				//求视线与光线的半向量
	
	//散射光
	float nDotViewPosition=max(0.0,dot(newNormal,vp)); 		//求法向量与vp的点积与0的最大值
	diffuse = lightDiffuse*nDotViewPosition;			//计算最终散射光强度
	//反射光
	float shininess = 50.0;						//粗糙程度
	float nDotViewHalfVector = dot(newNormal,halfVector);		//法线与半向量的点积
	float powerFactor = max(0.0,pow(nDotViewHalfVector,shininess)); //镜面反射光强度因子
	specular = lightSpecular*powerFactor;				//最终的镜面光强度
	//环境光
	ambient=lightAmbient;			//直接得出环境光的最终强度  
}

void main()
{
	gl_Position = uMVPMatrix * vec4(aPosition,1);
	vec4 ambientTemp = vec4(0.0,0.0,0.0,0.0);
	vec4 diffuseTemp = vec4(0.0,0.0,0.0,0.0);
	vec4 specularTemp = vec4(0.0,0.0,0.0,0.0);
	pointLight(normalize(aNormal), ambientTemp, diffuseTemp, specularTemp, uLightLocation,
		   uLightAmbient, uLightDiffuse, uLightSpecular);
	vAmbient = ambientTemp;
	vDiffuse = diffuseTemp;
	vSpecular = specularTemp;
	vTextureCoord = aTexCoor;
}