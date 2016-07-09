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
   private String hostName;
   private int portNumber1, portNumber2;
   private Socket bsSocket;
   private ObjectInputStream in;
   private ObjectOutputStream out;
   private boolean isTurn= true;
                     
   public BSClientThread(String hN, int pN1, int pN2)
   {
      hostName= hN;
      portNumber1= pN1;
      portNumber2= pN2;   
   }
   
   public void run()
   {
      JDialog waitMsgBox= new JDialog(new JFrame(),"Waiting",false);
      JLabel waitMsg= new JLabel("Waiting for other player to join.");
      waitMsg.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      waitMsgBox.setContentPane(waitMsg);
      waitMsgBox.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      waitMsgBox.setLocationRelativeTo(null);
      waitMsgBox.pack();
      waitMsgBox.setVisible(true);
         
      try
      {  
         boolean isPlayer1;
              
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
         
         waitMsgBox.setVisible(false);
         waitMsgBox.dispose();
                     
         new PlaceShips(this);
      }
      catch(UnknownHostException e)
      {
         JOptionPane.showMessageDialog(null ,"Could not connect to server.\nCheck hostname and port numbers and try again.", "Connection Error", JOptionPane.ERROR_MESSAGE);          
      
         new BattleshipClient();
         
         if(waitMsgBox.isDisplayable())
            waitMsgBox.dispose();
            
         interrupt();
      } 
      catch(IOException e)
      {
         JOptionPane.showMessageDialog(null ,"Error communicating with server.\nCheck hostname and port numbers and try again.", "Communication Error", JOptionPane.ERROR_MESSAGE);          
      
         new BattleshipClient();
         
         if(waitMsgBox.isDisplayable())
            waitMsgBox.dispose();
            
         interrupt();
      }
   }
   
   public boolean getTurn()
   {
      return(isTurn);
   }
   
   public Object sendObject(Object toSend)
   {
      if(isTurn)
      {
         try
         {
            isTurn= !isTurn;
            out.writeObject(toSend);
            out.flush();
            return(in.readObject());
         }
         catch(IOException e)
         {
            JOptionPane.showMessageDialog(null ,"Error communicating with server.\nCheck connection to server and try again.", "Communication Error", JOptionPane.ERROR_MESSAGE);          
            closeConnection();
            return(null);
         }
         catch(ClassNotFoundException e)
         {
            JOptionPane.showMessageDialog(null ,"Error sending attack.\nCheck connection to server and try again.", "Communication Error", JOptionPane.ERROR_MESSAGE);          
            closeConnection();
            return(null);
         }
      }
      else
         return(null);
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