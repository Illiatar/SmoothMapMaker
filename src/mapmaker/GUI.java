package mapmaker;

import com.google.gson.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JPopupMenu.Separator;

/**
 * GUI class for MapMaker
 * 
 */
public final class GUI extends JPanel implements ComponentListener, ActionListener {
    //Frame for the GUI
    private JFrame frame = new JFrame("MapMaker");
    
    //Menu Bar contains File, Edit, etc
    private JMenuBar menuBar = new JMenuBar();
    
    //Menu for File
    private JMenu file = new JMenu("File");
    
    //Menu for Edit
    private JMenu edit = new JMenu("Edit");
    
    //Menu for Map
    private JMenu map = new JMenu("Map");
    
    //Exit button for File menu
    private JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_F4);
    
    //New Map button for File menu
    private JMenuItem newItem = new JMenuItem("New Map", KeyEvent.VK_F1);
    
    //Open Button for File menu
    private JMenuItem openItem = new JMenuItem("Open Map", KeyEvent.VK_O);
    
    //Open Button for File menu
    private static JMenuItem saveItem = new JMenuItem("Save Map", KeyEvent.VK_S);
    
    //New Tileset for Map menu
    private JMenuItem newTilesetItem = new JMenuItem("New Tileset", KeyEvent.VK_T);
    
    //File Chooser for the program
    private final JFileChooser fileC = new JFileChooser();
    
    //Panel for the map portion of the screen
    private JPanel mapPanel = new JPanel();
    
    //Panel for the tiles
    private JPanel utilPanel = new JPanel();
    
    //Information panel
    private JPanel infoPanel = new JPanel();
    
    //Scroll pane for map panel
    JScrollPane mapScroll;
    
    //Scroll pane for tile panel
    JScrollPane tileScroll;
    
    //SplitPane separating the map and tile panel
    private JSplitPane mapTileSplit;
    
    //SplitPane separating the mapTileSplit and the bottom panel
    private JSplitPane splitPane2;
    
    //Width of window
    private int width;
    
    //Height of window
    private int height;
    
    //Map being edited
    private static Map curMap;
    
    //Utility Table
    private UtilityTable utilityTable = new UtilityTable();
    
    //Information Table
    private InformationTable infoTable = new InformationTable();
    
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    /**
     * Constructor, sets up initial values and calls display
     */
    public GUI() {
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatcher());
        //Toolkit for screen dimensions
        Toolkit tk = Toolkit.getDefaultToolkit();
        width = (int) tk.getScreenSize().getWidth();
        height = (int) tk.getScreenSize().getHeight();
        
        //Frame's default values
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        //frame.setSize(width - (width/8), height - (height/8));
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        
        frame.addComponentListener(this);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Menu bar setup
        frame.setJMenuBar(menuBar);
        exitItem.setMnemonic(KeyEvent.VK_F4);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        newItem.setMnemonic(KeyEvent.VK_F1);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.ALT_MASK));
        openItem.setMnemonic(KeyEvent.VK_O);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveItem.setEnabled(false);
        newTilesetItem.setMnemonic(KeyEvent.VK_T);
        newTilesetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        newTilesetItem.setEnabled(false);
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(map);
        file.add(newItem);
        file.add(openItem);
        file.add(saveItem);
        file.add(new Separator());
        file.add(exitItem);
        map.add(newTilesetItem);
        newTilesetItem.addActionListener(this);
        newItem.addActionListener(this);
        exitItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        
        //1x1 GridLayout
        setLayout(new GridLayout(1,1));
        
        //FileChooser setup
        fileC.setCurrentDirectory(new File("."));
        
        //mapPanel's background is white, set it here
        mapPanel.setBackground(Color.white);
        mapPanel.setPreferredSize(new Dimension(0, 0));
        
        //utilPanel setup
        utilPanel.setLayout(new GridLayout(1,1));
        
        //infoPanel setup
        infoPanel.setLayout(new GridLayout(1,1));
        
        //Scroll pane setups
        mapScroll = new JScrollPane(mapPanel);
        mapScroll.getVerticalScrollBar().setUnitIncrement(16);
        mapScroll.getHorizontalScrollBar().setUnitIncrement(16);
        tileScroll = new JScrollPane(utilPanel);
        tileScroll.getVerticalScrollBar().setUnitIncrement(16);
        tileScroll.getHorizontalScrollBar().setUnitIncrement(16);
        
        
        //Split Pane setup
        mapTileSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mapScroll, infoPanel);
        splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapTileSplit, tileScroll);
        mapTileSplit.setDividerLocation(height - 200);
        splitPane2.setDividerLocation(width - 200);
        frame.add(splitPane2);
        utilPanel.removeAll();
        utilPanel.add(utilityTable);
        utilPanel.updateUI();
        infoPanel.removeAll();
        infoPanel.add(infoTable);
        infoPanel.updateUI();
        
        
        //Display the frame
        display();
    }
    
    /**
     * Displays the frame
     */
    public void display() {
        frame.setVisible(true);
    }

    /**
     * Action is performed from the main screen
     * 
     * @param e ActionEvent context
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == exitItem) {
            System.exit(0);
        }
        if(e.getSource() == newItem) {
            Map tempMap = new NewMapPopup().display(frame);
            if(tempMap != null) {
                curMap = tempMap;
                mapPanel.removeAll();
                mapPanel.setLayout(new GridLayout(1,1));
                mapPanel.add(curMap);
                mapPanel.setPreferredSize(curMap.getSize());
                mapPanel.updateUI();
                newTilesetItem.setEnabled(true);
            }
        }
        if(e.getSource() == openItem) {
            fileC.showOpenDialog(this);
        }
        if(e.getSource() == saveItem) {
            if(fileC.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileC.getSelectedFile();
                try {
                    BufferedWriter writer = null;
                    JsonArray toSave = new JsonArray();
                    toSave.add(curMap.pack());
                    //toSave = toSave.replaceAll("\\\\n", "\n");//THIS IS DUMB AND I NOW HATE THIS IMPLEMENTATION OF JSON
                    //toSave = toSave.replaceAll("\\\\", ""); //THIS IS DUMB AND I NOW HATE THIS IMPLEMENTATION OF JSON
                    System.out.println(toSave);
                    
                    writer = new BufferedWriter(new FileWriter(saveFile));
                    writer.write(gson.toJson(toSave));
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        }
    }

    /**
     * Checks when the window is resized, resets the split Panes when
     * the windows is resized.
     * 
     * @param e ComponentEvent context
     */
    @Override
    public void componentResized(ComponentEvent e) {
        width = frame.getSize().width;
        height = frame.getSize().height;
        mapTileSplit.setDividerLocation(height - 200);
        splitPane2.setDividerLocation(width - 200);
        
    }

    //UNUSED INTERFACE METHODS
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}

    public static Map getCurMap() {
        return curMap;
    }

    public static JMenuItem getSaveItem() {
        return saveItem;
    }
    
    private class KeyDispatcher implements KeyEventDispatcher {

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if(e.getKeyCode() == KeyEvent.VK_M) {
                    for(UtilityButton b : UtilityTable.buttons) {
                        if(b.getType() == UtilityType.Move) {
                            UtilityTable.selectedButton = b;
                            utilityTable.repaint();
                        }
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_R) {
                    for(UtilityButton b : UtilityTable.buttons) {
                        if(b.getType() == UtilityType.Rotate) {
                            UtilityTable.selectedButton = b;
                            utilityTable.repaint();
                        }
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_E) {
                    for(UtilityButton b : UtilityTable.buttons) {
                        if(b.getType() == UtilityType.Rectangle) {
                            UtilityTable.selectedButton = b;
                            utilityTable.repaint();
                        }
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_S) {
                    for(UtilityButton b : UtilityTable.buttons) {
                        if(b.getType() == UtilityType.Select) {
                            UtilityTable.selectedButton = b;
                            utilityTable.repaint();
                        }
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_D) {
                    for(UtilityButton b : UtilityTable.buttons) {
                        if(b.getType() == UtilityType.Delete) {
                            UtilityTable.selectedButton = b;
                            utilityTable.repaint();
                        }
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_A) {
                    if(UtilityTable.isAnchor()) UtilityTable.setAnchor(false);
                    else UtilityTable.setAnchor(true);
                    utilityTable.repaint();
                    curMap.repaint();
                }
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                
            }
            return false;
        }
        
    }
}
