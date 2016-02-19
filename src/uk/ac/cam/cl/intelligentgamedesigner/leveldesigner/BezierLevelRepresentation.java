package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

import java.util.Random;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class BezierLevelRepresentation extends LevelRepresentation {

	//2D Array of floats (that are vertical heights (positive and negative)
	//Crossover is easy and so is mutate
	//getDesign is annoying
	//cant use beziers due to line distance problem
	//
	
	BezierLevelRepresentation(Random random) {
		super(random);
		
		
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void crossoverWith(LevelRepresentation levelRepresentation) {
		// TODO Auto-generated method stub
	}

	@Override
	public Design getDesign() {
		
		
		
		return null;
	}

	@Override
	public double getAestheticFitness() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getConstraintFitness() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String representationToString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printRepresentation() {
		// TODO Auto-generated method stub
		
	}

}
