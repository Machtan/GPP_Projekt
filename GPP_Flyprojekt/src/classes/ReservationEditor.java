package classes;

import interfaces.IDatabaseHandler.ConnectionError;
import interfaces.ISeatChooser;
import interfaces.IValidatable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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

/**
 * The ReservationEditor class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 27-Nov-2013
 */
public class ReservationEditor extends ReturnableFrame {
    
    private Reservation reservation;
    private AdditionalPassengersPanel pasPanel;
    private ReservationInfoPanel resPanel;
    private final SeatChooser chooser;
    private FlightPanel flightPanel;
    private JButton addReservationButton;
    private JButton returnButton;
    
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
        
        // Bottom stuff
        JPanel bottomPanel = new JPanel(new GridLayout(0, 1));
        JPanel saveButtonPanel = new JPanel(new BorderLayout());
        JPanel returnButtonPanel = new JPanel(new BorderLayout());
        
        returnButton = new JButton("Gå tilbage");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnActionPerformed(e);
            }
        });
        returnButtonPanel.add(returnButton, BorderLayout.NORTH);
        
        addReservationButton = new JButton("Gem reservation");
        final ReservationEditor editor = this;
        addReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.onSaveButtonClicked();
            }
        });
        
        saveButtonPanel.add(addReservationButton, BorderLayout.SOUTH);
        bottomPanel.add(returnButtonPanel);
        bottomPanel.add(saveButtonPanel);
        
        flightInfoPanel.add(flightPanel, BorderLayout.NORTH);
        flightInfoPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        
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
     * Overriden method of ReturnableFrame to ensure that the editor doesn't
     * delete all the user's data on accident.
     * @param evt Any actionevent that will notify an ActionListener
     */
    protected void returnActionPerformed(java.awt.event.ActionEvent evt) {
        boolean hasPassengers = !pasPanel.getPersons().isEmpty();
        boolean hasPasEdit = pasPanel.isEditing();
        boolean hasFlight = flightPanel.getFlight() != null;
        boolean hasInfo = false;
        for (String val : resPanel.getReservationInfo().values()) {
            if (!val.equals("")) {
                hasInfo = true;
            }
        }
        
        if (reservation != null ) { // If the user is just editing, don't prompt
            if (returnListener != null) {
                returnListener.actionPerformed(evt);
                return;
            }
        }
        
        //System.out.println(String.format("pas %s | edi %s | fli %s | inf %s", 
        //        hasPassengers, hasPasEdit, hasFlight, hasInfo);
        if (hasPassengers || hasPasEdit || hasFlight || hasInfo) { // Data added => prompt
            String[] options = {"Nej", "Ja"};
            int answer = JOptionPane.showOptionDialog(new JFrame(), 
                "Du har indtastede informationer.\nVil du kassere disse og forlade editoren?", 
                "Bekræft lukning", JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            switch (answer) {
                case 0: //No => do nothing
                    System.out.println("DONT LEAVE");
                    return;
                case 1: //Yes => return
                    System.out.println("YES LEAVE");
                    if (returnListener != null) {
                        returnListener.actionPerformed(evt);
                    }
                    break;
            }
        } else {
            if (returnListener != null) {
                returnListener.actionPerformed(evt);
            }
        }
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
                errorString.append("- "+ex.getMessage()+"\n");
            }
        } else {
            errorString.append("- Ingen afgang valgt\n");
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
                "Der er følgende fejl i de indtastede informationer:\n" +
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
     * @return The combined reservation in the editor
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
