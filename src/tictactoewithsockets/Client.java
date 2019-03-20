
package tictactoewithsockets;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author jose
 */
public class Client implements Runnable{

    private Socket client;
    private DataOutputStream output;
    private DataInputStream intput;
    private int port = 2027;
    private String host = "localhost";
    
    private String message;
    private Game frameGame;
    private JButton[][] buttons;
    
    
    private boolean inning;

    public Client(Game frameGame) {
        try {
            this.frameGame = frameGame;
            client = new Socket(host,port);
            intput = new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());
            buttons = this.frameGame.getButtons();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    
    
    @Override
    public void run() {
        try {
            message = intput.readUTF();
            String data[] = message.split(";");
            frameGame.inningsText(data[0]);
            String player = data[0].split(" ")[1];
            inning = Boolean.valueOf(data[1]);
            
            while(true){
                message = intput.readUTF();
                String[] messages = message.split(";");
                
                int players = Integer.parseInt(messages[0]);
                int row = Integer.parseInt(messages[1]);
                int column = Integer.parseInt(messages[2]);
                
                if(players == 1){
                    buttons[row][column].setForeground(Color.blue);
                    buttons[row][column].setText("X");   
                }else{
                    buttons[row][column].setForeground(Color.red);
                    buttons[row][column].setText("O");   
                }
                
                buttons[row][column].removeActionListener(buttons[row][column].getActionListeners()[0]);
                inning = !inning;
                
                if(player.equals(messages[3])){
                    System.out.println(messages[3]);
                    JOptionPane.showMessageDialog(frameGame, "Ganaste !!!");
                    new Game().setVisible(true);
                    frameGame.dispose();
                }else if("Tablas".equals(messages[3])){
                    JOptionPane.showMessageDialog(frameGame, "En Tablas !!!");
                    new Game().setVisible(true);
                    frameGame.dispose();
                }else if(!"Ninguno".equals(messages[3]) &&  !messages[3].equals(messages[0])){
                    JOptionPane.showMessageDialog(frameGame, "Lo Sineto Perdiste !!!");
                    new Game().setVisible(true);
                    frameGame.dispose();
                    
                }
                
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    public void setInning(int row, int column){
        try {
            if(inning){
                String data = "";
                data += row + ";";
                data += column + ";";
                output.writeUTF(data);
            }else{
                JOptionPane.showMessageDialog(frameGame, "Espera tu turno");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
}
