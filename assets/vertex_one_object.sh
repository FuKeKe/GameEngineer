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
	in vec3 normal,							//������
	inout vec4 ambient,						//����������ǿ��
	inout vec4 diffuse,						//ɢ�������ǿ��
	inout vec4 specular,						//���������ǿ��
	in vec3 lightLocation,						//��Դλ��
	in vec4 lightAmbient,						//������ǿ��
	in vec4 lightDiffuse,						//ɢ���ǿ��
	in vec4 lightSpecular						//�����ǿ��
	)
{
	//������
	vec3 normalTarget = aPosition+normal;				//����任������
	vec3 newNormal = (uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
	newNormal = normalize(newNormal);
	
	//����Ҫ�õ�������
	vec3 eye = normalize(uCameraLocation-(uMMatrix*vec4(aPosition,1)).xyz);	//�������㵽�����������
	//�������㵽��Դλ�õ����������Ϊ��ֵ��˵���Ƕ����
	vec3 vp = normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);
	vec3 halfVector = normalize(vp+eye);				//����������ߵİ�����
	
	//ɢ���
	float nDotViewPosition=max(0.0,dot(newNormal,vp)); 		//��������vp�ĵ����0�����ֵ
	diffuse = lightDiffuse*nDotViewPosition;			//��������ɢ���ǿ��
	//�����
	float shininess = 50.0;						//�ֲڳ̶�
	float nDotViewHalfVector = dot(newNormal,halfVector);		//������������ĵ��
	float powerFactor = max(0.0,pow(nDotViewHalfVector,shininess)); //���淴���ǿ������
	specular = lightSpecular*powerFactor;				//���յľ����ǿ��
	//������
	ambient=lightAmbient;			//ֱ�ӵó������������ǿ��  
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