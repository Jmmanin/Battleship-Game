/*
Multiplayer Battleship Game
Client Thread
Jeremy Manin
*/

import java.io.*;
import java.net.*;
import javax.swing.*;

public class BSClientThread extends Thread
{
   private String hostName;
   private int portNumber1, portNumber2;
   private Socket bsSocket;
   private ObjectInputStream in;
   private ObjectOutputStream out;
   private boolean isTurn= true;
   private boolean contToMain= false;
   private boolean endGame= false;
                     
   public BSClientThread(String hN, int pN1, int pN2)
   {
      hostName= hN;
      portNumber1= pN1;
      portNumber2= pN2;   
   }
   
   public void run()
   {         
      boolean isPlayer1;
      Attack received;
          
      PlaceShips placeShips= new PlaceShips(this);
         
      try
      {     
         bsSocket= new Socket(hostName, portNumber1);
         in= new ObjectInputStream(bsSocket.getInputStream());
         out= new ObjectOutputStream(bsSocket.getOutputStream());
         out.flush();
                  
         isPlayer1= in.readBoolean();
               
         if(!isPlayer1)
         {
            bsSocket.close();
            bsSocket= new Socket(hostName, portNumber2);
            in= new ObjectInputStream(bsSocket.getInputStream());
            out= new ObjectOutputStream(bsSocket.getOutputStream());
            out.flush();
            
            isTurn= false;
         }
      }
      catch(UnknownHostException e)
      {
         JOptionPane.showMessageDialog(null ,"Could not connect to server.\nCheck hostname and port numbers and try again.", "Connection Error", JOptionPane.ERROR_MESSAGE);          
         System.exit(1);
      } 
      catch(IOException e)
      {
         JOptionPane.showMessageDialog(null ,"Error communicating with server.\nCheck hostname and port numbers and try again.", "Communication Error", JOptionPane.ERROR_MESSAGE);          
         System.exit(1);
      }
                
      placeShips.closeWaitBox();
         
      try
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
                  
      BattleshipUI mainGame= new BattleshipUI(placeShips.getShips(), this);
           
      try
      {         
         while(!endGame)
         {
            if(isTurn)
            {
               received= (Attack)in.readObject();
               mainGame.proccessAttack(received); 
            }
            else
            {
               received= (Attack)in.readObject();
               received= mainGame.proccessAttack(received);
               sendAttack(received);
            }
         
            endGame= received.getEndGame();
         
            out.flush();
            
            isTurn= !isTurn;
            mainGame.updateTurnLabels(isTurn);            
         }
         
         bsSocket.close();
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
      catch(NullPointerException e)
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
         JOptionPane.showMessageDialog(null ,"Error closing connection.", "Communication Error", JOptionPane.INFORMATION_MESSAGE);          
      }
   }
}