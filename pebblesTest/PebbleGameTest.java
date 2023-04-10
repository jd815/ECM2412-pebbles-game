/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author jakubdavison
 */
public class PebbleGameTest {

    //used to store the generic game for testing
    private PebbleGame game;
    private ArrayList <Integer> nums = new ArrayList<>();
    private PebbleGame.Bag bag;
    private PebbleGame.Player player;


    public PebbleGameTest() {
    }
    @Before //Ran before every @Test
    public void setUp(){
        //Create a new game and store it
        this.game = new PebbleGame();
        for(int i=1; i<101; i++){
            nums.add(i);
        }
    }
    
    /**
     * We did not know how to test while needing inputs but we ran multiple tests while writing the code
     * Such as having a print that told us everything we needed to see. 
     */
    
    /**
     * Test of Bag constructor method, of class Bag.
     */
    @Test
    public void testBagConstructor(){
        bag = this.game.new Bag ("A");
        String name = bag.getName();
        assertEquals("A", name);
    }
    
    /**
     * Test of setPebbles method, of class Bag.
     */
    @Test
    public void testSetPebbles(){
        bag = this.game.new Bag ("A");
        bag.setPebbles(nums);
        int bagSize = bag.getBagSize();
        assertEquals(100, bagSize);
    }
    
    /**
     * Test of clearPebbles method, of class Bag.
     */
    @Test
    public void testClearPebbles(){
        bag = this.game.new Bag ("A");
        bag.setPebbles(nums);
        bag.clearPebbles();
        int bagSize = bag.getBagSize();
        assertEquals(0, bagSize);

    }
    /**
     * Test of getPebble method, of class Bag.
     */
    @Test
    public void testGetPebble(){
        bag = this.game.new Bag ("A");
        bag.setPebbles(nums);
        int pebble = bag.getPebble(3);
        assertEquals(4, pebble);

    }
    
    /**
     * Test of getAllPebbles method, of class Bag.
     */
    @Test
    public void testGetAllPebbles(){
        bag = this.game.new Bag ("A");
        bag.setPebbles(nums);
        ArrayList<Integer> pebbles = new ArrayList<>();
        pebbles = bag.getAllPebbles();
        int bagSize = bag.getBagSize();
        assertEquals(nums, pebbles);
        assertEquals(0, bagSize);

    }
    
    /**
     * Test of addPebble method, of class Bag.
     */
    @Test
    public void testAddPebble(){
        bag = this.game.new Bag ("A");
        bag.setPebbles(nums);
        bag.addPebble(101);
        int peb = bag.getPebble(100);
        assertEquals(101, peb);

    }
    
    /**
     * Test of Player constructor, showHand, and getName method, of class Player.
     */
    @Test
    public void testPlayerConstructor(){
        player = this.game.new Player (nums, "player1");
        String hand = "";
            for (int i : nums) {
                hand = hand + i + ", ";
            }
            hand = hand.replaceAll(", $", "");
        String output = player.showHand();
        assertEquals(hand, output);

        String name = player.getName();
        assertEquals("player1", name);

    }
    /**
     * Test of removePebble method, of class Player.
     */
    @Test
    public void testRemovePebble(){
        player = this.game.new Player (nums, "player1");
        String hand = "";
            for (int i=0;i<99; i++) {
                hand = hand + nums.get(i) + ", ";
            }
        hand = hand.replaceAll(", $", "");
        player.removePebble();
        String output = player.showHand();
        assertEquals(hand, output);

    }
    /**
     * Test of addPebble method, of class Player.
     */
    @Test
    public void testAddPebblePlayer(){
        player = this.game.new Player (nums, "player1");
        String hand = "";
            for (int i=1;i<102; i++) {
                hand = hand + i + ", ";
            }
        hand = hand.replaceAll(", $", "");
        player.addPebble(101);
        String output = player.showHand();
        assertEquals(hand, output);

    }
    /**
     * Test of checkTotal method, of class Player.
     */
    @Test
    public void testCheckTotal(){
        ArrayList<Integer> hand = new ArrayList<>();
        hand.addAll(Arrays.asList(10,10,10,10,10,10,10,10,10,10));
        player = this.game.new Player (hand, "player1");
        boolean output = player.checkTotal();
        assertEquals(false, output);

    }
    /**
     * Test of getLast method, of class Player.
     */
    @Test
    public void testGetLast(){;
        player = this.game.new Player (nums, "player1");
        int last = player.getLast();
        assertEquals(100, last);

    }
}
