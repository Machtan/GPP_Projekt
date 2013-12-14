package classes;

import external.SpringUtilities;
import interfaces.IFlight;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This is a panel which includes all the information about a flight.
 * The panel is a part of the ReservationEditor.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 08-Dec-2013
 */
public class FlightPanel extends JPanel {

    private JTextField flightIDValue;
    private JTextField planeNameValue;
    private JTextField flightDateValue;
    private JTextField flightOriginValue;
    private JTextField flightDepartureTime;
    private JTextField flightDestinationValue;
    private JTextField flightArrivalTime;
    private final ArrayList<ActionListener> listeners;
    private IFlight flight;
    
    private String[] monthName = new String[] {
        "januar", "februar", "marts",     "april",   "maj",      "juni",
        "juli",   "august",  "september", "oktober", "november", "december"
    };
    
    private final int minWidth = 100;
    private final Dimension minFieldSize = new Dimension(minWidth, 0);

    public FlightPanel() {
        super(new SpringLayout());
        this.setPreferredSize(new Dimension(300,220));
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

    public void loadFlight(IFlight flight) {
        if (flight == null) return;

        this.flight = flight;
        // This is the 'action' performed by this component
        for(ActionListener a: listeners) {
            a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
                });
        }
        
        Date departure = flight.getDepartureTime();
        Date arrival = flight.getArrivalTime();
        
        Calendar dcal = Calendar.getInstance();
        dcal.setTime(departure);
        
        String departTime = String.format("%02d:%02d", 
                dcal.get(Calendar.HOUR_OF_DAY), 
                dcal.get(Calendar.MINUTE));
        
        String date = String.format("%02d. %s %04d", 
                dcal.get(Calendar.DAY_OF_MONTH),
                monthName[dcal.get(Calendar.MONTH)],
                dcal.get(Calendar.YEAR));
        
        Calendar acal = Calendar.getInstance();
        acal.setTime(arrival);
        String arriveTime = String.format("%02d:%02d", 
                acal.get(Calendar.HOUR_OF_DAY), 
                acal.get(Calendar.MINUTE));
        
        flightIDValue.setText(flight.getID());
        planeNameValue.setText(flight.getPlane().name);
        flightDateValue.setText(date);
        flightOriginValue.setText(flight.getOrigin());
        flightDepartureTime.setText(departTime);
        flightDestinationValue.setText(flight.getDestination());
        flightArrivalTime.setText(arriveTime);
    }

    private void initComponents() {
        JPanel infoPanel = new JPanel(new BorderLayout());

        // Assorted info (see the labels for better descriptions)
        JPanel flightIDPanel = new JPanel(new SpringLayout());
        JLabel flightIDLabel = new JLabel("Afgangs-ID:");
        flightIDValue = new JTextField("");
        flightIDValue.setEditable(false);
        flightIDValue.setMinimumSize(minFieldSize);
        JLabel planeNameLabel = new JLabel("Flytype:");
        planeNameValue = new JTextField("");
        planeNameValue.setEditable(false);
        planeNameValue.setMinimumSize(minFieldSize);
        JLabel flightDateLabel = new JLabel("Dato:");
        flightDateValue = new JTextField("");
        flightDateValue.setEditable(false);
        flightDateValue.setMinimumSize(minFieldSize);

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
        flightOriginValue.setPreferredSize(new Dimension(minWidth-5-
                flightOriginLabel.getPreferredSize().width, 
                flightOriginValue.getPreferredSize().height));
        flightOriginValue.setEditable(false);
        flightDepartureTime = new JTextField("00:00");
        flightDepartureTime.setEditable(false);

        JLabel flightDestinationLabel = new JLabel("Til:");
        flightDestinationValue = new JTextField("");
        flightDestinationValue.setPreferredSize(new Dimension(minWidth-5-
                flightDestinationLabel.getPreferredSize().width, 
                flightDestinationValue.getPreferredSize().height));
        flightDestinationValue.setEditable(false);
        flightArrivalTime = new JTextField("00:00");
        flightArrivalTime.setEditable(false);

        flightInfoPanel.add(flightOriginLabel); // Pack it
        flightInfoPanel.add(flightOriginValue);
        flightInfoPanel.add(flightDepartureTime);
        flightInfoPanel.add(flightDestinationLabel);
        flightInfoPanel.add(flightDestinationValue);
        flightInfoPanel.add(flightArrivalTime);
        SpringUtilities.makeCompactGrid(flightInfoPanel, 2, 3, 5, 5, 5, 5);

        // Create a button to choose a flight
        JButton chooseFlightButton = new JButton("Vælg Afgang");
        final FlightPanel fpanel = this;
        chooseFlightButton.addActionListener(new ActionListener() {
        
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFrame editor = (JFrame) SwingUtilities.getWindowAncestor(fpanel);
                final FlightChooser chooser = new FlightChooser();
                chooser.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fpanel.loadFlight(chooser.getChosen());
                        chooser.dispose();
                        editor.setVisible(true);
                    }
                });
                Utils.transition(editor, chooser);
            }
        });

        // Combine the panels
        infoPanel.add(flightIDPanel, BorderLayout.NORTH);
        infoPanel.add(flightInfoPanel, BorderLayout.SOUTH);

        // Add it to this panel
        this.add(infoPanel);
        this.add(chooseFlightButton);
        SpringUtilities.makeCompactGrid(this, 2, 1, 5, 5, 5, 5);
        
        //Dimension pidSize = flightIDPanel.getPreferredSize();
        //Dimension pinSize = flightInfoPanel.getPreferredSize();
        //System.out.println("Preferred size: "+this.getPreferredSize());
        //System.out.println("fid size: "+flightIDPanel.getPreferredSize());
        //System.out.println("fin size: "+flightInfoPanel.getPreferredSize());
    }
}