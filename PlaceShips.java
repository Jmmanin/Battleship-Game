/*
Multiplayer Battleship Game
PlaceShips
Jeremy Manin and John Dott
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlaceShips
{
   private JFrame theFrame;
   private JPanel gridPanel;
   private BSGrid theGrid;
   private JPanel shipPanel;
   private JPanel msgPanel;
   private JLabel battleship;
   private JLabel carrier;
   private JLabel cruiser;
   private JLabel destroyer;
   private JLabel submarine;
   private JButton orientation;
   private JLabel msgTxt;
   private JLabel orientTxt;
   private int shipOrientation;
   private String shipSelected;
   private boolean baPlaced;
   private boolean caPlaced;
   private boolean crPlaced;
   private boolean dePlaced;
   private boolean suPlaced;
   
   public PlaceShips()
   {
      theFrame= new JFrame("Ship Placement");
      theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      theFrame.setResizable(false);
      theFrame.getContentPane().setLayout(new BoxLayout(theFrame.getContentPane(), BoxLayout.Y_AXIS));
      
      gridPanel= new JPanel();
      gridPanel.setLayout(new BoxLayout(gridPanel,BoxLayout.X_AXIS));
      
      theGrid= new BSGrid(10);
      theGrid.addMouseListener(new GridMouser());
      
      gridPanel.add(Box.createRigidArea(new Dimension(84,0)));
      gridPanel.add(theGrid);
      gridPanel.add(Box.createRigidArea(new Dimension(84,0)));
      
      theFrame.add(gridPanel);
      
      shipPanel= new JPanel();
      shipPanel.setLayout(new GridLayout(2,3));
      
      ImageIcon bsImg= new ImageIcon("resources/battleship.png");
      ImageIcon caImg= new ImageIcon("resources/carrier.png");
      ImageIcon crImg= new ImageIcon("resources/cruiser.png");
      ImageIcon deImg= new ImageIcon("resources/destroyer.png");
      ImageIcon suImg= new ImageIcon("resources/submarine.png");
      
      battleship= new JLabel(bsImg);
      carrier= new JLabel(caImg);
      cruiser= new JLabel(crImg);
      destroyer= new JLabel(deImg);
      submarine= new JLabel(suImg);
   
      shipOrientation= 0;
      orientation= new JButton("Change Orientation");
      orientation.addActionListener(new ButtonHandler());
      
      shipPanel.add(battleship);
      shipPanel.add(carrier);
      shipPanel.add(cruiser);
      shipPanel.add(destroyer);
      shipPanel.add(submarine);
      shipPanel.add(orientation);
      
      shipPanel.addMouseListener(new ShipMouser());
      
      theFrame.add(shipPanel);
      
      msgPanel= new JPanel();
      msgPanel.setLayout(new GridLayout(1,2));
      
      msgTxt= new JLabel("Select a ship to place.");
      orientTxt= new JLabel("Orientation: Horizontal");
      orientTxt.setHorizontalAlignment(SwingConstants.RIGHT);
      
      msgPanel.add(msgTxt);
      msgPanel.add(orientTxt);
      
      theFrame.add(msgPanel);
      
      theFrame.pack();
      theFrame.setVisible(true);
      
      shipSelected= null;
      baPlaced= false;
      caPlaced= false;
      crPlaced= false;
      dePlaced= false;
      suPlaced= false;
   }
   
   private class GridMouser extends MouseAdapter
   {
      public void mouseClicked(MouseEvent e)
      {
         int xPos= e.getX();
         int yPos= e.getY();
         StringBuilder imgName= new StringBuilder();
         int i;
                  
         imgName.append("resources/" + shipSelected + "_");
         
         System.out.println(xPos + " " + yPos);
         
         if(xPos>=30 && yPos>=30)
         {
            if(shipSelected=="ba" && baPlaced==false)
            {
               if(shipOrientation==0)
               {               
                  imgName.append("hor_");
               
                  for(i=1;i<=4;i++)
                  {
                     JLabel temp= (JLabel)theGrid.findComponentAt(xPos,yPos);
                     imgName.append(i + ".png");
                     temp.setIcon(new ImageIcon(imgName.toString()));
                     imgName.delete(17,22);
                     xPos= xPos+30;   
                  }
               }
               else
               {
                  imgName.append("ver_");
               
                  for(i=1;i<=4;i++)
                  {
                     JLabel temp= (JLabel)theGrid.findComponentAt(xPos,yPos);
                     imgName.append(i + ".png");
                     temp.setIcon(new ImageIcon(imgName.toString()));
                     imgName.delete(17,22);
                     yPos= yPos+30;   
                  }
               }
            
               baPlaced= true;
            }
            else if(shipSelected=="ca" && caPlaced==false)
            {
               if(shipOrientation==0)
               {               
                  imgName.append("hor_");
               
                  for(i=1;i<=5;i++)
                  {
                     JLabel temp= (JLabel)theGrid.findComponentAt(xPos,yPos);
                     imgName.append(i + ".png");
                     temp.setIcon(new ImageIcon(imgName.toString()));
                     imgName.delete(17,22);
                     xPos= xPos+30;   
                  }
               }
               else
               {
                  imgName.append("ver_");
               
                  for(i=1;i<=5;i++)
                  {
                     JLabel temp= (JLabel)theGrid.findComponentAt(xPos,yPos);
                     imgName.append(i + ".png");
                     temp.setIcon(new ImageIcon(imgName.toString()));
                     imgName.delete(17,22);
                     yPos= yPos+30;   
                  }
               }
            
               caPlaced= true;
            }
            else if(shipSelected=="cr" && crPlaced==false)
            {
               if(shipOrientation==0)
               {               
                  imgName.append("hor_");
               
                  for(i=1;i<=3;i++)
                  {
                     JLabel temp= (JLabel)theGrid.findComponentAt(xPos,yPos);
                     imgName.append(i + ".png");
                     temp.setIcon(new ImageIcon(imgName.toString()));
                     imgName.delete(17,22);
                     xPos= xPos+30;   
                  }
               }
               else
               {
                  imgName.append("ver_");
               
                  for(i=1;i<=3;i++)
                  {
                     JLabel temp= (JLabel)theGrid.findComponentAt(xPos,yPos);
                     imgName.append(i + ".png");
                     temp.setIcon(new ImageIcon(imgName.toString()));
                     imgName.delete(17,22);
                     yPos= yPos+30;   
                  }
               }
            
               crPlaced= true;
            }
            else if(shipSelected=="de" && dePlaced==false)
            {
               if(shipOrientation==0)
               {               
                  imgName.append("hor_");
               
                  for(i=1;i<=2;i++)
                  {
                     JLabel temp= (JLabel)theGrid.findComponentAt(xPos,yPos);
                     imgName.append(i + ".png");
                     temp.setIcon(new ImageIcon(imgName.toString()));
                     imgName.delete(17,22);
                     xPos= xPos+30;   
                  }
               }
               else
               {
                  imgName.append("ver_");
               
                  for(i=1;i<=2;i++)
                  {
                     JLabel temp= (JLabel)theGrid.findComponentAt(xPos,yPos);
                     imgName.append(i + ".png");
                     temp.setIcon(new ImageIcon(imgName.toString()));
                     imgName.delete(17,22);
                     yPos= yPos+30;   
                  }
               }
            
               dePlaced= true;
            }
            else if(shipSelected=="su" && suPlaced==false)
            {
               if(shipOrientation==0)
               {               
                  imgName.append("hor_");
               
                  for(i=1;i<=3;i++)
                  {
                     JLabel temp= (JLabel)theGrid.findComponentAt(xPos,yPos);
                     imgName.append(i + ".png");
                     temp.setIcon(new ImageIcon(imgName.toString()));
                     imgName.delete(17,22);
                     xPos= xPos+30;   
                  }
               }
               else
               {
                  imgName.append("ver_");
               
                  for(i=1;i<=3;i++)
                  {
                     JLabel temp= (JLabel)theGrid.findComponentAt(xPos,yPos);
                     imgName.append(i + ".png");
                     temp.setIcon(new ImageIcon(imgName.toString()));
                     imgName.delete(17,22);
                     yPos= yPos+30;   
                  }
               }
            
               suPlaced= true;
            }
         }
      }
   }
   
   private class ShipMouser extends MouseAdapter
   {
      public void mouseClicked(MouseEvent e)
      {
         int xPos= e.getX();
         int yPos= e.getY();
         String labelTxt;
                  
         if(shipPanel.findComponentAt(xPos,yPos)==battleship)
         {
            msgTxt.setText("Battleship selected");
            shipSelected= "ba";
         }
         if(shipPanel.findComponentAt(xPos,yPos)==carrier)
         {
            msgTxt.setText("Aircraft Carrier selected");
            shipSelected= "ca";
         }
         if(shipPanel.findComponentAt(xPos,yPos)==cruiser)
         {
            msgTxt.setText("Cruiser selected");
            shipSelected= "cr";
         }
         if(shipPanel.findComponentAt(xPos,yPos)==destroyer)
         {
            msgTxt.setText("Destroyer selected");
            shipSelected= "de";
         }
         if(shipPanel.findComponentAt(xPos,yPos)==submarine)
         {
            msgTxt.setText("Submarine selected");
            shipSelected= "su";
         }
      }
   }
   
   private class ButtonHandler implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(shipOrientation==0)
         {
            orientTxt.setText("Orientation: Veritcal");
            shipOrientation= 1;
         }   
         else
         {
            orientTxt.setText("Orientation: Horizontal");
            shipOrientation= 0;
         }     
      }
   }
   
   public static void main(String args[])
   {
      new PlaceShips();
   }
}