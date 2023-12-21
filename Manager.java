package za.ac.tut.bl;

import java.sql.*;

/**
 *
 * @author neots
 */
public class Manager implements ManagerInterface{
    
    private Double balance;
    private Connection con;
    
    public Manager(String url, String user, String password) throws SQLException {
        con = DriverManager.getConnection(url, user, password);
        String sql = "SELECT BALANCE FROM FAMILYACCOUNT WHERE ID =?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            
            balance = rs.getDouble(1);
        ps.close();
        
    }
    
    @Override
    public synchronized Double getBalance() throws SQLException {
        String sql = "SELECT BALANCE FROM FAMILYACCOUNT WHERE ID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            balance = rs.getDouble(1);
        ps.close();
        return balance;
    }

    @Override
    public synchronized String withdraw(Double amount) throws SQLException {
        String sql = "UPDATE FAMILYACCOUNT SET BALANCE = ? WHERE ID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        String string;
        if(amount > balance){
            string = "BALANCE INSUFFICIENT";
        }else{
            ps.setDouble(1, getBalance()-amount);
            ps.setInt(2, 1);
            ps.executeUpdate();
            ps.close();
            string = "Debit: -R"+amount;
        }        
        return string;
    }

    @Override
    public synchronized void deposit(Double amount) throws SQLException {
        String sql = "UPDATE FAMILYACCOUNT SET BALANCE = ? WHERE ID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        
        ps.setDouble(1, getBalance()+amount);
        ps.setInt(2, 1);
        ps.executeUpdate();
        balance += amount;
    }
    
}
