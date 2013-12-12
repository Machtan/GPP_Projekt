package classes;

import interfaces.IFlight;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The EditReservationBrowser is a reservation browser, which opens up a
 * reservation for editing, when it is double-clicked
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 07-Dec-2013
 */
public class EditReservationBrowser extends ReservationBrowser{
    
    /**
     * Constructor for the EditReservationBrowser class
     */
    public EditReservationBrowser (IFlight flight) {
        super(flight);
        final EditReservationBrowser browser = this;
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ReservationEditor editor = new ReservationEditor(getChosen());
                editor.setTitle("Redigér reservation");
                Utils.transition(browser, editor);
            }
        });
    }
}
