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
      gameUI.setResizable(false);
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
         JLabel tempLabel;
         char rowChar= 'A';
         
         this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
         
         JPanel colLabels= new JPanel();
         colLabels.setLayout(new GridLayout(1,size+1));
         
         colLabels.add(new JLabel());
         
         for(int i=1;i<=size;i++)
         {
            tempLabel= new JLabel(Integer.toString(i));
            tempLabel.setHorizontalAlignment((int)CENTER_ALIGNMENT);
            colLabels.add(tempLabel);
         }
         
         this.add(colLabels);
         
         JPanel rowAndGrid= new JPanel();
         rowAndGrid.setLayout(new BoxLayout(rowAndGrid,BoxLayout.X_AXIS));
         
         JPanel rowLabels= new JPanel();
         rowLabels.setLayout(new GridLayout(size,1));
         
         for(int i=0;i<size;i++)
         {
            tempLabel= new JLabel(Character.toString(rowChar));
            tempLabel.setHorizontalAlignment((int)CENTER_ALIGNMENT);
            rowChar++;   
            rowLabels.add(tempLabel);
         }
         
         rowAndGrid.add(Box.createRigidArea(new Dimension(11,0)));
         
         rowAndGrid.add(rowLabels);
         
         rowAndGrid.add(Box.createRigidArea(new Dimension(10,0)));
                  
         JPanel grid= new JPanel();
         grid.setLayout(new GridLayout(size,size));

         ImageIcon gridBG= new ImageIcon("ocean.png");

         for(int i=0;i<(size*size);i++)
         { 
            tempLabel= new JLabel();
            tempLabel.setIcon(gridBG);
            grid.add(tempLabel);
         }
         
         MouseHandler theMouse= new MouseHandler();
         grid.addMouseListener(theMouse);
         
         rowAndGrid.add(grid);
         
         this.add(rowAndGrid);                           
      }
   
      private class MouseHandler extends MouseAdapter
      {
         public void mouseClicked(MouseEvent e)
         {
            xPos= e.getX();
            yPos= e.getY();
            
            System.out.println("X Position: " + xPos + "\nY Position: " + yPos);
            
            if((xPos>=0 && xPos<30) && (yPos>=0 && yPos<30))
               System.out.println("Clicked A1\n");
               
            if((xPos>=240 && xPos<270) && (yPos>=0 && yPos<30))
               System.out.println("Clicked A9\n");
         
            if((xPos>=240 && xPos<270) && (yPos>=240 && yPos<270))
               System.out.println("Clicked I9\n");
         }
      }
   }
   
   public static void main(String[] args)
   {
      new BattleShipUI();
   }
}