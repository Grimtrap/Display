//Graphics &GUI imports
import javax.swing.*;
import java.awt.*;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.Color.BLACK;


//Util


class Display extends JFrame {

    //class variables
    private static JFrame window;
    private JPanel displayPanel;


    //Main
    public static void main(String[] args) {
        window = new Display();
    }


    //Constructor - this runs first
    public Display() {
        super("Tournament Bracket");


        // Set the frame to full screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        //frame.setResizable(false);


        //Set up the game panel (where we put our graphics)
        displayPanel = new DisplayPanel();
        displayPanel.setBackground(new Color(50,50,50,255));
        this.add(displayPanel);
        MyKeyListener keyListener = new MyKeyListener();
        this.addKeyListener(keyListener);

        this.requestFocusInWindow(); //make sure the frame has focus

        this.setVisible(true);


    } //End of Constructor


    /**
     * --------- INNER CLASSES -------------
     **/

    // Inner class for the the game area - This is where all the drawing of the screen occurs
    private class DisplayPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g); //required
            setDoubleBuffered(true);
            g.setColor(BLACK);
            Image match = new ImageIcon("resources/match.png").getImage();
            //move enemies


            //check for collision

            g.drawImage(match,50,50,100,50,null);
            //repaint
            repaint();
        }
    }

    // -----------  Inner class for the keyboard listener - this detects key presses and runs the corresponding code
    private class MyKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            //System.out.println("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()));

            if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {  //If 'D' is pressed

            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {  //If ESC is pressed
                System.out.println("Quitting!"); //close frame & quit
                window.dispose();

            }
        }

        public void keyReleased(KeyEvent e) {
        }
        //end of keyboard listener
    }
}
  