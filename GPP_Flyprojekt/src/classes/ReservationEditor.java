package classes;

import external.SpringUtilities;
import interfaces.IFlight;
import interfaces.IValidatable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * The ReservationEditor class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 27-Nov-2013
 */
public class ReservationEditor extends JFrame {
    
    AdditionalPassengersPanel pasPanel;
    ReservationInfoPanel resPanel;
    SeatChooser chooser;
    FlightPanel flightPanel;
    
    /**
     * Constructor for the ReservationEditor class
     */
    public ReservationEditor () {
        super();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        // Make the panel for the reservation info
        JPanel reservationInfoPanel = new JPanel(new BorderLayout());
        resPanel = new ReservationInfoPanel(300, 300);
        reservationInfoPanel.add(resPanel, "North");
        pasPanel = new AdditionalPassengersPanel(300, 300);
        reservationInfoPanel.add(pasPanel, "South");
        
        // Make the panel for the flight and seating info
        JPanel flightInfoPanel = new JPanel(new BorderLayout());
        flightPanel = new FlightPanel();
        flightInfoPanel.add(flightPanel, BorderLayout.NORTH);
        
        //chooser = new SeatChooser(); // Make a version for no-flight-chosen?
        //chooser.setPreferredSize(new Dimension(300,500));
        //flightInfoPanel.add(chooser, BorderLayout.SOUTH);
        
        
        // Pack the editor window
        this.add(reservationInfoPanel, BorderLayout.WEST);
        this.add(flightInfoPanel, BorderLayout.EAST);
    }
    
    /**
     * 
     * @param res 
     */
    public ReservationEditor(Reservation res) {
        this();
        
        // Add reservation info
        HashMap<ReservationData, String> resData = new HashMap<ReservationData, String>();
        resData.put(ReservationData.CARDNUMBER, res.cardnumber);
        resData.put(ReservationData.PHONENUMBER, res.tlf);
        resPanel.resInfoList.setData(resData);
        resPanel.resRefLabel.setText(res.bookingNumber);
        
        // Add info about the person having reserved
        HashMap<PersonData, String> mainPassenger = new HashMap<PersonData, String>();
        mainPassenger.put(PersonData.CPR, res.passenger.cpr);
        mainPassenger.put(PersonData.NAME, res.passenger.name);
        mainPassenger.put(PersonData.NATIONALITY, res.passenger.nationality);
        resPanel.pasInfoList.setData(mainPassenger);
        
        // Add additional passengers
        for (Person per : res.additionalPassengers) {
            HashMap<PersonData, String> person = new HashMap<PersonData, String>();
            person.put(PersonData.CPR, per.cpr);
            person.put(PersonData.NAME, per.name);
            person.put(PersonData.NATIONALITY, per.nationality);
            pasPanel.dataList.addPerson(person);
        }
    }
    
    /**
     * This panel includes the information needed for every reservation, such as
     * the passenger, their phone number, the reservation ID, 
     */
    class ReservationInfoPanel extends JPanel {
        
        ValidatedListPanel resInfoList;
        ValidatedListPanel pasInfoList;
        JTextField resRefLabel;
        
        private String generateBookingReference() {
            Date date = new Date();
            String id = date.hashCode()+"";
            System.out.println("id: "+id);
            StringBuilder sb = new StringBuilder();
            int halfLength = (int)Math.floor(id.length()/2d);
            
            Character[] mchars = new Character[] {
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't'
            };
            Character[] chars = new Character[] {
                'a','b','c','d','e','f','g','h','i','j'
            };
            
            int index = 0;
            if (id.charAt(0) == '-') {
                sb.append(mchars[Integer.parseInt(id.substring(1, 2))]);
                index = 2;
            }
            
            while (index < id.length()) {
                if (index < halfLength) {
                    sb.append(chars[Integer.parseInt(id.substring(index, index+1))]);
                } else {
                    sb.append(id.charAt(index));
                }
                index++;
            }
            
            return sb.toString().toUpperCase();
            //XXXX XX XX XX
            //        
        }
        
