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
      
      gamePanel= new JPanel();
      gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
      
      enemyGrid= new BSGrid(10);
      enemyGrid.addMouseListener(new MouseHandler());
            
      msgPanel= new JPanel();
      msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
            
      statusPanel= new JPanel();
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
      
      yourGrid= new BSGrid(10);
            
      gamePanel.add(enemyGrid);
      gamePanel.add(Box.createVerticalStrut(10));
      gamePanel.add(new JSeparator(SwingConstants.HORIZONTAL));
      gamePanel.add(msgPanel);
      gamePanel.add(new JSeparator(SwingConstants.HORIZONTAL));
      gamePanel.add(yourGrid);
      gamePanel.add(Box.createVerticalStrut(8));
      
      shipsRemainingPanel= new JPanel();
      shipsRemainingPanel.setLayout(new BoxLayout(shipsRemainingPanel, BoxLayout.Y_AXIS));
      
      shipsRemainingLabel= new JLabel("<html><center>Enemy<br>Ships<br>Remaining</center></html>");
      shipsRemainingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      shipsRemainingLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
               
      battleship= new JLabel(new ImageIcon("resources/ba_ver.png"));
      battleship.setAlignmentX(Component.CENTER_ALIGNMENT);
      battleship.setAlignmentY(Component.CENTER_ALIGNMENT);
   
      carrier= new JLabel(new ImageIcon("resources/ca_ver.png"));
      carrier.setAlignmentX(Component.CENTER_ALIGNMENT);
      carrier.setAlignmentY(Component.CENTER_ALIGNMENT);
   
      cruiser= new JLabel(new ImageIcon("resources/cr_ver.png"));
      cruiser.setAlignmentX(Component.CENTER_ALIGNMENT);
      cruiser.setAlignmentY(Component.CENTER_ALIGNMENT);
   
      destroyer= new JLabel(new ImageIcon("resources/de_ver.png"));
      destroyer.setAlignmentX(Component.CENTER_ALIGNMENT);
      destroyer.setAlignmentY(Component.CENTER_ALIGNMENT);
   
      submarine= new JLabel(new ImageIcon("resources/su_ver.png"));
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
   
   private void placeYourShips(Ship[] yourShips)
   {
      try
      {
         StringBuilder imgName= new StringBuilder("resources/");
         BufferedImage shipImg;
         BufferedImage bgImg= ImageIO.read(new File("resources/ocean.png"));
         BufferedImage combinedImg= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
         BufferedImage hitPegImg= ImageIO.read(new File("resources/hit_peg.png"));
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
               
            shipImg= ImageIO.read(new File(imgName.toString()));
         
            for(j=0;j<yourShips[i].getSize();j++)
            {
               temp= (JLabel)yourGrid.findComponentAt(yourShips[i].getLocation(j));
            
               g= combinedImg.getGraphics();
               
               g.drawImage(bgImg,0,0,null);
               g.drawImage(shipImg.getSubimage(xPos,yPos,30,30),0,0,null);
                                    
               temp.setIcon(new ImageIcon(combinedImg));
            
               g= disabledImg.getGraphics();
            
               g.drawImage(combinedImg,0,0,null);
               g.drawImage(hitPegImg,0,0,null);
            
               temp.setDisabledIcon(new ImageIcon(disabledImg));
            
               if(yourShips[i].getOrientation()==0)
                  xPos= xPos+30;
               else
                  yPos= yPos+30; 
                  
               combinedImg= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
               disabledImg= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
            }
         
            imgName= new StringBuilder("resources/");
            xPos= 0;
            yPos= 0;
         }
      }
      catch(IOException e)
      {
         e.printStackTrace();
      }
   } 
      
   public Attack proccessAttack(Attack toProccess)
   {      
      JLabel temp= null;
      ImageIcon temp2;
      int i;
      boolean shipHit= false;
      boolean endGame= true;
      StringBuilder message= new StringBuilder();
      
      if(clientThread.getIsTurn())
      {
         if(turnCounter==0)
         {
            waitDialog.close();
            JOptionPane.showMessageDialog(theFrame, "Attack launched! Prepare for return fire!\nBattle stations! Battle stations!\nThis is not a drill!", "Attention Admiral!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/alarm.png"));
         }
         
         if(toProccess.getEndGame())
            new GameOverDialog(true);   
         
         temp= (JLabel)enemyGrid.findComponentAt(toProccess.getCoords());
         
         message.append("Attack at " + toProccess.getCoordName());
         
         if(toProccess.getIsHit())
         {
            try
            {
               temp2= (ImageIcon)temp.getIcon();
               BufferedImage a= ImageIO.read(new File(temp2.getDescription()));
               BufferedImage b= ImageIO.read(new File("resources/hit_peg.png"));
               BufferedImage combined= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
               Graphics g= combined.getGraphics();
               
               g.drawImage(a,0,0,null);
               g.drawImage(b,0,0,null);
               
               temp.setDisabledIcon(new ImageIcon(combined));
            }
            catch(IOException e){}
            
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
      else
      {
         if(turnCounter==0)
            JOptionPane.showMessageDialog(theFrame, "Enemy fleet activity detected!\nBattle stations! Battle stations!\nThis is not a drill!", "Attention Admiral!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/alarm.png"));
         
         temp= (JLabel)yourGrid.findComponentAt(toProccess.getCoords());
         
         message.append("Enemy attack at " + toProccess.getCoordName());
         
         for(i=0;i<yourShips.length;i++)
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
                     
         for(i=0;i<yourShips.length;i++)
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
      
      temp.setEnabled(false);
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
      
   private class MouseHandler extends MouseAdapter
   {
      public void mouseClicked(MouseEvent e)
      {
         int xPos= e.getX();
         int yPos= e.getY();
         
         if(xPos>=30 && yPos>=30 && clientThread.getIsTurn())
         {
            Point clicked;
            char yGrid= 64;
            int xGrid;
            StringBuilder gridLoc= new StringBuilder();
            Attack userAttack;
         
            clicked= new Point(xPos, yPos);
            yGrid= (char)(yGrid + (yPos/30));
            xGrid= xPos/30;
            gridLoc.append(yGrid + Integer.toString(xGrid));
            
            if(turnCounter==0)
               waitDialog= new WaitDialog();
            
            clientThread.sendAttack(new Attack(clicked, gridLoc.toString()));
         }
      }
   }
   
   public class WaitDialog extends JDialog
   {
      private JDialog waitBox;
      private JPanel waitPanel;
      private JLabel waitIcon;
      private JLabel waitMsg;
      
      public WaitDialog()
      {
         waitBox= new JDialog(theFrame,"Targeting",false);
         waitPanel= new JPanel();
         waitPanel.setLayout(new BoxLayout(waitPanel, BoxLayout.X_AXIS));
         waitPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         waitIcon= new JLabel(new ImageIcon("resources/sonar.png"));
         waitPanel.add(waitIcon);
         waitPanel.add(Box.createRigidArea(new Dimension(5,0)));
         waitMsg= new JLabel("Targeting enemy fleet.");
         waitPanel.add(waitMsg);
         waitBox.setContentPane(waitPanel);
         waitBox.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         waitBox.pack();
         waitBox.setResizable(false);
         waitBox.setLocationRelativeTo(theFrame);
         waitBox.setVisible(true);
      }
      
      public void close()
      {
         waitBox.setVisible(false);
         waitBox.dispose();
      }
   }
      
   public class ShipSunkDialog extends JDialog implements ActionListener
   {
      private JDialog shipSunkBox;
      private JPanel shipSunkPanel;
      private JPanel shipSunkMsgPanel;
      private JPanel shipSunkButtonPanel;
      private JLabel shipSunkIcon;
      private JLabel shipSunkMsg;
      private JButton shipSunkButton;
      
      public ShipSunkDialog(boolean turn, String shipName)
      {
         shipSunkBox= new JDialog(theFrame,"Attention Admiral!",false);
         shipSunkPanel= new JPanel();
         shipSunkPanel.setLayout(new BoxLayout(shipSunkPanel, BoxLayout.Y_AXIS));
         shipSunkPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         
         shipSunkMsgPanel= new JPanel();
         shipSunkMsgPanel.setLayout(new BoxLayout(shipSunkMsgPanel, BoxLayout.X_AXIS));
                  
         shipSunkIcon= new JLabel(new ImageIcon("resources/sinking.png"));
         shipSunkMsgPanel.add(shipSunkIcon);
      
         shipSunkMsgPanel.add(Box.createRigidArea(new Dimension(5,0)));
         
         if(turn)
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
         else
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
         
         shipSunkBox.setContentPane(shipSunkPanel);
         shipSunkBox.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         shipSunkBox.pack();
         shipSunkBox.setResizable(false);
         shipSunkBox.setLocationRelativeTo(theFrame);
         shipSunkBox.setVisible(true);
      }
      
      public void actionPerformed(ActionEvent e)
      {
         shipSunkBox.setVisible(false);
         shipSunkBox.dispose();
      }
   }
      
   public class GameOverDialog extends JDialog implements ActionListener
   {
      private JDialog gameOverBox;
      private JPanel gameOverPanel;
      private JLabel gameOverIcon;
      private JLabel gameOverMsg;
      private JButton gameOverButton;
         
      public GameOverDialog(boolean win)
      {
         gameOverBox= new JDialog(theFrame,"Game Over",false);
         gameOverPanel= new JPanel();
         gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
         gameOverPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         
         if(win)
         {
            gameOverIcon= new JLabel(new ImageIcon("resources/you_win.png"));
            gameOverMsg= new JLabel("Congratulations! Thanks for playing!");
         }
         else
         {
            gameOverIcon= new JLabel(new ImageIcon("resources/you_lose.png"));
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
         
         gameOverBox.setContentPane(gameOverPanel);
         gameOverBox.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         gameOverBox.pack();
         gameOverBox.setResizable(false);
         gameOverBox.setLocationRelativeTo(theFrame);
         gameOverBox.setVisible(true);
      }
         
      public void actionPerformed(ActionEvent e)
      {
         System.exit(0);
      }
   }
   
   /*public static void main(String args[])
   {
      new BattleshipUI(null);
   }*/
}