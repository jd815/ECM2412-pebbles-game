import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The ECM2414 Software Development paired coursework. The pebble game
 * 
 * @version 1.0
 * @author Jakub Davison and Iestynlee Davies
 */
public class PebbleGame {
    // Scanner called here so it can be used throughout the class
    private Scanner input = new Scanner(System.in);

    // Random called here so it can be used throughout the class
    private Random ran = new Random();

    // All the instances of the class Bag are initialized at the start so they can
    // be added to the black and white bags
    // which are used throughout the class
    private Bag A = new Bag("A");
    private Bag B = new Bag("B");
    private Bag C = new Bag("C");
    private Bag X = new Bag("X");
    private Bag Y = new Bag("Y");
    private Bag Z = new Bag("Z");
    private Bag[] blackBags = { X, Y, Z };
    private Bag[] whiteBags = { A, B, C };

    // This variable is used to make sure the Threads stop playing once a player has
    // won
    private boolean gameOver = false;

    // The arrayList of players which holds the players of class Player
    private ArrayList<Player> players = new ArrayList<>();

    // To avoid having a thread try to change a bag while one thread has alrady
    // called that method there is this variable
    private boolean changing = false;

    // The constructor for the pebbleGame class
    private PebbleGame() {
    }

    public static void main(String[] args) {
        // Main method which creates an instance of pebbleGame and starts the game
        PebbleGame game = new PebbleGame();
        game.startGame();
	}

    /**
     * This is the method used to clear everything when starting a new game so there
     * are no leftovers from the last game such as player outputFiles, pebbles in
     * bags, players
     */
    public void setVariablesStart() {
        for (Player i : players) {
            File myObj = new File(i.getName() + "_output.txt");
            myObj.delete();
        }
        this.blackBags[0].clearPebbles();
        this.blackBags[1].clearPebbles();
        this.blackBags[2].clearPebbles();
        this.whiteBags[0].clearPebbles();
        this.whiteBags[1].clearPebbles();
        this.whiteBags[2].clearPebbles();
        this.players.clear();
        this.changing = false;

    }

