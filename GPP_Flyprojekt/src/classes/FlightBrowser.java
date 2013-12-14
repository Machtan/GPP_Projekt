package classes;

import interfaces.IDatabaseHandler.ConnectionError;
import interfaces.ISeating;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;

/**
 * FlightBrowser is the top class for a flight browser, which lets the user
 * browse flights in a table view, search for specific flights and select a
 * specific flight.
 *
 * @author Patrick Evers Bjørkman
 */
public class FlightBrowser extends Browser {

    final Flight[] flights;

    /**
     * Creates new form FlightBrowser
     */
    public FlightBrowser() {
        initComponents();
        Flight[] tmpFlights;
        try {
            tmpFlights = DatabaseHandler.getHandler().getFlights();
        } catch (ConnectionError ce) {
            Utils.showNoConnectionNotice("Afgangene kunne ikke indlæses");
            tmpFlights = (Flight[])ce.value;
        }
        flights = tmpFlights; //To stop making NetBeans glower over above statement
        
        //Update Orgin and destination
        HashSet<String> origins = new HashSet<String>();
        HashSet<String> destinations = new HashSet<String>();
        for (Flight flight : flights) {
            if (!origins.contains(flight.getOrigin())) {
                origins.add(flight.getOrigin());
            }
            if (!destinations.contains(flight.getDestination())) {
                destinations.add(flight.getDestination());
            }
        }
        //Sort
        String[] originsArray = (String[]) origins.toArray(new String[origins.size()]);
        String[] destinationsArray = (String[]) destinations.toArray(new String[destinations.size()]);
        Arrays.sort(originsArray);
        Arrays.sort(destinationsArray);
        searchOriginComboBox.removeAllItems();
        searchOriginComboBox.addItem("Alle");
        for (String item : originsArray) {
            searchOriginComboBox.addItem(item);
        }
        searchDestinationComboBox.removeAllItems();
        searchDestinationComboBox.addItem("Alle");
        for (String item : destinationsArray) {
            searchDestinationComboBox.addItem(item);
        }
        UpdateTable();
    }

    /**
     * Returns the currently selected Flight
     *
     * @return The chosen flight or null if nothing is selected
     */
    public Flight getChosen() {
        if (flightTable.getSelectedRow() > -1) {
            return flights[flightTable.getSelectedRow()];
        } else {
            return null;
        }
    }

    void UpdateTable() {
        DefaultTableModel model = (DefaultTableModel) flightTable.getModel();
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }

        int arrivalHour = searchArrivalTimeHourTextField.GetIntegerValue();
        int arrivalMinute = searchArrivalTimeMinuteTextField.GetIntegerValue();
        int departureHour = searchDepartureTimeHourTextField.GetIntegerValue();
        int departureMinute = searchDepartureTimeMinuteTextField.GetIntegerValue();

