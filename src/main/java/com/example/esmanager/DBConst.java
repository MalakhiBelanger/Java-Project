package com.example.esmanager;

public class DBConst {
    public static final String DB_NAME = "eharnadekmd";
    public static final String DB_USER = "eharnadek";
    public static final String DB_PASS = "mtlkkmtlkk";


    /**
     * USER TABLE
     */
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_PASS = "password";
    public static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "(" +
            COLUMN_ID + " varchar(255) NOT NULL PRIMARY KEY, " +
            COLUMN_PASS + " varchar(255) NOT NULL);";

    /**
     * GAME TABLE
     */
    public static final String TABLE_GAME = "game";
    public static final String GAME_COLUMN_ID = "game_id";
    public static final String GAME_COLUMN_TITLE = "title";
    public static final String CREATE_TABLE_GAME =
            "CREATE TABLE " + TABLE_GAME + "(" +
            GAME_COLUMN_ID + " int NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
            GAME_COLUMN_TITLE + " varchar(255) NOT NULL, " +
            COLUMN_ID + " varchar(255) NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + "));";

    /**
     * MATCH TABLE
     */
    public static final String TABLE_MATCH = "match_played";
    public static final String MATCH_COLUMN_ID = "match_id";
    public static final String MATCH_COLUMN_WON = "won";
    public static final String MATCH_COLUMN_SCORE_A = "scoreA";
    public static final String MATCH_COLUMN_SCORE_B = "scoreB";
    public static final String CREATE_TABLE_MATCH =
            "CREATE TABLE " + TABLE_MATCH + "(" +
            MATCH_COLUMN_ID + " int NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
            MATCH_COLUMN_WON + " boolean, " +
            MATCH_COLUMN_SCORE_A + " int, " +
            MATCH_COLUMN_SCORE_B + " int," +
            COLUMN_ID + " varchar(255) NOT NULL, " +
            GAME_COLUMN_ID + " int NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ")," +
            "FOREIGN KEY (" + GAME_COLUMN_ID + ") REFERENCES " + TABLE_GAME + "(" + GAME_COLUMN_ID + "));";
}
