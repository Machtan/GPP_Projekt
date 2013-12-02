package experiments;

import classes.PersonData;
import classes.PersonDataList;
import classes.PersonEditor;
import java.util.ArrayList;
import classes.Utils.*;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import javax.swing.SpringLayout;

/**
 * This class is just there to run various experiments to learn how java works
 * ;)
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public class main {
    
    public static void test(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s+" ");
        }
        System.out.println("test with variable args: "+sb);
    }
    public static void test(String arg) {
        System.out.println("Test with one arg: "+arg);
    }
    
    public static class Derp {
        public String a; 
        public String b;         
        public String c;
        public Derp(String a, String b, String c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
    
    public static void testPersonDataList() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        
        HashMap<PersonData, String> patrick = new HashMap<PersonData, String>();
        patrick.put(PersonData.NAME, "Patrick The Bringer of Cutlery");
        patrick.put(PersonData.CPR, "6666666666");
        patrick.put(PersonData.NATIONALITY, "Demonic");
        //PersonDataPanel pane = new PersonDataPanel(patrick);
        //frame.add(pane, "North");
        
        final HashMap<PersonData, String> stinus = new HashMap<PersonData, String>();
        stinus.put(PersonData.NAME, "Stinus The Lord of Cake");
        stinus.put(PersonData.CPR, "1337133700");
        stinus.put(PersonData.NATIONALITY, "Equestrian");
        //PersonDataPanel pane2 = new PersonDataPanel(stinus);
        //frame.add(pane2, "South");
        
        //------------------
        final PersonDataList pdl = new PersonDataList();
        pdl.addPerson(patrick);
        pdl.addPerson(stinus);
        frame.add(pdl, "South");
        
        frame.add(new JLabel("TESTING"), "North");
        
        JButton addButton = new JButton("Add Stuff");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pdl.addPerson(stinus);
            }
            
        });
        addButton.setFocusable(false);
        
        frame.add(new PersonEditor(pdl), "East");
        
        frame.pack();
        frame.setVisible(true);
    }
    
    private static class ScrollablePanel extends JPanel {
        
        private int WIDTH = 200;
        private int HEIGHT = 200;
        
        public ScrollablePanel() {
            super();
            this.setLayout(new GridLayout(0, 1));
        }

        public void addText(String text) {
            this.add(new JLabel(text));
            revalidate();
        }
        public void addLines(ArrayList<String> lines) {
            for (String line : lines) {
                addText(line);
            }
        }
    }
    
    public static void testScrolling() {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(240,200));
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        ScrollablePanel pane = new ScrollablePanel();
        ScrollablePanel pane2 = new ScrollablePanel();
        for (int i = 1; i < 25; i++) {
            pane.addText("Line "+i);
            pane2.addText("Leniency +"+i);
        }
        
        //frame.add(pane, "West");
        //frame.add(pane2, "East");
        
        JScrollPane scrollPane = new JScrollPane(pane, VERTICAL_SCROLLBAR_AS_NEEDED, 
        HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(120, 200));
        JScrollPane scrollPane2 = new JScrollPane(pane2, VERTICAL_SCROLLBAR_AS_NEEDED, 
        HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane2.setPreferredSize(new Dimension(120, 200));
        //frame.setContentPane(scrollPane);
        //frame.setContentPane(scrollPane2);
        frame.add(scrollPane, "West");
        frame.add(scrollPane2, "East");

        //frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void testSpringLayout() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        SpringLayout layout = new SpringLayout();
        frame.setLayout(layout);
        JLabel label = new JLabel("Name: ");
        JLabel name = new JLabel("Bob Im Bap");
        frame.add(label);
        frame.add(name);
        
        layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, frame);
        layout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.NORTH, frame);
        
        layout.putConstraint(SpringLayout.WEST, name, 50, SpringLayout.EAST, label);
        layout.putConstraint(SpringLayout.NORTH, name, 5, SpringLayout.NORTH, frame);
        
        frame.pack();
        frame.setVisible(true);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void testPointsInSets() {
        Point p1 = new Point(2, 0);
        Point p2 = new Point(2, 0);
        Point p3 = (Point)p1.clone();
        
        HashSet set = new HashSet();
        set.add(p1);
        System.out.println(String.format("%s.contains(%s) -> %s", set, p2, set.contains(p2)));
        
        System.out.println(String.format("%s == %s -> %s", p1, p2, p1==p2));
        System.out.println(String.format("%s == %s -> %s", p1, p3, p1==p3));
        System.out.println(String.format("%s.equals(%s) -> %s", p1, p2, p1.equals(p2)));
    }
    
    /**
     * Constructor for the main class
     */
    public static void main (String[] args) throws Exception {
        testPersonDataList();
    }  
} 
