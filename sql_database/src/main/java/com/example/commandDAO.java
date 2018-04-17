package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by jesjames on 4/17/18.
 */

public class commandDAO implements CommandDAO {
    private String dbName = "ttrserver.sqlite";
    private String connectURL = "jdbc:sqlite:"+dbName;
    private Connection connect;

    static{
        final String driver = "org.sqlite.JDBC";
        try{
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String createlCommandTable = "create Table if not exists lobbyCommandSql(\n" +
            "gCmdJson String not null,\n"+
            ");";

    public String creategCommandTable = "create Table if not exists gameCommandSql(\n" +
            "lCmdJson String not null,\n"+
            ");";



    //Need to Finish Lobby Commands!
    @Override
    public void storeLobbyCommands(String jsonStr) throws SQLException {
        Statement stmt = null;
        connect = null;
        try {
            connect = DriverManager.getConnection(connectURL);
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            stmt.execute("drop table if exists lobbyCommandSql");
            stmt.execute(createlCommandTable);
            connect.commit();
            connect.close();
        }
        catch (Exception e) {
            connect.rollback();
            connect.close();
            System.out.println("error in storeLobbyCommands : clear tables " + e);
        }
        try{
            connect= DriverManager.getConnection(connectURL);
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            stmt.execute(createlCommandTable);
            connect.commit();
            connect.close();
        }catch (Exception e){
            connect.rollback();
            connect.close();
            System.out.println("error in cmdDao.storeLobbyCommands() - sqlCommandDao: "+e);
        }
        connect = null;
        PreparedStatement stmt2 = null;
        try{
            connect = DriverManager.getConnection(connectURL);
            String sql = "insert into lobbyCommandSql (gCmdJson)VALUES(?)";
            stmt2 = connect.prepareStatement(sql);
            stmt2.setString(1, jsonStr);
            stmt2.executeUpdate();
        }
        catch (Exception err){
            System.out.println("error in cmdDao.storeLobbyCommands(): "+ err);
        }
        finally {
            if (stmt != null){stmt.close();}
            connect.close();
        }

    }

    @Override
    public void storeGameCommands(String jsonStr) throws SQLException {

        Statement stmt = null;
        connect = null;
        try {
            connect = DriverManager.getConnection(connectURL);
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            stmt.execute("drop table if exists gameCommandSql");
            stmt.execute(creategCommandTable);
            connect.commit();
            connect.close();
        }
        catch (Exception e) {
            connect.rollback();
            connect.close();
            System.out.println("error in storeGameCommands : clear tables " + e);
        }
        try{
            connect= DriverManager.getConnection(connectURL);
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            stmt.execute(creategCommandTable);
            connect.commit();
            connect.close();
        }catch (Exception e){
            connect.rollback();
            connect.close();
            System.out.println("error in storeGameCommands sqlCommandDao "+e);
        }
        connect = null;
        PreparedStatement stmt2 = null;
        try{
            connect = DriverManager.getConnection(connectURL);
            String sql = "insert into gameCommandSql (gCmdJson)VALUES(?)";
            stmt2 = connect.prepareStatement(sql);
            stmt2.setString(1, jsonStr);
            stmt2.executeUpdate();
        }
        catch (Exception err){
            System.out.println("error in userdao: create user "+ err);
        }
        finally {
            if (stmt != null){stmt.close();}
            connect.close();
        }

    }

    @Override
    public String loadLobbyCommands() throws SQLException {
        PreparedStatement stmt = null;
        String gameCmdJson= "";
        ResultSet rs = null;
        try {
            connect=DriverManager.getConnection(connectURL);
            String sql = "select gCmdJson from gameCommandSql";
            stmt= connect.prepareStatement(sql);
            rs = stmt.executeQuery();
            gameCmdJson =new String(rs.getString(1));
        }catch (Exception e){
            System.out.println("Within CommandDao "+e);
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
        return gameCmdJson;
        //pulls string from table
    }

    @Override
    public String loadGameCommands() throws SQLException {
        PreparedStatement stmt = null;
        String lobbyCmdJson= "";
        ResultSet rs = null;
        try {
            connect=DriverManager.getConnection(connectURL);
            String sql = "select lCmdJson from lobbyCommandSql";
            stmt= connect.prepareStatement(sql);
            rs = stmt.executeQuery();
            lobbyCmdJson =new String(rs.getString(1));
        }catch (Exception e){
            System.out.println("Within CommandDao "+e);
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
        return lobbyCmdJson;
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
            stmt.execute("drop table if exists lobbyCommandSql");
            stmt.execute("drop table if exists gameCommandSql");
            stmt.execute(createlCommandTable);
            stmt.execute(creategCommandTable);
            connect.commit();
            connect.close();
        }
        catch (Exception e) {
            connect.rollback();
            connect.close();
            System.out.println("error in dao : clear tables " + e);
        }
    }
}
