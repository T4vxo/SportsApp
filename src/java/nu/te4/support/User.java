/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.support;

import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.List;
import javax.ws.rs.core.HttpHeaders;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author DanLun2
 */
public class User {

    public static boolean authoricate(HttpHeaders httpHeaders) {
        try {
            List<String> authHeader = httpHeaders.getRequestHeader(HttpHeaders.AUTHORIZATION);
            String header = authHeader.get(0);
            header = header.substring(header.indexOf(" ") + 1);
            byte[] decoded = Base64.getDecoder().decode(header);
            String userPass = new String(decoded);
            String username = userPass.substring(0, userPass.indexOf(":"));
            String password = userPass.substring(userPass.indexOf(":") + 1);
            
            Connection connection = ConnectionFactory.make("testserver");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet data = stmt.executeQuery();
            data.next();
            String hashedPass = data.getString("password");
            connection.close();
            if (BCrypt.checkpw(password, hashedPass)) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean createUser(String username, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try {
            Connection connection = ConnectionFactory.make("testserver");
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO users VALUES(?,?)");
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
            connection.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            return false;
        }

    }
}
