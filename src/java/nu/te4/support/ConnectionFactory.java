/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.support;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;

/**
 *
 * @author DanLun2
 */
public class ConnectionFactory {

    public static Connection make(String server) throws Exception {

        if (server.equals("testserver")) {        
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://10.16.58.152/sportapp", "root", "");
            return connection;
        }

        return null;
    }
}
