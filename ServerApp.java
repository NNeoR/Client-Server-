package za.ac.tut;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.ac.tut.bl.Manager;

/**
 *
 * @author neots
 */
public class ServerApp {
    
    private ServerSocket serverSocket;
    private Socket socket;

    public ServerApp(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer(){
        try{
            while(true){
                socket = serverSocket.accept();
                System.out.println("Processing request for IP Addr: "+socket);
                
                
                RequestHandler requestHandler = new RequestHandler(socket);
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(requestHandler);
            }
        }catch(IOException io){
            io.printStackTrace();
        }
    }
    public static void main(String[] args){
        try {
            ServerApp server = new ServerApp(new ServerSocket(1090));
            server.startServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
}
