/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.support;

import java.util.Base64;
import java.util.List;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author DanLun2
 */
public class User {
    
    public static boolean authoricate(HttpHeaders httpHeaders){
        try {
            List<String> authHeader = httpHeaders.getRequestHeader(HttpHeaders.AUTHORIZATION);
            String header = authHeader.get(0);
            header = header.substring(header.indexOf(" ")+1);
            byte[] decoded = Base64.getDecoder().decode(header);
            String userPass = new String(decoded);
            String username = userPass.substring(0,userPass.indexOf(":"));
            String password = userPass.substring(userPass.indexOf(":")+1);
                //temporär testlösning!!
            if(username.equals("daniel") && password.equals("lundberg")){
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
