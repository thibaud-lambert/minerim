package com.dualcorn.minerim.control;

import com.dualcorn.minerim.entity.player.Player;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public class PlayerControl extends BetterCharacterControl implements ActionListener{
	private float speed = 100000;
	private boolean left = false, right = false, up = false, down = false;
	private float moveSpeed = 0.5f;
	protected Player player;
	private Vector3f viewDir;
	private Quaternion qtmp = new Quaternion();
	private Node bodyNode;
	private Camera cam;
	
	public PlayerControl(Player player, float mass, Camera cam){
		super();
		this.player = player;
		rigidBody = new PhysicsRigidBody(getShape(),mass);
		this.cam = cam;
		rigidBody.setAngularFactor(0);
		viewDir = new Vector3f(0,0,0);
	}
	
	@Override
	public CollisionShape getShape(){
		CollisionShape capsule = new CapsuleCollisionShape(0.8f,1.8f);
		return capsule;
	}
	
	@Override
    public void update(float tpf) {
		super.update(tpf);
		Node playerNode = (Node) spatial;
		Node bodyNode = (Node) playerNode.getChild("bodyNode");
		Vector3f camDir = cam.getDirection().normalize();
		Vector3f upCam = spatial.getLocalTranslation().clone().normalize();
		Vector3f straightDir = camDir.subtract(upCam.mult(camDir.dot(upCam))).mult(speed*tpf);
		Vector3f leftDir = cam.getLeft().normalize().mult(speed * tpf);
        walkDirection.set(0, 0, 0);
        boolean dirty = false;
        
        if(left){
        	if(dirty){
        		viewDir.addLocal(leftDir);
        		
        	}else{
        		viewDir.set(leftDir);
        		dirty = true;
        	}
        	this.walkDirection.addLocal(leftDir);
        }
        if(right){
        	leftDir.negateLocal();
        	if(dirty){
        		viewDir.addLocal(leftDir);
        	}else{
        		viewDir.set(leftDir);
        		dirty = true;
        	}
        	this.walkDirection.addLocal(leftDir);
        }
        if(up){
        	if(dirty){
        		viewDir.addLocal(straightDir);
        	}else{
        		viewDir.set(straightDir);
        		dirty = true;
        	}
        	this.walkDirection.addLocal(straightDir);
        }
        if(down){
        	straightDir.negateLocal();
        	if(dirty){
        		viewDir.addLocal(straightDir);
        	}else{
        		viewDir.set(straightDir);
        		dirty = true;
        	}
        	this.walkDirection.addLocal(straightDir);
        }
        if(!viewDir.equals(Vector3f.ZERO)){
        	bodyNode.getWorldRotation().lookAt(viewDir, upCam);
        }
    }
	
	@Override
	public void prePhysicsTick(PhysicsSpace space, float tpf){
		super.prePhysicsTick(space,tpf);
		Vector3f gravity = new Vector3f(Vector3f.ZERO.subtract(spatial.getWorldTranslation()).normalize().mult(9.81f));
		setGravity(gravity);
	}
	
	@Override
	public void onAction(String binding, boolean value, float tpf) {
		if (binding.equals("jump")) {
            if (value) {
                jump = true;
            } else {
            	jump = false;
            }
		}
		if (binding.equals("left")) {
            if (value) {
                left = true;
            } else {
                left = false;
            }
        } else if (binding.equals("right")) {
            if (value) {
                right = true;
            } else {
                right = false;
            }
        } else if (binding.equals("forward")) {
            if (value) {
                up = true;
                
            } else {
                up = false;
            }
        } else if (binding.equals("backward")) {
            if (value) {
                down = true;
            } else {
                down = false;
            }
        }
    }
	
	void moveChar(float tpf) {
    }
}