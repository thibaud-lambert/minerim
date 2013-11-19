package com.dualcorn.minerim.entity.player;

import com.dualcorn.minerim.GameLoop;
import com.dualcorn.minerim.control.ThirdPersonCameraControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;

public class MainPlayer extends Player {
	private ThirdPersonCameraControl tpc = null;
	
	public MainPlayer(GameLoop app, String name, Vector3f location) {
		super(app, name, location);
		createTPCamera();
	}
	
	private void createTPCamera(){
		tpc = new ThirdPersonCameraControl(this.getNode());
		CameraControl camControl = new CameraControl(app.getCamera());
		Node camNode = new Node("camNode");
		Node pitchNode = new Node("pitchNode");
		Node yawNode = new Node("yawNode");
		this.getNode().attachChild(yawNode);
		yawNode.attachChild(pitchNode);
		pitchNode.attachChild(camNode);
		this.getHeadNode().attachChild(camNode);
		camNode.addControl(camControl);
		camNode.addControl(tpc);
		camNode.setLocalTranslation(0,5f,-20f);
		app.getGameplayManager().addPlayerListener(pc, tpc);
	}
}