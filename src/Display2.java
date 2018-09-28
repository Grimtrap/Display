/*
Display.java
A class that displays tournament brackets
@author Eric Ke
9/24/2018
*/

//graphics imports
import javax.swing.*;
import java.awt.*;

//keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.Color.WHITE;


public class Display2 extends JFrame {

    //class variables
    private JPanel displayPanel;
    private Bracket tournament;
    private static double scaleRatio;


    /**
     * Constructor for the display for the tournament
     * @param tournament a generated bracket
     */
    public Display2(Bracket tournament) {
        super("Tournament Bracket");

        this.tournament = tournament;


        // Set the frame to full screen
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        scaleRatio = (double) Toolkit.getDefaultToolkit().getScreenSize().width / 1920; //scale ratio of the screen so it's compatible with other screens
        //frame.setResizable(false);


        //Set up the game panel (where we put our graphics)
        displayPanel = new DisplayPanel();
        displayPanel.setBackground(new Color(10, 10, 10, 255));
        this.add(displayPanel);
        MyKeyListener keyListener = new MyKeyListener();
        this.addKeyListener(keyListener);

        this.requestFocusInWindow(); //make sure the frame has focus

        this.setVisible(true);


    }

    public void update(Bracket tournament) {
        this.dispose();
        new Display2(tournament);

    }


    /**
     * --------- INNER CLASSES -------------
     **/

    // Inner class for the the game area - This is where all the drawing of the screen occurs
    private class DisplayPanel extends JPanel {


        /**
         * draws the tournament graphics on the screen
         * @param g graphics
         */
        public void paintComponent(Graphics g) {

            if(tournament instanceof SingleBracket) {
                drawSingleBracket(g);
            }
            //check for collision

            //repaint
        }

        /**
         * checks whether a team from a previous match has the possibility to advance
         * is for the purpose of drawing brackets in the correct positions
         * @param team1 the list of teams to check against the other, from the current match
         * @param team2 the list of teams from a match from a previous round
         * @return whether or not a match should be drawn
         */
        private boolean checkTeams(String[] team1, String[][] team2) {

            for(int i = 0; i < team1.length; i++) {
                String current = team1[i];
                for(int j = 0; j < team2.length; j++) {
                    for(int k = 0; k < team2[j].length; k++) {
                        String comparison = team2[j][k];
                        if (current.equalsIgnoreCase(comparison)) { return true; }
                    }

                }
            }
            return false;

        }


        /**
         * draws the tournament bracket on the screen for single brackets
         * @param g paintComponent graphics
         */
        private void drawSingleBracket(Graphics g) {

            super.paintComponent(g);
            setDoubleBuffered(true);
            Image match = new ImageIcon("resources/match.png").getImage();

            g.setColor(WHITE);
            //i = round number
            for (int i = tournament.getNumberOfRounds(); i > 0; i--) {
                double center = (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2) - 140*scaleRatio;

                //j = match number
                for (int j = 1; j <= tournament.getNumberOfMatchesInRound(i); j++) {

                    String[][] teams = tournament.getTeamsInMatch(i, j);

                    //coordinates
                    double baseY = (center/Math.pow(2, tournament.getNumberOfRounds() - i))*scaleRatio;
                    double gap = 2*baseY;
                    double currentY = (baseY + (j-1)*gap)*scaleRatio;
                    int currentX = (int)((600 + 180 * (i)) * scaleRatio);
                    double nextShift = baseY/2;


                    for(int u = 1; u <= tournament.getNumberOfMatchesInRound(i-1); u++) {

                        int connectionPointX = (int) ((600 + 180 * (i - 1)) * scaleRatio);

                        String[][] teams1;

                        teams1 = tournament.getTeamsInMatch(i - 1, u);

                        //draws the final bracket
                        if (i == tournament.getNumberOfRounds()) {
                            g.drawImage(match, (currentX), (int) (currentY), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                            drawTeams(g, teams, (currentX), (int) (currentY));
                        }




                        //checks whether the winner of a previous match can go to the current match, draws accordingly
                            if (teams1 != null && teams1.length > 0) {
                                if (checkTeams(teams[0], teams1)) {
                                    g.drawImage(match, connectionPointX, (int)(currentY-nextShift), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                    g.drawLine(currentX, (int)(currentY+(35*scaleRatio)), (int)(connectionPointX+140*scaleRatio), (int)(currentY-nextShift+35*scaleRatio));
                                    drawTeams(g, teams1, connectionPointX, (int)(currentY-nextShift));

                                }

                            }
                            if (teams1 != null && teams1.length > 0) {
                                if (checkTeams(teams[1], teams1)) {
                                    g.drawImage(match, connectionPointX, (int)(currentY+nextShift), (int) (140 * scaleRatio), (int) (70 * scaleRatio), null);
                                    g.drawLine(currentX, (int)(currentY+(35*scaleRatio)), (int)(connectionPointX+140*scaleRatio), (int)(currentY+nextShift+35*scaleRatio));

                                    drawTeams(g, teams1, connectionPointX, (int)(currentY+nextShift));
                                }

                            }

                    }

                }

            }
        }

        private void drawTeams(Graphics g, String[][] teams, int x, int y) {
            for (int i = 0; i< teams.length; i++) {
                //if(teams[i].length == 1) {
                    Font font = new Font("Arial", Font.PLAIN, (int)(16*scaleRatio));
                    g.setFont(font);
                    g.setColor(WHITE);
                    g.drawString(teams[i][0], (int)(x+15*scaleRatio), (int)(y+(25+35*i)*scaleRatio));
                //}
            }

        }

    }

    // -----------  Inner class for the keyboard listener - this detects key presses and runs the corresponding code

    private class MyKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            //System.out.println("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()));

            if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {
                update(tournament);

            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {  //If ESC is pressed
                System.out.println("Quitting!"); //close frame & quit


            }
        }

        public void keyReleased(KeyEvent e) {
        }
        //end of keyboard listener
    }
}
  