package classes;

/**
 * The FlightChooser class is a class that lets the user choose a flight from
 * inside the reservation editor. It's mostly just a FlightBrowser with 
 * customized button text.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 14-Dec-2013
 */
public class FlightChooser extends FlightBrowser {
    
    /**
     * Constructor for the FlightChooser class
     */
    public FlightChooser () {
        super();
        this.setActionButtonText("Vælg denne afgang");
        this.returnToMainMenuButton.setText("Gå tilbage");
    }
    
}
