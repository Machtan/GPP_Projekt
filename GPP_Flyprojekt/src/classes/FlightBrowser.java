package classes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The FlightBrowser class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 07-Dec-2013
 */
public class FlightBrowser extends FlightWindow {
    
    /**
     * Constructor for the FlightBrowser class
     */
    public FlightBrowser () {
        super();
        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (flightTable.getSelectedRow() > -1) {
                        ReservationBrowser reservationBrowser = new ReservationBrowser(getChosen());
                        reservationBrowser.pack();
                        reservationBrowser.setVisible(true);
                        dispose();
                    }
            }
        });
    }
    
}
