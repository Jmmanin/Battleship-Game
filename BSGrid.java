/*
Multiplayer Battleship Game
BattleShip Grid
Jeremy Manin and John Dott
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BSGrid extends JPanel
{
   private int size;
   private int xPos, yPos;
   
   public BSGrid(int s)
   {
      size= s;
      JLabel tempLabel;
      ImageIcon gridBG= new ImageIcon("resources/ocean.png");
      char rowChar= 'A';
      
      setLayout(new GridLayout(11,11));
      
      add(new JLabel());
      
      for(int i= 1;i<=size;i++)
      {
         tempLabel= new JLabel(Integer.toString(i));
         tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
         add(tempLabel);
      }   

      for(int i=0;i<size*size+size;i++)
      {
         if(i%11==0)
         {
            tempLabel= new JLabel(Character.toString(rowChar));
            rowChar++;
         }   
         else
            tempLabel= new JLabel(gridBG);
         
         tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
         add(tempLabel);
      }
   }
}