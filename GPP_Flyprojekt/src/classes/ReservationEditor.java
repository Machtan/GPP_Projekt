package classes;

import interfaces.IValidatable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The ReservationEditor class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 27-Nov-2013
 */
public class ReservationEditor {
    
    AdditionalPassengersPanel pasPanel;
    ReservationInfoPanel resPanel;
    
    /**
     * Constructor for the ReservationEditor class
     */
    public ReservationEditor () {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        resPanel = new ReservationInfoPanel(300, 300);
        frame.add(resPanel, "North");
        pasPanel = new AdditionalPassengersPanel(300, 300);
        frame.add(pasPanel, "South");
        
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * 
     * @param res 
     */
    public ReservationEditor(Reservation res) {
        this();
        for (Person per : res.additionalPassengers) {
            HashMap<PersonData, String> person = new HashMap<PersonData, String>();
            person.put(PersonData.CPR, per.cpr);
            person.put(PersonData.NAME, per.name);
            person.put(PersonData.NATIONALITY, per.nationality);
            pasPanel.
        }
    }
    
    class ReservationInfoPanel extends JPanel {
        
        ValidatedListPanel resInfoList;
        ValidatedListPanel pasInfoList;
        
        public ReservationInfoPanel(int width, int height) {
            super();
            
            JPanel resPanel = new JPanel(new BorderLayout());
            JPanel pasPanel = new JPanel(new BorderLayout());
            
            int labelHeight = new JLabel("Info").getPreferredSize().height;
            
            resInfoList = new ValidatedListPanel(new Validator(), width/2, 
                    height - labelHeight, ReservationData.values());
            
            JPanel resLabelPanel = new JPanel(new GridBagLayout());
            JLabel resLabel = new JLabel("Reservations-info");
            resLabelPanel.add(resLabel);
            
            resPanel.add(resLabelPanel, BorderLayout.NORTH);
            resPanel.add(resInfoList, BorderLayout.SOUTH);
            
            pasInfoList = new ValidatedListPanel(new Validator(), width/2, 
                    height - labelHeight, PersonData.values());
            
            JPanel pasLabelPanel = new JPanel(new GridBagLayout());
            JLabel pasLabel = new JLabel("Passager-info");
            pasLabelPanel.add(pasLabel);
            
            
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
    
    class AdditionalPassengersPanel extends JPanel {
        
        final PersonDataList dataList;
        final ValidatedListPanel editPanel;
        
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
            return dataList.
        }
    }
}
