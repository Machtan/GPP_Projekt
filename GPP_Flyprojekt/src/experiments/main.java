package experiments;

import classes.Airplane;
import classes.AirplaneLayout;
import classes.AirplaneSeat;
import classes.DatabaseHandler;
import classes.Flight;
import classes.PersonData;
import classes.PassengerList;
import classes.Reservation;
import classes.ReservationData;
import classes.ReservationEditor;
import classes.FlightPanel;
import classes.Person;
import classes.Utils;
import java.util.ArrayList;
import classes.ValidatedListPanel;
import classes.Validator;
import interfaces.IDatabaseHandler;
import interfaces.IFlight;
import interfaces.IValidatable;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
        final PassengerList pdl = new PassengerList(new Validator(), 350,500,"Redigér Passager", 
                "Slet Passager");
        pdl.addPerson(patrick);
        pdl.addPerson(stinus);
        
        
        frame.add(pdl, "West");
        
        frame.add(new JLabel("TESTING"), "North");
        
        JButton addButton = new JButton("Add Stuff");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pdl.addPerson(stinus);
            }
            
        });
        addButton.setFocusable(false);
        
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
    
    public static void testPointsInSets() {
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
    
    public static void testValidatableLists() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        HashMap<IValidatable, String> data = new HashMap<IValidatable, String>();
        data.put(PersonData.CPR, "1222334455");
        data.put(PersonData.NAME, "Bob the bad");
        data.put(PersonData.NATIONALITY, "DEMONIC DUCK");
        data.put(ReservationData.CARDNUMBER, "20009");
        data.put(ReservationData.PHONENUMBER, "34343434");
        
        ValidatedListPanel panel = new ValidatedListPanel(data, new Validator(), 
                200, 200, PersonData.CPR, PersonData.NAME, PersonData.NATIONALITY, 
                ReservationData.CARDNUMBER, ReservationData.PHONENUMBER);
        frame.add(panel);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void testReservationEditor() {
        DatabaseHandler db = DatabaseHandler.getHandler();
        try {
            db.connect();
        } catch (Exception ex) {
            return;
        }
        try {
            Reservation res = db.getReservations(db.getFlights()[0])[0];
        } catch (IDatabaseHandler.ConnectionError ce) {
            Utils.showNoConnectionNotice("Reservationen kunne ikke hentes");
        }
        ReservationEditor editor = new ReservationEditor();
        editor.pack();
        editor.setVisible(true);
    }
    
    public static void testWindowDisposal() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        JButton openButton = new JButton("Open test window");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a frame with some special closing mumbo jumbo
                final JFrame closeFrame = new JFrame(); 
                closeFrame.setTitle("Test Window!");
                closeFrame.getContentPane().setLayout(new GridBagLayout()); //To center the button
                final JButton closeButton = new JButton("Close");
                closeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        closeFrame.dispose();
                    }
                });
                closeFrame.add(closeButton);

                closeFrame.addWindowListener( new WindowAdapter() {
                    public void windowClosing(WindowEvent e)
                    {
                        JFrame frame = (JFrame)e.getSource();

                        int result = JOptionPane.showConfirmDialog(
                            frame,
                            "Are you sure you want to exit the application?",
                            "Exit Application",
                            JOptionPane.YES_NO_OPTION);

                        if (result == JOptionPane.YES_OPTION)
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        }
                    }
                );
                
                closeFrame.pack();
                closeFrame.setVisible(true);
            }
        });
        
        
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.add(openButton);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void testFlightPanel() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        DatabaseHandler db = DatabaseHandler.getHandler();
        IFlight flight;
        try {
            flight = db.getFlights()[0]; // Mistah test
        } catch (IDatabaseHandler.ConnectionError ce) {
            Utils.showNoConnectionNotice("Afgangen kunne ikke hentes");
            return;
        }
        
        FlightPanel panel = new FlightPanel();
        panel.loadFlight(flight);
        frame.add(panel);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main (String[] args) throws Exception {
        //testReservationEditor();
    }
} 
