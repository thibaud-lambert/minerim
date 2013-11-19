package com.dualcorn.minerim.control;

import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

public class ThirdPersonCameraControl extends AbstractControl implements AnalogListener{
	
	static float rotationSpeed = 20f;
	private Vector3f axis = new Vector3f();
	private Quaternion q = new Quaternion();
	private Node camNode; 
	private Node pitchNode; 
	private Node yawNode;
	private Node playerNode;
	
	
	public ThirdPersonCameraControl(Node playerNode){
		this.playerNode = playerNode;
	}
	
	@Override
	public void onAnalog(String name, float keyValue, float tpf) {
		if(tpf > 0.05){
			// cas o� le tpf est aberrant
			return;
		}
		float angle = tpf * rotationSpeed;
		
		//Node  = (Node) playerNode.getChild("yawNode");
		
		axis = yawNode.getLocalTranslation();
		//System.out.println(yawNode.getLocalRotation());
		float dotCam  = camNode.getWorldTranslation().subtract(yawNode.getWorldTranslation()).normalizeLocal().dot(yawNode.getParent().getWorldTranslation().subtract(yawNode.getWorldTranslation()).normalizeLocal());
		if(name.equals("rotateLeft")){
			q.fromAngleAxis(-angle, axis);
			yawNode.rotate(q);
		}
		if (name.equals("rotateRight")){
			q.fromAngleAxis(angle, axis);
			yawNode.rotate(q);
		}
		if (name.equals("rotateUp")){
			pitchNode.rotate(angle, 0, 0);
			if(pitchNode.getChild("camNode").getWorldTranslation().subtract(yawNode.getWorldTranslation()).normalizeLocal().dot(yawNode.getParent().getWorldTranslation().subtract(yawNode.getWorldTranslation()).normalizeLocal()) > dotCam && dotCam < 0){
				pitchNode.rotate(-angle, 0, 0);
			}
		}
		if (name.equals("rotateDown")){
			pitchNode.rotate(-angle, 0, 0);
			if(pitchNode.getChild("camNode").getWorldTranslation().subtract(yawNode.getWorldTranslation()).normalizeLocal().dot(yawNode.getParent().getWorldTranslation().subtract(yawNode.getWorldTranslation()).normalizeLocal()) < dotCam && dotCam > 0){
				pitchNode.rotate(angle, 0, 0);
			}
		}
	}

	@Override
	protected void controlRender(RenderManager arg0, ViewPort arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void controlUpdate(float tpf) {
		if(camNode == null){
			camNode = (Node) spatial;
			pitchNode = (Node) camNode.getParent();
			yawNode = (Node) pitchNode.getParent();
		}
	}
}
