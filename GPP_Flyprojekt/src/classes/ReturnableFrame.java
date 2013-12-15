package classes;

import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 * The ReturnableFrame class holds the functions needed for a window to easily
 * transition back to the previous window using Utils.transition.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 15-Dec-2013
 */
public abstract class ReturnableFrame extends JFrame{
    
    protected ActionListener returnListener;
    
    /**
     * Constructor for the ReturnableFrame class
     */
    public ReturnableFrame () {
        super();
    }
    
    /**
     * Binds the return button of the frame to notify the given action listener
     * @param a The action listener to handle the "return" call
     */
    public void bindReturnAction(ActionListener a) {
        returnListener = a;
    }
    
    /**
     * When the 'return to previous window' (or something) button is pressed
     * @param evt An event of no notability
     */
    protected void returnActionPerformed(java.awt.event.ActionEvent evt) {
        if (returnListener != null) {
            returnListener.actionPerformed(evt);
        }
    }
    
}
