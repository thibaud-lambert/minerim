package com.dualcorn.minerim.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class SunControl extends AbstractControl{
	private float speed; 
	public SunControl(float rotationSpeed){
		this.speed = rotationSpeed;
	}
	@Override
	protected void controlRender(RenderManager arg0, ViewPort arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void controlUpdate(float tpf) {
		// TODO Auto-generated method stub
		//System.out.println(speed*tpf);
		//spatial.setLocalTranslation(0.0f, 0.0f, (float)Math.random());
		spatial.rotate(0f, - speed * tpf, 0f);
	}

}
