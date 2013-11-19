package com.dualcorn.minerim.entity;

import com.dualcorn.minerim.GameLoop;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public abstract class Entity {
	protected GameLoop app = null;
	protected Node node = null;
	protected String name = null;
	
	public Entity(GameLoop app, String name){
		this.app = app;
		node = new Node("name");
		this.name = name;
	}
	
	public Entity(GameLoop app, String name, Vector3f location){
		this(app,name);
		setLocation(location);
	}
	
	public Node getNode(){
		return node;
	}
	
	public void setLocation(Vector3f location){
		node.setLocalTranslation(location);
	}
	
	public abstract void createMesh();
	
	public String getName(){
		return name;
	}
}