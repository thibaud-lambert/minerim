package com.dualcorn.minerim.control;

import com.jme3.light.DirectionalLight;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class DirectionalLightControl extends AbstractControl{

	private DirectionalLight light;
	public DirectionalLightControl(DirectionalLight l){
		light = l;
		
	}
	@Override
	protected void controlRender(RenderManager arg0, ViewPort arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void controlUpdate(float tpf) {
		//System.out.println(spatial.getWorldTranslation());
		light.setDirection(spatial.getWorldTranslation());
	}

}