        public ReservationInfoPanel(int width, int height) {
            super();
            
            JPanel resPanel = new JPanel(new SpringLayout());
            JPanel pasPanel = new JPanel(new BorderLayout());
            
            JPanel resLabelPanel = new JPanel(new GridBagLayout());
            JLabel resLabel = new JLabel("Reservations-info");
            resLabelPanel.add(resLabel);
            
            JPanel resRefPanel = new JPanel(new SpringLayout());
            JLabel resRefInfoLabel = new JLabel("Booking-reference:");
            resRefLabel = new JTextField(generateBookingReference());
            resRefLabel.setEditable(false);
            
            resRefPanel.add(resRefInfoLabel, BorderLayout.NORTH);
            resRefPanel.add(resRefLabel, BorderLayout.SOUTH);
            resRefPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            SpringUtilities.makeCompactGrid(resRefPanel, 2, 1, 5, 5, 5, 5);
            
            // Make the bottom of the reservation info panel
            int takenHeight = resLabel.getPreferredSize().height + 
                    resRefPanel.getPreferredSize().height + 10; //The 10 is just 2x padding
            
            resInfoList = new ValidatedListPanel(new Validator(), width/2, 
                    height - takenHeight, ReservationData.values());
            
            // Combine the reservation info panel
            
            resPanel.add(resLabelPanel);
            resPanel.add(resRefPanel);
            resPanel.add(resInfoList);
            
            SpringUtilities.makeCompactGrid(resPanel, 3, 1, 5, 5, 5, 5);
            
            // Make the passenger info panel
            JPanel pasLabelPanel = new JPanel(new GridBagLayout());
            JLabel pasLabel = new JLabel("Passager-info");
            pasLabelPanel.add(pasLabel);
            
            pasInfoList = new ValidatedListPanel(new Validator(), width/2, 
                    height - pasLabel.getPreferredSize().height, PersonData.values());
            
            pasPanel.add(pasLabelPanel, BorderLayout.NORTH);
            pasPanel.add(pasInfoList, BorderLayout.SOUTH);
            
            this.add(resPanel, "West");
            this.add(pasPanel, "East");
            this.setMaximumSize(new Dimension(width, height));
        }
        
        /**
         * Returns a HashMap with the value of all fields for this item
         * @return 
         */
        public HashMap<IValidatable, String> getReservationInfo() {
            HashMap<IValidatable, String> data = new HashMap<IValidatable, String>();
            HashMap<IValidatable, String> resData = resInfoList.getData();
            HashMap<IValidatable, String> pasData = pasInfoList.getData();
            
            for (IValidatable key : resData.keySet()) {
                data.put(key, resData.get(key));
            }
            for (IValidatable key : pasData.keySet()) {
                data.put(key, pasData.get(key));
            }
            
            return data;
        }
        
        /**
         * Returns the reservation's booking reference
         * @return 
         */
        public String getBookingReference() {
            return resRefLabel.getText();
        }
        
        /**
         * Returns whether all data entries in this component are valid
         * @return 
         */
        public boolean isDataValid() {
            return (resInfoList.areFieldsValid() && pasInfoList.areFieldsValid());
        }
        
        /**
         * Returns a list of the invalid fields for this component
         * @return 
         */
        public IValidatable[] getInvalidFields() {
            IValidatable[] badResFields = resInfoList.getInvalidFields();
            IValidatable[] badPasFields = pasInfoList.getInvalidFields();
            IValidatable[] badFields = new IValidatable[badResFields.length + badPasFields.length];
            System.arraycopy(badResFields, 0, badFields, 0, badResFields.length);
            System.arraycopy(badPasFields, 0, badFields, badResFields.length, 
                    badResFields.length + badPasFields.length);
            return badFields;
        }
    }
    
    /**
     * This panel includes all information about additional passengers, and
     * a panel to edit and add new passengers
     */
    class AdditionalPassengersPanel extends JPanel {
        
        final private PersonDataList dataList;
        final private ValidatedListPanel editPanel;
        
