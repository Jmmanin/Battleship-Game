/*
Multiplayer Battleship Game
Game UI
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
   private JLabel enemyLabel;
   private BSGrid yourGrid;
   private JLabel yourLabel;
   private JPanel statusPanel;
   private JLabel statusTurn;
   private JLabel statusTurnNum;
   private JLabel statusTime;
   
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
      
      yourGrid= new BSGrid(10);
      
      enemyLabel= new JLabel("Enemy Grid");
      enemyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      yourLabel= new JLabel("Your Grid");
      yourLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
   
      statusPanel= new JPanel();
      statusPanel.setLayout(new GridLayout(1,3));
      
      if(clientThread.getIsTurn())
         statusTurn= new JLabel("Your Turn");
      else
         statusTurn= new JLabel("Enemy Turn");
   
      statusTurnNum= new JLabel("0 Turns");
      statusTurnNum.setHorizontalAlignment(SwingConstants.CENTER);
      statusTime= new JLabel("0:00");
      statusTime.setHorizontalAlignment(SwingConstants.RIGHT);
   
      statusPanel.add(statusTurn);
      statusPanel.add(statusTurnNum);
      statusPanel.add(statusTime);
   
      gameUI.add(enemyLabel);
      gameUI.add(enemyGrid);
      gameUI.add(yourLabel);
      gameUI.add(yourGrid);
      gameUI.add(statusPanel);
      
      gameUI.pack();
      gameUI.setLocationRelativeTo(null);
      gameUI.setVisible(true);
      
      yourShips= yS;
      placeYourShips(yourShips);
                  
      JOptionPane.showMessageDialog(gameUI, "Enemy fleet located!\nBattle stations! Battle stations!\nThis is not a drill!", "Attention Admiral!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/alarm.png"));
   } 
   
   public Attack proccessAttack(Attack toProcess)
   {
      if(clientThread.getIsTurn())
      {
         System.out.println("Attack Processed");
         return(toProcess);            
      }
      else
      {
         System.out.println("Attack Processed");
         return(toProcess);
      }
   }
   
   public void updateTurnLabel(boolean turn)
   {
      if(turn)
         statusTurn.setText("Your Turn");
      else
         statusTurn.setText("Enemy Turn");   
   }     
   
   private void placeYourShips(Ship[] yourShips)
   {
      StringBuilder imgName= new StringBuilder("resources/");
      int i,j;
      
      for(i=0;i<yourShips.length;i++)
      {         
         imgName.append(yourShips[i].getName() + "_");
         
         if(yourShips[i].getOrientation()==0)
         {
            imgName.append("hor_");
               
            for(j=1;j<=yourShips[i].getSize();j++)
            {
               JLabel temp= (JLabel)yourGrid.findComponentAt(yourShips[i].getLocation(j-1));
               imgName.append(j + ".png");
               temp.setIcon(new ImageIcon(imgName.toString()));
               
               try
               {
                  BufferedImage a= ImageIO.read(new File(imgName.toString()));
                  BufferedImage b= ImageIO.read(new File("resources/hit_peg.png"));
                  BufferedImage combined= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
                  Graphics g= combined.getGraphics();
               
                  g.drawImage(a,0,0,null);
                  g.drawImage(b,0,0,null);
               
                  temp.setDisabledIcon(new ImageIcon(combined));
               }
               catch(IOException e){}
                           
               imgName.delete(17,22);
            }            
         }
         else
         {
            imgName.append("ver_");
               
            for(j=1;j<=yourShips[i].getSize();j++)
            {
               JLabel temp= (JLabel)yourGrid.findComponentAt(yourShips[i].getLocation(j-1));
               imgName.append(j + ".png");
               temp.setIcon(new ImageIcon(imgName.toString()));
               
               try
               {
                  BufferedImage a= ImageIO.read(new File(imgName.toString()));
                  BufferedImage b= ImageIO.read(new File("resources/hit_peg.png"));
                  BufferedImage combined= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
                  Graphics g= combined.getGraphics();
               
                  g.drawImage(a,0,0,null);
                  g.drawImage(b,0,0,null);
               
                  temp.setDisabledIcon(new ImageIcon(combined));
               }
               catch(IOException e){}
                           
               imgName.delete(17,22);
            }
         }   
      
         imgName= new StringBuilder("resources/");
      }      
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
            
            clientThread.sendAttack(new Attack(clicked, gridLoc.toString()));
            
            System.out.println("Attack Sent");             
         }
      }
   }
   
   /*public static void main(String args[])
   {
      new BattleshipUI(null);
   }*/
}