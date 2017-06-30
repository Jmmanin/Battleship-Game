/*
Multiplayer Battleship Game
Battleship Grid
Jeremy Manin
*/

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BSGrid extends JPanel
{
   private int size;
   
   public BSGrid(int s) //takes in size argument (should always be 10 for the game)
   {
      size= s;
      JLabel tempLabel;
      ImageIcon gridBG= new ImageIcon(getImageFile("/resources/ocean.png"));
      ImageIcon disabledGridBG= null;
      char rowChar= 'A';
      
      BufferedImage a= getImageFile("/resources/ocean.png"); //overlays miss peg onto ocean for disabled icon (miss)
      BufferedImage b= getImageFile("/resources/miss_peg.png");
      BufferedImage combined= new BufferedImage(30,30,BufferedImage.TYPE_INT_ARGB);
      Graphics g= combined.getGraphics();
            
      g.drawImage(a,0,0,null);
      g.drawImage(b,0,0,null);
            
      disabledGridBG= new ImageIcon(combined);
                           
      setLayout(new GridLayout(11,11));
      
      add(new JLabel()); //blank space in top left corner
      
      for(int i= 1;i<=size;i++) //top row (numbers)
      {
         tempLabel= new JLabel(Integer.toString(i));
         tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
         add(tempLabel);
      }   
   
      for(int i=0;i<size*size+size;i++) //
      {
         if(i%11==0) //for row labels (letters)
         {
            tempLabel= new JLabel(Character.toString(rowChar));
            rowChar++;
         }   
         else //for ocean
         {
            tempLabel= new JLabel(gridBG);
            tempLabel.setDisabledIcon(disabledGridBG);
         }   
         
         tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
         add(tempLabel);
      }
   }
   
   private BufferedImage getImageFile(String filename) //JAR-friendly method to load image files
   {
      try
      {
         return((BufferedImage)ImageIO.read(BSGrid.class.getResourceAsStream(filename)));
      }
      catch(Exception e)
      {
         JOptionPane.showMessageDialog(null ,"Error loading image resource.", "Load Error", JOptionPane.ERROR_MESSAGE);          
         System.exit(1);
      }
      
      return(null);
   }
}