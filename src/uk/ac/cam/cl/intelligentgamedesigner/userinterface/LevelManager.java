package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.io.*;
import java.util.ArrayList;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;
import uk.ac.cam.cl.intelligentgamedesigner.testing.TestCase;

public class LevelManager {
	static int levels_so_far;
	static final String location = System.getProperty("user.dir") + File.separator;
	static final String suffix = ".lv";
	//of the format 1. name.lv
	
	private ArrayList<Design> levels;
	
	public LevelManager() {
		levels_so_far = 0;
		levels = new ArrayList<Design>();
		searchForLevels();
	}
	
	private void searchForLevels(){
		File root = new File(location);
		File[] files = root.listFiles();
		boolean foundEnd = false;
		while(!foundEnd){
			foundEnd = true;
			for(File f:files) {
				if(f.getName().startsWith(levels_so_far+". ")){
					//add the latest numbered level to the list
					Design latest;
		            try {
		                // Read in current array of test cases and add the new test
		                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(f));
		                latest = (Design) objectInputStream.readObject();
		                objectInputStream.close();
			            levels.add(latest);
						foundEnd = false;
						levels_so_far++;
		            } catch (EOFException e) {
		            	System.err.println("Error in reading file");
		            } catch (FileNotFoundException e) {
		            	System.err.println("Error in reading file");
					} catch (IOException e) {
		            	System.err.println("Error in reading file");
					} catch (ClassNotFoundException e) {
		            	System.err.println("Error in reading file");
					}
					break;
				}
			}
		}
	}
	
	public int get_next_num(){
		return levels_so_far+1;
	}

	public void saveLevel(String fileName, Design level) {
		//get the level number
		int level_num = 0;
		try{
			level_num = Integer.parseInt(fileName.split(".")[0]);
		} catch (NumberFormatException e){
			System.err.println("error in file name");
		}
		
		//remove existing files with that number
		File root = new File(location);
		File[] files = root.listFiles();
		for(File f:files){
			if(f.getName().startsWith(level_num+".")){
				f.delete();
			}
		}
		
		if(level_num>0){ //update the manager's list of levels
			levels.set(level_num-1, level);
		}
		
		//create and save the new file
		File file = new File(location + fileName + suffix);
		try {
			file.createNewFile();
		} catch (IOException e) {
			System.err.println("error in saving the new file");
		}
		
	}
	
	public static File createLocalFile (String fileName) throws IOException {

        // TODO: Add non-UNIX operating system support
        File file = new File(location + fileName);

        // Create the file if it doesn't exist
        if (!file.exists()) file.createNewFile();

        return file;
    }

}
