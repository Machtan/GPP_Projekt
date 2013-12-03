package classes;

import interfaces.IValidatable;
import interfaces.IValidator;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * The ValidatedListPanel class <More docs goes here>
 * @author Jakob Lautrup Nysom (jaln@itu.dk)
 * @version 03-Dec-2013
 */
public class ValidatedListPanel extends JScrollPane {
    
    private IValidator validator;
    private final HashMap<IValidatable, String> data;
    private final HashMap<IValidatable, JTextField> textFields;
    private JPanel panel;
    private final int width;
    private final int height;
    private final int SCRPAD = 16; // How much padding should be left for the scrollbar
    private final int XPAD = 5; // The padding on the x-axis
    private final int YPAD = 3; // The padding on the y-axis
    private final int ICONW = 16; // The width of the status icon
    private final int LABH = 16; // The height of the panel with the label and status
    
    /**
     * Constructor for the ValidatedListPanel class
     * @param data The data to be shown in this panel
     * @param validator The validator for this panel
     * @param width The desired width of the panel
     * @param height The desired height of the panel
     * @param fields The fields that this panel should support and display
     */
    public ValidatedListPanel (HashMap data, IValidator validator, int width, 
            int height, IValidatable... fields) {
        this(validator, width, height, fields);
        setData(data);
    }
    
    /**
     * Constructor for the ValidatedListPanel class
     * @param validator The validator for this panel
     * @param width The desired width of the panel
     * @param height The desired height of the panel
     * @param fields The fields that this panel should support and display
     */
    public ValidatedListPanel (IValidator validator, int width, int height, 
            IValidatable... fields) {
        super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        
        this.width = width;
        this.height = height;
        this.validator = validator;
        this.data = new HashMap<IValidatable, String>();
        for (IValidatable fieldName : fields) {
            data.put(fieldName, "");
        }
        textFields = new HashMap<IValidatable, JTextField>();
        initComponents(fields);
        
    }
    
    private void initComponents(IValidatable[] fields) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        
        for (IValidatable fieldName : fields) {
            JPanel topPanel = new JPanel();
            topPanel.setLayout(null);
            int maxWidth = width-SCRPAD;
            int maxHeight = LABH + YPAD*2;
            topPanel.setPreferredSize(new Dimension(maxWidth, maxHeight));

            JLabel fieldLabel = new JLabel(fieldName+"");
            topPanel.add(fieldLabel);
            fieldLabel.setBounds(XPAD, YPAD, maxWidth - ICONW - XPAD*2, LABH);

            JTextField valueField = new JTextField("");
            valueField.getDocument().putProperty("filterNewlines", Boolean.TRUE);

            valueField.setMaximumSize(new Dimension(maxWidth, LABH));

            textFields.put(fieldName, valueField);

            StatusLabel statusLabel = new StatusLabel(valueField, fieldName, validator);
            topPanel.add(statusLabel);
            statusLabel.setBounds(maxWidth - XPAD - ICONW, YPAD, ICONW, LABH);

            valueField.addFocusListener(statusLabel);
            
            topPanel.validate();
            topPanel.repaint();
            panel.add(topPanel);
            panel.add(valueField);
        }
        
        this.setViewportView(panel);
        this.setMaximumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));        
    }
    
    public HashMap<IValidatable, String> getData() {
        return this.data;
    }
    
    public void setData(HashMap<IValidatable, String> data) {
        for (IValidatable key : data.keySet()) {
            if (this.data.containsKey(key)) {
                this.data.put(key, data.get(key));
            }
        }
    }
    
    public void setValidator(IValidator validator) {
        this.validator = validator;
    }
    
}
