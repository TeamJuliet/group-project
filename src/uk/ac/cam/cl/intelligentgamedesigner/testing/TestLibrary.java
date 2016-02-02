package uk.ac.cam.cl.intelligentgamedesigner.testing;

import javax.swing.*;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

public class TestLibrary {

    static final String UNIT_TESTS_PREFIX = "unittest-";
    static final String UNIT_TESTS_EXTENSION = ".ut";

    public static String getUnitTestDirectoryPath () {
        // TODO: Add non-UNIX operating system support
        return System.getProperty("user.dir") + File.separator + "unit_tests" + File.separator;
    }

    // Opens a local file with the given filename, creating it if it doesn't already exist
    public static File createLocalFile (String fileName) throws IOException {
        File unitTestDirectory = new File(getUnitTestDirectoryPath());

        // Create unit test directory if it doesn't already exist
        // This will return a SecurityException is sudo/admin access is required!
        if (!unitTestDirectory.exists()) unitTestDirectory.mkdir();

        File file = new File(getUnitTestDirectoryPath() + UNIT_TESTS_PREFIX + fileName + UNIT_TESTS_EXTENSION);

        // Throw Exception if file already exists
        if (file.exists()) throw new FileAlreadyExistsException(file.getName());
        else file.createNewFile();

        return file;
    }

    public static boolean addTest (TestCase testCase) {

        try {
            File unitTestFile = createLocalFile(testCase.getFileName());

            // Output the new array of test cases
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(unitTestFile));
            objectOutputStream.writeObject(testCase);
            objectOutputStream.close();

            return true;
        } catch (FileAlreadyExistsException e) {
            // Return false if file already exists
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This reads in all unit tests (as TestCase objects) within the unit_tests/ directory.
     * It then appends them to an ArrayList and returns that.
     *
     * @return An ArrayList of saved TestCase objects
     */
    public static ArrayList<TestCase> getTests () {
        try {
            File dir = new File(getUnitTestDirectoryPath());
            File[] foundFiles = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith(UNIT_TESTS_PREFIX);
                }
            });

            ArrayList<TestCase> testCases = new ArrayList<>();
            for (File file : foundFiles) {
                // Read in test case and add it to array to be returned
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                Object test = objectInputStream.readObject();
                testCases.add((TestCase) test);
                System.out.println("I HAPPEN");
                objectInputStream.close();
            }
            return testCases;
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }
}
