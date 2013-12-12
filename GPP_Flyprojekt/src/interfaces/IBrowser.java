package interfaces;

import java.awt.event.ActionListener;

/**
 * The IBrowser interface is the interface for the flight browsers and 
 * reservation browsers. It allows for smoother transitions.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 22-Nov-2013
 */
public interface IBrowser {
    void addActionListener(ActionListener a); // Adds a listener for when an object is chosen
    <T> T getChosen(); // Returns the chosen object
    void bindReturnButton(ActionListener a); // Binds the return button to this action
    void updateLayout(); // Updates the layout of the browser
}
