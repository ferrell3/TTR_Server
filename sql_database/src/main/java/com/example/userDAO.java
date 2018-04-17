package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class userDAO implements UserDAO{
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

    //create table statement:
    public String createUserTable = "create Table if not exists Users(\n" +
            "userJson String not null,\n"+
            ");";




    @Override
    public void storeUsers(String jsonUsers) throws SQLException {
        Statement stmt = null;
        connect = null;
        try {
            connect = DriverManager.getConnection(connectURL);
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            stmt.execute("drop table if exists Users");
            stmt.execute(createUserTable);
            connect.commit();
            connect.close();
        }
        catch (Exception e) {
            connect.rollback();
            connect.close();
            System.out.println("error in storeUsers : clear tables " + e);
        }
        try{
            connect= DriverManager.getConnection(connectURL);
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            stmt.execute(createUserTable);
            connect.commit();
            connect.close();
        }catch (Exception e){
            connect.rollback();
            connect.close();
            System.out.println("error in storeUsers sqluserDAO "+e);
        }
        connect = null;
        PreparedStatement stmt2 = null;
        try{
            connect = DriverManager.getConnection(connectURL);
            String sql = "insert into Users (userJson)VALUES(?)";
            stmt2 = connect.prepareStatement(sql);
            stmt2.setString(1, jsonUsers);
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
    public String loadUsers() throws SQLException {

        PreparedStatement stmt = null;
        String userJson= "";
        ResultSet rs = null;
        try {
            connect=DriverManager.getConnection(connectURL);
            String sql = "select userJson from Users";
            stmt= connect.prepareStatement(sql);
            rs = stmt.executeQuery();
            userJson =new String(rs.getString(1));
        }catch (Exception e){
            System.out.println("Within userDAO "+e);
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
        return userJson;
    }




    @Override
    public void clear() throws SQLException {
        Statement stmt = null;
        connect  = null;
        try {
            connect = DriverManager.getConnection(connectURL);
            connect.setAutoCommit(false);
            stmt = connect.createStatement();
            stmt.execute("drop table if exists Users");
            stmt.execute(createUserTable);
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
