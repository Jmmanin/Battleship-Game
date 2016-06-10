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
   JFrame gameUI;
   BSGrid enemyGrid;
   JLabel enemyLabel;
   BSGrid yourGrid;
   JLabel yourLabel;
   JPanel statusPanel;
   JLabel statusTurn;
   JLabel statusTurnNum;
   JLabel statusTime;
   
   public BattleShipUI()
   {
      gameUI= new JFrame("Battleship Game");
      gameUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      gameUI.getContentPane().setLayout(new BoxLayout(gameUI.getContentPane(), BoxLayout.Y_AXIS));
      
      enemyGrid= new BSGrid(10);
      yourGrid= new BSGrid(10);
      
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
   }   
      
   private class BSGrid extends JPanel
   {
      private int mode;
      private int size;
      
      public BSGrid(int s)
      {
         size= s;
         int colNum= 1;
         JLabel tempColLabel;
         char rowChar= 'A';
         JLabel tempGridLabel= null;
         
         this.setLayout(new GridLayout(size+1, size+1));
         
         ImageIcon gridBG= new ImageIcon("ocean.png");
         
         this.add(new JLabel());
         
         for(int i=0;i<size;i++)
         {
            tempColLabel= new JLabel(Integer.toString(colNum));
            tempColLabel.setHorizontalAlignment((int)CENTER_ALIGNMENT);
            this.add(tempColLabel);
            colNum++;
         }   
         
         for(int i=0;i<(size*size+size);i++)
         {
            if(i%11==0)
            {
               tempGridLabel= new JLabel(Character.toString(rowChar));
               tempGridLabel.setHorizontalAlignment((int)CENTER_ALIGNMENT);
               rowChar++;
            }
            else
            {   
               tempGridLabel= new JLabel();
               tempGridLabel.setIcon(gridBG);
            }
               
            this.add(tempGridLabel);
         }   
      }
   
      /*private class GridHandler extends ActionListener
      {
         public void actionPerformed(ActionEvent e)
         {
         
         }
      }*/
   }
   
   public static void main(String[] args)
   {
      new BattleShipUI();
   }
}