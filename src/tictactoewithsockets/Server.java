/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoewithsockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author jose armando Lopez Rodriguez
 */
public class Server {
    
    private final int port = 2027;
    private final int connection = 2;
    private LinkedList<Socket> users = new LinkedList<Socket>();
    private Boolean inning = true;
    private int game[][] = new int[3][3];
    private int innings = 1;

    /**
     * method to receive connections
     */
    public void toReceive(){
        try {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    game[i][j] = -1;
                }
            }
            
            ServerSocket server = new ServerSocket(port,connection);
            System.out.println("Esperando Judadores");
            while(true){
                Socket client = server.accept();
                users.add(client);
                int players = innings % 2 == 0 ? 1 : 0;
                innings ++;
                
                Runnable run = new GameThreads(client, users, players, game);
                Thread thread = new Thread(run);
                thread.start();
                
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.toReceive();
    }
    
}
