package classes;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Set;
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
        GridLayout grid = new GridLayout(0, 2);
        this.setLayout(grid);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        for (PersonData field : PersonData.values()) {
            System.out.println("pd: "+field);
            if (!data.keySet().contains(field)) {
                continue;
            }
            JLabel identifier = new JLabel(field+":");
            JLabel value = new JLabel(data.get(field));
            this.add(identifier);
            this.add(value);
        }
    }
}
