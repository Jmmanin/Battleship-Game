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
   
   private JDialog waitBox;
   private JPanel waitPanel;
   private JLabel waitIcon;
   private JLabel waitMsg;
   
   private JDialog gameOverBox;
   private JPanel gameOverPanel;
   private JLabel gameOverIcon;
   private JLabel gameOverMsg;
   private JButton gameOverButton;
   
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
      turnNumLabel= new JLabel("Turns " + turnCounter);
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
      
      gameOverBox= new JDialog(gameUI,"Game Over",true);
      gameOverPanel= new JPanel();
      gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
      gameOverPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      gameOverIcon= new JLabel(new ImageIcon("resources/you_win.png"));
      gameOverIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
      gameOverPanel.add(gameOverIcon);
      gameOverMsg= new JLabel("Better luck next time. Thanks for playing!");
      gameOverMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
      gameOverPanel.add(gameOverMsg);
      gameOverButton= new JButton("Close");
      gameOverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
      //gameOverButton.addActionListener(new GameOverListener()); //MAKE CLASS FOR
      gameOverPanel.add(gameOverButton);
      gameOverBox.setContentPane(gameOverPanel);
      gameOverBox.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      gameOverBox.pack();
      gameOverBox.setResizable(false);
      gameOverBox.setLocationRelativeTo(gameUI);
            
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
      JLabel temp;
      ImageIcon temp2;
      int i;
      boolean shipHit= false;
      boolean endGame= true;
      
      if(clientThread.getIsTurn())
      {
         if(turnCounter==0)
         {
            waitBox.setVisible(false);
            waitBox.dispose();
            JOptionPane.showMessageDialog(gameUI, "Attack launched! Prepare for return fire!\nBattle stations! Battle stations!\nThis is not a drill!", "Attention Admiral!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/alarm.png"));
         }
         
         temp= (JLabel)enemyGrid.findComponentAt(toProccess.getCoords());
         
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
         }
            
         temp.setEnabled(false);
         
         return(toProccess);            
      }
      else
      {
         if(turnCounter==0)
            JOptionPane.showMessageDialog(gameUI, "Enemy fleet activity detected!\nBattle stations! Battle stations!\nThis is not a drill!", "Attention Admiral!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/alarm.png"));
         
         temp= (JLabel)yourGrid.findComponentAt(toProccess.getCoords());
         temp.setEnabled(false);
         
         for(i=0;i<yourShips.length;i++)
         {
            shipHit= yourShips[i].hitSpace(toProccess.getCoords());
            
            if(shipHit)
            {
               toProccess.setIsHit(yourShips[i].getName(),yourShips[i].getIsSunk());
               break;
            }   
         }
         
         for(i=0;i<yourShips.length;i++)
         {
            if(!yourShips[i].getIsSunk())
               endGame= false;
         }
         
         toProccess.setEndGame(endGame);
         
         return(toProccess);
      }
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
               waitBox.setVisible(true);
            
            clientThread.sendAttack(new Attack(clicked, gridLoc.toString()));
         }
      }
   }
   
   /*public static void main(String args[])
   {
      new BattleshipUI(null);
   }*/
}