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
   
   public Ship(String n,int s,int o,Point[] p)
   {
      name= n;
      size= s;
      orientation= o;
      locations= p;           
   }
   
   public String getName()
   {
      return(name);
   }
   
   public int getSize()
   {
      return(size);
   }
   
   public int getOrientation()
   {
      return(orientation);
   }
      
   public Point getLocation(int i)
   {
      return(locations[i]);
   }
   
   public String toString()
   {
      StringBuilder output= new StringBuilder();
      
      output.append("Ship Name: " + name + "\n");
      output.append("Size: " + size + "\n");
      output.append("Orientation: " + orientation + "\n");
      for(int i=0;i<locations.length;i++)
         output.append(locations[i].toString() + "\n");
      
      return(output.toString());   
   }
}