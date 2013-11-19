package com.dualcorn.minerim;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.dualcorn.minerim.control.DirectionalLightControl;
import com.dualcorn.minerim.control.GameplayManager;
import com.dualcorn.minerim.control.SunControl;
import com.dualcorn.minerim.entity.player.MainPlayer;
import com.dualcorn.minerim.entity.player.Player;
import com.dualcorn.minerim.environment.terrain.WorldFactory;
import com.dualcorn.minerim.rendering.PlanetRenderState;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class GameLoop extends SimpleApplication {
	private WorldFactory worldFactory;
	private BulletAppState bulletAppState;
	private GameplayManager gameplayManager;
	private ScheduledThreadPoolExecutor executor;
	private PlanetRenderState planetRenderState;
	private Node sunNode, sun;
	private SunControl sunControl;
	
	// private Node player;
	public static void main(String[] args) {
		GameLoop app = new GameLoop();
		/*AppSettings newSetting = new AppSettings(true);
		newSetting.setFrameRate(0);
		app.setSettings(newSetting);*/
		app.start(); // start the game
	}

	public ScheduledThreadPoolExecutor getExecutor(){
		return executor;
	}
	
	@Override
	public void simpleUpdate(float tpf)
	{
		super.simpleUpdate(tpf);
	}
	
	@Override
	public void simpleInitApp() {
		assetManager.registerLocator("assets/",FileLocator.class);
		executor = new ScheduledThreadPoolExecutor(3);
		gameplayManager = new GameplayManager(this);
		bulletAppState = new BulletAppState();
		bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
		gameplayManager.setupKeys();
		stateManager.attach(bulletAppState);
		bulletAppState.getPhysicsSpace().enableDebug(assetManager);
		// ne pas lancer avant le moteur physique
		worldFactory = new WorldFactory(this);
		
		stateManager.attach(worldFactory);
		flyCam.setEnabled(true);
		flyCam.setMoveSpeed(500);
		
		buildWorld();
		Vector3f vect = new Vector3f();
		vect.x = -3200;//-50;//-3000;
		vect.y = 10;
		vect.z = 100;
		cam.setLocation(vect);
		cam.setFrustumFar(100000);
		MainPlayer hero = new MainPlayer(this, "Hero",  new Vector3f(vect));
		hero.createMesh();
		buildPlayer();
		
		//Ambient Light
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.7f));
		rootNode.addLight(al);
		
		DirectionalLight s = new DirectionalLight();
		sunNode = new Node("sunNode");
		sun = new Node("sun");
		rootNode.attachChild(sunNode);
		sunNode.attachChild(sun);
		//sunNode.setLocalTranslation(0f, 1.0f, 1.0f);
		sun.setLocalTranslation(1.0f, 0f, 0f);
		rootNode.addLight(s);
		DirectionalLightControl lc = new DirectionalLightControl(s);
		sun.addControl(lc);
		sunControl = new SunControl(0.01f);
		sunNode.addControl(sunControl);
		
		planetRenderState = new PlanetRenderState(3000f, s);
		stateManager.attach(planetRenderState);
	}

	private void buildWorld() {
		worldFactory.createWorld(Vector3f.ZERO);
	}

	private void buildPlayer() {
		Vector3f vect = new Vector3f();
		Player player;
		for (int i = 0; i < 50; i++) {
			vect.x = (float)((Math.random() - 0.5) * 2000);//-300;//
			vect.y = (float)((Math.random() - 0.5) * 2000);//10;//
			vect.z = (float)((Math.random() - 0.5) * 2000);//30;//
			player = new Player(this, "toto"+i,new Vector3f(vect));
			player.createMesh();
			
		}
	}
	
	public GameplayManager getGameplayManager(){
		return gameplayManager;
	}
	
	@Override
	public void destroy(){
		super.destroy();
		stateManager.detach(worldFactory);
		executor.shutdown();
	}
}