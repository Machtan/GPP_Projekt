package classes;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import classes.Utils.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import javax.swing.SpringLayout;
import external.SpringUtilities;
import interfaces.IPersonDataList;
import interfaces.IValidatedList;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The PersonDataList class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 27-Nov-2013
 */
public class PersonDataList extends JScrollPane implements IPersonDataList {
    
    private int width;
    private int height;
    private String editButtonTip;
    private String deleteButtonTip;
    
    private ArrayList<HashMap<PersonData, String>> persons;
    private JPanel panel;
    
    private ArrayList<ArrayList<Component>> comps;
    
    private IValidatedList editor;
    private SpringLayout layout;
    
    /**
     * Constructor for the PersonDataList class
     */
    public PersonDataList (int width, int height, String editButtonTip, 
            String deleteButtonTip) {
        super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        panel = new JPanel();
        
        this.width = width;
        this.height = height;
        this.editButtonTip = editButtonTip;
        this.deleteButtonTip = deleteButtonTip;
        
        this.setViewportView(panel);
        this.setPreferredSize(new Dimension(width, height));
        
        persons = new ArrayList<HashMap<PersonData, String>>();
        comps = new ArrayList<ArrayList<Component>>();
        
        layout = new SpringLayout();
        panel.setLayout(layout);
        
        updateLayout();
        this.setVisible(true);
    }
    
    /**
     * Returns whether all fields on the given person are valid
     * @return 
     */
    private boolean verifyPerson(HashMap<PersonData, String> person) {
        for (PersonData field : person.keySet()) {
            if (!new Validator().validate(field, person.get(field))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Updates the layout for the list
     */
    private void updateLayout() {
        SpringUtilities.makeCompactGrid(panel, persons.size(), 5, 5, 5, 5, 5);
        this.validate();
        this.repaint();
    }
    
    @Override
    public void addPerson(final HashMap<PersonData, String> person) {
        persons.add(person);
        
        ArrayList<Component> lineComps = new ArrayList<Component>();
        
        JLabel lineLabel = new JLabel(""+persons.size());
        JLabel nameLabel = new JLabel(person.get(PersonData.NAME));

        boolean verified = verifyPerson(person);
        
        JLabel statusLabel;
        if (verified) {
            ImageIcon icon = Utils.getIcon("images/okaystatus.png");
            statusLabel = new JLabel(icon);
            statusLabel.setToolTipText("All fields verified!");
        } else {
            ImageIcon icon = Utils.getIcon("images/notokaystatus.png");
            statusLabel = new JLabel(icon);
            statusLabel.setToolTipText("Some fields are invalid!");
        }

        JButton editButton = new JButton(Utils.getIcon("images/editicon.png"));
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editPerson(person);
            }
        });
        editButton.setToolTipText(editButtonTip);
        editButton.setFocusable(false);

        JButton deleteButton = new JButton(Utils.getIcon("images/deleteicon.png"));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePerson(person);
            }
        });
        deleteButton.setToolTipText(deleteButtonTip);
        deleteButton.setFocusable(false);
        
        lineComps.add(lineLabel);
        lineComps.add(nameLabel);
        lineComps.add(statusLabel);
        lineComps.add(editButton);
        lineComps.add(deleteButton);
        for (Component comp : lineComps) {
            panel.add(comp);
        }
        comps.add(lineComps);
        
        updateLayout();
    }
    
    /**
     * Deletes the entry for the person and updates the layout
     * @param person 
     */
    public void deletePerson(HashMap<PersonData, String> person) {
        System.out.println("Removing "+person.get(PersonData.NAME));
        
        ArrayList<Component> lineComps = comps.remove(persons.indexOf(person));
        persons.remove(person);
        for (Component comp : lineComps) {
            panel.remove(comp);
        }
        
        // Update the line number labels
        for (int i = 0; i < comps.size(); i++) {
            ((JLabel)comps.get(i).get(0)).setText(""+(i+1));
        }
        
        updateLayout();
    }
    
    /**
     * Opens the editor for the given person
     * @param person
     */
    public void editPerson(HashMap<PersonData, String> person) {
        System.out.println("Editing "+person.get(PersonData.NAME));
        //openPersonForEditing(index, persons.get(index));
        if (this.editor == null) {
            System.err.println("NO EDITOR SET FOR THE PERSONDATALIST!");
        } else {
            if (! editor.isEmpty()) {
                Object[] options = {"Ja", "Nej"};
                int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Der er allerede data under rettelse.\nOverskriv og fortsæt?",
                    "Advarsel",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
                switch(n) {
                    case 0:
                        break; //Yes => delete and send to the editor
                    case 1:
                        return; //No => just exit :)
                }
            }
            // Move the person from the list to the editor for editing
            this.deletePerson(person); 
            this.editor.setData(person);
            
        }
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
     * Adds a list of persons to the list and updates the layout
     * @param persons 
     */
    public void addPersons(ArrayList<HashMap<PersonData, String>> persons) {
        for (HashMap<PersonData, String> person : persons) {
            addPerson(person);
        }
    }

    @Override
    public void setEditor(IValidatedList editor) {
        this.editor = editor;
    }
}
