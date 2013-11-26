package experiments;

import classes.PersonData;
import classes.PersonDataPanel;
import classes.Utils;
import java.util.ArrayList;
import java.util.Date;
import classes.Utils.*;
import java.util.HashMap;
import javax.swing.JFrame;

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
    
    /**
     * Constructor for the main class
     */
    public static void main (String[] args) throws Exception {
        for (PersonData pd : PersonData.values()) {
            System.out.println(pd);
        }
        
        test("Hello");
        test("Hello", "World");
        
        ArrayList<Derp> derps = new ArrayList<Derp>();
        derps.add(new Derp("hello", "new", "world"));
        derps.add(new Derp("Something", "went", "wrong"));
        ArrayList<String> formats = Utils.quickFormatList(derps, "%s %s %s", "a", "b", "c");
        System.out.println(Utils.joinList(formats, ", "));
        
        String[] targs = new String[] {"spooky", "weird", "dusty"};
        System.out.println(String.format("hello %s, %s and %s world", targs));
        
        Date test = new Date(2013, 11, 22, 9, 9);
        System.out.println("TestDate: "+test);
        
        // YAAY
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        HashMap<PersonData, String> data = new HashMap<PersonData, String>();
        data.put(PersonData.NAME, "Patrick The Devilish");
        data.put(PersonData.CPR, "6666666666");
        data.put(PersonData.NATIONALITY, "Demonic");
        PersonDataPanel pane = new PersonDataPanel(data);
        frame.add(pane, "North");
        
        HashMap<PersonData, String> data2 = new HashMap<PersonData, String>();
        data2.put(PersonData.NAME, "Stinus The Brave");
        data2.put(PersonData.CPR, "1337133700");
        data2.put(PersonData.NATIONALITY, "Equestrian");
        PersonDataPanel pane2 = new PersonDataPanel(data2);
        frame.add(pane2, "South");
        
        
        frame.pack();
        frame.setVisible(true);
    }  
} 
