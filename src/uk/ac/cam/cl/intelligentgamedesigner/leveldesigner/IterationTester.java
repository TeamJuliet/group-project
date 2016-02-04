package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class IterationTester {
	
	public static void main(String[] args) {
		Random r = new Random();
		ArrayLevelRepresentation l = new ArrayLevelRepresentationScore(r);
		double fitness = l.getAestheticFitness();
		int iterations = 0;
		while (fitness < 1) {
			ArrayLevelRepresentation mutation = l.clone();
			mutation.mutate();
			
			double mutationFitness = mutation.getAestheticFitness();
			if (mutationFitness > fitness) {
				l = mutation;
				fitness = mutationFitness;
			}
			
			/*int x = r.nextInt(5);
			int y = r.nextInt(10);
			l.board[x][y] = l.board[9 - x][y];
			fitness = l.getAestheticFitness();*/
			
			iterations++;
			System.out.println("Iteration " + iterations + " - Fitness " + fitness);
		}
		
		System.out.println("Iterations: " + iterations);
		System.out.println("Fitness: " + fitness);
		l.printBoard();
	}
}
