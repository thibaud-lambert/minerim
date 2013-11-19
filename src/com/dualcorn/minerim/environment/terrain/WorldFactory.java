package com.dualcorn.minerim.environment.terrain;

import com.dualcorn.minerim.GameLoop;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
/*import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;*/
import com.jme3.math.Vector3f;

public class WorldFactory extends AbstractAppState{
	public ChunkManager w;
	//private SimpleApplication app;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		w = new ChunkManager((SimpleApplication) app);
		w.start();
	}
	public WorldFactory(GameLoop app){
		//this.app = app;
		
	}
	
	public void /*Spatial*/ createWorld(Vector3f position){
		
	}
	
	 @Override
	 public void update(float tpf) {
		 w.updateChunkRender();
	 }
	
	 @Override
	 public void cleanup(){
		 w.cancel();
	 }
}