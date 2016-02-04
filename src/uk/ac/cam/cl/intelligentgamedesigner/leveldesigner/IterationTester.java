package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

public class IterationTester {
	
	public static void testMutation() {
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
			
			iterations++;
			System.out.println("Iteration " + iterations + " - Fitness " + fitness);
		}
		
		System.out.println("Iterations: " + iterations);
		System.out.println("Fitness: " + fitness);
		l.printBoard();
	}
	
	public static void testCrossover() {
		Random r = new Random();
		ArrayLevelRepresentation a = new ArrayLevelRepresentationScore(r);
		ArrayLevelRepresentation b = new ArrayLevelRepresentationScore(r);
		
		System.out.println("Fitness before: " + a.getAestheticFitness());
		a.printBoard();
		System.out.println();
		b.printBoard();
		
		a.crossoverWith(b);
		
		System.out.println("Fitness after: " + a.getAestheticFitness());
		a.printBoard();
		System.out.println();
		b.printBoard();
	}
	
	public static void main(String[] args) {
		testCrossover();
	}
}
