package classes;

import interfaces.IDatabaseHandler.ConnectionError;
import interfaces.ISeatChooser;
import interfaces.IValidatable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/**
 * The ReservationEditor class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 27-Nov-2013
 */
public class ReservationEditor extends JFrame {
    
    Reservation reservation;
    AdditionalPassengersPanel pasPanel;
    ReservationInfoPanel resPanel;
    final SeatChooser chooser;
    FlightPanel flightPanel;
    JButton addReservationButton;
    
    /**
     * Constructor for the ReservationEditor class
     */
    public ReservationEditor () {
        super();
        setResizable(false);
        reservation = null;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        // Make the panel for the reservation info
        JPanel reservationInfoPanel = new JPanel(new BorderLayout());
        resPanel = new ReservationInfoPanel(400, 300);
        reservationInfoPanel.add(resPanel, "North");
        pasPanel = new AdditionalPassengersPanel(400, 300);
        reservationInfoPanel.add(pasPanel, "South");
        reservationInfoPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // Make the panel for the flight and seating info
        JPanel flightInfoPanel = new JPanel(new BorderLayout());
        flightPanel = new FlightPanel();
        addReservationButton = new JButton("Gem reservation");
        
        final ReservationEditor editor = this;
        addReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.onSaveButtonClicked();
            }
        });
        
        flightInfoPanel.add(flightPanel, BorderLayout.NORTH);
        flightInfoPanel.add(addReservationButton, BorderLayout.SOUTH);
        
        
        // Pack all the info together
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(reservationInfoPanel, BorderLayout.WEST);
        infoPanel.add(flightInfoPanel, BorderLayout.EAST);
        
        // Make the seat chooser panel
        chooser = new SeatChooser(); // Make a version for no-flight-chosen?
        chooser.setPreferredSize(new Dimension(300,700));
        flightPanel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                chooser.setFlight(flightPanel.getFlight());
            }
        });
        
        // Update how many seats can be chosen
        pasPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Setting passenger request to "+editor.numberOfPassengers());
                chooser.setRequestedSeats(editor.numberOfPassengers());
            }
        });
        
        // Pack the editor window
        this.add(infoPanel, BorderLayout.WEST);
        this.add(chooser, BorderLayout.EAST);
    }
    
    /**
     * Handles what happens when the save button is clicked
     */
    private void onSaveButtonClicked() {
        // If the stuff isn't valid, tell the user so :u!
        StringBuilder errorString = new StringBuilder();
        boolean passengerError = false;
        boolean seatError = false;
        boolean infoError = false;
        boolean flightError = false;

        ArrayList<Point> seats;
        
        flightError = (flightPanel.getFlight() == null);
        if (!flightError) {
            try {
                seats = chooser.getSeats(numberOfPassengers());
            } catch (ISeatChooser.NotEnoughSeatsException ex) {
                seatError = true;
                errorString.append("- "+ex.getMessage()+" (KRITISK!)\n");
            }
        } else {
            errorString.append("- Ingen afgang valgt (KRITISK!)\n");
        }
         
        if (!resPanel.isDataValid()) {
            errorString.append("- Fejl i reservationsdata\n");
            infoError = true;
        }
        if (!pasPanel.isDataValid()) {
            errorString.append("- Fejl i passager-info\n");
            passengerError = true;
        } 

        if (passengerError || seatError || infoError || flightError) {
            JOptionPane.showMessageDialog(new JFrame(), 
                "der er følgende fejl i de indtastede informationer:\n" +
                errorString.toString()+"\n"+
                "Reservationen kan ikke gemmes.", 
                "Fejl!", 
                JOptionPane.ERROR_MESSAGE);
        } else {
            saveReservation();
        }
    }
    
    /**
     * This method was somewhat hastily concocted. Please bear with it.
     * @param readOnly Whether the editor shouldn't be able to make changes to
     * reservations
     */
    @Deprecated
    public void setReadOnly(boolean readOnly) {
        addReservationButton.setVisible(!readOnly);
    }
    
    /**
     * Returns the number of passengers on this reservation. Used often enough.
     * @return 
     */
    private int numberOfPassengers() {
        return pasPanel.getPersons().size()+1;
    }
    
    /**
     * Returns the reservation assembled from the data currently in the editor
     * @return 
     */
    private Reservation getReservation() {
        HashMap<IValidatable, String> resData = resPanel.getReservationInfo();
        Person passenger = new Person(
            resData.get(PersonData.NAME), 
            resData.get(PersonData.NATIONALITY), 
            resData.get(PersonData.CPR));
        
        ArrayList<Person> persons = new ArrayList<Person>();
        for (HashMap<PersonData, String> per : pasPanel.getPersons()) {
            Person newPer = new Person(
                per.get(PersonData.NAME), 
                per.get(PersonData.NATIONALITY), 
                per.get(PersonData.CPR));
            persons.add(newPer);
        }
        
        try {
            Reservation res;
            if (reservation != null) { //Old reservation data is present
                res = new Reservation(
                    reservation.reservationID,
                    passenger, 
                    persons, 
                    chooser.getSeats(numberOfPassengers()), 
                    flightPanel.getFlight(), 
                    resData.get(ReservationData.PHONENUMBER),
                    resData.get(ReservationData.CARDNUMBER),
                    resPanel.getBookingReference());
            
            } else { // If no data => no id to overwrite
                res = new Reservation(
                    passenger, 
                    persons, 
                    chooser.getSeats(numberOfPassengers()), 
                    flightPanel.getFlight(), 
                    resData.get(ReservationData.PHONENUMBER),
                    resData.get(ReservationData.CARDNUMBER),
                    resPanel.getBookingReference());
            }
            return res;
            
        } catch (ISeatChooser.NotEnoughSeatsException ex) {
            System.out.println("Something has gone horribly wrong....");
            return null;
        }
    }
    
    /**
     * Saves the reservation currently being edited. Prompts the user if 
     * fields are left invalid or un-validated
     */
    private void saveReservation() {
        DatabaseHandler handler = DatabaseHandler.getHandler();
        try {
            if (reservation != null) {
                System.out.println("Updating...");
                handler.updateReservation(getReservation());
            } else {
                System.out.println("Adding...");
                Reservation res = handler.addReservation(getReservation());
                if (res != null) {
                    reservation = res;
                }
            }
            JOptionPane.showMessageDialog(new JFrame(), 
                    "Reservationen er gemt!", 
                    "Succes", 
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (ConnectionError ce) {
            Utils.showNoConnectionNotice("Reservationen kunne ikke gemmes");
        }
    }
    
    /**
     * 
     * @param res 
     */
    public ReservationEditor(Reservation res) {
        this();
        reservation = res;
        
        // Add reservation info
        HashMap<IValidatable, String> resInfo = new HashMap<IValidatable, String>();
        resInfo.put(ReservationData.CARDNUMBER, res.cardnumber);
        resInfo.put(ReservationData.PHONENUMBER, res.tlf);
        
        // Add info about the person having reserved
        resInfo.put(PersonData.CPR, res.passenger.cpr);
        resInfo.put(PersonData.NAME, res.passenger.name);
        resInfo.put(PersonData.NATIONALITY, res.passenger.nationality);
        
        // Update the data in the panel :)
        resPanel.setReservationInfo(resInfo);
        resPanel.setBookingReference(res.bookingNumber);
        
        // Add additional passengers
        for (Person per : res.additionalPassengers) {
            HashMap<PersonData, String> person = new HashMap<PersonData, String>();
            person.put(PersonData.CPR, per.cpr);
            person.put(PersonData.NAME, per.name);
            person.put(PersonData.NATIONALITY, per.nationality);
            pasPanel.addPerson(person);
        }
        
        // Add the flight :p
        flightPanel.loadFlight(res.flight);
        chooser.setChosen(res.seats);
    }
}
