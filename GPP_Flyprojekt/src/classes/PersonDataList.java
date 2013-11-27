package classes;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import classes.Utils.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * The PersonDataList class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 27-Nov-2013
 */
public class PersonDataList extends JPanel {
    
    private ArrayList<HashMap<PersonData, String>> persons;
    GridLayout grid;
    
    /**
     * Constructor for the PersonDataList class
     */
    public PersonDataList () {
        super();
        persons = new ArrayList<HashMap<PersonData, String>>();
        grid = new GridLayout(0, 1);
        updateLayout();
        this.setVisible(true);
    }
    
    /**
     * This should return whether the data in the given hashmap is valid
     * @return 
     */
    private boolean verifyPerson(HashMap<PersonData, String> person) {
        return true;
    }
    
    /**
     * Updates the layout for the list
     */
    public void updateLayout() {
        System.out.println(String.format("Updating Layout. %s persons present", persons.size()));
        this.removeAll(); // Reset
        
        JPanel numberColumn = new JPanel(); // Init
        JPanel nameColumn = new JPanel();
        JPanel statusColumn = new JPanel();
        JPanel editColumn = new JPanel();
        JPanel deleteColumn = new JPanel();
        
        numberColumn.setLayout(grid); // Set layout
        nameColumn.setLayout(grid);
        statusColumn.setLayout(grid);
        editColumn.setLayout(grid);
        deleteColumn.setLayout(grid);
        
        JLabel statusLabel;
        for (int i = 0; i < persons.size(); i++) {
            HashMap<PersonData, String> person = persons.get(i);
            
            numberColumn.add(new JLabel(""+(i+1)));
            nameColumn.add(new JLabel(person.get(PersonData.NAME)));
            
            boolean verified = verifyPerson(person);
            if (verified) {
                ImageIcon icon = Utils.getIcon("images/okaystatus.png");
                statusLabel = new JLabel(icon);
                statusLabel.setToolTipText("All fields verified!");
            } else {
                ImageIcon icon = Utils.getIcon("images/notokaystatus.png");
                statusLabel = new JLabel(icon);
                statusLabel.setToolTipText("Some fields are invalid!");
            }
            statusColumn.add(statusLabel);
            
            final int j = i;
            JButton editButton = new JButton(Utils.getIcon("images/editicon.png"));
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editPerson(j);
                }
            });
            editColumn.add(editButton);
            
            JButton deleteButton = new JButton(Utils.getIcon("images/deleteicon.png"));
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deletePerson(j);
                }
            });
            deleteColumn.add(deleteButton);
        }
        
        this.add(numberColumn, "West");
        this.add(nameColumn, "West");
        this.add(statusColumn, "West");
        this.add(editColumn, "West");
        this.add(deleteColumn, "West");
        
        this.repaint();
        if (this.getParent() != null) {
            System.out.println("Preferred size: "+this.getParent().getPreferredSize());
            System.out.println("Actual size:    "+this.getParent().getSize());
            //this.getParent().setSize(this.getParent().getSize()); //notworking
            System.out.println("new size:    "+this.getParent().getPreferredSize());
            this.getParent().getParent().validate();
            System.out.println("newer size:    "+this.getParent().getSize());
            //this.get
        }
    }
    
    /**
     * Deletes the entry for the person at the given index and updates the 
     * layout
     * @param index 
     */
    public void deletePerson(int index) {
        System.out.println("Removing "+persons.get(index).get(PersonData.NAME));
        persons.remove(index);
        updateLayout();
    }
    
    /**
     * Opens the editor for the person on the given index in the 'persons' 
     * arrayList
     * @param index 
     */
    public void editPerson(int index) {
        System.out.println("Editing "+persons.get(index).get(PersonData.NAME));
        //openPersonForEditing(index, persons.get(index));
        throw new UnsupportedOperationException("editPerson not yet implemented");
    }
    
    /**
     * Changes the persondata of the given index to the given person and updates
     * the layout
     * @param index
     * @param person 
     */
    public void changePersonData(int index, HashMap<PersonData, String> person) {
        persons.remove(index);
        persons.add(index, person);
        updateLayout();
    } 
    
    /**
     * Adds a single person to the list and updates the layout
     * @param person The person
     */
    public void addPerson(HashMap<PersonData, String> person) {
        persons.add(person);
        updateLayout();
    }
    
    /**
     * Adds a list of persons to the list and updates the layout
     * @param persons 
     */
    public void addPersons(ArrayList<HashMap<PersonData, String>> persons) {
        this.persons.addAll(persons);
        updateLayout();
    }   
}
