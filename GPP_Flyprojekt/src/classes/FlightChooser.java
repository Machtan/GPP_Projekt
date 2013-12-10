package classes;

import interfaces.IFlight;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread.*;

/**
 * The FlightChooser class prompts the user to choose a flight
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 07-Dec-2013
 */
public class FlightChooser extends FlightInspector {
    
    /**
     * Constructor for the FlightChooser class
     */
    
    private Flight chosen;
    
    public FlightChooser() {
        super();
        chosen = null;
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                chosen = getChosen();
            }
        });
    }
    
    public static IFlight get() {
        FlightChooser chooser = new FlightChooser();
        chooser.setTitle("Vælg en afgang");
        chooser.pack();
        chooser.setVisible(true);
        while (chooser.chosen == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                System.out.println("Oh, hey, an interrupt :o!");
            }
        }
        IFlight flight = chooser.chosen;
        chooser.dispose();
        return flight;
    }
    
}