        Calendar calendar = Calendar.getInstance();
        for (Flight flight : flights) {
            if ((flight.getID() != "" && !flight.getID().toLowerCase().contains(searchFlightIDTextField.getText()))) {
                continue;
            }
            if ((searchOriginComboBox.getSelectedIndex() != 0 && !flight.getOrigin().contains((String) searchOriginComboBox.getSelectedItem()))) {
                continue;
            }
            if ((searchDestinationComboBox.getSelectedIndex() != 0 && !flight.getDestination().contains((String) searchDestinationComboBox.getSelectedItem()))) {
                continue;
            }
            if (searchDepartureJXDatePicker.getDate() != null) {
                calendar.setTime(searchDepartureJXDatePicker.getDate());
                calendar.set(Calendar.MINUTE, departureMinute);
                calendar.set(Calendar.HOUR_OF_DAY, departureHour);
                if (calendar.getTime().compareTo(flight.getDepartureTime()) > 0) {
                    continue;
                }


            } else if (searchDepartureTimeHourTextField.GetIntegerValue() > 0 || searchDepartureTimeHourTextField.GetIntegerValue() > 0) {
                calendar.setTime(flight.getDepartureTime());
                calendar.set(Calendar.MINUTE, departureMinute);
                calendar.set(Calendar.HOUR_OF_DAY, departureHour);
                if (calendar.getTime().compareTo(flight.getDepartureTime()) > 0) {
                    continue;
                }

            }

            if (searchArrivalJXDatePicker.getDate() != null) {
                calendar.setTime(searchArrivalJXDatePicker.getDate());
                calendar.set(Calendar.MINUTE, arrivalMinute);
                calendar.set(Calendar.HOUR_OF_DAY, arrivalHour);
                if (calendar.getTime().compareTo(flight.getArrivalTime()) < 0) {
                    continue;
                }

            } else if (searchArrivalTimeHourTextField.GetIntegerValue() > 0 || searchArrivalTimeHourTextField.GetIntegerValue() > 0) {
                calendar.setTime(flight.getArrivalTime());
                calendar.set(Calendar.MINUTE, arrivalMinute);
                calendar.set(Calendar.HOUR_OF_DAY, arrivalHour);
                if (calendar.getTime().compareTo(flight.getArrivalTime()) < 0) {
                    continue;
                }

            }

            //Get number of avavlible seats
            ISeating seating = new Seating(flight);
            Iterator<Point> seats = seating.getSeatIterator();
            int numberOfFreeSeats = seating.getNumberOfFreeSeats();
            calendar.setTime(flight.getDepartureTime());
            String departureDate = String.format("%02d-%02d-%04d %02d:%02d", calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR),calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
              
            calendar.setTime(flight.getArrivalTime());
            String arrivalDate =   String.format("%02d-%02d-%04d %02d:%02d", calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR),calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
     
            // Search based on free seats
            if (numberOfFreeSeats < Integer.parseInt(searchMinSeatsTextField.getText())) {
                continue;
            }

            model.addRow(new Object[]{flight.id, flight.getOrigin(),
                flight.getDestination(), departureDate, arrivalDate, numberOfFreeSeats});

        }
    }

    void initComponents() {
        setTitle("Vælg en afgang");
        setName("flightFrame"); // NOI18N
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1045, 600));
        searchPanel = new javax.swing.JPanel();
        searchFlightIDTextField = new javax.swing.JTextField();
        searchFlightIDLabel = new javax.swing.JLabel();
        searchButton = new javax.swing.JButton();
        searchOriginLabel = new javax.swing.JLabel();
        searchOriginComboBox = new javax.swing.JComboBox();
        searchDestinationLabel = new javax.swing.JLabel();
        searchDestinationComboBox = new javax.swing.JComboBox();
        searchDepartureDLabel = new javax.swing.JLabel();
        searchArrivalLabel = new javax.swing.JLabel();
        searchArrivalJXDatePicker = new JXDatePicker();
        searchDepartureJXDatePicker = new JXDatePicker();
        searchArrivalTimeHourTextField = new external.HintTextField("Time");
        searchArrivalTimeMinuteTextField = new external.HintTextField("Minut");
        searchDepartureTimeHourTextField = new external.HintTextField("Time");
        searchDepartureTimeMinuteTextField = new external.HintTextField("Minut");
        searchTimeSplitLetterDepartureLabel = new javax.swing.JLabel();
        searchTimeSplitLetterArrivalLabel = new javax.swing.JLabel();
        searchArrivalTimeLabel = new javax.swing.JLabel();
        searchDepartureTimeLabel = new javax.swing.JLabel();
        searchMinSeatsLabel = new javax.swing.JLabel();
        searchMinSeatsTextField = new javax.swing.JFormattedTextField(NumberFormat.getNumberInstance());
        clearSearchButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        flightTable = new javax.swing.JTable();
        showReservationButton = new javax.swing.JButton();
        actionsPanel = new javax.swing.JPanel();
        actionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Handlinger"));
        returnToMainMenuButton = new javax.swing.JButton();
        searchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Søg"));
        searchPanel.setToolTipText("");
        searchPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        searchPanel.add(searchFlightIDTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 18, 204, 25));

        searchFlightIDLabel.setText("ID");
        searchPanel.add(searchFlightIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 18, -1, 25));

        searchButton.setText("Søg");
        searchButton.setPreferredSize(new Dimension(90, 25));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        searchOriginLabel.setText("Udgangspunkt");
        searchPanel.add(searchOriginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 47, -1, 25));

        searchOriginComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Alle"}));
        searchOriginComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchOriginComboBoxActionPerformed(evt);
            }
        });
        searchPanel.add(searchOriginComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 47, 204, 25));

        searchDestinationLabel.setText("Destination");
        searchPanel.add(searchDestinationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 77, -1, 25));

        searchDestinationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Alle"}));
        searchPanel.add(searchDestinationComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 77, 204, 25));


        searchPanel.add(searchDepartureJXDatePicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 18, 195, 25));


        searchDepartureDLabel.setText("Afgangs dato");
        searchPanel.add(searchDepartureDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 18, -1, 25));

        searchDepartureTimeLabel.setText("Afgangs tidspunkt");
        searchPanel.add(searchDepartureTimeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 48, -1, 25));
        searchPanel.add(searchDepartureTimeHourTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 48, 70, 25));
        searchTimeSplitLetterDepartureLabel.setText(":");
        searchPanel.add(searchTimeSplitLetterDepartureLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(536, 48, -1, 25));
        searchPanel.add(searchDepartureTimeMinuteTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(565, 48, 70, 25));

        searchArrivalLabel.setText("Ankomst dato");
        searchPanel.add(searchArrivalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 77, -1, 25));
        searchPanel.add(searchArrivalJXDatePicker, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 77, 195, 25));
        searchArrivalTimeLabel.setText("Ankomst tidspunkt");
        searchPanel.add(searchArrivalTimeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 107, -1, 25));
        searchPanel.add(searchArrivalTimeHourTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 107, 70, 25));
        searchTimeSplitLetterArrivalLabel.setText(":");
        searchPanel.add(searchTimeSplitLetterArrivalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(536, 107, -1, 25));
        searchPanel.add(searchArrivalTimeMinuteTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(565, 107, 70, 25));

        searchMinSeatsLabel.setText("Antal fri sæder");
        searchPanel.add(searchMinSeatsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 136, -1, 25));

        searchMinSeatsTextField.setValue(new Double(0));
        searchMinSeatsTextField.setColumns(10);
        searchPanel.add(searchMinSeatsTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 136, 195, 25));

        clearSearchButton.setText("Ryd felter");
        clearSearchButton.setPreferredSize(new Dimension(90, 25));
        clearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSearchButtonActionPerformed(evt);
            }
        });
        searchPanel.add(clearSearchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 107, -1, 25));
        searchPanel.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 136, -1, 25));

        actionsPanel.setPreferredSize(new Dimension(283, 175));
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{},
                new String[]{
            "ID", "Udgangspunkt", "Destination", "Afgang", "Ankomst", "Antal frie sæder"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        flightTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                // your valueChanged overridden method
                if ((me.getClickCount() == 2) && (getChosen() != null)) {
                    onActionPerformed();
                }
            }
        });
        flightTable.setModel(tableModel);
        flightTable.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(flightTable);
        showReservationButton.setText("Vis reservationer på afgangen");
        showReservationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (flightTable.getSelectedRow() > -1) {
                    activateActionPerformed(evt);
                }
            }
        });

        actionsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        returnToMainMenuButton.setText("Tilbage til hovedmenuen");
        returnToMainMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnActionPerformed(evt);
            }
        });
        actionsPanel.add(showReservationButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 107, 260, 25));
        actionsPanel.add(returnToMainMenuButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 136, 260, 25));



        JPanel buttonPane = new JPanel();



        buttonPane.setLayout(new BorderLayout());
        buttonPane.add(searchPanel, BorderLayout.CENTER);
        buttonPane.add(actionsPanel, BorderLayout.EAST);

        add(jScrollPane2, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);
        pack();
    }

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        UpdateTable();
    }

    private void clearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        searchFlightIDTextField.setText("");
        searchOriginComboBox.setSelectedIndex(0);
        searchDestinationComboBox.setSelectedIndex(0);
        searchDepartureJXDatePicker.setDate(null);
        searchArrivalJXDatePicker.setDate(null);
        searchMinSeatsTextField.setText("0");
        searchArrivalTimeHourTextField.setText(searchArrivalTimeHourTextField.hint);
        searchArrivalTimeMinuteTextField.setText(searchArrivalTimeMinuteTextField.hint);
        searchDepartureTimeHourTextField.setText(searchDepartureTimeHourTextField.hint);
        searchDepartureTimeMinuteTextField.setText(searchDepartureTimeMinuteTextField.hint);
        UpdateTable();
    }

    private void searchOriginComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton clearSearchButton;
    protected javax.swing.JTable flightTable; //Hahahahahahahaha
    private javax.swing.JPanel actionsPanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton returnToMainMenuButton;
    private javax.swing.JLabel searchArrivalLabel;
    private JXDatePicker searchArrivalJXDatePicker;
    private JXDatePicker searchDepartureJXDatePicker;
    private javax.swing.JLabel searchArrivalTimeLabel;
    private javax.swing.JLabel searchDepartureTimeLabel;
    private javax.swing.JLabel searchTimeSplitLetterDepartureLabel;
    private javax.swing.JLabel searchTimeSplitLetterArrivalLabel;
    private external.HintTextField searchArrivalTimeHourTextField;
    private external.HintTextField searchArrivalTimeMinuteTextField;
    private external.HintTextField searchDepartureTimeHourTextField;
    private external.HintTextField searchDepartureTimeMinuteTextField;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel searchDepartureDLabel;
    private javax.swing.JComboBox searchDestinationComboBox;
    private javax.swing.JLabel searchDestinationLabel;
    private javax.swing.JLabel searchFlightIDLabel;
    private javax.swing.JTextField searchFlightIDTextField;
    private javax.swing.JLabel searchMinSeatsLabel;
    private javax.swing.JFormattedTextField searchMinSeatsTextField;
    private javax.swing.JComboBox searchOriginComboBox;
    private javax.swing.JLabel searchOriginLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JButton showReservationButton;
    // End of variables declaration           

    @Override
    public void updateLayout() {
        UpdateTable();
    }

    @Override
    public void setActionButtonText(String text) {
        showReservationButton.setText(text);
    }
}
