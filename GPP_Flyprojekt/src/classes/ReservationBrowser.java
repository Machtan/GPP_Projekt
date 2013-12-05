package classes;

import interfaces.IFlight;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Patrick Evers Bjørkman
 */
public class ReservationBrowser extends javax.swing.JFrame {

    final Reservation[] reservations;

    /**
     * Creates new form FlightBrowser
     */
    public ReservationBrowser(IFlight flight) {
        initComponents();
        reservations = flight.getReservations();
        UpdateTable();
    }

    private void initComponents() {

        setMinimumSize(new Dimension(875, 600));
        titleLabel = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        searchReservationIDTextField = new javax.swing.JTextField();
        searchFlightIDLabel = new javax.swing.JLabel();
        searchButton = new javax.swing.JButton();
        searchPassengerNameTextField = new javax.swing.JTextField();
        searchDepartureDLabel = new javax.swing.JLabel();
        searchArrivalLabel = new javax.swing.JLabel();
        searchTlfTextField = new javax.swing.JTextField();
        searchMinSeatsLabel = new javax.swing.JLabel();
        searchTotalPassengersTextField = new javax.swing.JFormattedTextField(NumberFormat.getNumberInstance());
        clearSearchButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        reservationTable = new javax.swing.JTable();
        showReservationButton = new javax.swing.JButton();
        actionsPanel = new javax.swing.JPanel();
        actionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));
        returnToFlightBrowserButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Reservation browser");
        setName("flightFrame"); // NOI18N

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        titleLabel.setText("Reservations");
        titleLabel.setName("titleLabel"); // NOI18N

        searchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));
        searchPanel.setToolTipText("");
        searchPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        searchPanel.add(searchReservationIDTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 240, -1));

        searchFlightIDLabel.setText("ReservationID");
        searchPanel.add(searchFlightIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 23, -1, -1));

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        searchPanel.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 80, -1, -1));
        searchPanel.add(searchPassengerNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 240, -1));

        searchDepartureDLabel.setText("Passenger name");
        searchPanel.add(searchDepartureDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 53, -1, -1));

        searchArrivalLabel.setText("Tlf");
        searchPanel.add(searchArrivalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 23, -1, -1));
        searchPanel.add(searchTlfTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 180, -1));

        searchMinSeatsLabel.setText("Total amount of passengers");
        searchPanel.add(searchMinSeatsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 83, -1, -1));

        searchTotalPassengersTextField.setText("0");

        searchTotalPassengersTextField.setValue(new Double(0));
        searchTotalPassengersTextField.setColumns(10);
        searchPanel.add(searchTotalPassengersTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(218, 80, 132, -1));

        clearSearchButton.setText("Clear");
        clearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSearchButtonActionPerformed(evt);
            }
        });
        searchPanel.add(clearSearchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, -1, -1));
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{},
                new String[]{
            "ReservationID", "Passenger name", "Total passengers", "Tlf"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        reservationTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                // your valueChanged overridden method
                if (me.getClickCount() == 2) {
                    if (reservationTable.getSelectedRow() > -1) {
                        //show reservation data
                        dispose();
                    }
                }

            }
        });
        reservationTable.setModel(tableModel);
        reservationTable.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(reservationTable);

        showReservationButton.setText("Edit or Show details on selected reservation");

        actionsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());


        returnToFlightBrowserButton.setText("Return to flightbrowser");
        returnToFlightBrowserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToFlightBrowserButtonActionPerformed(evt);
            }
        });
        actionsPanel.add(returnToFlightBrowserButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));
        searchPanel.setMinimumSize(new Dimension(655, 120));

        actionsPanel.setPreferredSize(new Dimension(185, 120));


        JPanel buttonPane = new JPanel();



        buttonPane.setLayout(new BorderLayout());
        buttonPane.add(searchPanel, BorderLayout.CENTER);
        buttonPane.add(actionsPanel, BorderLayout.EAST);

        add(jScrollPane2, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);


        pack();
    }

    void UpdateTable() {
        DefaultTableModel model = (DefaultTableModel) reservationTable.getModel();
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }
        for (Reservation reservation : reservations) {
            if ((searchReservationIDTextField.getText() != "" && !reservation.reservationID.toLowerCase().contains(searchReservationIDTextField.getText().toLowerCase()))) {
                continue;
            }
            if ((searchPassengerNameTextField.getText() != "" && !reservation.passenger.name.toLowerCase().contains(searchPassengerNameTextField.getText().toLowerCase()))) {
                continue;
            }
            if ((searchTlfTextField.getText() != "" && !reservation.tlf.toString().toLowerCase().contains(searchTlfTextField.getText().toLowerCase()))) {
                continue;
            }

            try {
                if ((Integer) searchTotalPassengersTextField.getValue() != 0 && reservation.seats.size() != (Integer) searchTotalPassengersTextField.getValue()) {
                    continue;
                }
            } catch (Exception ex) {
            }
            model.addRow(new Object[]{reservation.reservationID, reservation.passenger.name, reservation.seats.size(), reservation.tlf});

        }
    }

    private void returnToFlightBrowserButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        FlightBrowser menu = new FlightBrowser();
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
        searchReservationIDTextField.setText("");
        searchPassengerNameTextField.setText("");
        searchTlfTextField.setText("");
        searchTotalPassengersTextField.setText("0");
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton clearSearchButton;
    private javax.swing.JPanel actionsPanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable reservationTable;
    private javax.swing.JButton returnToFlightBrowserButton;
    private javax.swing.JLabel searchArrivalLabel;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel searchDepartureDLabel;
    private javax.swing.JLabel searchFlightIDLabel;
    private javax.swing.JLabel searchMinSeatsLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField searchPassengerNameTextField;
    private javax.swing.JTextField searchReservationIDTextField;
    private javax.swing.JTextField searchTlfTextField;
    private javax.swing.JFormattedTextField searchTotalPassengersTextField;
    private javax.swing.JButton showReservationButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration                  
}
