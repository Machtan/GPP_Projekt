package classes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 * The FlightInspector class lets the user browse flights and inspect reservations
 on these, without being able to edit them.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 07-Dec-2013
 */
public class FlightInspector extends FlightBrowser {
    
    /**
     * Constructor for the FlightBrowser class
     */
    public FlightInspector () {
        super();
        final FlightInspector browser = this;
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                SeatChooser chooser = new SeatChooser();
                chooser.setFlight(getChosen());
                frame.add(chooser);
                Utils.transition(browser, frame);
            }
        });
    }
}
