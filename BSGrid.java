/*
Multiplayer Battleship Game
BattleShip Grid
Jeremy Manin
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BSGrid extends JPanel
{
   private int size;
   private int xPos, yPos;
   
   public BSGrid(int s)
   {
      size= s;
      JLabel tempLabel;
      ImageIcon gridBG= new ImageIcon("resources/ocean.png");
      ImageIcon disabledGridBG= null;
      char rowChar= 'A';
      
      try
      {
         BufferedImage a= ImageIO.read(new File(gridBG.getDescription()));
         BufferedImage b= ImageIO.read(new File("resources/miss_peg.png"));
         BufferedImage combined= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
         Graphics g= combined.getGraphics();
            
         g.drawImage(a,0,0,null);
         g.drawImage(b,0,0,null);
            
         disabledGridBG= new ImageIcon(combined);
      }
      catch(IOException e){}
                  
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
         {
            tempLabel= new JLabel(gridBG);
            tempLabel.setDisabledIcon(disabledGridBG);
         }   
         
         tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
         add(tempLabel);
      }
   }
}