package classes;

import interfaces.IReservationEditor;
import interfaces.IValidatable;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
public class ReservationEditor implements IReservationEditor {
    
    /**
     * Constructor for the ReservationEditor class
     */
    public ReservationEditor () {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        frame.add(new ReservationInfoPanel(300, 400), "North");
        
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void open() {
        
    }

    @Override
    public void open(Reservation res) {
        
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
            resPanel.add(new JLabel("Reservations-info"), BorderLayout.NORTH);
            resPanel.add(resInfoList, BorderLayout.SOUTH);
            
            pasInfoList = new ValidatedListPanel(new Validator(), width/2, 
                    height - labelHeight, PersonData.values());
            pasPanel.add(new JLabel("Passager-info"), BorderLayout.NORTH);
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
        
        public AdditionalPassengersPanel(int width, int height) {
            super(new BorderLayout());
            JPanel pasListPanel = new JPanel();
            JPanel pasEditPanel = new JPanel();
            
            JLabel infoLabel = new JLabel("Yderligere Passagerer");
            int panelHeight = height-infoLabel.getPreferredSize().height;
            
            PersonDataList dataList = new PersonDataList(new Validator(), width/2, panelHeight, 
                    "Redigér Passager", "Slet Passager");
            pasListPanel.add(dataList);
            
            JButton pasEditButton = new JButton("Tilføj Passager");
            int pasEditHeight = panelHeight - pasEditButton.getPreferredSize().height;
            ValidatedListPanel editPanel = new ValidatedListPanel(new Validator(), 
                    width/2, pasEditHeight, PersonData.values());
            
            this.add(infoLabel, BorderLayout.NORTH);
            this.add(pasListPanel, BorderLayout.WEST);
            this.add(pasEditPanel, BorderLayout.EAST);
            
            
        }
    }
}
