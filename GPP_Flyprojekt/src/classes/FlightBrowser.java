package classes;

import interfaces.ISeating;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 * FlightBrowser is the top class for a flight browser, which lets the user 
 * browse flights in a table view, search for specific flights and select a 
 * specific flight.
 * @author Patrick Evers Bjørkman
 */
public class FlightBrowser extends Browser {

    final Flight[] flights;

    /**
     * Creates new form FlightBrowser
     */
    public FlightBrowser() {
        initComponents();
        flights = DatabaseHandler.getHandler().getFlights();
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
            if ((searchDepartureTextField.getText() != "" && !flight.getDepartureTime().toString().contains(searchDepartureTextField.getText()))) {
                continue;
            }
            if ((searchArrivalTextField.getText() != "" && !flight.getArrivalTime().toString().contains(searchArrivalTextField.getText()))) {
                continue;
            }

            //Get number of avavlible seats
            ISeating seating = new Seating(flight);
            Iterator<Point> seats = seating.getSeatIterator();
            int numberOfFreeSeats = seating.getNumberOfFreeSeats();
            
            String departureTime = flight.getDepartureTime().toString();
            String arrivalTime = flight.getArrivalTime().toString();
            
            model.addRow(new Object[]{flight.id, flight.getOrigin(), 
                flight.getDestination(), departureTime, arrivalTime, numberOfFreeSeats});

        }
    }

    void initComponents() {
        setTitle("Vælg en afgang");
        setName("flightFrame"); // NOI18N
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(950, 600));
        searchPanel = new javax.swing.JPanel();
        searchFlightIDTextField = new javax.swing.JTextField();
        searchFlightIDLabel = new javax.swing.JLabel();
        searchButton = new javax.swing.JButton();
        searchOriginLabel = new javax.swing.JLabel();
        searchOriginComboBox = new javax.swing.JComboBox();
        searchDestinationLabel = new javax.swing.JLabel();
        searchDestinationComboBox = new javax.swing.JComboBox();
        searchDepartureTextField = new javax.swing.JTextField();
        searchDepartureDLabel = new javax.swing.JLabel();
        searchArrivalLabel = new javax.swing.JLabel();
        searchArrivalTextField = new javax.swing.JTextField();
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
        searchPanel.add(searchFlightIDTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 18, 264, -1));

        searchFlightIDLabel.setText("ID");
        searchPanel.add(searchFlightIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 21, -1, -1));

        searchButton.setText("Søg");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        searchPanel.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 78, -1, -1));

        searchOriginLabel.setText("Udgangspunkt");
        searchPanel.add(searchOriginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 51, -1, -1));

        searchOriginComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"All"}));
        searchOriginComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchOriginComboBoxActionPerformed(evt);
            }
        });
        searchPanel.add(searchOriginComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 47, 264, -1));

        searchDestinationLabel.setText("Destination");
        searchPanel.add(searchDestinationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 82, -1, -1));

        searchDestinationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"All"}));
        searchPanel.add(searchDestinationComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 78, 264, -1));
        searchPanel.add(searchDepartureTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 18, 255, -1));

        searchDepartureDLabel.setText("Afgang");
        searchPanel.add(searchDepartureDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 21, -1, -1));

        searchArrivalLabel.setText("Ankomst");
        searchPanel.add(searchArrivalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 51, -1, -1));
        searchPanel.add(searchArrivalTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 48, 255, -1));

        searchMinSeatsLabel.setText("Antal fri sæder");
        searchPanel.add(searchMinSeatsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 82, -1, -1));

        searchMinSeatsTextField.setValue(new Double(0));
        searchMinSeatsTextField.setColumns(10);
        searchPanel.add(searchMinSeatsTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(542, 79, 132, -1));

        clearSearchButton.setText("Ryd felter");
        clearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSearchButtonActionPerformed(evt);
            }
        });
        searchPanel.add(clearSearchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 45, -1, -1));
        searchPanel.setMinimumSize(new Dimension(775, 120));

        actionsPanel.setPreferredSize(new Dimension(165, 120));
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
        showReservationButton.setText("Vis reservationer for den valgte afgang");
        showReservationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activateActionPerformed(evt);
            }
        });

        actionsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        returnToMainMenuButton.setText("Tilbage til hovedmenuen");
        returnToMainMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnActionPerformed(evt);
            }
        });
        actionsPanel.add(returnToMainMenuButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 78, -1, -1));



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
        searchFlightIDTextField.setText("All");
        searchOriginComboBox.setSelectedIndex(-1);
        searchDestinationComboBox.setSelectedIndex(-1);
        searchDepartureTextField.setText("");
        searchArrivalTextField.setText("");
        searchMinSeatsTextField.setText("0");
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
    private javax.swing.JTextField searchArrivalTextField;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel searchDepartureDLabel;
    private javax.swing.JTextField searchDepartureTextField;
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
}
