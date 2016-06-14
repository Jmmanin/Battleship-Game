/*
Multiplayer Battleship Game
PlaceShips
Jeremy Manin and John Dott
*/

import javax.swing.*;
import java.awt.*;

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
   
   public PlaceShips()
   {
      theFrame= new JFrame("Ship Placement");
      theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      theFrame.setResizable(false);
      theFrame.getContentPane().setLayout(new BoxLayout(theFrame.getContentPane(), BoxLayout.Y_AXIS));
      
      gridPanel= new JPanel();
      gridPanel.setLayout(new BoxLayout(gridPanel,BoxLayout.X_AXIS));
      
      theGrid= new BSGrid(10);
      
      gridPanel.add(Box.createRigidArea(new Dimension(60,0)));
      gridPanel.add(theGrid);
      gridPanel.add(Box.createRigidArea(new Dimension(60,0)));
      
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
      
      orientation= new JButton("Vertical");
      
      shipPanel.add(battleship);
      shipPanel.add(carrier);
      shipPanel.add(cruiser);
      shipPanel.add(destroyer);
      shipPanel.add(submarine);
      shipPanel.add(orientation);
      
      theFrame.add(shipPanel);
      
      msgPanel= new JPanel();
      msgPanel.setLayout(new FlowLayout());
      
      msgTxt= new JLabel("TEST MESSAGE");
      
      msgPanel.add(msgTxt);
      
      theFrame.add(msgPanel);
      
      theFrame.pack();
      theFrame.setVisible(true);
   }
   
   public static void main(String args[])
   {
      new PlaceShips();
   }
}   