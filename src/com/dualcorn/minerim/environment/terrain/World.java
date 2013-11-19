package com.dualcorn.minerim.environment.terrain;

import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;


public class World extends Mesh {

	private SimpleApplication app;
	private Node rootNode;
	
	/**
	 * Contructeur de la classe World
	 */
	public World(SimpleApplication app)
	{
		this.app =app;
		this.rootNode = app.getRootNode();
		
	}

	
	public void attachGeometry(Geometry g)
	{
		rootNode.attachChild(g);
	}
	
	public void removeGeometry(Geometry g)
	{
		rootNode.detachChild(g);
	}
	
	public void attachRigidBodyControl(RigidBodyControl b)
	{
		app.getStateManager().getState(BulletAppState.class)
		.getPhysicsSpace().add(b);
	}
	
	public void removeRigidBodyControl(RigidBodyControl b)
	{
		app.getStateManager().getState(BulletAppState.class)
		.getPhysicsSpace().remove(b);
	}
	

	
	
}
