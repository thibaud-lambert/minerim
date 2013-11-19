package com.dualcorn.minerim.scene;

import java.util.ArrayList;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;


public abstract class Model extends Mesh {
	static private int vertexSize = 3;
	static private int normalSize = 3;
	static private int colorSize = 4;
	
	protected ArrayList <Vector3f> vertex = new ArrayList<Vector3f>(); // liste de points
	protected ArrayList <Vector3f> normals = new ArrayList<Vector3f>(); // liste de normales
	protected ArrayList <Face> face = new ArrayList<Face>(); // association de 3 points, 3 normales et 3 couleurs.
	//protected ArrayList <Vector2f> texCoords;
	
	protected float[] vertexBuffer;
	protected float[] colorBuffer;
	protected float[] normalBuffer;
	protected int[] indexBuffer;

	
	protected abstract void build();

	public void fillBuffer(){
		int size = face.size();
		int indexInd = 0;
		int colorInd = 0;
		int vertexInd = 0;
		int normalInd = 0;
		vertexBuffer = new float[vertexSize*size*3];
		normalBuffer = new float[normalSize*size*3];
		colorBuffer = new float[colorSize*size*3];
		indexBuffer = new int[size*3];
		// pour chaque face
		for (int i = 0; i < size; i++) {
			Face f = face.get(i);
			// pour chaque vertex de la face
			for (int j = 0; j < 3; j++) {
				// on recupere la couleur, les coordonnees de texture et la
				// position
				Vector3f c = new Vector3f(f.color[j]);
				
				Vector3f n = normals.get(f.normalIndex[j]);
				//Vector2f tc = texCoords.get(face.get(i).texCoordIndex[j]);
				
				Vector3f v = vertex.get(f.vertexIndex[j]);
				
				colorBuffer[colorInd++] = c.x;
				
				colorBuffer[colorInd++] = c.y;
				colorBuffer[colorInd++] = c.z;
				colorBuffer[colorInd++] = 1.0f;
				
				vertexBuffer[vertexInd++] = v.x;
				vertexBuffer[vertexInd++] = v.y;
				vertexBuffer[vertexInd++] = v.z;
				
				normalBuffer[normalInd++] = n.x;
				normalBuffer[normalInd++] = n.y;
				normalBuffer[normalInd++] = n.z;
				
				indexBuffer[indexInd] = indexInd++;
			}
		}
		
		this.setBuffer(Type.Position, vertexSize, vertexBuffer);
		this.setBuffer(Type.Color, colorSize, colorBuffer);
		this.setBuffer(Type.Normal, normalSize, normalBuffer);
		//this.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
		//this.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indexBuffer));
		this.updateBound();
		this.setStatic();
	}
}
