package classes;

import interfaces.IDatabaseHandler.ConnectionError;
import interfaces.IFlight;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The RemoveReservationBrowser lets the user browse for a reservation, and
 * prompts the user to delete a reservation when it is double-clicked
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 07-Dec-2013
 */
public class RemoveReservationBrowser extends ReservationBrowser {
    
    /**
     * Constructor for the DeleteReservationBrowser class
     */
    public RemoveReservationBrowser (IFlight flight) {
        super(flight);
        
        final ReservationBrowser browser = this;
        
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                browser.setEnabled(true);
                Object[] options = {"Nej", "Ja"};
                int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Vil du slette den valgte reservation?",
                    "Slet Reservation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[1]);
                switch(n) {
                    case 1: //Yes => delete the reservation
                        try {
                            DatabaseHandler.getHandler().deleteReservation(getChosen());
                            browser.updateLayout();
                        } catch (ConnectionError ce) {
                            Utils.showNoConnectionNotice("Den valgte reservation kan ikke slettes");
                        }
                        
                        break; 
                    case 0: //No => just exit :)
                        break;
                }
            }
        });
        
        this.setActionButtonText("Slet reservation");
    }
}
