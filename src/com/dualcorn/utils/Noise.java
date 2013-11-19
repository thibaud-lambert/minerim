package com.dualcorn.utils;

public class Noise {

	private int perm[] = new int[512];
	private static int grad[][] = {
		{1,1,0},{-1,1,0},{1,-1,0},{-1,-1,0},
		{1,0,1},{-1,0,1},{1,0,-1},{-1,0,-1},
		{0,1,1},{0,-1,1},{0,1,-1},{0,-1,-1},
		
		{1,0,-1},{-1,0,-1},{0,-1,1},{0,1,1}
	};

	public Noise(long seed)
	{
		int p[] = permutationGenerator.get(256,seed);
		for(int i=0; i<256; i++)
			perm[256+i] = perm[i] = p[i];
	}

	
	
	public double getValue(double x, double y, double z)
	{
		double x_frac, y_frac, z_frac;
		int x_int, y_int, z_int,
		x_mod, y_mod, z_mod;

		x_int = (int) x;
		y_int = (int) y; 
		z_int = (int) z;
		
		x_frac = x - x_int;
		y_frac = y - y_int;
		z_frac = z - z_int;
		
		x_mod = x_int & 255;
		y_mod = y_int & 255;
		z_mod = z_int & 255;
		
		double g000 = dotProduct(getGrad(x_mod, y_mod, z_mod), x_frac, y_frac, z_frac);
		double g001 = dotProduct(getGrad(x_mod, y_mod, z_mod + 1), x_frac, y_frac, z_frac - 1.);
		double g010 = dotProduct(getGrad(x_mod, y_mod + 1, z_mod), x_frac, y_frac - 1., z_frac);
		double g011 = dotProduct(getGrad(x_mod, y_mod + 1, z_mod + 1), x_frac, y_frac - 1, z_frac - 1);
		double g100 = dotProduct(getGrad(x_mod + 1, y_mod, z_mod), x_frac - 1, y_frac, z_frac);
		double g101 = dotProduct(getGrad(x_mod + 1, y_mod, z_mod + 1), x_frac - 1, y_frac, z_frac - 1);
		double g110 = dotProduct(getGrad(x_mod + 1, y_mod + 1, z_mod), x_frac - 1, y_frac - 1, z_frac);
		double g111 = dotProduct(getGrad(x_mod + 1, y_mod + 1, z_mod + 1), x_frac - 1, y_frac - 1, z_frac - 1);
	/*
		double u = quintic_poly(x_frac);
		double v = quintic_poly(y_frac);
		double w = quintic_poly(z_frac);
	*/
		double u = x_frac;
		double v = y_frac;
		double w = z_frac;
		
		double x00 = g000 + u * (g100 - g000);
		double x10 = g010 + u * (g110 - g010);
		double x01 = g001 + u * (g101 - g001);
		double x11 = g011 + u * (g111 - g011);
	
		double xy0 = x00 + v * (x10 - x00);
		double xy1 = x01 + v * (x11 - x01);
	
		double xyz = xy0 + w * (xy1 - xy0);
		
		return xyz;
	}
	
	private double dotProduct(int v[], double x, double y, double z)
	{
		return v[0] * x + v[1] * y + v[2] * z;
	}

	private int[] getGrad(int x, int y, int z)
	{
		int rand_value = perm[z + perm[y + perm[x]]];
		return grad[rand_value & 15];
	}
	
	private double quintic_poly(double t)
	{
		double t3 = t * t * t;
		return t3 * (t * (t * 6. -15.) + 10.);
	}
}