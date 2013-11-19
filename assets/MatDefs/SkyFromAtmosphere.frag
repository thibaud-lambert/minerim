uniform vec3 m_camPos;
uniform vec3 m_sunDir;
uniform float m_sunIntensity;
uniform float m_g;


varying vec3 vertexPos;
varying vec4 colorR;
varying vec4 colorM;




void main (void){
	gl_FragColor = colorM + colorR;
	//gl_FragColor.a = colorR.a;
}

/*

float phaseR(float mu2)
{   
   return 0.06 * (1.0 + mu2);
}

float phaseM(float mu, float mu2)
{   
   return 0.12 * ((1.0 - m_g * m_g) * (1.0 + mu2))/((2.0 + m_g * m_g) * pow(1.0 + m_g * m_g - 2.0 * m_g * mu, 1.5));
   
}

void main (void)
{
	float mu = dot(m_sunDir, vertexPos) / length(vertexPos);
    	float mu2 = mu*mu;
	gl_FragColor = phaseR(mu2) * colorR + phaseM(mu,mu2) * colorM;
	gl_FragColor.a = max(max(gl_FragColor.r, gl_FragColor.g), gl_FragColor.b);
	//gl_FragColor = 1.0 - exp(-m_sunIntensity * gl_FragColor);
}


*/

/*uniform vec3 m_v3LightPos;
uniform float m_fg;
uniform float m_fg2;
uniform float m_fExposure;

varying vec3 v3Direction;
varying vec4 v4RayleighColor;
varying vec4 v4MieColor;

// Mie phase function
float getMiePhase(float fCos, float fCos2, float g, float g2)
{
   return 1.5 * ((1.0 - g2) / (2.0 + g2)) * (1.0 + fCos2) / pow(1.0 + g2 - 2.0*g*fCos, 1.5);
}

// Rayleigh phase function
float getRayleighPhase(float fCos2)
{   
   //return 0.75 + 0.75 * fCos2;
   return 0.75 * (2.0 + 0.5 * fCos2);
   
}

void main (void)
{
	
    float fCos = dot(m_v3LightPos, v3Direction) / length(v3Direction);
    float fCos2 = fCos*fCos;
    gl_FragColor = getRayleighPhase(fCos2) * v4RayleighColor + getMiePhase(fCos, fCos2, m_fg, m_fg2) * v4MieColor;
    gl_FragColor.a = max(max(gl_FragColor.r, gl_FragColor.g), gl_FragColor.b);
    gl_FragColor = 1.0 - exp(-m_fExposure * gl_FragColor);
}*/