    /**
     * Starts the game by printing the required text. Then allows the player to
     * input the number of players and the name of the file for the bags. It makes
     * sure all the necessary conditions are met. These are: number of players is
     * strictly positive (it also checks that the input is E if the player wants to
     * end the game), the file is accessible, has at least 11*number of players
     * numbers in it, and that none of those numbers are negative or 0
     */
    public void startGame() {
        setVariablesStart();
        System.out.println(
                "Welcome to the PebbleGame!! \nYou will be asked to enter the number of players \nand then for the location of three files in turn containing comma seperated integer values for the pebble weights. \nThe integer values must be strictly positive. \nThe game will then be simulated, and output written to the files in this dirrectory.");
        System.out.println("Please enter the number of players");
        // Might have to have input here into a string and check if its e and then
        // convertable because otherwise it doesn't print this out and goes straight to
        // the loop if input has next
        // Add the text to choose the file for bags
        boolean f = true;
        int numPlayers;
        while (true) {
            String numPlayersString = input.nextLine();
            if (numPlayersString.equals("E")) { // checks if the player wants to end the game
                endGame();
            } else {
                try {
                    numPlayers = Integer.valueOf(numPlayersString); // if input is not a number it will throw a NumberFormat
                    if (numPlayers > 0) {
                        break;
                    } else {
                        System.out.println(
                                "The number of players you have entered needs to be a strictly positive integer. Please try again");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("You need to enter a number or E. Please try again");
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            while (f == true) {
                System.out.println("Please enter the location of bag number " + i + " to load");
                String fileName = input.nextLine();
                try {
                    File myObj = new File(fileName); // if file not found a FileNotFoundException is thrown
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        String[] entriesString = data.split(",");
                        int[] entries = Arrays.asList(entriesString).stream().mapToInt(Integer::parseInt).toArray(); // converts the entire array of String[] to an array of int[]. If there is any non integer in the array an exception will be thrown
                        if (entries.length < 11 * numPlayers) {
                            throw new Exception(); // if not at least 11*number of players
                        }
                        for (int x = 0; x < entries.length; x++) {
                            if (entries[x] < 1) { // if any of the integers is a non-positive integer, the bag will be cleared and the process restarted
                                this.blackBags[i].clearPebbles();
                                throw new NumberFormatException();
                            }
                            this.blackBags[i].addPebble(entries[x]);
                        }
                    }
                    myReader.close();
                    f = !f;
                } catch (FileNotFoundException e) {
                    System.out.println("A file with that name was not found. Please try again.");
                } catch (NumberFormatException e) {
                    System.out.println("You have a non positive integer in your files. Please enter new file.");
                } catch (Exception e) {
                    System.out.println(
                            "The file you supplied does not contain at least 11 times the number of players. PLease supply a new file");
                }
            } f=!f;
        }
        runPlayers(numPlayers);
    }

    /**
     * After the game is started and the bags are set. This method is called which
     * initializes the players (threads)
     * 
     * @param numPlayers
     */
    public void runPlayers(int numPlayers) {
        this.gameOver = false;
        for (int i = 0; i < numPlayers; i++) {
            String n = "player" + (i + 1);
            new Thread(Integer.toString(i + 1)) {
                public void run() {
                    ArrayList<Integer> h = new ArrayList<>();
                    for (int x = 0; x < 9; x++) {
                        int bagNumber = ran.nextInt(3);
                        int peb = 1;
                        while (true) {
                            try {
                                peb = blackBags[bagNumber].getPebble(ran.nextInt(blackBags[bagNumber].getBagSize()));
                                break;
                            } catch (Exception e) {
                            }
                        }
                        h.add(peb);
                        whiteBags[bagNumber].addPebble(peb);
                    }
                    Player p = new Player(h, n); // Send email to double check if this is acceptable aka if the hand can
                                                 // be made at this stage
                    players.add(p);
                    System.out.println("Player " + n + " initial hand is " + p.showHand());
                    while (gameOver != true) {// while no one has won the threads keep running their turn
                        playerTurn(p);
                    }

                }
            }.start();
        }
    }

    /**
     * the game finishes
     */
    public void endGame() {
        System.out.println("The game is over");
        System.exit(0);
    }

    /**
     * This method changes the bags and takes the ranBag param to know which white
     * and black bag to select
     * 
     * @param ranBag
     */
    public synchronized void changeBag(int ranBag) {
        this.blackBags[ranBag].setPebbles(this.whiteBags[ranBag].getAllPebbles());
        this.changing = false;
    }

    /**
     * This is what each player does during their turn. It checks whether the random
     * bag they selected is empty, if so it is exchanged and if not then they
     * continute to pick a random pebble and this is written into their .txt files
     * 
     * @param p
     */
    public void playerTurn(Player p) {
        while (true) {
            int ranBag = ran.nextInt(3);
            if ((this.blackBags[ranBag].getBagSize() == 0) && (gameOver == false)) {// in case a thread hasn't finished
                                                                                    // yet and would try to continue
                                                                                    // with their turn
                if (changing == false) {
                    this.changing = true;
                    changeBag(ranBag);
                } else {
                    ranBag = ran.nextInt(3);
                }
            } else if ((gameOver == false) && (this.blackBags[ranBag].getBagSize() > 0) && (changing == false)) {
                int ranPeb = 0;
                while (true) {
                    try {
                        ranPeb = this.blackBags[ranBag].getPebble(ran.nextInt(this.blackBags[ranBag].getBagSize())); // if error happens here it will catch it and the true loop will continue till there is no error
                        break;
                    } catch (Exception e) {
                    }
                }
                p.addPebble(ranPeb);
                if (p.checkTotal() == false) {
                    gameOver = true;
                    System.out
                            .println("Congratulations! " + p.getName() + " won the game with the hand " + p.showHand());
                    startGame();
                } else {
                    p.writeToFileBeforeToss(ranBag);
                    int toss = p.removePebble();
                    this.whiteBags[ranBag].addPebble(toss);
                    p.writeToFileAfterToss(ranBag, toss);
                    break;
                }
            }
        }
    }

    // Nested class called Bag
    public class Bag {
        // Synchronized array list to make sure there are no conflicts when accessing it
        private List<Integer> pebbles = Collections.synchronizedList(new ArrayList<Integer>());
        private String name;

        /**
         * Constructor for Bag class
         * 
         * @param n
         */
        Bag(String n) {
            this.name = n;
        }

        /**
         * sets the pebbles in the bag. Has to be done by a for loop because if it was
         * just pebbles = peb when one bag's pebbles were altered the pebbles of all
         * bags would be changed
         * 
         * @param peb
         */
        public void setPebbles(ArrayList<Integer> peb) {
            for (int i : peb) {
                this.pebbles.add(i);
            }
        }

        // clears the pebbles
        public void clearPebbles() {
            this.pebbles.clear();
        }

        /**
         * gets a pebble from the bag and then remove. Synchronized to make sure no
         * conflicts occur
         * 
         * @param x --> the position of the pebble to be gotten
         * @return --> the pebble that was taken from the bag
         */
        public synchronized int getPebble(int x) {
            int ret = this.pebbles.get(x);
            this.pebbles.remove(x);
            return ret;
        }

        /**
         * Returns and clears all the pebbles in a bag. Used in white bags. Synchronized
         * to make sure no conflicts occur
         * 
         * @return --> all the pebbles in the bag
         */
        public synchronized ArrayList<Integer> getAllPebbles() {
            ArrayList<Integer> ret = new ArrayList<>();
            for (int i : pebbles) {
                ret.add(i);
            }
            pebbles.clear();
            return ret;
        }

        /**
         * Adds a pebble into the bag. Synchronized to avoid conflicts between threads
         * 
         * @param x --> the size of the pebble that should be added to the bag
         */
        public synchronized void addPebble(int x) {
            pebbles.add(x);
        }

        /**
         * Gets the size of the bag. Synchronized so the bag size doesn't change when
         * someone is working with the bag
         * 
         * @return --> size of the bag
         */
        public synchronized int getBagSize() {
            return pebbles.size();
        }

        /**
         * gets the name of the bag
         * 
         * @return --> name of the bag
         */
        public String getName() {
            return this.name;
        }
    }

    // Nested class called Player
    class Player {
        private ArrayList<Integer> hand = new ArrayList<>();
        private String name;

        /**
         * The constructor method for Player sets the player's original hand and the
         * name
         * 
         * @param h --> hand
         * @param n --> name
         */
        Player(ArrayList<Integer> h, String n) {
            // has to be done one by one because if its hand = h when h is cleared it clears
            // hand as well
            for (int i : h) {
                hand.add(i);
            }
            this.name = n;
            //createFile();
        }

        /**
         * Finds the largest pebble in the hand and removes it and then returns the
         * value. A random value could have been chosen but with the provided values the
         * games were too long so we chose to go with this way
         * 
         * @return --> the pebble that was removed
         */
        public int removePebble() {
            int biggest = 0;
            int position = -1;
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i) > biggest) {
                    biggest = hand.get(i);
                    position = i;
                }
            }
            hand.remove(position);
            return biggest;
        }

