package uk.ac.cam.cl.intelligentgamedesigner.userinterface;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import uk.ac.cam.cl.intelligentgamedesigner.coregame.Design;

public class LevelManager {
	static int levels_so_far;
	static final String location = System.getProperty("user.dir") + File.separator + "levels" + File.separator;
	static final String suffix = ".lv";
	//of the format 1. name.lv
	
	private ArrayList<Design> levels;
	private ArrayList<String> level_names;
	
	public LevelManager() {
		levels_so_far = 0;
		levels = new ArrayList<Design>();
		level_names = new ArrayList<String>();
		searchForLevels();
	}
	
	private void searchForLevels(){
		File root = new File(location);
		File[] files = root.listFiles();
		boolean foundEnd = false;
		if(files != null){
			while(!foundEnd){
				foundEnd = true;
				for(File f:files) {
					//System.out.println("found "+f.getName());
					if(f.getName().startsWith((levels_so_far+1)+". ")){
						//add the latest numbered level to the list
						Design latest;
			            try {
			                // Read in current array of test cases and add the new test
			                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(f));
			                latest = (Design) objectInputStream.readObject();
			                objectInputStream.close();
				            levels.add(latest);
				            level_names.add(f.getName());
							foundEnd = false;
							levels_so_far++;
			            } catch (EOFException e) {
			            	System.err.println("Error in reading file (End of file)");
			            } catch (FileNotFoundException e) {
			            	System.err.println("Error in reading file (File not found)");
						} catch (InvalidClassException e) {
			            	System.err.println("Error in reading file (Invalid Class)");
						}catch (IOException e) {
			            	System.err.println("Error in reading file (IO)");
						} catch (ClassNotFoundException e) {
			            	System.err.println("Error in reading file (Class not found)");
						} 
						break;
					}
				}
			}
		}
	}
	
	public int get_next_num(){
		return levels_so_far+1;
	}
	
	public String[] getLevelNames() {
		int num = 0;
		String[] name_list = new String[level_names.size()];
		for(String name:level_names){
			name_list[num] = name;
			num++;
		}
		return name_list;
	}
	
	public Design getLevel(int level_number) {
		try{
			return levels.get(level_number-1);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void deleteLevel(int level_num) {
		File root = new File(location);
		File[] files = root.listFiles();
		if(files!=null){
			for(File f:files){
				if(f.getName().startsWith(level_num+".")){
					f.delete();
				}
			}
		}
		//rename the files etc.
		levels.remove(level_num-1);
		level_names.remove(level_num-1);
		
		for(int n=level_num-1;n<level_names.size();n++){
			String name_minus_num = "." + level_names.get(n).split("\\.")[1] + suffix;
			level_names.set(n, (n+1)+name_minus_num);
			if(files!=null){
				for(File f:files){
					if(f.getName().startsWith((n+2)+".")){
						boolean success = f.renameTo(new File(location+(n+1)+name_minus_num));
						if(success)System.out.println("renamed to "+((n+1)+name_minus_num));
					}
				}
			}
		}
		
		levels_so_far--;
	}
	
    public static File createLocalFile (String fileName) throws IOException {
        File unitTestDirectory = new File(location);
        // Create unit test directory if it doesn't already exist
        // This will return a SecurityException is sudo/admin access is required!
        if (!unitTestDirectory.exists()){
        	unitTestDirectory.mkdir();
        	System.out.println("New directory made");
        }
        File file = new File(location + fileName + suffix);

        //All files of that number deleted
        file.createNewFile();
        System.out.println("Made file");

        return file;
    }
	
	public boolean saveLevel(String fileName, Design level) {
		//get the level number
		int level_num = 0;
		try{
			level_num = Integer.parseInt(fileName.split("\\.")[0]);
		} catch (NumberFormatException e){
			System.err.println("error in file name");
		}
		
		if(level_num == levels_so_far+1)levels_so_far++;
		
		//remove existing files with that number
		File root = new File(location);
		File[] files = root.listFiles();
		if(files != null){
			for(File f:files){
				if(f.getName().startsWith(level_num+".")){
					f.delete();
				}
			}
		}
		
		if(level_num>0){ //update the manager's list of levels
			if(levels.size()>level_num-1){
				levels.set(level_num-1, level);
				level_names.set(level_num-1, fileName);
			}
			else {
				levels.add(level);
				level_names.add(fileName);
			}
		}
		
		//create the new file
		File file;
		try {
			file = createLocalFile(fileName);
			System.out.println("made!");
	        //save the new file
	        try {
	            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
				objectOutputStream.writeObject(level);
		        objectOutputStream.close();
			} catch (IOException e) {
				System.err.println("error in writing the new file");
				e.printStackTrace();
				return false;
			}
	        
		} catch (IOException e) {
			System.err.println("error in making the new file");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void printNames(){
		System.out.println("The list:");
		for(String s:level_names){
			System.out.println(s);
		}
		System.out.println();
	}

}
