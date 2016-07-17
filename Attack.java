/*
Multiplayer Battleship Game
Attack Object
Jeremy Manin
*/

import java.awt.Point;
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
   
   public void setIsHit(String sN, boolean sS)
   {
      isHit= true;
      shipName= sN;
      shipSunk= sS;
   }
   
   public void setEndGame(boolean eG)
   {
      endGame= eG;
   }
   
   public Point getCoords()
   {
      return(coords);
   }
   
   public String getCoordName()
   {
      return(coordName);
   }
   
   public boolean getIsHit()
   {
      return(isHit);
   }
   
   public String getShipName()
   {
      return(shipName);
   }
   
   public boolean getShipSunk()
   {
      return(shipSunk);
   }
   
   public boolean getEndGame()
   {
      return(endGame);
   }
   
   public String toString()
   {
      StringBuilder output= new StringBuilder("Attack:\n");
      
      output.append(coords.toString());
      output.append("\n" + coordName + " Hit:" + isHit + " Ship:" + shipName + " Sunk:" + shipSunk + " End:" + endGame);
      
      return(output.toString());
   }
}