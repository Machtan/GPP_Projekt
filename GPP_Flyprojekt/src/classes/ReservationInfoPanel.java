package classes;

import external.SpringUtilities;
import interfaces.IValidatable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * This panel includes the information needed for every reservation, such as
 * the passenger, their phone number, the reservation ID, and the booking 
 * reference
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 08-Dec-2013
 */
public class ReservationInfoPanel extends JPanel {

    private ValidatedListPanel resInfoList;
    private ValidatedListPanel pasInfoList;
    private JTextField resRefLabel;

    /**
     * Generates a new booking reference based on the current time
     * @return A String with the new booking reference
     */
    private String generateBookingReference() {
        java.util.Date date = new java.util.Date();
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
    
    /**
     * Creates a new ReservationInfoPanel with the specified width and height 
     * @param width
     * @param height 
     */
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
     * Returns a HashMap with the value of all fields for this item.
     * This includes both the passenger with PersonData keys, and the reservation
     * with ReservationData keys.
     * @return A HashMap of the contained data, except for the booking reference
     * @see getBookingReference
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
     * Sets the information in this panel based on the given HashMap.
     * The HashMap should contain an entry for every possible ReservationData 
     * and PersonData key.
     * @param data 
     */
    public void setReservationInfo(HashMap<IValidatable, String> data) {
        resInfoList.setData(data); // These should sort themselves out :)
        pasInfoList.setData(data);
    }
    
    /**
     * Returns the reservation's booking reference
     * @return 
     */
    public String getBookingReference() {
        return resRefLabel.getText();
    }
    
    /**
     * Sets the booking reference to the given String
     * @param reference The new booking reference to be used
     */
    public void setBookingReference(String reference) {
        resRefLabel.setText(reference);
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
