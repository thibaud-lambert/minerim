package com.dualcorn.minerim.scene;

import com.jme3.math.Vector3f;



public class Face {
	int vertexIndex[] = new int[3];
	//int texCoordIndex[] = new int[3];
	int normalIndex[] = new int[3];
	Vector3f color[] = new Vector3f[3];
	
	public Face(int [] vertInd, int [] normInd, Vector3f [] color){
		this.vertexIndex[0] = vertInd[0];
		this.vertexIndex[1] = vertInd[1];
		this.vertexIndex[2] = vertInd[2];
		this.normalIndex[0] = normInd[0];
		this.normalIndex[1] = normInd[1];
		this.normalIndex[2] = normInd[2];
		this.color[0] = new Vector3f(color[0]);
		this.color[1] = new Vector3f(color[1]);
		this.color[2] = new Vector3f(color[2]);
	}
}
