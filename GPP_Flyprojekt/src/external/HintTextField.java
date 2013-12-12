/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package external;

/**
 *
 * @author patr0805
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import javax.swing.JTextField;

public class HintTextField extends JTextField {

    Font gainFont = new Font("Tahoma", Font.PLAIN, 11);
    Font lostFont = new Font("Tahoma", Font.ITALIC, 11);
    public String hint = "";

    public int GetIntegerValue() {
        if (getText() == null ? hint == null : getText().equals(hint)) {
            return 0;
        }
        try {
            return Integer.parseInt(getText());
        } catch (Exception e)  {
        }
        return 0;
    }

    public HintTextField(final String hint) {
        this.hint = hint;
        setText(hint);
        setFont(lostFont);
        setForeground(Color.GRAY);

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(hint)) {
                    setText("");
                    setFont(gainFont);
                } else {
                    setText(getText());
                    setFont(gainFont);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().equals(hint) || getText().length() == 0) {
                    setText(hint);
                    setFont(lostFont);
                    setForeground(Color.GRAY);
                } else {
                    setText(getText());
                    setFont(gainFont);
                    setForeground(Color.BLACK);
                }
            }
        });

    }
}
