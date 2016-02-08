package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

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
		ArrayLevelRepresentation a = new ArrayLevelRepresentationJelly(r);
		ArrayLevelRepresentation b = new ArrayLevelRepresentationJelly(r);
		
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
		Specification s = new Specification(0.5, GameMode.JELLY);
		LevelDesignerManager m = new LevelDesignerManager(s);
		m.run();
		m.levelDesigner.printBestIndividual();
	}
}