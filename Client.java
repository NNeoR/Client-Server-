package za.ac.tut;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author neots
 */
public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
    }
    public void run(){
        try{
            String command = "", serverResponse;
            Double amount;
            while(socket.isConnected()){
                
                Integer option = getOption();
                System.out.println(">");
                switch(option){
                    case 1:
                        command = "get balance";
                        
                        break;
                    case 2:
                        amount = getAmount();
                        command = "withdraw#"+amount;
                        break;
                    case 3:
                        amount = getAmount();
                        command = "deposit#"+amount;
                        break;
                    case 4:
                        command = "cancel";
                        break;       
                }
                
                out.println(command);
                
                serverResponse = in.readLine();
                if(serverResponse == null) break;
                System.err.println("[SERVER]::"+serverResponse);
                System.out.println(">");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            try{
                if(socket != null)
                    socket.close();
                if(in != null)
                    in.close();
                if(out != null)
                    out.close();
            }catch(IOException io){
                io.printStackTrace();
            }
        }
    }

    private Integer getOption() {
        System.out.print("Choose an option: \n"
                + "1 - check balance\n"
                + "2 - withdraw\n"
                + "3 - deposit\n"
                + "4 - cancel/exit\n"
                + "Your option: ");
        Integer option = new Scanner(System.in).nextInt();
        return option;
    }
    public static void main(String[] args){
        try{
            Socket socket = new Socket("localhost", 1090);
            Client client = new Client(socket);
            client.run();
        }catch(IOException io){
            io.printStackTrace();
        }
        
    }

    private Double getAmount() {
        System.out.print("Amount: ");
        Double amount = new Scanner(System.in).nextDouble();
        return amount;
    }
}
