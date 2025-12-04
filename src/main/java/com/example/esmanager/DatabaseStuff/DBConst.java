package com.example.esmanager.DatabaseStuff;

public class DBConst {
    public static final String DB_NAME = "eharnadekdb";
    public static final String DB_USER = "eharnadek";
    public static final String DB_PASS = "mtlkkmtlkk";


    /**
     * USER TABLE
     */
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_PASS = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "(" +
            COLUMN_ID + " varchar(255) NOT NULL PRIMARY KEY, " +
            COLUMN_PASS + " varchar(255) NOT NULL, " +
            COLUMN_EMAIL + " varchar(255) NOT NULL);";

    /**
     * GAME TABLE
     */
    public static final String TABLE_GAME = "game";
    public static final String GAME_COLUMN_ID = "game_id";
    public static final String GAME_COLUMN_TITLE = "title";
    public static final String GAME_COLUMN_IMAGE_PATH = "image_path";
    public static final String CREATE_TABLE_GAME =
            "CREATE TABLE " + TABLE_GAME + "(" +
            GAME_COLUMN_ID + " int NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
            GAME_COLUMN_TITLE + " varchar(255) NOT NULL, " +
            GAME_COLUMN_IMAGE_PATH + " varchar(512), " +
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


    /*Finances Table*/

    public static final String TABLE_FINANCE = "finance";
    public static final String FINANCE_COLUMN_ID = "finance_id";
    public static final String FINANCE_COLUMN_CATEGORY = "category";
    public static final String FINANCE_COLUMN_AMOUNT = "amount";
    public static final String FINANCE_COLUMN_DATE = "date";

    public static final String CREATE_TABLE_FINANCE =
            "CREATE TABLE " + TABLE_FINANCE + "(" +
                    FINANCE_COLUMN_ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    FINANCE_COLUMN_CATEGORY + " VARCHAR(255) NOT NULL, " +
                    FINANCE_COLUMN_AMOUNT + " DOUBLE NOT NULL, " +
                    FINANCE_COLUMN_DATE + " VARCHAR(255) NOT NULL, " +
                    COLUMN_ID + " VARCHAR(255) NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ")" +
                    ");";

    /* Players tabel*/

    public static final String TABLE_PLAYER = "player";
    public static final String PLAYER_COLUMN_ID = "player_id";
    public static final String PLAYER_COLUMN_NAME = "name";
    public static final String PLAYER_COLUMN_NUMBER = "number";
    public static final String PLAYER_COLUMN_POSITION = "position";

    public static final String CREATE_TABLE_PLAYER =
            "CREATE TABLE " + TABLE_PLAYER + "(" +
                    PLAYER_COLUMN_ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    PLAYER_COLUMN_NAME + " VARCHAR(255) NOT NULL, " +
                    PLAYER_COLUMN_NUMBER + " INT NOT NULL, " +
                    PLAYER_COLUMN_POSITION + " VARCHAR(255) NOT NULL, " +
                    COLUMN_ID + " VARCHAR(255) NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ")" +
                    ");";



    /* Statistics table */

    public static final String TABLE_STAT = "stats";
    public static final String STAT_COLUMN_ID = "stat_id";
    public static final String STAT_COLUMN_TYPE = "type";
    public static final String STAT_COLUMN_VALUE = "value";

    public static final String CREATE_TABLE_STAT =
            "CREATE TABLE " + TABLE_STAT + "(" +
                    STAT_COLUMN_ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    STAT_COLUMN_TYPE + " VARCHAR(255) NOT NULL, " +
                    STAT_COLUMN_VALUE + " INT NOT NULL, " +
                    COLUMN_ID + " VARCHAR(255) NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ")" +
                    ");";
}
