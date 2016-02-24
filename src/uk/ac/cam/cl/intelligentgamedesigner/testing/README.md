# Testing

## Setup - IMPORTANT
In order to get JUnit working with the unit test classes defined below, you will need to add `junit-4.12.jar` and 
`hamcrest-core-1.3.jar` (these are in the `libs` directory in this repo) to the test class path.

#### IntelliJ
To do this in IntelliJ:

* Right click on the jar files and click `Add As Library`.
* `Run->Edit Configurations`
* Click the add button to add a new configuration.
* Select JUnit.
* Type in the class name of the test you want to add in the class field.
* Click `Apply`
* Click `Okay`

#### Eclipse
* Right click on the project and click `properties`.
* Click on the `Java Build Path` on the left side.
* Click the `Add External JARs` button.
* Navigate to the git directory and include the JARs.
* Click `Apply`
* Click `Okay`

## Contents
This package contains three different classes for running unit tests:

* `GameStateTestRunner`
* `LevelDesignerTestRunner`
* `SimulatedPlayersTestRunner`

The rest of the unit tests for a given class will be given in the file `class name` + `Test`. 

#### GameStateTestRunner
This test launches a basic graphical user interface that runs unit tests from files in the `unit_tests` directory. 
These unit tests can be created in the user interface that Ben has built. I decided not to use JUnit for these tests,
 since it was easier to write/read tests from a file using my own implementation.

#### LevelDesignerTestRunner
Running this class will run all of the unit test methods annotated with `@Test`. This class uses JUnit.

#### SimulatedPlayersTestRunner
Running this class will run all of the unit test methods annotated with `@Test`. This class uses JUnit.