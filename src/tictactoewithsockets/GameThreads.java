package tictactoewithsockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author jose armando Lopez Rodriguez
 */
public class GameThreads implements Runnable{
 
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream intput;
    private int players;
    private int game[][];
    private Boolean inning;
    private LinkedList<Socket> users = new LinkedList<Socket>();

    public GameThreads(Socket socket, LinkedList users,int players, int[][] game) {
        this.socket = socket;
        this.users = users;
        this.players = players;
        this.game = game;
    }

    @Override
    public void run() {
        try {
            intput = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            inning = players == 1;
            String message = "";
            message += "Juega: " + (inning ? "X;":"O;");
            message +=  inning;
            output.writeUTF(message);
            
            while(true){
                String dataReceived = intput.readUTF();
                String data[] = dataReceived.split(";");
                
                int row = Integer.parseInt(data[0]);
                int column = Integer.parseInt(data[1]);
                
                game[row][column] = players;
                String movement = "";
                movement += players + ";";
                movement += row + ";";
                movement += column + ";";
                
                boolean winner = winner(players);
                boolean full = full();
                
                if(!winner && !full){
                    movement += "Ninguno";
                }else if(!winner && full){
                    movement += "Tablas";
                }else if(winner){
                    cleanMatrix();
                    movement += players == 1 ? "X":"O";
                }
                for (Socket user : users){
                    output = new DataOutputStream(user.getOutputStream());
                    output.writeUTF(movement);
                }
                
            }
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
            for (int i = 0; i < users.size(); i++) {
                if(users.get(i) == socket){
                    users.remove(i);break;
                }
            }
            cleanMatrix();
        }
    }

    private boolean winner(int players) {
        for (int i = 0; i < 3; i++) {
            boolean winner = true;
            for (int j = 0; j < 3; j++) {
                winner = winner && (game[i][j] == players);        
            }
            if(winner){
                return true;
            }     
        }
        for (int i = 0; i < 3; i++) {
            boolean winner = true;
            for (int j = 0; j < 3; j++) {
                winner = winner && (game[j][i] == players);        
            }
            if(winner){
                return true;
            }     
        }
        if(game[0][0] == players && game[1][1] == players && game[2][2] == players) return true;
        
        return false;
        
    }

    private boolean full() {
         for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(game[i][j] == -1)return false;
            }
        }
        
        cleanMatrix();
        return true;
    }

    private void cleanMatrix() {
     for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    game[i][j] = -1;
                }
        }
    }
    
}
    

