/*
Multiplayer Battleship Game
Game UI
Jeremy Manin and John Dott
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
   
   public BattleshipUI(Ship[] yourShips, String hN, int pN1, int pN2)
   {
      gameUI= new JFrame("Battleship Game");
      gameUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      gameUI.setResizable(false);
      gameUI.getContentPane().setLayout(new BoxLayout(gameUI.getContentPane(), BoxLayout.Y_AXIS));
      
      enemyGrid= new BSGrid(10);
      enemyGrid.addMouseListener(new MouseHandler());
      
      yourGrid= new BSGrid(10);
      yourGrid.addMouseListener(new MouseHandler());
      
      enemyLabel= new JLabel("Enemy Grid");
      enemyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      yourLabel= new JLabel("Your Grid");
      yourLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
   
      statusPanel= new JPanel();
      statusPanel.setLayout(new GridLayout(1,3));
      
      statusTurn= new JLabel("Your Turn");
      //statusTurn.setFont(
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
      
      placeYourShips(yourShips);
            
      JOptionPane.showMessageDialog(gameUI, "Enemy fleet located!\nBattle stations! Battle stations!\nThis is not a drill!", "Attention Admiral!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("resources/alarm.png"));
   }   
   
   private void placeYourShips(Ship[] yourShips)
   {
      StringBuilder imgName= new StringBuilder("resources/");
      int i,j;
      
      for(i=0;i<yourShips.length;i++)
      {
         //System.out.println(yourShips[i].toString());
         
         imgName.append(yourShips[i].getName() + "_");
         
         if(yourShips[i].getOrientation()==0)
         {
            imgName.append("hor_");
               
            for(j=1;j<=yourShips[i].getSize();j++)
            {
               JLabel temp= (JLabel)yourGrid.findComponentAt(yourShips[i].getLocation(j-1));
               imgName.append(j + ".png");
               temp.setIcon(new ImageIcon(imgName.toString()));
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
         Point clicked;
         char yGrid= 64;
         int xGrid;
         StringBuilder gridLoc= new StringBuilder();
         Attack userAttack;
         
         if(xPos>=30 && yPos>=30)
         { 
            clicked= new Point(xPos, yPos);
            yGrid= (char)(yGrid + (yPos/30));
            xGrid= xPos/30;
            gridLoc.append(yGrid + Integer.toString(xGrid));
         
            userAttack= new Attack(clicked, gridLoc.toString());
            
            /*try //places cross over icon
            {
               JLabel temp= (JLabel)yourGrid.findComponentAt(clicked);
               ImageIcon temp2= (ImageIcon)temp.getIcon();
               BufferedImage a= ImageIO.read(new File(temp2.getDescription()));
               BufferedImage b= ImageIO.read(new File("resources/cross.png"));
               BufferedImage combined= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
               Graphics g= combined.getGraphics();
            
               g.drawImage(a,0,0,null);
               g.drawImage(b,0,0,null);
            
               temp.setIcon(new ImageIcon(combined));
               temp.setEnabled(false);
            }
            catch(IOException e2)
            {
               System.out.println(e2.getMessage());   
            }*/
            
            System.out.println(clicked);
            System.out.println(gridLoc);
            System.out.println(userAttack);
         }
      }
   }
   
   /*public static void main(String args[])
   {
      new BattleshipUI(null);
   }*/
}