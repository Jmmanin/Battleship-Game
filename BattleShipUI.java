/*
Multiplayer Battleship Game
Game UI
Jeremy Manin and John Dott
*/

import javax.swing.*;
import java.awt.*;

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
        
   public static void main(String[] args)
   {
      new BattleShipUI();
   }
}