        public AdditionalPassengersPanel(int width, int height) {
            super(new BorderLayout());
            JPanel pasListPanel = new JPanel(new BorderLayout());
            JPanel pasEditPanel = new JPanel(new BorderLayout());
            int pasEditWidth = (width > 500)? 200: width/3;
            int pasListWidth = (width > 500)? width-200: (width/3)*2;
            
            JPanel infoLabelPanel = new JPanel(new GridBagLayout());
            JLabel infoLabel = new JLabel("Yderligere Passagerer");
            infoLabelPanel.add(infoLabel);
            
            int panelHeight = height-infoLabel.getPreferredSize().height;
            
            dataList = new PersonDataList(new Validator(), 
                    pasListWidth, panelHeight, "Redigér Passager", "Slet Passager");
            pasListPanel.add(dataList);
            
            // Editor panel
            JButton pasEditButton = new JButton("Tilføj Passager");
            
            int pasEditHeight = panelHeight - pasEditButton.getPreferredSize().height;
            editPanel = new ValidatedListPanel(new Validator(), 
                    pasEditWidth, pasEditHeight, PersonData.values());
            
            // Let the button add stuff :)
            pasEditButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    HashMap<IValidatable, String> data = editPanel.getData();
                    HashMap<PersonData, String> person = new HashMap<PersonData, String>();
                    for (IValidatable key : data.keySet()) {
                        if (PersonData.class.isInstance(key)) {
                            person.put((PersonData)key, data.get(key));
                        }
                    }
                    dataList.addPerson(person);
                    editPanel.setData(new HashMap<PersonData, String>());
                }
            });
            
            dataList.setEditor(editPanel);
            pasEditPanel.add(editPanel, BorderLayout.NORTH);
            pasEditPanel.add(pasEditButton, BorderLayout.SOUTH);
            
            this.add(infoLabelPanel, BorderLayout.NORTH);
            this.add(pasListPanel, BorderLayout.WEST);
            this.add(pasEditPanel, BorderLayout.EAST);
        }
        
        public ArrayList<HashMap<PersonData, String>> getPersons() {
            return dataList.getPersons();
        }
    }

    /**
     * This is a panel which includes all the information about a flight.
     * The panel is a part of the ReservationEditor.
     */
    class FlightPanel extends JPanel {
        
        private JTextField flightIDValue;
        private JTextField planeNameValue;
        private JTextField flightDateValue;
        private JTextField flightOriginValue;
        private JTextField flightDepartureTime;
        private JTextField flightDestinationValue;
        private JTextField flightArrivalTime;
        private final ArrayList<ActionListener> listeners;
        private IFlight flight;
        
        public FlightPanel() {
            super(new SpringLayout());
            listeners = new ArrayList<ActionListener>();
            flight = null;
            initComponents();
        }
        
        /**
         * Adds the given ActionListener to this object
         * @param a 
         */
        public void addActionListener(ActionListener a) {
            if (!listeners.contains(a)) {
                listeners.add(a);
            }
        }
        
        /**
         * Returns the chosen flight
         * @return 
         */
        public IFlight getFlight() {
            return flight;
        }
        
        private void loadFlight(IFlight flight) {
            if (flight == null) return;
            
            this.flight = flight;
            // This is the 'action' performed by this component
            for(ActionListener a: listeners) {
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
                    });
            }
            
            Date departure = flight.getDepartureTime();
            Date arrival = flight.getArrivalTime();
            
            flightIDValue.setText(flight.getID());
            planeNameValue.setText(flight.getPlane().name);
            flightDateValue.setText(departure.getDate()+"-"+
                    (departure.getMonth()+1)+" "+
                    departure.getYear());
            flightOriginValue.setText(flight.getOrigin());
            flightDepartureTime.setText(departure.getHours()+":"+departure.getMinutes());
            flightDestinationValue.setText(flight.getDestination());
            flightArrivalTime.setText(arrival.getHours()+":"+arrival.getMinutes());
        }
        
        private void initComponents() {
            JPanel infoPanel = new JPanel(new BorderLayout());
            
            // Assorted info (see the labels for better descriptions)
            JPanel flightIDPanel = new JPanel(new SpringLayout());
            JLabel flightIDLabel = new JLabel("Afgangs-ID:");
            flightIDValue = new JTextField("");
            flightIDValue.setEditable(false);
            JLabel planeNameLabel = new JLabel("Flytype:");
            planeNameValue = new JTextField("");
            planeNameValue.setEditable(false);
            JLabel flightDateLabel = new JLabel("Dato:");
            flightDateValue = new JTextField("");
            flightDateValue.setEditable(false);
            
            flightIDPanel.add(flightIDLabel); // Pack it
            flightIDPanel.add(flightIDValue);
            flightIDPanel.add(planeNameLabel);
            flightIDPanel.add(planeNameValue);
            flightIDPanel.add(flightDateLabel);
            flightIDPanel.add(flightDateValue);
            SpringUtilities.makeCompactGrid(flightIDPanel, 3, 2, 5, 5, 5, 5);
            
            //The info for the flight
            JPanel flightInfoPanel = new JPanel(new SpringLayout());
            JLabel flightOriginLabel = new JLabel("Fra:");
            flightOriginValue = new JTextField("");
            flightOriginValue.setEditable(false);
            flightDepartureTime = new JTextField("");
            flightDepartureTime.setEditable(false);
            
            JLabel flightDestinationLabel = new JLabel("Til:");
            flightDestinationValue = new JTextField("");
            flightDestinationValue.setEditable(false);
            flightArrivalTime = new JTextField("");
            flightArrivalTime.setEditable(false);
            
            flightInfoPanel.add(flightOriginLabel); // Pack it
            flightInfoPanel.add(flightOriginValue);
            flightInfoPanel.add(flightDepartureTime);
            flightInfoPanel.add(flightDestinationLabel);
            flightInfoPanel.add(flightDestinationValue);
            flightInfoPanel.add(flightArrivalTime);
            SpringUtilities.makeCompactGrid(flightIDPanel, 2, 3, 5, 5, 5, 5);
            
            // Create a button to choose a flight
            JButton chooseFlightButton = new JButton("Vælg Afgang");
            chooseFlightButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    loadFlight(FlightChooser.get());
                }
            });
            
            // Combine the panels
            infoPanel.add(flightIDPanel, BorderLayout.NORTH);
            infoPanel.add(flightInfoPanel, BorderLayout.SOUTH);
            
            // Add it to this panel
            this.add(infoPanel, BorderLayout.NORTH);
            
            
        }
        
    }
}
