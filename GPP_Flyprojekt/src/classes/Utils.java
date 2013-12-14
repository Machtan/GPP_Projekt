package classes;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * The Utils class <More docs goes here>
 *
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 26-Nov-2013
 */
public class Utils {

    /**
     * Joins a Collection of objects by a given separator. The objects are
     * represented by their toString() method
     *
     * @param <T> Anything with a toString method
     * @param arr The Collection to join
     * @param separator The separator string
     * @return A string with the joined elements
     */
    public static <T> String joinList(Collection<T> arr, String separator) {
        StringBuilder sb = new StringBuilder();
        Iterator iter = arr.iterator();

        int lastIndex = arr.size() - 1;
        int i = 0;
        while (iter.hasNext()) {
            String s = iter.next().toString();
            s = (i == lastIndex) ? s : s + separator;
            sb.append(s);
            i++;
        }
        return sb.toString();
    }

    /**
     * Same as above, just with arrays instead of array lists
     *
     * @param <T>
     * @param arr
     * @param separator
     * @return
     */
    public static <T> String joinList(T[] arr, String separator) {
        StringBuilder sb = new StringBuilder();
        int lastIndex = arr.length - 1;
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i].toString();
            s = (i == lastIndex) ? s : s + separator;
            sb.append(s);
        }
        return sb.toString();
    }


    /**
     * Returns an array of the values for the given
     * field name on the list of objects
     *
     * @param <T> The type contained by the given ArrayList
     * @param fieldnames The names of the fields to access
     * @param objs The objects from which to get it
     * @return A list of values for the fields of the objects
     */
    public static <T> ArrayList<Object[]> forVarsOnObjInList(ArrayList<T> objs, String... fieldnames) {
        ArrayList<Object[]> arr = new ArrayList<Object[]>();
        int nargs = fieldnames.length;
        try {
            for (Object obj : objs) {
                Object[] vars = new Object[nargs];
                for (int i = 0; i < nargs; i++) {
                    try {
                        vars[i] = obj.getClass().getField(fieldnames[i]).get(obj);
                    } catch (NoSuchFieldException ex) {
                        vars[i] = "ERROR"; // Default value when missing is this string
                    }
                }
                arr.add(vars);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return arr;
    }

    /**
     * Returns an array of the values for the given
     * field name on the given objects
     *
     * @param <T> The type contained by the given ArrayList
     * @param fieldnames The names of the fields to access
     * @param objs The objects from which to get it
     * @return A list of values for the fields of the objects
     */
    public static <T> ArrayList<Object[]> forVarsOnObjInList(T obj, String... fieldnames) {
        ArrayList<Object[]> arr = new ArrayList<Object[]>();
        int nargs = fieldnames.length;
        try {
            Object[] vars = new Object[nargs];
            for (int i = 0; i < nargs; i++) {
                try {
                    vars[i] = obj.getClass().getField(fieldnames[i]).get(obj);
                } catch (NoSuchFieldException ex) {
                    vars[i] = "ERROR"; // Default value when missing is this string
                }
            }
            arr.add(vars);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return arr;
    }

    /**
     * Simplifies the getting of a single variable on the list of objects
     *
     * @param <T> The class of the objects contained by the given ArrayList
     * @param objs The objects to iterate over
     * @param fieldname The name of the field variable to get
     * @return A list of Object variables
     */
    public static <T> ArrayList<Object> forVarOnObjInList(ArrayList<T> objs, String fieldname) {
        ArrayList<Object[]> res = forVarsOnObjInList(objs, fieldname, "");
        ArrayList<Object> result = new ArrayList<Object>();
        for (Object[] vars : res) {
            result.add(vars[0]);
        }
        return result;
    }

    /**
     * Quickly formats the variables on every objects for a based on the given
     * template
     *
     * @param <T>
     * @param objs
     * @param formatString
     * @param fields
     * @return A list of formatted strings
     */
    public static <T> ArrayList<String> quickFormatList(ArrayList<T> objs,
            String formatString, String... fields) {
        ArrayList<Object[]> res = forVarsOnObjInList(objs, fields);
        ArrayList<String> result = new ArrayList<String>();
        for (Object[] vars : res) {
            result.add(String.format(formatString, vars));
        }
        return result;
    }

    /**
     * Quickly formats the variables on the given object based on the given
     * template
     *
     * @param <T>
     * @param obj
     * @param formatString
     * @param fields
     * @return A list of formatted strings
     */
    public static <T> ArrayList<String> quickFormatList(T obj,
            String formatString, String... fields) {
        ArrayList<Object[]> res = forVarsOnObjInList(obj, fields);
        ArrayList<String> result = new ArrayList<String>();
        for (Object[] vars : res) {
            result.add(String.format(formatString, vars));
        }
        return result;
    }

    /**
     * Returns a String of the joint variables of each obj in the given list,
     * formatted according to the passed string.
     *
     * @param <T>
     * @param objs
     * @param formatString
     * @param separator
     * @param fields
     * @return
     */
    public static <T> String formatAndJoinVars(ArrayList<T> objs,
            String formatString, String separator, String... fields) {
        ArrayList<String> formatted = quickFormatList(objs, formatString, fields);
        return joinList(formatted, separator);
    }

    /**
     * Returns a String of the joint variables of the given obj, formatted
     * according to the passed string.
     *
     * @param <T>
     * @param obj
     * @param formatString
     * @param separator
     * @param fields
     * @return
     */
    public static <T> String formatAndJoinVars(T obj,
            String formatString, String separator, String... fields) {
        ArrayList<String> formatted = quickFormatList(obj, formatString, fields);
        return joinList(formatted, separator);
    }

    /**
     * Returns an icon from a given path to a local image resource
     *
     * @param path
     * @return
     */
    public static Icon getIcon(String path) {
        try {
            ClassLoader cl = Utils.class.getClassLoader();
            URL url = cl.getResource(path);

            if (url != null) {
                ImageIcon icon = new ImageIcon(url, "");
                return icon;
            } else {
                System.err.println("Bad file path: " + path);
                //return UIManager.getIcon("FileView.floppyDriveIcon");
                return UIManager.getIcon("Tree.leafIcon");
            }
        } catch (Exception ex) {
            System.out.println("Exception while fetching image @ " + path);
        }
        return null;
    }

    /**
     * Sets the image icon of the frame to the icon at the given resource path
     *
     * @param frame The frame whose icon is to be set
     * @param iconPath The resource path to the icon (eg: images/icon.png)
     */
    public static void setFrameIcon(JFrame frame, String iconPath) {
        try {
            URL url = MainMenu.class.getClassLoader().getResource("images/Plane.png");
            Image img = Toolkit.getDefaultToolkit().createImage(url);
            frame.setIconImage(img);
        } catch (Exception ex) {
            System.out.println("Failed to load the image: " + iconPath + "\n=>\n" + ex);
        }
    }

    /**
     * Automatically transitions from component a to b, so that a closes and b
     * opens, and so that a is reopened when b is closed again
     *
     * @param a The origin window
     * @param b The new window
     */
    public static void transition(final JFrame a, final JFrame b) {
        b.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        b.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                a.setVisible(true);
            }
        });
        a.dispose();

        setFrameIcon(b, "images/Plane.png"); // Remember the application icon <3

        b.pack();
        b.setVisible(true);
    }

    /**
     * Automatically transitions from component a to b, so that a closes and b
     * opens, and so that a is reopened when b is closed again
     *
     * @param a The origin window
     * @param b The new window
     */
    public static void transition(final JFrame a, final Browser b) {
        b.bindReturnButton(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                a.setVisible(true);
                b.dispose();
            }
        });
        transition(a, (JFrame) b);
    }

    /**
     * Prints a formatted string more easily fp is short for FormattedPrint
     *
     * @param formatString the format specification
     * @param args The arguments to be formatted
     */
    public static void fp(String formatString, Object... args) {
        System.out.println(String.format(formatString, args));
    }
    
    /**
     * Shows a notice about the program having no connection to the database
     * at present, along with a more specific note about what this impacts.
     * @param additionalNotice The explanation of what the lack of connection
     * has an impact on, e.g: "The reservations couldn't be loaded"
     */
    public static void showNoConnectionNotice(String additionalNotice) {
        JOptionPane.showMessageDialog(new JFrame(), 
                "Der er desværre ingen forbindelse til databasen.\n"+
                additionalNotice, 
                "Forbindelsesfejl", JOptionPane.WARNING_MESSAGE);
    }
}
