/*
Multiplayer Battleship Game
Attack Object
Jeremy Manin
*/

import java.awt.Point;
import java.io.Serializable; //allows object to be send over a network

public class Attack implements Serializable
{
   private Point coords;
   private String coordName;
   private boolean isHit;
   private String shipName;
   private boolean shipSunk;
   private boolean endGame;
   
   public Attack(Point c, String cN) //constructor only sets coords and grid space name (ie D3)
   {
      coords= c;
      coordName= cN;
      isHit= false;
      shipName= null;
      shipSunk= false;
      endGame= false;
   }
   
   public void setIsHit(String sN, boolean sS) //sets that attack has hit a ship, which ship was hit and if ship was sunk
   {
      isHit= true;
      shipName= sN;
      shipSunk= sS;
   }
   
   public void setEndGame(boolean eG) //sets if this attack has ended the game
   {
      endGame= eG;
   }
   
   public Point getCoords() //gets coords of attack
   {
      return(coords);
   }
   
   public String getCoordName() //gets coord name of attack
   {
      return(coordName);
   }
   
   public boolean getIsHit() //gets if attack was a hit
   {
      return(isHit);
   }
   
   public String getShipName() //gets which ship was hit by attack
   {
      return(shipName);
   }
   
   public boolean getShipSunk() //gets if ship hit by attack was sunk
   {
      return(shipSunk);
   }
   
   public boolean getEndGame() //gets if attack ended game
   {
      return(endGame);
   }
   
   public String toString() //writes attack as string (for debugging)
   {
      StringBuilder output= new StringBuilder("Attack:\n");
      
      output.append(coords.toString());
      output.append("\n" + coordName + " Hit:" + isHit + " Ship:" + shipName + " Sunk:" + shipSunk + " End:" + endGame);
      
      return(output.toString());
   }
}