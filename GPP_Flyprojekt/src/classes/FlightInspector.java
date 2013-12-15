package classes;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The FlightInspector class lets the user browse flights and inspect 
 * reservations on these, without being able to edit them.
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 07-Dec-2013
 */
public class FlightInspector extends FlightBrowser {
    
    /**
     * Constructor for the FlightBrowser class
     */
    public FlightInspector () {
        super();
        final FlightInspector browser = this;
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                JPanel panel = new JPanel(new GridBagLayout());
                panel.setPreferredSize(new Dimension(450, 750));
                frame.setTitle("Sæder for flight #"+browser.getChosen().id);
                SeatChooser chooser = new SeatChooser();
                chooser.setFlight(getChosen());
                panel.add(chooser);
                frame.add(panel);
                Utils.transition(browser, frame);
            }
        });
        this.setActionButtonText("Se ledige sæder");
    }
}
