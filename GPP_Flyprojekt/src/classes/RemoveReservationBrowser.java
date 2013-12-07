package classes;

import interfaces.IFlight;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The RemoveReservationBrowser lets the user browse for a reservation, and
 prompts the user to delete a reservation when it is double-clicked
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 07-Dec-2013
 */
public class RemoveReservationBrowser extends ReservationBrowser {
    
    /**
     * Constructor for the DeleteReservationBrowser class
     */
    public RemoveReservationBrowser (IFlight flight) {
        super(flight);
        
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Ja", "Nej"};
                int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Vil du slette den valgte reservation?",
                    "Slet Reservation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
                switch(n) {
                    case 0: //Yes => delete the reservation
                        DatabaseHandler.getHandler().deleteReservation(getChosen());
                        break; 
                    case 1: //No => just exit :)
                        return; 
                }
            }
        });
    }
    
}
