/*
Multiplayer Battleship Game
Game UI
Jeremy Manin and John Dott
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BattleShipUI
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
   
   public BattleShipUI(Ship[] yourShips)
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
      gameUI.setVisible(true);
      
      placeYourShips(yourShips);      
   }   
   
   private void placeYourShips(Ship[] yourShips)
   {
      StringBuilder imgName= new StringBuilder("resources/");
      int i,j;
      
      for(i=0;i<yourShips.length;i++)
      {
         System.out.println(yourShips[i].toString());
         
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
         
         System.out.println("X Position: " + xPos + "\nY Position: " + yPos);
         
         xPos= xPos-30;
         yPos= yPos-30;
         
         if((xPos>=0 && xPos<30) && (yPos>=0 && yPos<30))
            System.out.println("Clicked A1\n");
            
         if((xPos>=240 && xPos<270) && (yPos>=0 && yPos<30))
            System.out.println("Clicked A9\n");
      
         if((xPos>=240 && xPos<270) && (yPos>=240 && yPos<270))
            System.out.println("Clicked I9\n");
      }
   }
}