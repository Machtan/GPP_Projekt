package classes;

import java.util.ArrayList;

/**
 * The Utils class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 26-Nov-2013
 */
public class Utils {
    /**
     * Joins a list of strings by a given separator.
     * If a list of objects is passed, their .toString() method will be used.
     * @param arr The array to join
     * @param separator The separator string
     * @return A string with the joined elements
     */
    public static <T> String joinList (ArrayList<T> arr, String separator) {
        StringBuilder sb=new StringBuilder();
        int lastIndex = arr.size() - 1;
        for(int i = 0; i < arr.size(); i++) {
            String s = arr.get(i).toString();
            s = (i == lastIndex)? s: s+separator;
            sb.append(s);
        }
        return sb.toString();
    }
    
    /**
     * THIS. IS. PYTHONNNNNNN!
     * Returns an array of the values for the given field name on the list of
     * objects
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
     * Simplifies the getting of a single variable on the list of objects
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
     * Returns a String of the joint variables of each obj in the given list,
     * formatted according to the passed string.
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
}