        /**
         * adds the pebble to the hand
         * 
         * @param peb --> pebble to be added
         */
        public void addPebble(int peb) {
            hand.add(peb);
        }

        /**
         * Returns the total weight of the hand
         * 
         * @return --> weight of hand
         */
        public boolean checkTotal() {
            int total = 0;
            for (int i : hand) {
                total = total + i;
            }
            if (total == 100) {
                return false;
            } else {
                return true;
            }
        }

        /**
         * Shows the hand in one String
         * 
         * @return --> hand
         */
        public String showHand() {
            String ret = "";
            for (int i : hand) {
                ret = ret + i + ", ";
            }
            // removes the last comma
            ret = ret.replaceAll(", $", "");
            return ret;
        }

        /**
         * Returns the name of the Player
         * 
         * @return --> name
         */
        public String getName() {
            return this.name;
        }

        /**
         * Returns the last pebble in the hand
         * 
         * @return --> last pebble in hand
         */
        public int getLast() {
            return this.hand.get(hand.size() - 1);
        }

        // Creates a new file for the player
        public void createFile() {
            try {
                String fileName = this.name + "_output.txt";
                File myObj = new File(fileName);
                myObj.createNewFile();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        /**
         * Writes into the file without deleting what was already there
         * 
         * @param ranBag --> number of bag
         */
        public void writeToFileBeforeToss(int ranBag) {
            File log = new File(this.getName() + "_output.txt");
            String logs = this.getName() + " has drawn a " + this.getLast() + " from bag " + blackBags[ranBag].getName()
                    + "\n" + this.getName() + " hand is " + this.showHand() + "\n";
            try {
                PrintWriter out = new PrintWriter(new FileWriter(log, true));
                out.append(logs);
                out.close();
            } catch (IOException e) {
                System.out.println("COULD NOT LOG!!");
            }
        }

        /**
         * Writes into the file without deleting what was already there
         * 
         * @param ranBag --> number of bag
         * @param toss   --> value of what was tossed
         */
        public void writeToFileAfterToss(int ranBag, int toss) {
            File log = new File(this.getName() + "_output.txt");
            String logs = this.getName() + " has discarded a " + toss + " to bag " + whiteBags[ranBag].getName() + "\n"
                    + this.getName() + " hand is " + this.showHand() + "\n";
            try {
                PrintWriter out = new PrintWriter(new FileWriter(log, true));
                out.append(logs);
                out.close();
            } catch (IOException e) {
                System.out.println("COULD NOT LOG!!");
            }
        }
    }
}