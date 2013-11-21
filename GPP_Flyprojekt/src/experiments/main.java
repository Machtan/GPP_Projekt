/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package experiments;

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
    }  
} 
