package classes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The FlightBrowser class lets the user browse flights and inspect reservations
 * on these, without being able to edit them.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 07-Dec-2013
 */
public class FlightBrowser extends FlightWindow {
    
    /**
     * Constructor for the FlightBrowser class
     */
    public FlightBrowser () {
        super();
        final FlightBrowser browser = this;
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (browser.getChosen() != null) {
                    final ReservationBrowser reservationBrowser = new ReservationBrowser(getChosen());
                    reservationBrowser.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ReservationEditor editor = new ReservationEditor(reservationBrowser.getChosen());
                            editor.setReadOnly(true);
                            Utils.transition(reservationBrowser, editor);
                        }
                    });
                    Utils.transition(browser, reservationBrowser);
                }
            }
        });
    }
    
}
