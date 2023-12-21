package za.ac.tut;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.ac.tut.bl.Manager;

/**
 *
 * @author neots
 */
class RequestHandler extends Thread {
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    public RequestHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
    }
    @Override
    public void run(){
        try{
            Manager m = new Manager("jdbc:derby://localhost:1527/sport","app","123");
            Double balance, input;
            Double amount;
            while(true){
                String data = in.readLine();
                if(data.equals("cancel")) break;
                
                if(data.contains("balance")){
                    balance = m.getBalance();
                    out.println("You balance: R"+balance);
                    
                }else if(data.contains("withdraw")){
                    String[] split = data.split("#");
                    input = Double.parseDouble(split[1]);
                    String string = m.withdraw(input);
                    out.println(string);
                    
                }else if(data.contains("deposit")){
                    String[] split = data.split("#");
                    input = Double.parseDouble(split[1]);
                    m.deposit(input);
                    out.println("Credit: +R"+input);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
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

}
