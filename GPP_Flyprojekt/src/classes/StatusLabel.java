package classes;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * The StatusLabel class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 02-Dec-2013
 */
public class StatusLabel extends JLabel implements FocusListener {
    
    JTextField field;
    PersonData fieldType;
    
    /**
     * Constructor for the StatusLabel class
     * @param field
     * @param fieldType
     */
    public StatusLabel (JTextField field, PersonData fieldType) {
        this.field = field;
        this.fieldType = fieldType;
        verifyField();
    }
    
    /**
     * Updates the status label, so that it reflects the validity of the 
     * field it is tracking
     */
    private void verifyField() {
        boolean valid = Validator.validate(fieldType, field.getText());
        String iconPath;
        String toolTipText;
        if (valid) {
             iconPath = "images/okaystatus.png";
             toolTipText = "The field is valid";
        } else {
            iconPath = "images/notokaystatus.png";
            toolTipText = fieldType.getErrorTip();
        }
        
        this.setToolTipText(toolTipText);
        this.setIcon(Utils.getIcon(iconPath));
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        verifyField();
    }
    
}
