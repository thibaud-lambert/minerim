package com.dualcorn.minerim.entity.player;

import java.util.HashMap;

import com.dualcorn.minerim.GameLoop;
import com.dualcorn.minerim.control.PlayerControl;
import com.dualcorn.minerim.entity.Entity;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


public class Player extends Entity{
	
	/*
	 * 			playerNode
	 * 				/\
	 * 			   /  \
	 * 		bodyNode  neckNode
	 * 					\
	 * 					headNode
	 */
	static protected HashMap<String,Player> map = null;
	static private AssetManager assetManager;
	static Node parentNode = null;
	protected PlayerControl pc;
	
	private Node headNode;
	private Node bodyNode;
	private Node neckNode;
	private Node head;
	private Node body;
	private Vector3f headLocation;
	private Vector3f bodyLocation;
	
	
	public Player(GameLoop app, String name, Vector3f location){
		super(app, name,location);
		if(this.parentNode == null){
			this.parentNode = new Node();
			app.getRootNode().attachChild(parentNode);
		}
		parentNode.attachChild(this.node);
		pc = new PlayerControl(this,60f,app.getCamera());
		app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(pc);
		
		if (assetManager == null){
			assetManager = app.getAssetManager();
		}
		
		if(map == null){
			map = new HashMap<String,Player>();
		}
		//node = new Node("player");
		System.out.println(parentNode);
		parentNode.attachChild(node);
		node.addControl(pc);
		
		bodyLocation = new Vector3f(0f,0f,0f);
		bodyNode = new Node("bodyNode");
		node.attachChild(bodyNode);
		bodyNode.setLocalTranslation(bodyLocation);
		
		headLocation = new Vector3f(0f,1.2f,0f);
		headNode = new Node("headNode");
		neckNode = new Node("neckNode");
		node.attachChild(neckNode);
		neckNode.attachChild(headNode);
		neckNode.setLocalTranslation(headLocation);
		headNode.setLocalTranslation(0f, 0f, 0f);
		map.put(name, this);
	}
	
	public Vector3f getHeadLocalTranslation(){
		return headLocation;
	}
	
	public Vector3f getBodyLocalTranslation(){
		return bodyLocation;
	}
	
	@Override
	public void createMesh() {
		body =  (Node) assetManager.loadModel("Models/persoBody/persoBody.mesh.xml" );
		body.setLocalTranslation(0f,0f,0f);
		bodyNode.attachChild(body);
		head = (Node) assetManager.loadModel("Models/persoHead/persoHead.mesh.xml" );
		bodyNode.attachChild(head);
		head.setLocalTranslation(headLocation);
	}
	
	public Node getNeckNode(){
		return neckNode;
	}
	
	public Node getHeadNode(){
		return headNode;
	}
	
	public Node getBodyNode(){
		return bodyNode;
	}
	
	public Node getHead(){
		return head;
	}
	
	public Node getBody(){
		return body;
	}
	
}
