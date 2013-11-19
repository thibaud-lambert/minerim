package com.dualcorn.minerim.rendering;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

public class PlanetRenderState implements AppState {
	//private Geometry world;
	private Spatial atm;
	private DirectionalLight sun;
	private Application app;
	private Material skyFromAtmosphere;
	private float time = 0;
	
	// customizable values
    private float sunIntensity;     	// Sun brightness constant
    private float G;                	// The Mie phase asymmetry factor
    private float innerRadius;     		// Ground radius (outer radius is always 1.025 * innerRadius)
    private float scale;            	// 1 / (outerRadius - innerRadius)
    private float scaleDepth;       	// The scale depth (i.e. the altitude at which the atmosphere's average density is found)
    private float scaleOverScaleDepth; 	// scale / scaleDepth
    //private Vector3f wavelength;
    private Vector3f wavelengthFact; 	// 1 / pow(wavelength, 4) for the red, green, and blue channels
    private float exposure;
	
	public PlanetRenderState(float radius, DirectionalLight sun){
		this.innerRadius = radius;
		this.sun = sun;
		this.wavelengthFact = new Vector3f();
		defaultValues();
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = app;
		createAtmosphere();
		skyFromAtmosphere = new Material(app.getAssetManager(), "MatDefs/SkyFromAtmosphere.j3md");
		Node rootNode = (Node)app.getViewPort().getScenes().get(0);
		rootNode.attachChild(atm);
	}
	
	private void createAtmosphere() {
        Sphere d = new Sphere(180, 360, getOuterRadius(),false,false);
        atm = new Geometry("atm",d);
        atm.setMaterial(skyFromAtmosphere);
    }
	
	private void setupSkyMaterial(Material mat) {
		Vector3f cameraLocation = app.getCamera().getLocation();        
	    //Vector3f planetToCamera = cameraLocation;
	    //float cameraHeight = planetToCamera.length();
	    Vector3f sunDir = sun.getDirection();
        mat.setVector3("camPos", cameraLocation);
        mat.setVector3("sunDir", sun.getDirection());
        mat.setFloat("g", G);
        mat.setFloat("sunIntensity", sunIntensity);
        mat.setVector3("wavelengthFact", wavelengthFact);
        mat.setFloat("rand", 10f/*time*/);
        atm.setMaterial(mat);
    }
	
    public float getOuterRadius() { return innerRadius * 3f; }   
    
    private void defaultValues() {
        // values that work well
        sunIntensity = 2f;
        exposure = 2f;
        //wavelength = new Vector3f(0.650f, 0.570f, 0.455f);
        G = -0.990f;            
        scaleDepth = 0.25f;
        updateCalculations();
    }
    /**
     * Call this method after changing parameter values
     */
    public void updateCalculations() {
        scale = 1.0f / (getOuterRadius() - innerRadius);
        scaleOverScaleDepth = scale / scaleDepth;
        wavelengthFact.x = 0.1f;
        wavelengthFact.y = 0.05f;
        wavelengthFact.z = 0.01f;
    }
    
    @Override
	public void cleanup() {
	}
    
    @Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public void postRender() {
		
	}
	@Override
	public void render(RenderManager arg0) {
		
	}
	@Override
	public void setEnabled(boolean arg0) {
		
	}
	@Override
	public void stateAttached(AppStateManager arg0) {
		
	}
	@Override
	public void stateDetached(AppStateManager arg0) {
		
	}
	@Override
	public void update(float tpf) {
		time = (time+tpf)%(2.0f*(float)Math.PI);
		setupSkyMaterial(skyFromAtmosphere);
	}
	@Override
	public boolean isInitialized() {
		return skyFromAtmosphere != null;
	}
}

