package com.example.esmanager;

import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.esmanager.DBConst.*;

public class UserTable {
    private static UserTable instance;
    Database db = Database.getInstance();

    public User getUser(String id, String pass) {
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_ID + " = '" + id + "' AND " + COLUMN_PASS + " = '" + pass + "';";
        try {
            Statement getUser = db.getConnection().createStatement();
            ResultSet rs = getUser.executeQuery(query);
            if(rs.next()) {
                return new User(id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static UserTable getInstance() {
        if(instance == null) {
            instance = new UserTable();
        }
        return instance;
    }
    public boolean createUser(String id, String pass) {
        String check = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_ID + " = '" + id + "';";
        try {
            Statement getUser = db.getConnection().createStatement();
            ResultSet rs = getUser.executeQuery(check);
            if(rs.next()) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        String query = "INSERT INTO " + TABLE_USER + " VALUES ('" + id + "', '" + pass + "');";
        try {
            Statement createUser = db.getConnection().createStatement();
            boolean user = createUser.execute(query);
            if(user) {
                System.out.println("User created");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
