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

    @Override
    public <T> T getChosen() {
        throw new UnsupportedOperationException("classes.Browser.getChosen is not supported yet.");
    }

    @Override
    public void bindReturnButton(ActionListener a) {
        returnListener = a;
    }

    @Override
    public abstract void updateLayout();
    
    protected void activateActionPerformed(java.awt.event.ActionEvent evt) {
        onActionPerformed();
    }
    
    protected void returnActionPerformed(java.awt.event.ActionEvent evt) {
        if (returnListener != null) {
            returnListener.actionPerformed(evt);
        }
    }
}
