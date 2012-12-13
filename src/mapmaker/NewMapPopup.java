package mapmaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPopupMenu.Separator;

/**
 * The pop-up window for creating a new map
 * 
 */
public class NewMapPopup extends JPanel {
    //Pane that pops up
    private JOptionPane window = new JOptionPane(JOptionPane.OK_CANCEL_OPTION);
    
    //Panel for the popup
    private JPanel panel = new JPanel();
    
    //Label with text: "Map Width"
    private JLabel widthLbl = new JLabel("Map Width:");
    
    //Label with text: "Map Height"
    private JLabel heightLbl = new JLabel("Map Height:");
    
    //SpinnerNumberModel for width so the JSpinners can't go into negative values
    private SpinnerModel widthModel = new SpinnerNumberModel(5000, 0, 10000, 1);
    
    //SpinnerNumberModel for height so the JSpinners can't go into negative values
    private SpinnerModel heightModel = new SpinnerNumberModel(5000, 0, 10000, 1);
    
    //Text Field that holds the information about the map width
    private JSpinner widthInput = new JSpinner(widthModel);
    
    //Text Field that holds the information about the map height
    private JSpinner heightInput = new JSpinner(heightModel);
    
    //Title of window
    private String title = "New Map Specification";
    
    /**
     * Creates the new map pop-up and sets up the panel
     */
    public NewMapPopup() {
        
        //Add to panel
        panel.setLayout(new GridLayout(4, 2, 0, 4));
        panel.add(widthLbl);
        panel.add(widthInput);
        panel.add(heightLbl);
        panel.add(heightInput);
        panel.add(new Separator());
        panel.add(new Separator());
    }
    
    /**
     * Displays the window and creates a new map object based on the items
     * specified
     * 
     * @param frame Frame for dialog
     * @return New Map Object
     */
    public Map display(JFrame frame) {
        //Creates the window dialog and assigns the value (ok, cancel) to an int
        int check = window.showConfirmDialog(
            frame, 
            panel, 
            title,
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if(check == JOptionPane.OK_OPTION) {
            //create new map with specified input
            return new Map((Integer)widthInput.getValue(), 
                (Integer)heightInput.getValue(),
                frame);
        }
        //if cancel is clicked, return a null map
        else return null;
    }
}
