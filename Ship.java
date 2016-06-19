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
   private Rectangle spaceOccupied;
   
   public Ship(String n,int s,int o,Point[] p)
   {
      name= n;
      size= s;
      orientation= o;
      locations= p;
      
      spaceOccupied= makeRectangle();      
   }
   
   private Rectangle makeRectangle()
   {
      int x,y;
      
      x= (int)locations[0].getX();
      y= (int)locations[0].getY();
      
      x= (x/30)*30;
      y= (y/30)*30;
            
      if(orientation==0)
      {
         return(new Rectangle(x,y,size*30,30));
      }
      else
      {
         return(new Rectangle(x,y,30,size*30));
      }
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
   
   public Rectangle getSpaceOccupied()
   {
      return(spaceOccupied);
   }
   
   public boolean contains(Point p)
   {
      return(spaceOccupied.contains(p));
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