package uk.ac.cam.cl.intelligentgamedesigner.testing;

import uk.ac.cam.cl.intelligentgamedesigner.coregame.GameState;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class TestLibrary {

    static final String UNIT_TESTS_FILENAME = "unittests.jobj";

    // Opens a local file with the given filename, creating it if it doesn't already exist
    public static File createLocalFile (String fileName) throws IOException {

        // TODO: Add non-UNIX operating system support
        File file = new File(System.getProperty("user.dir") + File.separator + fileName);

        // Create the file if it doesn't exist
        if (!file.exists()) file.createNewFile();

        return file;
    }

    public static void addTest (TestCase testCase) {

        try {
            File unitTestFile = createLocalFile(UNIT_TESTS_FILENAME);

            // Create file if it doesn't exist
            if (!unitTestFile.exists()) unitTestFile.createNewFile();

            ArrayList<TestCase> testCases;
            try {
                // Read in current array of test cases and add the new test
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(unitTestFile));
                testCases = (ArrayList) objectInputStream.readObject();
                objectInputStream.close();
            } catch (EOFException e) {
                // This will be thrown if no tests have been written yet
                testCases = new ArrayList<>();
            }
            testCases.add(testCase);

            // Output the new array of test cases
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(unitTestFile));
            objectOutputStream.writeObject(testCases);
            objectOutputStream.close();

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void runTests () {
        try {
            File unitTestFile = createLocalFile(UNIT_TESTS_FILENAME);

            // Read in the array of test cases and run them
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(unitTestFile));
            ArrayList<TestCase> testCases = (ArrayList) objectInputStream.readObject();

            for (TestCase testCase : testCases) {
                testCase.run();
            }

            objectInputStream.close();

        } catch (ClassNotFoundException | IOException e) {

        }
    }


}
