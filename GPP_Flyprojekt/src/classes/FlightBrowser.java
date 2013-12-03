/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import interfaces.ISeating;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author patr0805
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
            if (!origins.contains(flight.getOrigin()))
            {
                origins.add(flight.getOrigin());
            }
            if (!destinations.contains(flight.getDestination()))
            {
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
        for (String item : originsArray)
        {
           searchOriginComboBox.addItem(item);
        }
        searchDestinationComboBox.removeAllItems();
        searchDestinationComboBox.addItem("All");
        for (String item : destinationsArray)
             {
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
            if ((flight.getID() != "" && !flight.getID().contains(searchFlightIDTextField.getText()))) {
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
                if (!seating.getSeatFree(seatPoint.x,seatPoint.y)) {
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

    void initComponents2()
    {
        setTitle("Flightbrowser");
        setName("flightFrame"); // NOI18N
        titleLabel = new javax.swing.JLabel();
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
        searchMinSeatsTextField = new javax.swing.JTextField();
        clearSearchButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        flightTable = new javax.swing.JTable();
        showReservationButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        returnToMainMenuButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
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

        searchOriginComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All" }));
        searchOriginComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchOriginComboBoxActionPerformed(evt);
            }
        });
        searchPanel.add(searchOriginComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 47, 264, -1));

        searchDestinationLabel.setText("Destination");
        searchPanel.add(searchDestinationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 82, -1, -1));

        searchDestinationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All" }));
        searchPanel.add(searchDestinationComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 78, 264, -1));
        searchPanel.add(searchDepartureTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 18, 255, -1));

        searchDepartureDLabel.setText("Departure");
        searchPanel.add(searchDepartureDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 21, -1, -1));

        searchArrivalLabel.setText("Arrival");
        searchPanel.add(searchArrivalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 51, -1, -1));
        searchPanel.add(searchArrivalTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 48, 255, -1));

        searchMinSeatsLabel.setText("Minimum number of free seats");
        searchPanel.add(searchMinSeatsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 82, -1, -1));

        searchMinSeatsTextField.setText("0");
        searchPanel.add(searchMinSeatsTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(542, 79, 132, -1));

        clearSearchButton.setText("Clear");
        clearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSearchButtonActionPerformed(evt);
            }
        });
        searchPanel.add(clearSearchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 45, -1, -1));

        flightTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "FlightID", "Origin", "Destination", "Departure", "Arrival", "Free seats"
            }
        ));
        flightTable.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(flightTable);

        showReservationButton.setText("Show reservations for selection");
        showReservationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showReservationButtonActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        returnToMainMenuButton.setText("Return to mainmenu");
        returnToMainMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToMainMenuButtonActionPerformed(evt);
            }
        });
        jPanel2.add(returnToMainMenuButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 93, -1, -1));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, -30, -1, -1));
        
    
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
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
        searchMinSeatsTextField = new javax.swing.JTextField();
        clearSearchButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        flightTable = new javax.swing.JTable();
        showReservationButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        returnToMainMenuButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flightbrowser");
        setName("flightFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(900, 499));

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        titleLabel.setText("Flights");
        titleLabel.setName("titleLabel"); // NOI18N

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

        searchOriginComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All" }));
        searchOriginComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchOriginComboBoxActionPerformed(evt);
            }
        });
        searchPanel.add(searchOriginComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 47, 264, -1));

        searchDestinationLabel.setText("Destination");
        searchPanel.add(searchDestinationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 82, -1, -1));

        searchDestinationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All" }));
        searchPanel.add(searchDestinationComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 78, 264, -1));
        searchPanel.add(searchDepartureTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 18, 255, -1));

        searchDepartureDLabel.setText("Departure");
        searchPanel.add(searchDepartureDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 21, -1, -1));

        searchArrivalLabel.setText("Arrival");
        searchPanel.add(searchArrivalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 51, -1, -1));
        searchPanel.add(searchArrivalTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 48, 255, -1));

        searchMinSeatsLabel.setText("Minimum number of free seats");
        searchPanel.add(searchMinSeatsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(358, 82, -1, -1));

        searchMinSeatsTextField.setText("0");
        searchPanel.add(searchMinSeatsTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(542, 79, 132, -1));

        clearSearchButton.setText("Clear");
        clearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSearchButtonActionPerformed(evt);
            }
        });
        searchPanel.add(clearSearchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 45, -1, -1));

        flightTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "FlightID", "Origin", "Destination", "Departure", "Arrival", "Free seats"
            }
        ));
        flightTable.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(flightTable);

        showReservationButton.setText("Show reservations for selection");
        showReservationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showReservationButtonActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        returnToMainMenuButton.setText("Return to mainmenu");
        returnToMainMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToMainMenuButtonActionPerformed(evt);
            }
        });
        jPanel2.add(returnToMainMenuButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 93, -1, -1));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, -30, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 777, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 949, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showReservationButton)
                    .addComponent(titleLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showReservationButton)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void returnToMainMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnToMainMenuButtonActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_returnToMainMenuButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:

        UpdateTable();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void clearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSearchButtonActionPerformed
        // TODO add your handling code here:
        searchFlightIDTextField.setText("");
        searchOriginComboBox.setSelectedIndex(-1);
        searchDestinationComboBox.setSelectedIndex(-1);
        searchDepartureTextField.setText("");
        searchArrivalTextField.setText("");
        searchMinSeatsTextField.setText("0");
    }//GEN-LAST:event_clearSearchButtonActionPerformed

    private void searchOriginComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchOriginComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchOriginComboBoxActionPerformed

    private void showReservationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showReservationButtonActionPerformed
        // TODO add your handling code here:
        ReservationBrowser reservationBrowser = new ReservationBrowser(flights[flightTable.getSelectedRow()]);        
        reservationBrowser.pack();
        reservationBrowser.setVisible(true);
    }//GEN-LAST:event_showReservationButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearSearchButton;
    private javax.swing.JTable flightTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
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
    private javax.swing.JTextField searchMinSeatsTextField;
    private javax.swing.JComboBox searchOriginComboBox;
    private javax.swing.JLabel searchOriginLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JButton showReservationButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
