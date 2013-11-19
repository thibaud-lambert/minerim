package com.dualcorn.utils;

import java.util.ArrayList;
import java.util.Random;

public class permutationGenerator {
	
	private permutationGenerator(){};
	
	static private int [] randomLhemerVector(int n, long seed)
	{
		Random random = new Random(seed);
		int [] vect = new int[n];
		vect[0] = 0;
		for(int i = 1; i < n; i++)
			vect[i] = random.nextInt(i);

		return vect;
	}

	static int[] get(int size, long seed)
	{
		int [] vect = randomLhemerVector(size,seed);
		ArrayList<Integer> list = new ArrayList<Integer>();
		int [] perm = new int[size];
		for(int i = 0; i < size; i++)
			list.add(new Integer(i));
		for(int i = size-1; i > 0; i--)
			perm[list.get(vect[i]).intValue()]=i;
	
		return perm;
	} 
}
