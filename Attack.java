/*
Multiplayer Battleship Game
Attack
Jeremy Manin and John Dott
*/

import java.awt.*;

public class Attack
{
   private Point coords;
   private String coordName;
   private boolean isHit;
   private String shipName;
   
   public Attack(Point c, String cN)
   {
      coords= c;
      coordName= cN;
      isHit= false;
      shipName= null;
   }
   
   public void setIsHit(String sN)
   {
      isHit= true;
      shipName= sN;
   }
   
   public String toString()
   {
      StringBuilder output= new StringBuilder("Attack:\n");
      
      output.append(coords.toString());
      output.append("\n" + coordName + " " + isHit + " " + shipName);
      
      return(output.toString());
   }
}