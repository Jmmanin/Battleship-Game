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
   private JFrame gameUI;
   private BSGrid enemyGrid;
   private JPanel msgPanel;
   private JLabel msgLabel;
   private BSGrid yourGrid;
   private JPanel statusPanel;
   private JLabel turnLabel;
   private JLabel turnNumLabel;
   private JLabel statusTime;
   
   private WaitDialog waitDialog;
   
   private int turnCounter;
   private Ship[] yourShips;
   
   private BSClientThread clientThread;  
   
   public BattleshipUI(Ship[] yS, BSClientThread cT)
   {
      clientThread= cT;
      
      gameUI= new JFrame("Battleship Game");
      gameUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      gameUI.setResizable(false);
      gameUI.getContentPane().setLayout(new BoxLayout(gameUI.getContentPane(), BoxLayout.Y_AXIS));
      
      enemyGrid= new BSGrid(10);
      enemyGrid.addMouseListener(new MouseHandler());
            
      msgPanel= new JPanel();
      msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
            
      statusPanel= new JPanel();
      statusPanel.setLayout(new GridLayout(1,3));
      
      if(clientThread.getIsTurn())
         turnLabel= new JLabel("Your Turn");
      else
         turnLabel= new JLabel("Enemy Turn");
      turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
   
      turnCounter= 0;
      turnNumLabel= new JLabel("Turn: " + turnCounter);
      turnNumLabel.setHorizontalAlignment(SwingConstants.CENTER);
   
      statusPanel.add(turnLabel);
      statusPanel.add(new JLabel());
      statusPanel.add(turnNumLabel);
   
      msgPanel.add(statusPanel);
            
      if(clientThread.getIsTurn())
         msgLabel= new JLabel("Make your first attack.");
      else
         msgLabel= new JLabel("Scanning for enemy attack.");
      msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      msgLabel.setFont(new Font("Arial",Font.BOLD,16));
      msgPanel.add(msgLabel);
      
      yourGrid= new BSGrid(10);
            
      gameUI.add(enemyGrid);
      gameUI.add(new JSeparator(SwingConstants.HORIZONTAL));
      gameUI.add(msgPanel);
      gameUI.add(new JSeparator(SwingConstants.HORIZONTAL));
      gameUI.add(yourGrid);
      gameUI.add(Box.createRigidArea(new Dimension(0,5)));
      
      gameUI.pack();
      gameUI.setLocationRelativeTo(null);
      gameUI.setVisible(true);
            
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
            JOptionPane.showMessageDialog(gameUI, "Attack launched! Prepare for return fire!\nBattle stations! Battle stations!\nThis is not a drill!", "Attention Admiral!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/alarm.png"));
         }
         
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
            
         if(toProccess.getEndGame())
            new GameOverDialog(true);   
      }
      else
      {
         if(turnCounter==0)
            JOptionPane.showMessageDialog(gameUI, "Enemy fleet activity detected!\nBattle stations! Battle stations!\nThis is not a drill!", "Attention Admiral!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/alarm.png"));
         
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
         
         if(toProccess.getShipSunk())
            new ShipSunkDialog(false, toProccess.getShipName());   
         
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
         waitBox= new JDialog(gameUI,"Targeting",false);
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
         waitBox.setLocationRelativeTo(gameUI);
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
         shipSunkBox= new JDialog(gameUI,"Attention Admiral!",false);
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
               shipSunkMsg= new JLabel("Enemy battleship has been sunk!");
            else if(shipName.equals("ca"))
               shipSunkMsg= new JLabel("Enemy carrier has been sunk!");
            else if(shipName.equals("cr"))
               shipSunkMsg= new JLabel("Enemy cruiser has been sunk!");
            else if(shipName.equals("de"))
               shipSunkMsg= new JLabel("Enemy destroyer has been sunk!");
            else
               shipSunkMsg= new JLabel("Enemy submarine has been sunk!");
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
               shipSunkMsg= new JLabel("Battleship has been sunk!");
            else
               shipSunkMsg= new JLabel("Destroyer has been sunk!");
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
         shipSunkBox.setLocationRelativeTo(gameUI);
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
         gameOverBox= new JDialog(gameUI,"Game Over",false);
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
         gameOverBox.setLocationRelativeTo(gameUI);
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