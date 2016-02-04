package uk.ac.cam.cl.intelligentgamedesigner.testing;

import org.junit.Test;

import javax.swing.*;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

public class TestLibrary {

    static final String UNIT_TESTS_EXTENSION = ".ut";

    /**
     * Returns the file path to the project directory on the machine running this code.
     * Currently works for UNIX - but need to check Windows.
     *
     * @return
     */
    public static String getUnitTestDirectoryPath () {
        // TODO: Add non-UNIX operating system support
        return System.getProperty("user.dir") + File.separator + "unit_tests" + File.separator;
    }

    /**
     * Opens a local file with the given filename, creating it if it doesn't already exist.
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File createLocalFile (String fileName) throws IOException {
        File unitTestDirectory = new File(getUnitTestDirectoryPath());

        // Create unit test directory if it doesn't already exist
        // This will return a SecurityException is sudo/admin access is required!
        if (!unitTestDirectory.exists()) unitTestDirectory.mkdir();

        File file = new File(getUnitTestDirectoryPath() + fileName + UNIT_TESTS_EXTENSION);

        // Throw Exception if file already exists
        if (file.exists()) throw new FileAlreadyExistsException(file.getName());
        else file.createNewFile();

        return file;
    }

    /**
     * This takes an instance of TestCase and saves it to the file name stored within the instance.
     *
     * @param testCase
     * @return Returns true if the addition was successful, and false otherwise.
     */
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
                    return name.endsWith(UNIT_TESTS_EXTENSION);
                }
            });

            ArrayList<TestCase> testCases = new ArrayList<>();
            for (File file : foundFiles) {
                // Read in test case and add it to array to be returned
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                testCases.add((TestCase) objectInputStream.readObject());
                objectInputStream.close();
            }
            return testCases;
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }

    /**
     * This returns an instance of TestCase saved at the given file name.
     *
     * @param name
     * @return The instance of TestCase loaded from the file.
     * @throws NoSuchUnitTest   This is thrown if the file doesn't exist.
     */
    public static TestCase getTest (String name) throws NoSuchUnitTest {
        try {
            File unitTestFile = new File(getUnitTestDirectoryPath() + name + UNIT_TESTS_EXTENSION);

            if (!unitTestFile.exists()) throw new NoSuchUnitTest(unitTestFile.getAbsolutePath() + "\n does not exist.");

            // Read in test case and add it to array to be returned
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(unitTestFile));
            TestCase testCase = (TestCase) objectInputStream.readObject();
            objectInputStream.close();

            return testCase;
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }
}
