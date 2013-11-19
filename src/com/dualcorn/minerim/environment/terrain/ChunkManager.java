package com.dualcorn.minerim.environment.terrain;

import java.util.ArrayList;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;

public class ChunkManager extends Thread {

	/** rayon minimal du monde */
	static protected int rayon = 3000;

	/** profondeur maximun de l'arbre */
	static protected int depthMax = 4;
	
	/** Nombre de region initial du monde */
	static protected int nbRegion = 20;
	
	/** Distance minimun avant les updates*/
	static protected float updateDistance = 10.f;
	
	/** Arbre representant l'aborecense des chunks*/
	private QuadTree[] chunkTree;
	
	/** Le mesh represant le monde*/
	private World world;
	
	private Vector3f oldCamPosition;
	
	private SimpleApplication app;

	
	private boolean running = true;
	
	public ChunkManager(SimpleApplication app) {
		this.app = app;
		this.world = new World(app);
		Chunk.initMaterial(app);
		this.chunkTree = new QuadTree[nbRegion];
		genIcoSphere();
		oldCamPosition = app.getCamera().getLocation().clone();
		for (QuadTree t : chunkTree) {
			// Mise a jour du chunk
			t.updateChunk(oldCamPosition);
		}
	}
	
	/**
	 * Génération de l'icosphere de base
	 */
	private void genIcoSphere()
	{
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		//facteur pour la forme de base de l'icopshere (n'influe pas sur le rayon)
		float t = (float) ((1.0 + Math.sqrt(5.0)) / 2.0);
		float d = (float) (rayon / Math.sqrt(t * t + 1));
		int i,j;
		
		//Calcul des points formant l'icopshere
		for (j = 3; j > 0; j--) {
			for (i = 0; i < 4; i++) {
				float[] coord = { -(1 - (i % 2) * 2) * d,
						(1 - (i / 2) * 2) * t * d, 0 };
				points.add(new Vector3f(coord[j % 3], coord[(j + 1) % 3],
						coord[(j + 2) % 3]));
			}
		}
		
		//Liste des faces formants l'icosphere
		int [][] tab = { { 0, 11, 5 }, { 0, 5, 1 }, { 0, 1, 7 }, { 0, 7, 10 },
				{ 0, 10, 11 }, { 1, 5, 9 }, { 5, 11, 4 }, { 11, 10, 2 },
				{ 10, 7, 6 }, { 7, 1, 8 }, { 3, 9, 4 }, { 3, 4, 2 },
				{ 3, 2, 6 }, { 3, 6, 8 }, { 3, 8, 9 }, { 4, 9, 5 },
				{ 2, 4, 11 }, { 6, 2, 10 }, { 8, 6, 7 }, { 9, 8, 1 } };
		
		for(i=0;i<nbRegion;i++)
			this.chunkTree[i] = new QuadTree(new Vector3f[] {points.get(tab[i][0]), points.get(tab[i][1]), points.get(tab[i][2])});
	}
	


	protected void updateChunks() {
		Vector3f camPosition = app.getCamera().getLocation().clone();
		
		//Vector3f camPosition = new Vector3f();
		//camPosition.x = -3000;
		//camPosition.y = 11;
		//camPosition.z = 100;
		if(oldCamPosition.distance(camPosition) >= updateDistance)
		{
			//System.out.println("Update:");
			for (QuadTree t : chunkTree) {
				// Mise a jour du chunck
				t.updateChunk(camPosition);
			}
			for(int i =0; i < ChunkManager.depthMax + 1; i++)
			{
				//System.out.println("depth:");
				//System.out.println(QuadTreeNode.debuTab[i]);
				QuadTreeNode.debuTab[i] = 0;
			}
			oldCamPosition.set(camPosition);
		}
	}


	public void updateChunkRender() {
		boolean isObsolete = false;
		for (QuadTree t : chunkTree){
			if(t.isObsolete())
			{
				isObsolete = true;
				break;
			}
		}
		if (isObsolete) 
		{
			for (QuadTree t1 : chunkTree)
			{
				t1.updateMesh(world);
			}	
		}
	}

	public void run() {
		while (running) {
			updateChunks();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void cancel(){
		running = false;
		Chunk.executor.shutdown();
	}
}