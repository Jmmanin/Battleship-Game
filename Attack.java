/*
Multiplayer Battleship Game
Attack
Jeremy Manin and John Dott
*/

import java.awt.*;
import java.io.Serializable;

public class Attack implements Serializable
{
   private Point coords;
   private String coordName;
   private boolean isHit;
   private String shipName;
   private boolean shipSunk;
   private boolean endGame;
   
   public Attack(Point c, String cN)
   {
      coords= c;
      coordName= cN;
      isHit= false;
      shipName= null;
      shipSunk= false;
      endGame= false;
   }
   
   public void setIsHit(String sN, boolean sS, boolean eG)
   {
      isHit= true;
      shipName= sN;
      shipSunk= sS;
      endGame= eG;
   }
   
   public String toString()
   {
      StringBuilder output= new StringBuilder("Attack:\n");
      
      output.append(coords.toString());
      output.append("\n" + coordName + " " + isHit + " " + shipName);
      
      return(output.toString());
   }
}