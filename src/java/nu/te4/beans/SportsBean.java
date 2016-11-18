/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.beans;

import com.mysql.jdbc.Connection;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import nu.te4.support.ConnectionFactory;

@Stateless
public class SportsBean {

    public JsonArray getGames() {
        try {

            Connection connection = ConnectionFactory.make("testserver");
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM matcher";
            ResultSet data = stmt.executeQuery(sql);
            //arraybuilder
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                int id = data.getInt("id");
                int hl = data.getInt("hemmalag");
                int bl = data.getInt("bortalag");
                int hp = data.getInt("poanghemma");
                int bp = data.getInt("poangbort");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("id", id)
                        .add("hemmalag", hl)
                        .add("bortalag", bl)
                        .add("poanghemma", hp)
                        .add("poangborta", bp).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
        return null;
    }

    public boolean addGame(String body) {
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject data = jsonReader.readObject();
        jsonReader.close();
        int hl = data.getInt("hemmalag");
        int bl = data.getInt("bortalag");
        int ph = data.getInt("poanghemma");
        int pb = data.getInt("poangborta");
        System.out.println("hemmalag:"+hl);
        System.out.println("bortalag:"+bl);
        System.out.println("phemma:"+ph);
        System.out.println("pBorta:"+pb);
        //regler
        if ((ph + pb == 3) && (hl > 0 && hl <= 10) && (bl > 0 && bl <= 10)) {
            try {
                Connection connection = ConnectionFactory.make("testserver");
                 PreparedStatement stmt = connection.prepareStatement("INSERT INTO matcher VALUES(NULL,?,?,?,?)");
                 stmt.setInt(1, hl);
                 stmt.setInt(2, bl);
                 stmt.setInt(3, ph);
                 stmt.setInt(4, pb);
                 stmt.executeUpdate();
                 connection.close();
                 return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean deleteGame(int id) {
        try {
            Connection connection = ConnectionFactory.make("testserver");
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM matcher WHERE id=?");
            stmt.setInt(1, id);
            System.out.println(stmt.toString());
            stmt.executeUpdate();
            connection.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error: "+ex.getMessage());
            return false;
        }
    }

    public boolean updateGame(String body) {
         JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject data = jsonReader.readObject();
        jsonReader.close();
        int id = data.getInt("id");
        int hl = data.getInt("hemmalag");
        int bl = data.getInt("bortalag");
        int ph = data.getInt("poanghemma");
        int pb = data.getInt("poangborta");
        
        //regler
        if ((ph + pb == 3) && (hl > 0 && hl <= 10) && (bl > 0 && bl <= 10)) {
            try {
                Connection connection = ConnectionFactory.make("testserver");
                 PreparedStatement stmt = connection.prepareStatement("UPDATE matcher SET hemmalag = ?, bortalag = ?, poanghemma = ?, poangbort = ? WHERE id = ?");
                 stmt.setInt(1, hl);
                 stmt.setInt(2, bl);
                 stmt.setInt(3, ph);
                 stmt.setInt(4, pb);
                 stmt.setInt(5, id);
                 stmt.executeUpdate();
                 connection.close();
                 return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }

        
    }
    
    public JsonObject getTeam(int id){
             try {
            Connection connection = ConnectionFactory.make("testserver");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tabellen WHERE lagnamn = (SELECT lag.namn FROM lag WHERE lag.id = ?)");
            stmt.setInt(1, id);
            System.out.println(stmt.toString());
            ResultSet data = stmt.executeQuery();
            data.next();
            JsonObject object = Json.createObjectBuilder().add("lag", data.getString("lagnamn")).add("p",data.getInt("p")).build();
           connection.close();
            return object;
        } catch (Exception ex) {
            System.out.println("Error: "+ex.getMessage());
            return null;
        }
    }
    
    public JsonArray getTeams(){
             try {
            Connection connection = ConnectionFactory.make("testserver");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM lag");
            System.out.println(stmt.toString());
            ResultSet data = stmt.executeQuery();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while(data.next()){
                arrayBuilder.add(Json.createObjectBuilder().add("id", data.getInt("id")).add("lagnamn",data.getString("namn")).build());
            }
           connection.close();
            return arrayBuilder.build();
        } catch (Exception ex) {
            System.out.println("Error: "+ex.getMessage());
            return null;
        }
    }
     
    public JsonArray getTable(){
             try {
            Connection connection = ConnectionFactory.make("testserver");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM tabellen");
            System.out.println(stmt.toString());
            ResultSet data = stmt.executeQuery();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while(data.next()){
                arrayBuilder.add(Json.createObjectBuilder().add("lagnamn",data.getString("lagnamn")).add("p", data.getInt("p")).build());
            }
           connection.close();
            return arrayBuilder.build();
        } catch (Exception ex) {
            System.out.println("Error: "+ex.getMessage());
            return null;
        }
    }
}
