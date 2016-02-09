package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameMode;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
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
		l.printRepresentation();
	}
	
	public static void testCrossover() {
		Random r = new Random();
		ArrayLevelRepresentation a = new ArrayLevelRepresentationJelly(r);
		ArrayLevelRepresentation b = new ArrayLevelRepresentationJelly(r);
		
		System.out.println("Fitness before: " + a.getAestheticFitness());
		a.printRepresentation();
		System.out.println();
		b.printRepresentation();
		
		a.crossoverWith(b);
		
		System.out.println("Fitness after: " + a.getAestheticFitness());
		a.printRepresentation();
		System.out.println();
		b.printRepresentation();
	}

	public static void main(String[] args) {

		// NOTE:
		// Exceptions will be thrown when this is run, because we currently don't filter out boards with no available moves
		// Also, game state doesn't re-shuffle the candies whenever no moves remain yet
		//
		// If you want this to run without exceptions, just return 0.5 in getDifficultyFitness() in LevelDesignerManager
		Specification s = new Specification(0.5, GameMode.HIGHSCORE);
		LevelDesignerManager m = new LevelDesignerManager(s);
		m.run();
		m.levelDesigner.printIndividuals();
	}
}
