package com.dualcorn.minerim.environment.terrain;

import com.jme3.math.Vector3f;

public class QuadTree {
	/** Racine de l'arbre*/
	private QuadTreeNode root;
	
	/**
	 * Contructeur de la classe QuadTree
	 * @param vect Les trois points definissant la premiére region pour la racine
	 */
	public QuadTree(Vector3f[] vect){
		root = new QuadTreeNode(0,vect);
	}
	
	/**
	 * Met à jour l'arbre et les chunk qui le compose
	 * @param camPosition la position de la camera
	 */
	public void updateChunk(Vector3f camPosition)
	{
		root.updateChunk(camPosition);
	}
	
	/**
	 * Met à jour le Mesh du monde
	 * @param w le Mesh du monde
	 */
	public void updateMesh(World w)
	{
		root.updateMesh(w);
	}
	
	/**
	 * Indique si l'arbre est obsolete
	 * @return vrai si un des chunks est obsolete, faux sinon
	 */
	public boolean isObsolete()
	{
		return root.isObsolete();
	}

}
