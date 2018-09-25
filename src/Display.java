/*
Display.java
A program that displays tournament brackets
@author Eric Ke
9/24/2018
 */

//Graphics &GUI imports
import javax.swing.*;
import java.awt.*;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.Color.white;


//Util


public class Display extends JFrame {

    //class variables
    private static JFrame window;
    private JPanel displayPanel;
    private Bracket tournament;
    private static double scaleRatio;


    //Constructor - this runs first
    public Display(Bracket tournament) {
        super("Tournament Bracket");

        this.tournament = tournament;


        // Set the frame to full screen
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        scaleRatio = (double) Toolkit.getDefaultToolkit().getScreenSize().width / 1920; //scale ratio of the screen so it's compatible with other screens
        System.out.println(scaleRatio);
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
            Font font1 = new Font("Arial", Font.PLAIN, (int)(16*scaleRatio));

            int numOfRounds = tournament.getNumberOfRounds();

            super.paintComponent(g);
            setDoubleBuffered(true);
            g.setColor(BLACK);
            Image match = new ImageIcon("resources/match.png").getImage();

            g.setColor(white);
            //i = round number
            for (int i = 1; i <= numOfRounds; i++) {
                int gap = (int) Math.pow(2, (i-1));
                int biggerGap = gap*2;
                int x, y;

                //j = match number
                for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) {


                    String[][] teams = tournament.getTeamsInMatch(i, j);
                    x = (int)((30 + 180*(i-1))*scaleRatio); //initially 30 far away, 160 distance from each other, left edge
                   // y = (int) ((90 * (j - 1) * gap + 45 * gap) * scaleRatio);
                    if(i == 1 && tournament.getNumberOfTeams()%2 == 1) {

                        y = (int)((90*(j)*gap + 45*gap)*scaleRatio);
                    } else {

                        y = (int) ((90 * (j - 1) * gap + 45 * gap) * scaleRatio); //uses gap to determine spacing between

                    }

                    int connectionPointX = (int)((30 + 180*(i))*scaleRatio);
                    int connectionPointY = (int)((90*((j-1)/2)*biggerGap + 45*biggerGap + 35)*scaleRatio);

                    g.drawImage(match, x, y,(int)(140*scaleRatio),(int)(70*scaleRatio),null);

                    if(i < numOfRounds) {
                        g.drawLine((int)(x + 140* scaleRatio), (int)(y + 35*scaleRatio), connectionPointX, connectionPointY);
                    }

                    for (int u = 0; u < teams.length; u++) {
                        g.setFont(font1);

                        g.drawString(teams[u][teams[u].length-1], (int)(x+5*scaleRatio), (int)(y+30*scaleRatio+(35*scaleRatio)*u));
                    }

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
  