package classes;

import interfaces.IDatabaseHandler.ConnectionError;
import interfaces.IFlight;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Patrick Evers Bjørkman
 */
public class ReservationBrowser extends Browser {

    private Reservation[] reservations;
    private final IFlight flight;
    // Variables declaration - do not modify                     
    private javax.swing.JButton clearSearchButton;
    private javax.swing.JPanel actionsPanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable reservationTable;
    private javax.swing.JButton returnToFlightBrowserButton;
    private javax.swing.JLabel searchArrivalLabel;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel searchDepartureDLabel;
    private javax.swing.JLabel searchBookingIDLabel;
    private javax.swing.JLabel searchMinSeatsLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField searchPassengerNameTextField;
    private javax.swing.JTextField searchBookingIDTextField;
    private javax.swing.JTextField searchTlfTextField;
    private javax.swing.JFormattedTextField searchTotalPassengersTextField;
    private javax.swing.JButton showReservationButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration 

    /**
     * Creates new form FlightBrowser
     */
    public ReservationBrowser(IFlight flight) {
        super();
        this.flight = flight;
        initComponents();
        //Get reservations for the given flight.
        reservations = flight.getReservations();
        //Update the form table to show the reservations.
        updateTable();
        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                /* code run when component hidden*/
            }

            public void componentShown(ComponentEvent e) {
                updateLayout(); // update the layout ;)
            }
        });
    }

    /**
     * Returns the chosen reservation, or null if none is chosen
     *
     * @return The selected reservation
     */
    @Override
    public Reservation getChosen() {
        if (reservationTable.getSelectedRow() > -1) {
            return reservations[reservationTable.getSelectedRow()];
        } else {
            return null;
        }
    }

    /**
     * Updates the browser's layout to reflect changes
     */
    @Override
    public void updateLayout() {
        try {
            reservations = DatabaseHandler.getHandler().getReservations(flight);
        } catch (ConnectionError ce) {
            reservations = (Reservation[]) ce.value;
        }
        updateTable(); //Redraw - ish
        repaint();
    }

    /**
     * Initializes all components and adds them to the form with a fitting
     * layout.
     */
    private void initComponents() {
        setTitle("Vælg en reservation");
        setMinimumSize(new Dimension(1000, 600));
        titleLabel = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        searchBookingIDTextField = new javax.swing.JTextField();
        searchBookingIDLabel = new javax.swing.JLabel();
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

        setTitle("Vælg en reservation");
        setName("flightFrame"); // NOI18N

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        titleLabel.setText("Reservationer");
        titleLabel.setName("titleLabel"); // NOI18N

        searchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Søg"));
        searchPanel.setToolTipText("");
        searchPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        searchPanel.add(searchBookingIDTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 20, 195, 25));

        searchBookingIDLabel.setText("BookingID");
        searchPanel.add(searchBookingIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 20, -1, 25));

        searchButton.setText("Søg");
        searchButton.setPreferredSize(new Dimension(90, 25));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        searchPanel.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(655, 80, -1, 25));
        searchPanel.add(searchPassengerNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 49, 195, 25));

        searchDepartureDLabel.setText("Navn på passager");
        searchPanel.add(searchDepartureDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 49, -1, 25));

        searchArrivalLabel.setText("Telefonnummer");
        searchPanel.add(searchArrivalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, -1, 25));
        searchPanel.add(searchTlfTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 195, 25));

        searchMinSeatsLabel.setText("Antal passagerer");
        searchPanel.add(searchMinSeatsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 78, -1, 25));

        searchTotalPassengersTextField.setText("0");

        searchTotalPassengersTextField.setValue(new Double(0));
        searchTotalPassengersTextField.setColumns(10);
        searchPanel.add(searchTotalPassengersTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 78, 195, 25));

        clearSearchButton.setText("Ryd felter");
        clearSearchButton.setPreferredSize(new Dimension(90, 25));
        clearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSearchButtonActionPerformed(evt);
            }
        });
        searchPanel.add(clearSearchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(655, 50, -1, 25));
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{},
                new String[]{
            "BookingNumber", "Navn på passager", "Antal passagerer", "Telefonnummer"
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
                    if (getChosen() != null) {
                        onActionPerformed();
                    }
                }
            }
        });
        reservationTable.setModel(tableModel);
        reservationTable.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(reservationTable);
        showReservationButton.setPreferredSize(new Dimension(210, 25));
        showReservationButton.setText("Redigér den valgte reservation");
        showReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onActionPerformed();
            }
        });

        actionsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());


        returnToFlightBrowserButton.setText("Tilbage til afgange");
        returnToFlightBrowserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnActionPerformed(evt);
            }
        });
        actionsPanel.add(returnToFlightBrowserButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 25));
        returnToFlightBrowserButton.setPreferredSize(new Dimension(210, 25));
        actionsPanel.add(showReservationButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 25));


        actionsPanel.setPreferredSize(new Dimension(230, 120));


        JPanel buttonPane = new JPanel();



        buttonPane.setLayout(new BorderLayout());
        buttonPane.add(searchPanel, BorderLayout.CENTER);
        buttonPane.add(actionsPanel, BorderLayout.EAST);

        add(jScrollPane2, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);


        pack();
    }
    /**
     * Updates the form table to reflect the correct reservations.
     * This method takes care of search properties as well as adding fitting flights to the table.
     */
    void updateTable() {
        DefaultTableModel model = (DefaultTableModel) reservationTable.getModel();
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }
        for (Reservation reservation : reservations) {
            if ((searchBookingIDTextField.getText() != "" && !reservation.bookingNumber.toLowerCase().contains(searchBookingIDTextField.getText().toLowerCase()))) {
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
            model.addRow(new Object[]{reservation.bookingNumber,
                reservation.passenger.name,
                reservation.seats.size(),
                reservation.tlf});
        }
    }
    /**
     * Invoked method when user clicks the search button.
     */
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

        updateTable();
    }
    /**
     * Invoked method when user clicks the clear search button.
     */
    private void clearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        searchBookingIDTextField.setText("");
        searchPassengerNameTextField.setText("");
        searchTlfTextField.setText("");
        searchTotalPassengersTextField.setText("0");
    }
    /**
     * This method allows external classes to change the text of the show
     * selected reservation button. This is generally used to change from "show
     * selected reservation" to "edit selected reservation".
     */
    @Override
    public void setActionButtonText(String text) {
        showReservationButton.setText(text);
    }
}
