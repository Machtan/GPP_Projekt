package classes;

import interfaces.IBrowser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * The Browser class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 12-Dec-2013
 */
public abstract class Browser extends JFrame implements IBrowser{
    
    final ArrayList<ActionListener> listeners;
    ActionListener returnListener;
    
    /**
     * Constructor for the Browser class
     */
    public Browser () {
        super();
        listeners = new ArrayList<ActionListener>();
        returnListener = null;
    }

    
    /**
     * Adds an ActionListener to this object, to be notified when a flight is
     * chosen.
     * @param a The action listener 
     */
    @Override
    public void addActionListener(ActionListener a) {
        if (!listeners.contains(a)) {
            listeners.add(a);
        }
    }
    
    /**
     * Called when the user picks an item
     */
    protected void onActionPerformed() {
        for (ActionListener a : listeners) {
            a.actionPerformed(new ActionEvent(this, 
                ActionEvent.ACTION_PERFORMED, null) {}
            );
        }
    }

    /**
     * Returns the item currently chosen in the browser
     * @param <T> Any type that the browser uses
     * @return The chosen item
     */
    @Override
    public abstract <T> T getChosen();
    
    /**
     * Binds the 'return to previous window' button to notify this action 
     * listener, so as to make the listener handle it.
     * @param a An ActionListener which handles the browser asking to return
     */
    @Override
    public void bindReturnButton(ActionListener a) {
        returnListener = a;
    }

    /**
     * Updates the layout of the browser to show recent changes (edits, deletes)
     */
    @Override
    public abstract void updateLayout();
    
    /**
     * Called when an item is double-clicked or the action button is pressed
     * with a chosen item selected. Notifies all action listeners of this.
     * @param evt An event
     */
    protected void activateActionPerformed(java.awt.event.ActionEvent evt) {
        onActionPerformed();
    }
    
    /**
     * Sets the text for the 'action' button of the browser
     */
    public abstract void setActionButtonText(String text);
    
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
