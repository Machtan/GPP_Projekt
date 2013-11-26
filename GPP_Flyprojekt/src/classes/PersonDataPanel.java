package classes;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The PersonDataPanel class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 22-Nov-2013
 */
public class PersonDataPanel extends JPanel {
    
    /**
     * Constructor for the PersonDataPanel class
     * @param data
     */
    public PersonDataPanel (HashMap<PersonData, String> data) {
        super();
        GridLayout grid = new GridLayout(2, 0);
        this.setLayout(grid);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        for (PersonData d : data.keySet()) {
            JLabel identifier = new JLabel(d+":");
            JLabel value = new JLabel(data.get(d));
            this.add(identifier);
            this.add(value);
        }
    
    }
    
}
