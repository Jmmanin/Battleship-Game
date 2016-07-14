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
   private int size;
   private Point[] locations;  
   private Rectangle[] spacesOccupied;
   private Rectangle totalSpaceOccupied;
   private boolean[] spacesHit;
   private int hitCount;
   private boolean isSunk;
   
   public Ship(String n,int s,int o,Point[] p)
   {
      int x,y,i;
      
      name= n;
      size= s;
      orientation= o;
      locations= p;
      spacesOccupied= new Rectangle[size];
      spacesHit= new boolean[size];
      hitCount= 0;
      isSunk= false;
   
      x= (int)locations[0].getX();
      y= (int)locations[0].getY();
      
      x= (x/30)*30;
      y= (y/30)*30;
          
      if(orientation==0)
      {
         totalSpaceOccupied= new Rectangle(x,y,size*30,30);
      }
      else
      {
         totalSpaceOccupied= new Rectangle(x,y,30,size*30);
      }
                  
      spacesOccupied[0]= new Rectangle(x,y,30,30);
               
      for(i=1;i<size;i++)
      {
         x= (int)locations[i].getX();
         y= (int)locations[i].getY();
      
         x= (x/30)*30;
         y= (y/30)*30;
                  
         spacesOccupied[i]= new Rectangle(x,y,30,30);
         
         spacesHit[i]= false;
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
   
   public Rectangle getTotalSpaceOccupied()
   {
      return(totalSpaceOccupied);
   }
   
   public Rectangle getOccupiedSpace(int i)
   {
      return(spacesOccupied[i]);
   }
   
   public boolean getIsSunk()
   {
      return(isSunk);
   }
   
   public boolean contains(Point p)
   {      
      for(int i= 0;i<size;i++)
      {
         if(spacesOccupied[i].contains(p))
            return(true);
      }
      
      return(false);   
   }
   
   public boolean hitSpace(Point p)
   {
      boolean toReturn= false;
      
      for(int i= 0;i<size;i++)
      {
         if(getOccupiedSpace(i).contains(p))
         {
            if(!spacesHit[i])
            {
               spacesHit[i]= true;
               hitCount++;
               toReturn= true;
            }
            
            break;
         }
      }
      
      if(hitCount==size)
         isSunk= true;
         
      return(toReturn);   
   }
   
   public boolean isSunk()
   {
      return(isSunk);
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