package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

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
	
	public static void testCrossover() {
		Random r = new Random();
		ArrayLevelRepresentation a = new ArrayLevelRepresentationScore(r);
		ArrayLevelRepresentation b = new ArrayLevelRepresentationScore(r);
		
		System.out.println("Fitness before: " + a.getAestheticFitness());
		a.printBoard();
		System.out.println();
		b.printBoard();
		
		ArrayLevelRepresentation[] children = a.crossoverWith(b);
		
		System.out.println("Fitness after: " + children[0].getAestheticFitness());
		children[0].printBoard();
		System.out.println();
		children[1].printBoard();
	}
	
	public static void main(String[] args) {
		Specification s = new Specification(0.5, GameMode.JELLY);
		new LevelDesignerManager(s);
	}
}
