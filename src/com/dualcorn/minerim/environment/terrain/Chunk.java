package com.dualcorn.minerim.environment.terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import com.dualcorn.utils.Noise;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class Chunk implements Runnable{
	private Vector3f camPosition;
	
	/** Pool de thread pour la mise a jour des chunks*/
	static protected ExecutorService executor = Executors.newFixedThreadPool(5);
	
	static private Material wmat;
	
	protected boolean toUpdate = true;
	/** */
	int colorsize;
	
	/** Nombre de niveau de detail different*/
	static protected int nbLod = 5;
	
	/** Les 3 sommets du chunck*/
	Vector3f[] mainVertex;
	
	/** Le niveau de detail actuel du chunk*/
	int currentLod;
	int oldLod;
	
	/** Le niveau de detail le plus eleve calcule pour le chunk*/
	int computedLod;
	
	/** Le facteur de distance pour le calcul du lod*/
	float factorLod;
	
	/** Liste des points composant le chunk*/
	ArrayList<Vector3f> vertexList = new ArrayList<Vector3f>();
	ArrayList<Vector3f> normalList = new ArrayList<Vector3f>();
	ArrayList<Vector4f> colorList = new ArrayList<Vector4f>();
	
	/** Hashmap faisant correspondre le nom des points a leur numero*/
	HashMap<String, Integer> vertexHashmap = new HashMap<String,Integer>();
	
	/** Liste des faces composant le chunk*/
	ArrayList<int[]> faceList = new ArrayList<int[]>();
	ArrayList<int[]> oldFaceList;
	
	/** Liste des meshs*/
	ArrayList<Mesh> meshList = new ArrayList<Mesh>(nbLod);
	ArrayList<Geometry> geometryList = new ArrayList<Geometry>(nbLod);
	ArrayList<RigidBodyControl> rigidBodyControlList = new ArrayList<RigidBodyControl>(nbLod);
	
	int[] indexes;
	float[] colorArray;
	float[] normalArray;
	
	private ReentrantLock mutex;
	
	private Random randomGenerator = new Random();
	
	public boolean isObsolete(){
		return toUpdate;
	}
	
	/**
	 * Constructeur du Chunk
	 */
	public Chunk(Vector3f[] mainVertex)
	{	
		this.mainVertex = mainVertex.clone();
		computedLod = 0;
		oldLod=-1;
		
		this.mutex = new ReentrantLock();
		
		//Initialisation des premiers points
		vertexList.add(mainVertex[0]);
		vertexList.add(mainVertex[1]);
		vertexList.add(mainVertex[2]);
		
		normalList.add(mainVertex[0].normalize());
		normalList.add(mainVertex[1].normalize());
		normalList.add(mainVertex[2].normalize());
		
		colorList.add(new Vector4f(0.5f,0.5f,1f,1.0f));
		colorList.add(new Vector4f(0.5f,0.5f,1f,1.0f));
		colorList.add(new Vector4f(0.5f,0.5f,1f,1.0f));
		
		vertexHashmap.put("0",0);
		vertexHashmap.put("1",1);
		vertexHashmap.put("2",2);
		
		//Création du premier niveau de détails 
		factorLod = (float)(mainVertex[0].distance(mainVertex[1])*Math.sqrt(3)/2.0f);
		faceList.add(new int[]{0,1,2});
		currentLod = 0;
		createLod();
		toUpdate = true;
	
	}
	
	public void setCamPosition(Vector3f position){
		this.camPosition = position;
	}
	
	/**
	 * !!! Appeler d'abord setCamPosition !!!
	 * \brief Met a jour le chunk en fonction de sa distance a la position passee prise en parametre
	 * \param position la position actuelle du joueur
	 */
	@Override
	public void run()
	{
		mutex.lock();
		int newLod = getNewLod(camPosition);
		//System.out.println(newLod);
		//Si on n'est pas au bon niveau de detail
		if(newLod!=currentLod)
		{
			//Si on n'a pas deja calcule le bon niveau de detail
			if(newLod > computedLod)
				computeLod(newLod);
			
			currentLod = newLod;
			toUpdate = true;
		}
		mutex.unlock();
	}
	
	/**
	 * 
	 */
	private void computeLod(int lod)
	{
		for(int i = computedLod+1; i < lod+1; i++)
		{	
			surfaceSubdivide(i);
			computedLod = i;
			createLod();
		}
	}
	
	
	private void createLod()
	{
		Vector3f[] vertexTab = new Vector3f[vertexList.size()];
		Vector3f[] normalTab = new Vector3f[normalList.size()];
		Vector4f[] colorTab = new Vector4f[colorList.size()];
		int[] faceTab = new int[faceList.size()*3];
		int j=0;
		for(int[] face : faceList)
		{
			for(int k=0; k<3; k++)
			{
				faceTab[j] = face[k];
				j++;
			}
		}
		
		Mesh mesh = new Mesh();
		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertexList.toArray(vertexTab)));
		mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normalList.toArray(normalTab)));
		mesh.setBuffer(Type.Color, 4,  BufferUtils.createFloatBuffer(colorList.toArray(colorTab)));
		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(faceTab));
		mesh.updateBound();
		meshList.add(mesh);
		
		Geometry geometry = new Geometry("chunky", mesh);
		geometry.setLocalTranslation(Vector3f.ZERO);
		geometry.setMaterial(wmat);
		geometryList.add(geometry);
		
		CollisionShape collisionShape = CollisionShapeFactory.createMeshShape(geometry);
		RigidBodyControl rigidBody = new RigidBodyControl(collisionShape, 0.0f);
		geometry.addControl(rigidBody);
		rigidBodyControlList.add(rigidBody);
	}
	
	/**
	 * Met à jour le chunk dans le mesh du monde
	 * @param w le monde auquel ajouter le chunk
	 */
	public void updateMesh(World w)
	{
		if(oldLod != currentLod)
		{
			if(oldLod != -1)
			{
				w.removeGeometry(geometryList.get(oldLod));
				w.removeRigidBodyControl(rigidBodyControlList.get(oldLod));
			}
				
			w.attachGeometry(geometryList.get(currentLod));
			w.attachRigidBodyControl(rigidBodyControlList.get(currentLod));
	
			oldLod = currentLod;
		}	
		toUpdate = false;
		
	}
	/**
	 * Calcul le niveau de detail
	 * @param position La position du joueur/camera 
	 */
	private int getNewLod(Vector3f position)
	{
		//On normalize la position au rayon de la planete 
		Vector3f positionNormalize = position.normalize();
		
		//On calcul la distance minimun au chunk
		float distance = Math.min(Math.min(positionNormalize.distance(mainVertex[0].normalize()),
				positionNormalize.distance(mainVertex[1].normalize())),
				positionNormalize.distance(mainVertex[2].normalize()));

		//On calcul le niveau de detail correspondant
		int l = Math.max(nbLod - 2*((int)Math.floor((distance*ChunkManager.rayon/factorLod))),0);
		
		return l;
	}
	
	
	/**
	 * Construit le chunk avec le niveau de detail passe en parametre
	 * @param lodStep nombre d'iteration a�faire
	 */
	private void surfaceSubdivide(int lod)
	{
		//On divise chaques faces en 4 
		oldFaceList = faceList;
		faceList =new ArrayList<int[]>();
		for(int[] face : oldFaceList)
		{
			//On calcul les trois milieus de chaques aretes
			int a = getMiddlePoint(face[0], face[1], lod);
			int b = getMiddlePoint(face[1], face[2], lod);
			int c = getMiddlePoint(face[2], face[0], lod);
					
			//On	 ajoute au lod suivant les nouvelles faces 
			faceList.add(new int[] {face[0], a, c});
			faceList.add(new int[] {face[1], b, a});
			faceList.add(new int[] {face[2], c, b});
			faceList.add(new int[] {a, b, c});
		}
	}
	
	/**
	 *  Calcul le point au milieu de id1 et id2
	 * @param id1 indice du premier point
	 * @param id2 indice du deuxieme point
	 */
	private int getMiddlePoint(int id1, int id2, int lod)
	{
		int smallerIndex;
		int greaterIndex;
		//On determine le nom du nouveau point
		if (id1 < id2){
			smallerIndex = id1;
			greaterIndex = id2;
		}else{
			smallerIndex = id2;
			greaterIndex = id1;
		}
		String key = "" + smallerIndex + "," + greaterIndex;
		//On verifie qu'il n'existe pas deja
		if (vertexHashmap.containsKey(key)) 
			return vertexHashmap.get(key);
		else
		{
			//On recupere les points
			
			Vector3f vertex1 = vertexList.get(id1);
			Vector3f vertex2 = vertexList.get(id2);
	
			//On calcule le milieu
			
			
			Vector3f middle = vertex1.add(vertex2).divide(2f);
			
		
			

		
			float noise = (float) new Noise(1528764).getValue(middle.x, middle.y, middle.z);
			System.out.println("Factor :" + noise);
		
			middle.multLocal(1+noise/1000);
			
			//middle.addLocal(middle.mult(((float)random-0.5f)/((float)Math.pow(lod+1,2)*0.2f*ChunkManager.rayon)));
			
			//On l'ajoute a la liste
			int i = vertexList.size();
			vertexHashmap.put(key,i);
			vertexList.add(middle);
			normalList.add(middle.normalize());
			colorList.add(new Vector4f(0.f,1.f,1f,1.0f));
			return i;
		}
			
	}
	
	static public void initMaterial(SimpleApplication app) {
		wmat = new Material(app.getAssetManager(),
				"Common/MatDefs/Light/Lighting.j3md");
		wmat.setBoolean("UseVertexColor", true);
		wmat.setBoolean("UseMaterialColors", true);
		wmat.setColor("Ambient", new ColorRGBA(0.5f, 0.5f, 1f, 1f));
		wmat.setColor("Diffuse", new ColorRGBA(0.5f, 0.5f, 1f, 1f));
		wmat.setColor("Specular", new ColorRGBA(0.5f, 0.5f, 1f, 1f));
		wmat.setBoolean("VertexLighting", true);
	}

}
