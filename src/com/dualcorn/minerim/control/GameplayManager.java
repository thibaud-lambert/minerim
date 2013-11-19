package com.dualcorn.minerim.control;

import com.dualcorn.minerim.GameLoop;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;

public class GameplayManager {

	private GameLoop app;
	private InputManager inputManager;

	public GameplayManager(GameLoop app) {
		this.app = app;
		this.inputManager = app.getInputManager();
	}

	public void setupKeys() {
		inputManager.addMapping("forward", new KeyTrigger(KeyInput.KEY_Z));
		inputManager.addMapping("backward", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("rotateDown", new MouseAxisTrigger(MouseInput.AXIS_Y,false));
		inputManager.addMapping("rotateUp", new MouseAxisTrigger(MouseInput.AXIS_Y,true));
		inputManager.addMapping("rotateRight", new MouseAxisTrigger(MouseInput.AXIS_X,true));
		inputManager.addMapping("rotateLeft", new MouseAxisTrigger(MouseInput.AXIS_X,false));
	}
	public void addPlayerListener(PlayerControl pc, ThirdPersonCameraControl tpc){
		if(pc != null){
			inputManager.addListener(pc, "right");
			inputManager.addListener(pc, "jump");
			inputManager.addListener(pc, "left");
			inputManager.addListener(pc, "backward");
			inputManager.addListener(pc, "forward");
		}
		if(tpc != null){
			inputManager.addListener(tpc, "rotateLeft");
			inputManager.addListener(tpc, "rotateRight");
			inputManager.addListener(tpc, "rotateUp");
			inputManager.addListener(tpc, "rotateDown");
		}
	}
}
