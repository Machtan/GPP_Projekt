package classes;

/**
 * The Person class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 21-Nov-2013
 */
public class Person {
    
    public String id;
    public String name;
    public String nationality;
    public String cpr;

    /**
     * Creates a new person with the given parameters
     * @param id The database ID of the Person 
     * @param name The name of the person
     * @param nationality The person's nationality
     * @param cpr The public service code or whatever for the person
     */
    public Person(String id, String name, String nationality, String cpr) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.cpr = cpr;
    }
    public Person(String name, String nationality, String cpr) {
        this.name = name;
        this.nationality = nationality;
        this.cpr = cpr;
    }
    /**
     * Default constructor for the Person class
     */
    public Person () {
        this("","", "", "");
    }
}
