/*
Multiplayer Battleship Game
Client Thread
Jeremy Manin
*/

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BSClientThread extends Thread
{
   private String hostName;
   private int portNumber1, portNumber2;
   private int gameMode;
   private Socket bsSocket;
   private ObjectInputStream in;
   private ObjectOutputStream out;
   private boolean isTurn= true;
   private boolean contToMain= false;
   private boolean endGame= false;
                     
   public BSClientThread(String hN, int pN1, int pN2, int gM)
   {
      hostName= hN;
      portNumber1= pN1;
      portNumber2= pN2;
      gameMode= gM;   
   }
   
   public void run()
   {         
      boolean connecting= true;
      boolean isPlayer1;
      Attack received= null;
      Attack processed= null;
          
      PlaceShips placeShips= new PlaceShips(this); //creates PlaceShips UI, displays connecting wait dialog 
      
      while(connecting) //while game is being set up
      {   
         try
         {     
            bsSocket= new Socket(hostName, portNumber1); //connects to server via port 1
            in= new ObjectInputStream(bsSocket.getInputStream());
            out= new ObjectOutputStream(bsSocket.getOutputStream());
            out.flush();
                  
            isPlayer1= in.readBoolean(); //gets boolean value to tell if P1 or P2
               
            if(!isPlayer1) //if P2
            {
               bsSocket.close(); //disconnects from port 1
               bsSocket= new Socket(hostName, portNumber2); //connects via port 2
               in= new ObjectInputStream(bsSocket.getInputStream());
               out= new ObjectOutputStream(bsSocket.getOutputStream());
               out.flush();
               
               isTurn= false; //P1 has 1st turn
            }
               
            connecting= false; //no longer connecting
         }
         catch(UnknownHostException e) //bad hostname
         {
            JOptionPane.showMessageDialog(null ,"Could not connect to server.\nCheck hostname and port numbers and try again.", "Connection Error", JOptionPane.ERROR_MESSAGE);          
            System.exit(1);
         }
         catch(IOException e) //closes connection if any IO error occurs
         {
            try
            {
               bsSocket.close();
            }
            catch(IOException e2){}            
         } 
      }
      
      int gameMode2= -1;
      
      try
      {
         out.writeInt(gameMode);
         out.flush();
         gameMode2= in.readInt();
      }
      catch(IOException e){}
      
      if(gameMode2!=gameMode)
      {
         StringBuilder errorMessage= new StringBuilder("Player chose \"");
         
         if(gameMode==0)
            errorMessage.append("standard\" ");
         else
            errorMessage.append("salvo\" ");
           
         errorMessage.append("and opponent chose \"");
            
         if(gameMode2==0)
            errorMessage.append("standard\".");
         else
            errorMessage.append("salvo\".");
            
         errorMessage.append("\nBoth players must choose the same game mode.");
                     
         JOptionPane.showMessageDialog(null , errorMessage.toString(), "Game Mode Mismatch", JOptionPane.ERROR_MESSAGE);          
         
         System.exit(0);
      } 
                
      placeShips.start(); //closes wait dialog and displays PlaceShips
         
      try //synchronized wait until player is done placing ships
      {
         synchronized(this)
         {
            while(!contToMain)
               this.wait();
         }
      }
      catch(InterruptedException e)
      {
         JOptionPane.showMessageDialog(null ,"Client thread was interrupted.", "Thread Interrupt Error", JOptionPane.ERROR_MESSAGE);          
         System.exit(1);
      }
                  
      BattleshipUI mainGame= new BattleshipUI(placeShips.getShips(), this, gameMode); //starts main game
           
      try
      {         
         while(!endGame) //main game loop
         {
            if(isTurn) //play's turn: gets processed attack from enemy and processes results of attack 
            {          //actual attack is sent in this.sendAttack()
               received= (Attack)in.readObject();
               mainGame.processAttack(received); 
            }
            else //enemy's turn: gets enemy attack, processes it, and returns results to enemy
            {
               received= (Attack)in.readObject();
               processed= mainGame.processAttack(received);
               sendAttack(processed);
            }
         
            endGame= received.getEndGame();
         
            out.flush();
            
            isTurn= !isTurn;
            mainGame.updateTurnLabels(isTurn);
            
            received= null;
            processed= null;            
         }
         
         bsSocket.close(); //closes connection once game is over
      } 
      catch(IOException e)
      {
         JOptionPane.showMessageDialog(null ,"Error communicating with server.\nCheck hostname and port numbers and try again.", "Communication Error", JOptionPane.ERROR_MESSAGE);          
         System.exit(1);
      }
      catch(ClassNotFoundException e)
      {
         JOptionPane.showMessageDialog(null ,"Error reading attack.", "Read Error", JOptionPane.ERROR_MESSAGE);          
         System.exit(1);
      }
      catch(NullPointerException e) //if attack is null, then opponent has left game
      {
         JOptionPane.showMessageDialog(null ,"Opponent has left game.", "Communication Error", JOptionPane.ERROR_MESSAGE);          
         System.exit(1);
      }
   }
   
   public boolean getIsTurn()
   {
      return(isTurn);
   }
   
   public void setContToMain(boolean toSet)
   {
      contToMain= toSet;   
   }
   
   public void sendAttack(Attack toSend)
   {
      try
      {
         out.writeObject(toSend);
         out.flush();
      }
      catch(IOException e)
      {
         JOptionPane.showMessageDialog(null ,"Error communicating with server.\nCheck connection to server and try again.", "Communication Error", JOptionPane.ERROR_MESSAGE);          
         closeConnection();
      }
   }
   
   public void closeConnection()
   {
      try
      {
         bsSocket.close();
         interrupt();
      }
      catch(IOException e)
      {
         JOptionPane.showMessageDialog(null ,"Error closing connection.", "Communication Error", JOptionPane.ERROR_MESSAGE);          
      }
   }
}