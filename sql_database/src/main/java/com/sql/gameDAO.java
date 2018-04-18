package com.sql;

import com.shared.GameDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by jesjames on 4/17/18.
 */

public class gameDAO implements GameDAO {

    //Class Properties:
    private String dbName = "ttrserver.sqlite";
    private String connectURL = "jdbc:sqlite:"+dbName;
    private Connection connect;

    //Name of the Database:
    //Games

    public String createGTable = "create Table if not exists Games(\n" +
            "gameJson String not null\n"+
            ");";

    @Override
    public void storeGames(String jsonStr) throws SQLException {
        Statement stmt = null;
        connect = null;
        try {
            connect = DriverManager.getConnection(connectURL);
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            stmt.execute("drop table if exists Games");
            stmt.execute(createGTable);
            connect.commit();
            connect.close();
        }
        catch (Exception e) {
            connect.rollback();
            connect.close();
            System.out.println("error in storeGames: clear tables " + e);
        }
        try{
            connect= DriverManager.getConnection(connectURL);
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            stmt.execute(createGTable);
            connect.commit();
            connect.close();
        }catch (Exception e){
            connect.rollback();
            connect.close();
            System.out.println("error in gameDAO.storeGames() - sqlGameDao: "+e);
        }
        connect = null;
        PreparedStatement stmt2 = null;
        try{
            connect = DriverManager.getConnection(connectURL);
            String sql = "insert into Games (gameJson)VALUES(?)";
            stmt2 = connect.prepareStatement(sql);
            stmt2.setString(1, jsonStr);
            stmt2.executeUpdate();
        }
        catch (Exception err){
            System.out.println("error in gameDao.storeGames(): "+ err);
        }
        finally {
            if (stmt != null){stmt.close();}
            connect.close();
        }
    }


    @Override
    public String loadGames() throws SQLException {

        PreparedStatement stmt = null;
        String gameJson= "";
        ResultSet rs = null;
        try {
            connect=DriverManager.getConnection(connectURL);
            String sql = "select gameJson from Games";
            stmt= connect.prepareStatement(sql);
            rs = stmt.executeQuery();
            gameJson =new String(rs.getString(1));
        }catch (Exception e){
            System.out.println("Within gameDAO "+e);
        }
        finally {
            if(rs != null){
                rs.close();
            }
            if(stmt != null){
                stmt.close();
            }
            connect.close();
        }
        return gameJson;
        //pulls string from table
    }


    @Override
    public void clear() throws SQLException {
        Statement stmt = null;
        connect  = null;
        try {
            connect = DriverManager.getConnection(connectURL);
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            stmt.execute("drop table if exists Games");
            stmt.execute(createGTable);
            connect.commit();
            connect.close();
        }
        catch (Exception e) {
            connect.rollback();
            connect.close();
            System.out.println("error in dao : clear tables " + e);
        }
    }


    //These are the Connection methods:
    static{
        final String driver = "org.sqlite.JDBC";
        try{
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
