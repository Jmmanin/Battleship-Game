/*
Multiplayer Battleship Game
Ship Object
Jeremy Manin and John Dott
*/

import java.awt.*;

public class Ship
{
   private String name;
   private int orientation;
   private Point[] locations;
   private int size;
   
   public Ship(String n,int size,int o,Point[] p)
   {
      name= n;
      orientation= o;
      locations= p;           
   }
   
   public int getSize()
   {
      return(size);
   }
   
   public Point getLocation(int i)
   {
      return(locations[i]);
   }
}