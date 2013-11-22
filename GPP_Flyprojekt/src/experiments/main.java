package experiments;

import classes.Date;

/**
 * This class is just there to run various experiments to learn how java works
 * ;)
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public class main {
    
    /**
     * Constructor for the main class
     */
    public static void main (String[] args) {
        for (PersonData pd : PersonData.values()) {
            System.out.println(pd);
        }
        
        Date test = new Date(2013, 11, 22, 9, 9);
        System.out.println("TestDate: "+test);
    }  
} 
