package mapmaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Pop-up window for error messages
 * 
 */
public class ErrorPopup extends JPanel {
    //window for error message
    private JOptionPane window = new JOptionPane(JOptionPane.OK_OPTION);
    
    //panel for error popup
    private JPanel panel = new JPanel();
    
    //Message the error message displays
    private JLabel message;
    
    /**
     * Creates a new error pop-up that will alert the user of an error 
     * specified through the error parameter
     * 
     * @param error String describing the error
     * @param frame Context frame
     */
    public ErrorPopup(String error, JFrame frame) {
        message = new JLabel(error);
        panel.setLayout(new GridLayout(2,1));
        panel.add(message);
        display(frame);
    }
    
    /**
     * Displays the pop-up
     * 
     * @param frame Context frame
     */
    private void display(JFrame frame) {
        window.showMessageDialog(
            frame, 
            panel, 
            "ERROR!",
            JOptionPane.ERROR_MESSAGE);
    }
    
}
