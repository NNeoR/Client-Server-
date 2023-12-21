package za.ac.tut.bl;

import java.sql.SQLException;
/**
 *
 * @author neots
 */
public interface ManagerInterface {
    
    public Double getBalance() throws SQLException;
    public void deposit(Double amount) throws SQLException;
    public String withdraw(Double amount) throws SQLException;
    
}
