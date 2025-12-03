package com.example.esmanager;

import java.sql.*;

import static com.example.esmanager.DBConst.*;

public class Database {
    private static Database instance;
    private Connection connection;

    private Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + DB_NAME + "?serverTimezone=UTC", DB_USER, DB_PASS);
            System.out.println("Connected");
            createTable(TABLE_USER, CREATE_TABLE_USER, connection);
            createTable(TABLE_GAME, CREATE_TABLE_GAME, connection);
            createTable(TABLE_MATCH, CREATE_TABLE_MATCH, connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTable(String tableName, String tableQuery, Connection connection) throws SQLException {
        Statement createTable;
        DatabaseMetaData md = connection.getMetaData();
        ResultSet resultSet = md.getTables(DB_NAME, null, tableName, null);
        if(resultSet.next()) {
            System.out.println("Table already exists");
        } else {
            createTable = connection.createStatement();
            createTable.execute(tableQuery);
            System.out.println("Table " + tableName + " created");
        }
    }

    public void query( String query, Connection connection) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        statement.execute(query);
        System.out.println(statement.getResultSet());
    }
}
