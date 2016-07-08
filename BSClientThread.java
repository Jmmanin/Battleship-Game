/*
Multiplayer Battleship Game
Client Thread
Jeremy Manin and John Dott
*/

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class BSClientThread extends Thread
{
   String hostName;
   int portNumber1, portNumber2;
            
   public BSClientThread(String hN, int pN1, int pN2)
   {
      hostName= hN;
      portNumber1= pN1;
      portNumber2= pN2;   
   }
   
   public void run()
   {
      try
      {
         Socket bsSocket;
         ObjectInputStream in;
         ObjectOutputStream out;
         boolean isPlayer1;
         boolean isTurn= true;
              
         bsSocket= new Socket(hostName, portNumber1);
         in= new ObjectInputStream(bsSocket.getInputStream());
         System.out.println("In made");
         out= new ObjectOutputStream(bsSocket.getOutputStream());
         out.flush();
         
         System.out.println("hello, boolean not yet recieved");
         //System.out.println("Boolean: " + in.readBoolean());
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
            
         if(isPlayer1)
            System.out.println("Player 1!");
         else
            System.out.println("Player 2!");
      }
      catch(UnknownHostException e)
      {
         JOptionPane.showMessageDialog(null ,"Could not connect to server.\nCheck hostname and port numbers and try again.", "Connection Error", JOptionPane.ERROR_MESSAGE);          
         //System.out.println(e.getMessage());
         interrupt();
      } 
      catch(IOException e)
      {
         JOptionPane.showMessageDialog(null ,"Could not connect to server.\nCheck hostname and port numbers and try again.", "Connection Error", JOptionPane.INFORMATION_MESSAGE);          
         //System.out.println(e.getMessage());
         interrupt();
      }
   }
}