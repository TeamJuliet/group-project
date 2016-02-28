package uk.ac.cam.cl.intelligentgamedesigner.coregame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilter;
import uk.ac.cam.cl.intelligentgamedesigner.testing.DebugFilterKey;

/**
 * 
 * Class that manages the RoundStatistics gathered from various game states that
 * were played by either a simulated or a real player.
 *
 */
public class RoundStatisticsManager {
	static final String location = System.getProperty("user.dir")
			+ File.separator + "round_statistics" + File.separator;
	static final String suffix = ".rs";

	static String latest_name;

	/**
	 * 
	 * Creates a named saved RoundStatistics ArrayList. This returns the name it
	 * finds for the file, for interface purposes. the GameMode and boolean are
	 * required for name formation
	 */
	public static String saveStats(ArrayList<RoundStatistics> stats,
			GameMode mode, boolean human_player) {

		try {
			String partOfName = // e.g. highscore_human_1.rs
			(mode == GameMode.HIGHSCORE) ? "highscore"
					: ((mode == GameMode.JELLY) ? "jelly" : "ingredients")
							+ (human_player ? "_human_" : "_simulated_");
			File statsFile = createLocalFile(partOfName);// updates latest name
															// too

			// Output the new array of test cases
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					new FileOutputStream(statsFile));
			objectOutputStream.writeObject(stats);
			objectOutputStream.close();

			return latest_name;
		} catch (FileAlreadyExistsException e) {
			// Return false if file already exists
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * Give the name for a .rs file (without the .rs). It returns the saved
	 * ArrayList<RoundStatistics> the file name will be
	 * (highscore/jelly/ingredients)_(human/simulated)_X select from the above.
	 * X will be a number starting from 1.
	 */
	public static ArrayList<RoundStatistics> getStats(String name) {
		try {
			File statsFile = new File(location + name + suffix);

			if (!statsFile.exists()) {
				DebugFilter.println("File doesn't exist",
						DebugFilterKey.USER_INTERFACE);
				return null;
			}

			// Read in test case and add it to array to be returned
			ObjectInputStream objectInputStream = new ObjectInputStream(
					new FileInputStream(statsFile));
			ArrayList<RoundStatistics> stats = (ArrayList<RoundStatistics>) objectInputStream
					.readObject();
			objectInputStream.close();

			return stats;
		} catch (ClassNotFoundException | IOException e) {
			return null;
		}
	}

	private static File createLocalFile(String fileName) throws IOException {
		File unitTestDirectory = new File(location);

		// Create unit test directory if it doesn't already exist
		// This will return a SecurityException is sudo/admin access is
		// required!
		if (!unitTestDirectory.exists())
			unitTestDirectory.mkdir();

		File file;
		int num_on = 1;
		while (true) {
			file = new File(location + fileName + num_on + suffix);
			if (file.exists())
				num_on++;
			else
				break;
		}
		file.createNewFile();
		latest_name = fileName + num_on + suffix;

		return file;
	}
}
