package com.dualcorn.minerim.environment;

import com.dualcorn.minerim.scene.Face;
import com.dualcorn.minerim.scene.Model;
import com.jme3.math.Vector3f;


public class Sun extends Model{

	static protected int nbX = 36;  // Largeur de la map en degre
	static protected int nbY = 18; // Hauteur ...
	static protected int rayon = 20; // rayon minimal auquel on ajoutera une valeur aleatoire
	protected Vector3f position; // position absolue du centre du monde
	static protected Vector3f baseColor = new Vector3f(0.5f, 0.5f, 1.0f);
		
	public Sun(/*Vector3f p*/) {
		super();
		position = new Vector3f(0f,0f,0f);
		/*position = new Vector3f(p);*/
	}

	
	public void build() {
		int teta, phi;
		float sinteta1,sinphi1,costeta1,cosphi1;
		float sinteta2,sinphi2,costeta2,cosphi2;
		float sinteta3,sinphi3,costeta3,cosphi3;
		float sinteta4,sinphi4,costeta4,cosphi4;
		int vertInd = 0; // nombre de sommets dans la liste de sommets
		int normInd = 0; // nombre de normales dans la liste des normales
		
		
		/* Calcul des rayons (altitudes) de 4 points : le point (r1,phi,teta)
		 * son voisin de droite (r2,phi+1,teta)
		 * son voisin du bas (r3,phi,teta+1)
		 * le point en bas a droite (r4,phi+1,teta+1)
		 */
		for (phi = 0; phi < 360; phi+=180/nbY) {
			for (teta = 0; teta < 180; teta+=360/nbX) {
				
				/* Calcul des positions des points */
				/* Transformation de coordonnées sphérique a cartesiennes */
				/* On ajoute chaque point a la liste de points */
				
				sinteta1 = (float) Math.sin(Math.toRadians(teta));
				sinphi1 = (float) Math.sin(Math.toRadians(phi));
				costeta1 = (float) Math.cos(Math.toRadians(teta));
				cosphi1 = (float) Math.cos(Math.toRadians(phi));
				Vector3f p1 = new Vector3f(rayon * sinteta1 * sinphi1, rayon * costeta1, rayon * sinteta1
						* cosphi1);
				vertex.add(new Vector3f(p1.x + position.x, p1.y + position.y, p1.z + position.z));				
				
				sinteta2 = (float) Math.sin(Math.toRadians(teta));
				sinphi2 = (float) Math.sin(Math.toRadians(phi+180/nbY));
				costeta2 = (float) Math.cos(Math.toRadians(teta));
				cosphi2 = (float) Math.cos(Math.toRadians(phi+180/nbY));
				Vector3f p2 = new Vector3f(rayon * sinteta2 * sinphi2, rayon * costeta2, rayon * sinteta2
						* cosphi2);
				vertex.add(new Vector3f(p2.x + position.x, p2.y + position.y, p2.z + position.z));
				
				sinteta3 = (float) Math.sin(Math.toRadians(teta+360/nbX));
				sinphi3 = (float) Math.sin(Math.toRadians(phi));
				costeta3 = (float) Math.cos(Math.toRadians(teta+360/nbX));
				cosphi3 = (float) Math.cos(Math.toRadians(phi));
				Vector3f p3 = new Vector3f(rayon * sinteta3 * sinphi3, rayon * costeta3, rayon * sinteta3
						* cosphi3);
				vertex.add(new Vector3f(p3.x + position.x, p3.y + position.y, p3.z + position.z));
				
				sinteta4 = (float) Math.sin(Math.toRadians(teta+360/nbX));
				sinphi4 = (float) Math.sin(Math.toRadians(phi+180/nbY));
				costeta4 = (float) Math.cos(Math.toRadians(teta+360/nbX));
				cosphi4 = (float) Math.cos(Math.toRadians(phi+180/nbY));
				Vector3f p4 = new Vector3f(rayon * sinteta4 * sinphi4, rayon * costeta4, rayon * sinteta4
						* cosphi4);
				vertex.add(new Vector3f(p4.x + position.x, p4.y + position.y, p4.z + position.z));
				
				
				/* Calcul des normales : on calcule 2 vecteur dans le plan 
				 * defini par les trois extremitées de la face.
				 * On fait le produit vectoriel et on normalise le vecteur resultat 
				 * ce qui nous donne la normale à la face.*/
				
				Vector3f v1 = new Vector3f(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
				Vector3f v2 = new Vector3f(p1.x - p3.x, p1.y - p3.y, p1.z - p3.z);
				
				Vector3f n1 = new Vector3f(v1.y*v2.z - v2.y*v1.z, v1.z*v2.x - v2.z*v1.x, v1.x*v2.y - v2.x*v1.y);
				
				Vector3f v3 = new Vector3f(p2.x - p4.x, p2.y - p4.y, p2.z - p4.z);
				Vector3f v4 = new Vector3f(p2.x - p3.x, p2.y - p3.y, p2.z - p3.z);
				
				Vector3f n2 = new Vector3f(v3.y*v4.z - v4.y*v3.z, v3.z*v4.x - v4.z*v3.x, v3.x*v4.y - v4.x*v3.y);
				
				normals.add(n1); // ajout de la normale au triangle superieur gauche
				normals.add(n2); // ajout de la normale au triangle inferieur droit
				
				
				/* Creation des faces :  
				 * une face est definie par un tableau de 
				 * -> 3 indices correspondants
				 * a trois points dans le tableaux des points
				 * -> 3 indices correspondants 
				 * a trois normales en ces trois points
				 * -> 3 indices correspondants 
				 * a trois couleurs en ces trois points*/
				
				/* pour le triangle superieur*/
				Vector3f[] color = new Vector3f[3];
				int [] vInd = {vertInd,vertInd+2,vertInd+1};
				int [] nInd = {normInd,normInd,normInd};
				
				color[0] = new Vector3f(baseColor);
				color[1] = new Vector3f(baseColor);
				color[2] = new Vector3f(baseColor);
				face.add(new Face(vInd,nInd,color));
				
				/* pour le triangle inferieur */
				vInd[0] = vertInd+1;
				vInd[1] = vertInd+2;
				vInd[2] = vertInd+3;
				
				nInd[0] = normInd+1;
				nInd[1] = normInd+1;
				nInd[2] = normInd+1;
				
				color[0] = new Vector3f(baseColor);
				color[1] = new Vector3f(baseColor);
				color[2] = new Vector3f(baseColor);
				face.add(new Face(vInd,nInd,color));
				
				vertInd+=4; // on a ajouté 4 points a la liste des points
				normInd+=2; // on a ajouté 2 normales car en chacun des 3 points 
							// de la face la normale est identique -> 1 valeur de 
							//normale suffit par face (on en a donc ajouté 2).
			}	
		}
		this.fillBuffer();
	}
	
}
