uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldMatrix;

uniform vec3 m_camPos;
uniform vec3 m_sunDir;
uniform float m_rand;

attribute vec4 inPosition; 			// jMonkey equivalent to gl_Vertex

varying vec3 vertexPos;
varying vec4 colorR;
varying vec4 colorM;

float nbSamples = 1.0;

float KrIntensity = 0.0005;
float KmIntensity = 0.000005;

void main(void){
	gl_Position = g_WorldViewProjectionMatrix * inPosition;
	vec3 camToVertex = inPosition.xyz - m_camPos;
	float altMin = 0.00;
	float altMid = 0.30;
	float altMax = 1.0;
	float red = 0.0;
	float green = 0.0;
	float blue = 0.0;
	float alt = dot(normalize(m_camPos),normalize(inPosition.xyz));
	float time = dot(normalize(m_camPos),normalize(-m_sunDir));
	float sunRed = 0.0;
	float sunGreen = 0.0;
	float sunBlue = 0.0;
	float redSunMin = 208.0;//255.0;
	float greenSunMin = 44.0; //255.0;
	float blueSunMin = 16.0; //230.0;
	float redSunMax = 255.0; //208.0;
	float greenSunMax = 255.0; //44.0;
	float blueSunMax = 230.0; //16.0;
		
	if(time > 0.0){
		sunRed = max((alt /*dot(normalize(inPosition.xyz),normalize(-m_sunDir)) /* time */ *  (redSunMax - redSunMin) + redSunMin)/255.0,0.0);
		sunGreen = max((alt /* time */ * (greenSunMax - greenSunMin) + greenSunMin)/255.0,0.0);
		sunBlue =  max((alt /* time */ * (blueSunMax - blueSunMin) + blueSunMin)/255.0,0.0);
	}
	
	/* *Min => amount of * at altMin etc. */
	
	/* At night */
	float nightRedMin = 241.0; float nightRedMid = 115.0; float nightRedMax = 25.0;
	float nightGreenMin = 225.0; float nightGreenMid = 14.0; float nightGreenMax = 15.0;
	float nightBlueMin = 49.0; float nightBlueMid = 88.0; float nightBlueMax = 129.0;	

	/* At midDay */
	float dayRedMin = 225.0; float dayRedMid = 146.0; float dayRedMax = 91.0;
	float dayGreenMin = 255.0; float dayGreenMid = 174.0; float dayGreenMax = 214.0;
	float dayBlueMin = 255.0; float dayBlueMid = 228.0; float dayBlueMax = 206.0;
		
	float redMin = 0.0; float redMid = 0.0; float redMax = 0.0;
	float greenMin = 0.0; float greenMid = 0.0; float greenMax = 0.0;
	float blueMin = 0.0; float blueMid = 0.0; float blueMax = 0.0;
	
	if (time >= 0.0) {
		float sqrt = sqrt(1.0 - time);
		redMin =  sqrt * (nightRedMin - dayRedMin) + dayRedMin;
		redMid = sqrt * (nightRedMid - dayRedMid) + dayRedMid;
		redMax = sqrt * (nightRedMax - dayRedMax) + dayRedMax;
		greenMin = sqrt * (nightGreenMin - dayGreenMin) + dayGreenMin;
		greenMid = sqrt * (nightGreenMid - dayGreenMid) + dayGreenMid;
		greenMax = sqrt * (nightGreenMax - dayGreenMax) + dayGreenMax;
		blueMin = sqrt * (nightBlueMin - dayBlueMin) + dayBlueMin;
		blueMid = sqrt * (nightBlueMid - dayBlueMid) + dayBlueMid;
		blueMax = sqrt * (nightBlueMax - dayBlueMax) + dayBlueMax;
	}
	if (alt < altMin){
		red = redMin/255.0;
		green = greenMin/255.0;
		blue = blueMin/255.0;
	}else if(alt >= altMin && alt < altMid){
		red = ((alt-altMin) * (1.0/(altMid-altMin)) * (redMid-redMin) + redMin)/255.0;
		green = ((alt-altMin) * (1.0/(altMid-altMin)) * (greenMid-greenMin) + greenMin)/255.0;
		blue = ((alt-altMin) * (1.0/(altMid-altMin)) * (blueMid-blueMin) + blueMin)/255.0;
	}else if(alt >= altMid && alt < altMax){
		red = ((alt-altMid) * (1.0/(altMax-altMid)) * (redMax-redMid) + redMid)/255.0;
		green = ((alt-altMid) * (1.0/(altMax-altMid)) * (greenMax-greenMid) + greenMid)/255.0;
		blue = ((alt-altMid) * (1.0/(altMax-altMid)) * (blueMax-blueMid) + blueMid)/255.0;
	}
	if(time < 0.5){
		sunRed = sqrt(2.0 * time) * sunRed;
		sunGreen = 2.0 * time * sunGreen;
		sunBlue  = 2.0 * time * sunBlue;
		red = sqrt(2.0 * time) * red;
		green = 2.0 * time * green;
		blue  = 2.0 * time * blue;
	}
	
	colorR = vec4(red,green,blue,max(red,max(green,blue)));
	float sunLight = dot(normalize(inPosition.xyz),-normalize(m_sunDir));
	sunLight = exp(100.0*(sunLight - 1.0));
	//colorM = max((9.0+cos(m_rand)/10.0)*sunLight*sunLight - (8.0)*sunLight ,0.0) * vec4(sunRed,sunGreen,sunBlue,max(sunRed,max(sunGreen,sunBlue)));
	colorM = sunLight*vec4(sunRed,sunGreen,sunBlue,max(sunRed,max(sunGreen,sunBlue)))/max(time*1.2,0.1);
}