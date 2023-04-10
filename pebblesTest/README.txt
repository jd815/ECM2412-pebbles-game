README
PebbleGame
The ECM2412 paired-programming coursework.
Implemented in java SE 15. The game is run by compiling the java file and then executing it with java PebbleGame.java
If you follow the in-game instructions you will be able to play the game no problem.
Testing
We did most of our testing for the PebbleGame class through controlled inputs and outputs. As we didn't know how to
manage inputs in the testing provided by jUnit4. We had expected outputs for given inputs which we would check by
System.out.println() which would show us if what we did was correct.
As for the Bag and Player classes we did our testing through our Java IDE NetBeans.
To run the test file you can run the PebbleGameTest.java file through most IDEs such as NetBeans
If using NetBeans:
  1.Create a new project using the category Java with Maven and Projects: Java Application and call the project anything you wish.
  2.Then Create a new class.java which will create  <default package> with the new class in it
  3.You can now delete the class and insert the PebbleGame.java file
  4.Right click on the java file and select Test file.
  5.This will create a "Test Packages" file in which you will copy and past the PebbleGameTest.java
  6.You can then run this and see all the tests

Again the tests cover the totality of the Bag class and everything except the methods that interact with files with 100% success rate.
The other methods weer tested manually.
