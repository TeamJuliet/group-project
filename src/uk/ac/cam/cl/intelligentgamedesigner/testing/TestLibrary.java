package uk.ac.cam.cl.intelligentgamedesigner.testing;

import java.io.*;
import java.util.ArrayList;

public class TestLibrary {

    public static void addTest (TestCase testCase) {
        try {
            // Read in current array of test cases and add the new test
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("unittests.jobj"));
            ArrayList<TestCase> testCases = (ArrayList) objectInputStream.readObject();
            testCases.add(testCase);
            objectInputStream.close();

            // Output the new array of test cases
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("unittests.jobj"));
            objectOutputStream.writeObject(testCases);
            objectOutputStream.close();

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void runTests () {
        try {
            // Read in current array of test cases and add the new test
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("unittests.jobj"));
            ArrayList<TestCase> testCases = (ArrayList) objectInputStream.readObject();

            for (TestCase testCase : testCases) {
                testCase.run();
            }

            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {

        }
    }


}
