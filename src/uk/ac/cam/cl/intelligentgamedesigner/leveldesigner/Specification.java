public class Specification {
       
    public final float difficulty;
       
       
    public Specification(float difficulty) {
    	this.difficulty = difficulty;
    	
        if(difficulty > 0.0f || difficulty < 1.0f)
        {
            throw new IllegalArgumentException();
        }        
    }


}