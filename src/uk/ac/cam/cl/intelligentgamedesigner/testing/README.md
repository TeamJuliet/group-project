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
TODO

## Contents
This package contains three different classes for running unit tests:

* `GameStateTestRunner`
* `LevelDesignerTestRunner`
* `SimulatedPlayersTestRunner`

#### GameStateTestRunner
This test launches a basic graphical user interface that runs unit tests from files in the `unit_tests` directory. 
These unit tests can be created in the user interface that Ben has built. I decided not to use JUnit for these tests,
 since it was easier to write/read tests from a file using my own implementation.

#### LevelDesignerTestRunner
Running this class will run all of the unit test methods annotated with `@Test`.

#### SimulatedPlayersTestRunner
Running this class will run all of the unit test methods annotated with `@Test`.