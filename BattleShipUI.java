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
      private int xPos, yPos;
      
      public BSGrid(int s)
      {
         size= s;
         int colNum= 1;
         JLabel tempColLabel;
         char rowChar= 'A';
         JLabel tempGridLabel= null;
         
         setLayout(new GridLayout(size+1, size+1/*, 1, 1*/)); //last 2 args provide spacing b/t images
         
         ImageIcon gridBG= new ImageIcon("ocean.png");
         
         add(new JLabel());
         
         for(int i=0;i<size;i++)
         {
            tempColLabel= new JLabel(Integer.toString(colNum));
            tempColLabel.setHorizontalAlignment((int)CENTER_ALIGNMENT);
            add(tempColLabel);
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
               
            add(tempGridLabel);
         } 
         
         MouseHandler theMouse= new MouseHandler();
         addMouseListener(theMouse);
      }
   
      private class MouseHandler extends MouseAdapter
      {
         public void mouseClicked(MouseEvent e)
         {
            xPos= e.getX();
            yPos= e.getY();
            
            System.out.println("X Position: " + xPos + "\nY Position: " + yPos);
            
            if((xPos>=30 && xPos<60) && (yPos>=30 && yPos<60))
               System.out.println("Clicked A1\n");
               
            if((xPos>=270 && xPos<300) && (yPos>=30 && yPos<60))
               System.out.println("Clicked A9\n");

            if((xPos>=270 && xPos<300) && (yPos>=270 && yPos<300))
               System.out.println("Clicked I9\n");
         }
      }
   }
   
   public static void main(String[] args)
   {
      new BattleShipUI();
   }
}