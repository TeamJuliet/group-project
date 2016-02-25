package uk.ac.cam.cl.intelligentgamedesigner.leveldesigner;

public class AndBinaryBoard extends BinaryBoard {

	public AndBinaryBoard(BaseBinaryBoard b1, BaseBinaryBoard b2) {		
		super(b1.width, b1.height);
		assert(b1.width == b2.width && b1.height == b2.height);
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(b1.get(i, j) && b2.get(i, j)) {
					set(i,j,true);
				}
			}
		}
	}

}
