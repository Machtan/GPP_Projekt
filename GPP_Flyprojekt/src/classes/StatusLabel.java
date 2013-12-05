package classes;

import interfaces.IValidatable;
import interfaces.IValidator;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * The StatusLabel class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 02-Dec-2013
 */
public class StatusLabel extends JLabel implements FocusListener {
    
    JTextField field;
    IValidatable fieldType;
    IValidator validator;
    
    /**
     * Constructor for the StatusLabel class
     * @param field The text field to validate
     * @param fieldType The type of field to validate (this is for the validator)
     * @param validator The validator to validate the field with
     */
    public StatusLabel (JTextField field, IValidatable fieldType, 
            IValidator validator) {
        this.field = field;
        this.fieldType = fieldType;
        this.validator = validator;
        verifyField();
    }
    
    /**
     * Updates the status label, so that it reflects the validity of the 
     * field it is tracking
     * @return Whether the field is valid
     */
    public boolean verifyField() {
        String iconPath;
        String toolTipText;
        boolean valid;
        try {
            valid = validator.validate(fieldType, field.getText());
            
            if (valid) {
                 iconPath = "images/okaystatus.png";
                 toolTipText = "The field is valid";
            } else {
                iconPath = "images/notokaystatus.png";
                toolTipText = fieldType.getErrorTip();
            }
        } catch (IValidator.NoValidatorException ex) {
            valid = false;
            iconPath = "images/warningstatus.png";
            toolTipText = validator.getNoValidatorTip();
        }
        
        this.setToolTipText(toolTipText);
        this.setIcon(Utils.getIcon(iconPath));
        return valid;
    }

    @Override
    public void focusGained(FocusEvent e) {}

    @Override
    public void focusLost(FocusEvent e) {
        verifyField();
    }
    
}
