//Graphics &GUI imports
import javax.swing.*;
import java.awt.*;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;


//Util


class Display extends JFrame {

    //class variables
    private static JFrame window;
    private JPanel displayPanel;
    private Bracket tournament;


    //Main
    public static void main(String[] args) {
        window = new Display(new Bracket());
    }


    //Constructor - this runs first
    public Display(Bracket tournament) {
        super("Tournament Bracket");

        this.tournament = tournament;


        // Set the frame to full screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        //frame.setResizable(false);


        //Set up the game panel (where we put our graphics)
        displayPanel = new DisplayPanel();
        displayPanel.setBackground(new Color(10,10,10,255));
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
            int numOfRounds = tournament.getNumberOfRounds();


            super.paintComponent(g); //required
            setDoubleBuffered(true);
            g.setColor(BLACK);
            Image match = new ImageIcon("resources/match.png").getImage();
            //move enemies


            //i = round number
            for (int i = 1; i <= numOfRounds; i++) {

                //j = match number
                for (int j = 1; j <= tournament.getNumOfMatchesInRound(i); j++) {
                    int gap = (int) Math.pow(2, (i-1));
                    g.drawImage(match,30 + 160*(i-1),80*(j-1)*gap + 30*(i-1) + 30*gap,120,60,null);
                }

            }


            //check for collision

            //repaint
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
  