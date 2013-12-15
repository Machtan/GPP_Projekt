package classes;

import interfaces.IBrowser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The Browser class is a top level class for browsers with a few customization
 * options, and options common in their functionality
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 12-Dec-2013
 */
public abstract class Browser extends ReturnableFrame implements IBrowser{
    
    final ArrayList<ActionListener> listeners;
    
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
        setEnabled(false); // Since this opens a new window, the window should disable
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
}
