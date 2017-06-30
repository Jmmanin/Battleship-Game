/*
Multiplayer Battleship Game
Main Game UI
Jeremy Manin
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BattleshipUI
{
   private JFrame theFrame;
   private JPanel gamePanel;
   private BSGrid enemyGrid;
   private JPanel msgPanel;
   private JLabel msgLabel;
   private BSGrid yourGrid;
   private JPanel statusPanel;
   private JLabel turnLabel;
   private JLabel turnNumLabel;
   private JLabel statusTime;
   private JPanel shipsRemainingPanel;
   private JLabel shipsRemainingLabel;
   private JLabel battleship;
   private JLabel carrier;
   private JLabel cruiser;
   private JLabel destroyer;
   private JLabel submarine;
   
   private WaitDialog waitDialog;
   
   private int turnCounter;
   private Ship[] yourShips;
   
   private BSClientThread clientThread;  
   
   public BattleshipUI(Ship[] yS, BSClientThread cT)
   {
      clientThread= cT;
      
      theFrame= new JFrame("Battleship Game");
      theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      theFrame.setResizable(false);
      theFrame.getContentPane().setLayout(new BoxLayout(theFrame.getContentPane(), BoxLayout.X_AXIS));
      
      gamePanel= new JPanel(); //main game panel
      gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
      
      enemyGrid= new BSGrid(10); //enemy grid (vertical grid in physical game)
      enemyGrid.addMouseListener(new MouseHandler());
            
      msgPanel= new JPanel(); //message display
      msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
            
      statusPanel= new JPanel(); //game status display
      statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
      
      if(clientThread.getIsTurn())
         turnLabel= new JLabel("Your Turn");
      else
         turnLabel= new JLabel("Enemy Turn");
      turnLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
   
      turnCounter= 0;
      turnNumLabel= new JLabel("Turn: " + turnCounter);
      turnNumLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
   
      statusPanel.add(turnLabel);
      statusPanel.add(Box.createHorizontalStrut(180));
      statusPanel.add(turnNumLabel);
   
      msgPanel.add(statusPanel);
            
      if(clientThread.getIsTurn())
         msgLabel= new JLabel("Make your first attack.");
      else
         msgLabel= new JLabel("Scanning for enemy attack.");
      msgLabel.setFont(new Font("Arial",Font.BOLD,16));
      msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      msgLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
      msgPanel.add(msgLabel);
      
      yourGrid= new BSGrid(10); //player's grid (horizontal grid in physical game)
            
      gamePanel.add(enemyGrid);
      gamePanel.add(Box.createVerticalStrut(10));
      gamePanel.add(new JSeparator(SwingConstants.HORIZONTAL));
      gamePanel.add(msgPanel);
      gamePanel.add(new JSeparator(SwingConstants.HORIZONTAL));
      gamePanel.add(yourGrid);
      gamePanel.add(Box.createVerticalStrut(8));
      
      shipsRemainingPanel= new JPanel(); //displays enemy ships remaining
      shipsRemainingPanel.setLayout(new BoxLayout(shipsRemainingPanel, BoxLayout.Y_AXIS));
      
      shipsRemainingLabel= new JLabel("<html><center>Enemy<br>Ships<br>Remaining</center></html>");
      shipsRemainingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      shipsRemainingLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
               
      battleship= new JLabel(new ImageIcon(getImageFile("/resources/ba_ver.png")));
      battleship.setAlignmentX(Component.CENTER_ALIGNMENT);
      battleship.setAlignmentY(Component.CENTER_ALIGNMENT);
   
      carrier= new JLabel(new ImageIcon(getImageFile("/resources/ca_ver.png")));
      carrier.setAlignmentX(Component.CENTER_ALIGNMENT);
      carrier.setAlignmentY(Component.CENTER_ALIGNMENT);
   
      cruiser= new JLabel(new ImageIcon(getImageFile("/resources/cr_ver.png")));
      cruiser.setAlignmentX(Component.CENTER_ALIGNMENT);
      cruiser.setAlignmentY(Component.CENTER_ALIGNMENT);
   
      destroyer= new JLabel(new ImageIcon(getImageFile("/resources/de_ver.png")));
      destroyer.setAlignmentX(Component.CENTER_ALIGNMENT);
      destroyer.setAlignmentY(Component.CENTER_ALIGNMENT);
   
      submarine= new JLabel(new ImageIcon(getImageFile("/resources/su_ver.png")));
      submarine.setAlignmentX(Component.CENTER_ALIGNMENT);
      submarine.setAlignmentY(Component.CENTER_ALIGNMENT);
      
      shipsRemainingPanel.add(Box.createVerticalStrut(15));
      shipsRemainingPanel.add(shipsRemainingLabel);
      shipsRemainingPanel.add(Box.createVerticalStrut(15));
      shipsRemainingPanel.add(battleship);
      shipsRemainingPanel.add(Box.createVerticalStrut(15));
      shipsRemainingPanel.add(carrier);
      shipsRemainingPanel.add(Box.createVerticalStrut(15));
      shipsRemainingPanel.add(cruiser);
      shipsRemainingPanel.add(Box.createVerticalStrut(15));
      shipsRemainingPanel.add(destroyer);
      shipsRemainingPanel.add(Box.createVerticalStrut(15));
      shipsRemainingPanel.add(submarine);
      shipsRemainingPanel.add(Box.createVerticalStrut(15));
                  
      theFrame.add(gamePanel);
      theFrame.add(new JSeparator(SwingConstants.VERTICAL));
      theFrame.add(shipsRemainingPanel);
      theFrame.add(Box.createHorizontalStrut(8));
      theFrame.pack();
      theFrame.setLocationRelativeTo(null);
      theFrame.setVisible(true);
            
      yourShips= yS;
      placeYourShips(yourShips);
   }
   
   private void placeYourShips(Ship[] yourShips) //adds your ships to your grid
   {
      StringBuilder imgName= new StringBuilder("/resources/");
      BufferedImage shipImg;
      BufferedImage bgImg= getImageFile("/resources/ocean.png");
      BufferedImage combinedImg= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
      BufferedImage hitPegImg= getImageFile("/resources/hit_peg.png");
      BufferedImage disabledImg= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
      JLabel temp;
      Graphics g;
      int xPos= 0, yPos= 0;
      int i,j;
      
      for(i=0;i<yourShips.length;i++)
      {
         imgName.append(yourShips[i].getName() + "_");
         if(yourShips[i].getOrientation()==0)
            imgName.append("hor.png");
         else
            imgName.append("ver.png");
               
         shipImg= getImageFile(imgName.toString());
         
         for(j=0;j<yourShips[i].getSize();j++) //overlays ship, grid space by grid space, over ocean
         {
            temp= (JLabel)yourGrid.findComponentAt(yourShips[i].getLocation(j)); //gets JLabel for target grid space
            
            g= combinedImg.getGraphics();
               
            g.drawImage(bgImg,0,0,null); //draws ocean background and section of ship
            g.drawImage(shipImg.getSubimage(xPos,yPos,30,30),0,0,null);
                                    
            temp.setIcon(new ImageIcon(combinedImg)); //sets new image for grid space
            
            g= disabledImg.getGraphics();
            
            g.drawImage(combinedImg,0,0,null); //draws hit peg over ship
            g.drawImage(hitPegImg,0,0,null);
            
            temp.setDisabledIcon(new ImageIcon(disabledImg)); //sets hit peg image as disabled icon
            
            if(yourShips[i].getOrientation()==0) //advances across or down ship image
               xPos= xPos+30;
            else
               yPos= yPos+30; 
                  
            combinedImg= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
            disabledImg= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
         }
         
         imgName= new StringBuilder("/resources/");
         xPos= 0;
         yPos= 0;
      }
   } 
      
   public Attack processAttack(Attack toProccess)
   {      
      JLabel temp= null;
      ImageIcon temp2;
      int i;
      boolean shipHit= false;
      boolean endGame= true;
      StringBuilder message= new StringBuilder();
      
      if(toProccess==null) //player receives null if an error has occured
         throw new NullPointerException();
      
      if(clientThread.getIsTurn()) //if player's turn
      {
         if(turnCounter==0) //for first turn only
         {
            waitDialog.close();
            JOptionPane.showMessageDialog(theFrame, "Attack launched! Prepare for return fire!\nBattle stations! Battle stations!\nThis is not a drill!", "Attention Admiral!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getImageFile("/resources/alarm.png")));
         }
         
         if(toProccess.getEndGame()) //if game is over
            new GameOverDialog(true);   
         
         temp= (JLabel)enemyGrid.findComponentAt(toProccess.getCoords()); //gets JLabel for target grid space on enemy grid
         
         message.append("Attack at " + toProccess.getCoordName());
         
         if(toProccess.getIsHit()) //if attack is hit
         {
            BufferedImage a= getImageFile("/resources/ocean.png"); //changes disabled image from miss to hit
            BufferedImage b= getImageFile("/resources/hit_peg.png");
            BufferedImage combined= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
            Graphics g= combined.getGraphics();
               
            g.drawImage(a,0,0,null);
            g.drawImage(b,0,0,null);
               
            temp.setDisabledIcon(new ImageIcon(combined));
            
            if(toProccess.getShipName().equals("ba"))
               message.append(" hits enemy battleship!");
            else if(toProccess.getShipName().equals("ca"))
               message.append(" hits enemy carrier!");
            else if(toProccess.getShipName().equals("cr"))
               message.append(" hits enemy cruiser!");
            else if(toProccess.getShipName().equals("de"))
               message.append(" hits enemy destroyer!");
            else
               message.append(" hits enemy submarine!");
               
            if(toProccess.getShipSunk())
               new ShipSunkDialog(true, toProccess.getShipName());   
         }
         else
            message.append(" misses.");            
      }
      else //if other player's turn
      {
         if(turnCounter==0)
            JOptionPane.showMessageDialog(theFrame, "Enemy fleet activity detected!\nBattle stations! Battle stations!\nThis is not a drill!", "Attention Admiral!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getImageFile("/resources/alarm.png")));
         
         temp= (JLabel)yourGrid.findComponentAt(toProccess.getCoords()); //gets JLabel for target grid space on your grid
         
         message.append("Enemy attack at " + toProccess.getCoordName());
         
         for(i=0;i<yourShips.length;i++) //iterates through ships to determine if attack is hit
         {
            shipHit= yourShips[i].hitSpace(toProccess.getCoords());
            
            if(shipHit)
            {
               toProccess.setIsHit(yourShips[i].getName(),yourShips[i].getIsSunk());
              
               if(toProccess.getShipName().equals("ba"))
                  message.append(" hits battleship!");
               else if(toProccess.getShipName().equals("ca"))
                  message.append(" hits carrier!");
               else if(toProccess.getShipName().equals("cr"))
                  message.append(" hits cruiser!");
               else if(toProccess.getShipName().equals("de"))
                  message.append(" hits destroyer!");
               else
                  message.append(" hits submarine!");
                      
               break;
            }   
         }
         
         if(!shipHit)
            message.append(" misses.");
                     
         for(i=0;i<yourShips.length;i++) //checks for game over
         {
            if(!yourShips[i].getIsSunk())
               endGame= false;
         }
         
         if(endGame)
            new GameOverDialog(false);
         
         toProccess.setEndGame(endGame);
         
         if(toProccess.getShipSunk())
            new ShipSunkDialog(false, toProccess.getShipName());            
      }
      
      temp.setEnabled(false); //disables attacked grid space (so you cant attack it again)
      msgLabel.setText(message.toString());
      return(toProccess);
   }
   
   public void updateTurnLabels(boolean turn)
   {
      if(turn)
         turnLabel.setText("Your Turn");
      else
         turnLabel.setText("Enemy Turn");
      
      turnCounter++;
      turnNumLabel.setText("Turn: " + turnCounter);      
   } 
   
   private BufferedImage getImageFile(String filename) //JAR-friendly method to load image files
   {
      try
      {
         return((BufferedImage)ImageIO.read(BattleshipUI.class.getResourceAsStream(filename)));
      }
      catch(Exception e)
      {
         JOptionPane.showMessageDialog(null ,"Error loading image resource.", "Load Error", JOptionPane.ERROR_MESSAGE);          
         System.exit(1);
      }
      
      return(null);
   }
         
   private class MouseHandler extends MouseAdapter //handles mouse clicks on enemy grid
   {
      public void mouseClicked(MouseEvent e)
      {
         int xPos= e.getX();
         int yPos= e.getY();
         JLabel temp= (JLabel)enemyGrid.findComponentAt(xPos,yPos); //gets JLabel of grid space at clicked point
         
         if(xPos>=30 && yPos>=30 && clientThread.getIsTurn() && temp.isEnabled()) //if click not on column/row labels, it's players turn
         {                                                                        //and grid space has not already been picked
            Point clicked;
            char yGrid= 64;
            int xGrid;
            StringBuilder gridLoc= new StringBuilder();
            Attack userAttack;
         
            clicked= new Point(xPos, yPos);
            yGrid= (char)(yGrid + (yPos/30));
            xGrid= xPos/30;
            gridLoc.append(yGrid + Integer.toString(xGrid));
            
            if(turnCounter==0) //first turn only
               waitDialog= new WaitDialog();
            
            clientThread.sendAttack(new Attack(clicked, gridLoc.toString())); //sends new attack
         }
      }
   }
   
   private class WaitDialog extends JDialog //dialog shown after first attack and before other player has
   {                                        //finished placing thier ships
      private JPanel waitPanel;
      private JLabel waitIcon;
      private JLabel waitMsg;
      
      public WaitDialog()
      {
         super(theFrame,"Targeting",false);
         waitPanel= new JPanel();
         waitPanel.setLayout(new BoxLayout(waitPanel, BoxLayout.X_AXIS));
         waitPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         waitIcon= new JLabel(new ImageIcon(getImageFile("/resources/sonar.png")));
         waitPanel.add(waitIcon);
         waitPanel.add(Box.createRigidArea(new Dimension(5,0)));
         waitMsg= new JLabel("Targeting enemy fleet.");
         waitPanel.add(waitMsg);
         this.setContentPane(waitPanel);
         this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         this.pack();
         this.setResizable(false);
         this.setLocationRelativeTo(theFrame);
         this.setVisible(true);
      }
      
      public void close()
      {
         this.setVisible(false);
         this.dispose();
      }
   }
      
   private class ShipSunkDialog extends JDialog implements ActionListener //dialog shown when ship is sunk
   {
      private JPanel shipSunkPanel;
      private JPanel shipSunkMsgPanel;
      private JPanel shipSunkButtonPanel;
      private JLabel shipSunkIcon;
      private JLabel shipSunkMsg;
      private JButton shipSunkButton;
      
      public ShipSunkDialog(boolean turn, String shipName)
      {
         super(theFrame,"Attention Admiral!",false);
         shipSunkPanel= new JPanel();
         shipSunkPanel.setLayout(new BoxLayout(shipSunkPanel, BoxLayout.Y_AXIS));
         shipSunkPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         
         shipSunkMsgPanel= new JPanel();
         shipSunkMsgPanel.setLayout(new BoxLayout(shipSunkMsgPanel, BoxLayout.X_AXIS));
                  
         shipSunkIcon= new JLabel(new ImageIcon(getImageFile("/resources/sinking.png")));
         shipSunkMsgPanel.add(shipSunkIcon);
      
         shipSunkMsgPanel.add(Box.createRigidArea(new Dimension(5,0)));
         
         if(turn) //if your turn
         {
            if(shipName.equals("ba"))
            {
               shipSunkMsg= new JLabel("Enemy battleship has been sunk!");
               battleship.setEnabled(false);
            }
            else if(shipName.equals("ca"))
            {
               shipSunkMsg= new JLabel("Enemy carrier has been sunk!");
               carrier.setEnabled(false);
            }
            else if(shipName.equals("cr"))
            {
               shipSunkMsg= new JLabel("Enemy cruiser has been sunk!");
               cruiser.setEnabled(false);
            }
            else if(shipName.equals("de"))
            {
               shipSunkMsg= new JLabel("Enemy destroyer has been sunk!");
               destroyer.setEnabled(false);
            }
            else
            {
               shipSunkMsg= new JLabel("Enemy submarine has been sunk!");
               submarine.setEnabled(false);
            }
         }
         else //if enemy turn
         {
            if(shipName.equals("ba"))
               shipSunkMsg= new JLabel("Battleship has been sunk!");
            else if(shipName.equals("ca"))
               shipSunkMsg= new JLabel("Carrier has been sunk!");
            else if(shipName.equals("cr"))
               shipSunkMsg= new JLabel("Cruiser has been sunk!");
            else if(shipName.equals("de"))
               shipSunkMsg= new JLabel("Destroyer has been sunk!");
            else
               shipSunkMsg= new JLabel("Submarine has been sunk!");
         }
         shipSunkMsgPanel.add(shipSunkMsg);
         
         shipSunkPanel.add(shipSunkMsgPanel);
         
         shipSunkButtonPanel= new JPanel();
         
         shipSunkButton= new JButton("Continue");
         shipSunkButton.addActionListener(this);
         shipSunkButtonPanel.add(shipSunkButton);
         
         shipSunkPanel.add(shipSunkButtonPanel);
         
         this.setContentPane(shipSunkPanel);
         this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         this.pack();
         this.setResizable(false);
         this.setLocationRelativeTo(theFrame);
         this.setVisible(true);
      }
      
      public void actionPerformed(ActionEvent e)
      {
         this.setVisible(false);
         this.dispose();
      }
   }
      
   private class GameOverDialog extends JDialog implements ActionListener //dialog shown when game is over
   {
      private JPanel gameOverPanel;
      private JLabel gameOverIcon;
      private JLabel gameOverMsg;
      private JButton gameOverButton;
         
      public GameOverDialog(boolean win)
      {
         super(theFrame,"Game Over",false);
         gameOverPanel= new JPanel();
         gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
         gameOverPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         
         if(win) //if player has won
         {
            gameOverIcon= new JLabel(new ImageIcon(getImageFile("/resources/you_win.png")));
            gameOverMsg= new JLabel("Congratulations! Thanks for playing!");
         }
         else //if player has lost
         {
            gameOverIcon= new JLabel(new ImageIcon(getImageFile("/resources/you_lose.png")));
            gameOverMsg= new JLabel("Better luck next time. Thanks for playing!");
         }   
                  
         gameOverIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
         gameOverPanel.add(gameOverIcon);
         
         gameOverMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
         gameOverPanel.add(gameOverMsg);
         
         gameOverButton= new JButton("Close");
         gameOverButton.addActionListener(this);
         gameOverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
         gameOverPanel.add(gameOverButton);
         
         this.setContentPane(gameOverPanel);
         this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         this.pack();
         this.setResizable(false);
         this.setLocationRelativeTo(theFrame);
         this.setVisible(true);
      }
         
      public void actionPerformed(ActionEvent e) //closes program when when close is clicked
      {
         System.exit(0);
      }
   }

}