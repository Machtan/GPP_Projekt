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
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Patrick Evers Bjørkman
 */
public class FlightBrowser extends javax.swing.JFrame {

    final Flight[] flights;

    /**
     * Creates new form FlightBrowser
     */
    public FlightBrowser() {
        initComponents();
        flights = DatabaseHandler.getHandle().getFlights();
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
        searchOriginComboBox.addItem("All");
        for (String item : originsArray) {
            searchOriginComboBox.addItem(item);
        }
        searchDestinationComboBox.removeAllItems();
        searchDestinationComboBox.addItem("All");
        for (String item : destinationsArray) {
            searchDestinationComboBox.addItem(item);
        }
        UpdateTable();
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
            int numberOfFreeSeats = 0;
            while (seats.hasNext()) {
                Point seatPoint = seats.next();
                if (!seating.getSeatFree(seatPoint.x, seatPoint.y)) {
                    numberOfFreeSeats++;
                }
            }
            try {
                if (numberOfFreeSeats < Integer.parseInt(searchMinSeatsTextField.getText())) {
                    continue;
                }
            } catch (Exception ex) {
            }
            model.addRow(new Object[]{flight.id, flight.getOrigin(), flight.getDestination(), flight.getDepartureTime(), flight.getArrivalTime(), numberOfFreeSeats});

        }
    }

    void initComponents() {
        setTitle("Flightbrowser");
        setName("flightFrame"); // NOI18N
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
        actionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));
        returnToMainMenuButton = new javax.swing.JButton();
        searchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));
        searchPanel.setToolTipText("");
        searchPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        searchPanel.add(searchFlightIDTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 18, 264, -1));

        searchFlightIDLabel.setText("FlightID");
        searchPanel.add(searchFlightIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 21, -1, -1));

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        searchPanel.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 78, -1, -1));

        searchOriginLabel.setText("Origin");
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

        searchDepartureDLabel.setText("Departure");
        searchPanel.add(searchDepartureDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 21, -1, -1));

        searchArrivalLabel.setText("Arrival");
        searchPanel.add(searchArrivalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 51, -1, -1));
        searchPanel.add(searchArrivalTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 48, 255, -1));

        searchMinSeatsLabel.setText("Minimum number of free seats");
        searchPanel.add(searchMinSeatsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 82, -1, -1));

        searchMinSeatsTextField.setValue(new Double(0));
        searchMinSeatsTextField.setColumns(10);
        searchPanel.add(searchMinSeatsTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(542, 79, 132, -1));

        clearSearchButton.setText("Clear");
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
            "FlightID", "Origin", "Destination", "Departure", "Arrival", "Free seats"
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
                if (me.getClickCount() == 2) {
                    if (flightTable.getSelectedRow() > -1) {
                        ReservationBrowser reservationBrowser = new ReservationBrowser(flights[flightTable.getSelectedRow()]);
                        reservationBrowser.pack();
                        reservationBrowser.setVisible(true);
                        dispose();
                    }
                }

            }
        });
        flightTable.setModel(tableModel);
        flightTable.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(flightTable);
        showReservationButton.setText("Show reservations for selection");
        showReservationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showReservationButtonActionPerformed(evt);
            }
        });


        actionsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        returnToMainMenuButton.setText("Return to mainmenu");
        returnToMainMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToMainMenuButtonActionPerformed(evt);
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

    private void returnToMainMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        MainMenu menu = new MainMenu();
        menu.pack();
        menu.setVisible(true);
        dispose();
    }

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

        UpdateTable();
    }

    private void clearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        searchFlightIDTextField.setText("");
        searchOriginComboBox.setSelectedIndex(-1);
        searchDestinationComboBox.setSelectedIndex(-1);
        searchDepartureTextField.setText("");
        searchArrivalTextField.setText("");
        searchMinSeatsTextField.setText("0");
    }

    private void searchOriginComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void showReservationButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        ReservationBrowser reservationBrowser = new ReservationBrowser(flights[flightTable.getSelectedRow()]);
        reservationBrowser.pack();
        reservationBrowser.setVisible(true);
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton clearSearchButton;
    private javax.swing.JTable flightTable;
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
}
