package com.dualcorn.minerim.environment.terrain;


import com.jme3.math.Vector3f;

public class QuadTreeNode {
	
	static int[] debuTab = new int[]{0,0,0,0,0};
	
	/** Nombre de fils du noeuds*/
	static final int nbChilds = 4;
	
	/** Nombre de sommets définissant la region */
	static final int nbMainVertices = 3;
	
	/** Tableau des fils du noeuds*/
	private QuadTreeNode[] childs;
	
	/** Centre de la region définie par le noeud*/
	private Vector3f center;

	/** Sommet définissant la region*/
	private Vector3f[] mainVertices;
	
	/** Chunk associé*/
	private Chunk chunk;
	
	/** profondeur du noeud dans l'arbre*/
	private int depth;
	
	/** Indique si le noeud est une feuille*/
	private boolean isLeaf;
	
	private boolean isActive;

	/**
	 * Constructeur de la classe QuadTreeNode
	 * @param depth Profondeur du noeud dans l'arbre
	 * @param vect Triplet de Vecteur representant les 3 sommets de la region
	 */
	public QuadTreeNode(int depth, Vector3f[] vect){
		this.depth = depth;
		this.mainVertices = vect;
		this.center=vect[0].normalize().add(vect[1].normalize().add(vect[2].normalize())).normalize().mult(ChunkManager.rayon);
		this.childs = new QuadTreeNode[nbChilds];
		this.isLeaf = true;
		this.isActive = true;
		if(depth==ChunkManager.depthMax)
			this.chunk = new Chunk(vect);
		else
			this.chunk = null;
	}

	/**
	 * Met à jour le noeuds et ses fils de façon récursive
	 * @param camPosition
	 */
	public void updateChunk(Vector3f camPosition)
	{

		if(center.normalize().angleBetween(camPosition.normalize()) <= Math.acos(ChunkManager.rayon/camPosition.length())
			||
			mainVertices[0].normalize().angleBetween(mainVertices[1].normalize()) >= mainVertices[0].normalize().angleBetween(camPosition.normalize())
			||
			mainVertices[2].normalize().angleBetween(mainVertices[1].normalize()) >= mainVertices[1].normalize().angleBetween(camPosition.normalize())
			||
			mainVertices[0].normalize().angleBetween(mainVertices[2].normalize()) >= mainVertices[2].normalize().angleBetween(camPosition.normalize()))
		{
			QuadTreeNode.debuTab[depth]++;
			isActive = true;
			if(depth==ChunkManager.depthMax)
			{
				chunk.setCamPosition(camPosition);
				Chunk.executor.execute(chunk);
			}
			else
			{
				if(isLeaf)
					this.subdivide();
				for(int i=0; i<nbChilds; i++)
					childs[i].updateChunk(camPosition);
			}
		}
		else
			isActive = false;
	}
	
	/**
	 * Calcul le point au milieu des deux points passés en parametre
	 * @param p1 vecteur represant la position du premier point
	 * @param p2 vecteur represant la position du deuxiéme point
	 * @return un vecteur representant le milieu
	 */
	private Vector3f getMiddlePoint(Vector3f p1, Vector3f p2)
	{
			//On calcule le milieu
			return p1.add(p2).normalize().mult(ChunkManager.rayon);
	}

	/**
	 * Créer les fils du noeud
	 */
	private void subdivide()
	{
		
		//On calcul les trois milieux
		Vector3f a = getMiddlePoint(mainVertices[0], mainVertices[1]);

		Vector3f b = getMiddlePoint(mainVertices[1], mainVertices[2]);

		Vector3f c = getMiddlePoint(mainVertices[2], mainVertices[0]);
	
		//On ajoute au lod suivant les nouvelles faces 
		childs[0] = new QuadTreeNode(depth+1, new Vector3f[] {mainVertices[0], a, c});
		childs[1] = new QuadTreeNode(depth+1, new Vector3f[] {mainVertices[1], b, a});
		childs[2] = new QuadTreeNode(depth+1, new Vector3f[] {mainVertices[2], c, b});
		childs[3] = new QuadTreeNode(depth+1, new Vector3f[] {a, b, c});
		isLeaf = false;
	}

	/**
	 * Met à jour le Mesh du monde
	 * @param w le Mesh du monde
	 */
	public void updateMesh(World w)
	{
		if(isActive)
		{
			if(depth==ChunkManager.depthMax)
			{
				chunk.updateMesh(w);
			}
			else
			{
				if(!isLeaf){
					for(int i=0; i<nbChilds; i++)
						childs[i].updateMesh(w);
				}
			}
		}
	}
	
	
	/**
	 * Indique si un des chunks est obsolete
	 * @return renvoie vraie si un de ses fils uu lui meme est obsolete, faux sinon
	 */
	public boolean isObsolete()
	{
		if(isActive)
		{
			if(depth==ChunkManager.depthMax)
			{
				return chunk.isObsolete();	
			}
			else
			{
				if(isLeaf)
					return false;
				else
					for(int i=0; i<nbChilds; i++)
						if(childs[i].isObsolete())
							return true;
					return false;
			}
		}
		return false;
	}
}
