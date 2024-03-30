/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Base64;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author ntu-user
 */
public class DB {

    private String fileName = "jdbc:sqlite:comp20081.db";
    private int timeout = 30;
    private String dataBaseName = "COMP20081";
    private String dataBaseTableName = "Users";
    Connection connection = null;
    private Random random = new SecureRandom();
    private String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private int iterations = 10000;
    private int keylength = 256;
    private String saltValue;

    private String dataBaseFileTable = "User_files";
    private String dataBasePermissionTable = "User_permission";

    public String getDataBaseFileTable() {
        return dataBaseFileTable;
    }

    public String getDataBasePermissionTable() {
        return dataBasePermissionTable;
    }

    /**
     * @brief constructor - generates the salt if it doesn't exists or load it
     * from the file .salt
     */
    DB() {
        try {
            File fp = new File(".salt");
            if (!fp.exists()) {
                saltValue = this.getSaltvalue(30);
                try (FileWriter myWriter = new FileWriter(fp)) {
                    myWriter.write(saltValue);
                    myWriter.close();
                }
            } else {
                Scanner myReader = new Scanner(fp);
                while (myReader.hasNextLine()) {
                    saltValue = myReader.nextLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws java.lang.ClassNotFoundException
     * @brief create a new table
     * @param tableName name of type String
     */
    public void createTable(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + "("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "name STRING NOT NULL, "
                    + "password STRING NOT NULL, "
                    + "user_type STRING NOT NULL, "
                    + "registeredDateTime TIMESTAMP NOT NULL)");

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public void createTable2(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);

            statement.executeUpdate("create table if not exists " + tableName + "("
                    + "id integer primary key autoincrement NOT NULL, "
                    + "file_path string  NOT NULL, "
                    + "user_id integer references user(id)  NOT NULL, " // Adding foreign key
                    + "file_name string  NOT NULL, " // Add file name column
                    + "file_type string  NOT NULL, " // Add file type column
                    + "file_size long  NOT NULL, " // Add file size column
                    + "uploaded_time timestamp default current_timestamp)"); // Add uploaded time column

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public void createUserPermissionTable(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "file_owner_id INTEGER NOT NULL, "
                    + "file_id INTEGER NOT NULL, "
                    + "access_level STRING NOT NULL, "
                    + "access_user STRING NOT NULL, "
                    + "last_update TIMESTAMP NOT NULL, "
                    + "FOREIGN KEY (file_owner_id) REFERENCES users(id), "
                    + "FOREIGN KEY (file_id) REFERENCES user_files(id))");

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * @throws java.lang.ClassNotFoundException
     * @brief delete table
     * @param tableName of type String
     */
    public void delTable(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("drop table if exists " + tableName);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * @throws java.security.spec.InvalidKeySpecException
     * @throws java.lang.ClassNotFoundException
     * @brief add data to the database method
     * @param user name of type String
     * @param password of type String
     */
    public void addDataToDB(String user, String password) throws InvalidKeySpecException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
//            System.out.println("Adding User: " + user + ", Password: " + password);
            statement.executeUpdate("insert into " + dataBaseTableName + " (name, password,user_type, registeredDateTime) values('" + user + "','" + generateSecurePassword(password) + "','user', current_timestamp)");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public void addFilesToDB(String user, String filePath) throws InvalidKeySpecException, ClassNotFoundException {
        try {
            int userId = getUserId(user);
            if (userId != -1) {  // Check if user exists
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(fileName);

                // Assuming you have a 'files' table with a foreign key column 'userId' referencing 'user' table
                String insertQuery = "INSERT INTO " + dataBaseFileTable + "  (file_path, user_id, file_name, file_type, file_size, uploaded_time) VALUES (?, ?, ?, ?, ?, current_timestamp)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    // Extracting metadata from the file path
                    Path file = Paths.get(filePath);
                    String fileName1 = file.getFileName().toString();
                    System.out.println(fileName1);
//                    String fileType = Files.probeContentType(file);
                    int lastDotIndex = fileName1.lastIndexOf(".");
                    String fileType = "Unknown";
                    System.out.println(lastDotIndex);
                    if (lastDotIndex != -1) {
                        if (lastDotIndex == 1) {
                            // Extract the file extension
                            fileType = Files.probeContentType(file);
                        } else {
                            fileType = fileName1.substring(lastDotIndex + 1).toLowerCase();
                        }
                    }
                    long fileSize = Files.size(file);

                    // Setting values for the prepared statement
                    preparedStatement.setString(1, filePath);
                    preparedStatement.setInt(2, userId);
                    preparedStatement.setString(3, fileName1);
                    preparedStatement.setString(4, fileType);
                    preparedStatement.setLong(5, fileSize);

                    // Executing the query
                    preparedStatement.executeUpdate();
                }
            } else {
                System.out.println("User not found: " + user);
            }

        } catch (SQLException | IOException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void savePermission(int author_id, int fileId, int userId, String accessLevel) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);

            // Check if the row with the same file_id and access_user already exists
            PreparedStatement checkStatement = connection.prepareStatement(
                    "SELECT * FROM " + dataBasePermissionTable + " WHERE file_id = ? AND access_user = ?");
            checkStatement.setInt(1, fileId);
            checkStatement.setInt(2, userId);

            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Row exists, update the last_update time and access_level
                PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE " + dataBasePermissionTable
                        + " SET last_update = CURRENT_TIMESTAMP, access_level = ?"
                        + " WHERE file_id = ? AND access_user = ?");
                updateStatement.setString(1, accessLevel);
                updateStatement.setInt(2, fileId);
                updateStatement.setInt(3, userId);

                updateStatement.executeUpdate();
            } else {
                // Row doesn't exist, insert a new row
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO " + dataBasePermissionTable
                        + " (file_owner_id, file_id, access_level, access_user, last_update) "
                        + "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)");

                insertStatement.setInt(1, author_id);       // Assuming userId is the file_owner_id
                insertStatement.setInt(2, fileId);
                insertStatement.setString(3, accessLevel);
                insertStatement.setInt(4, userId);

                insertStatement.executeUpdate();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();  // Handle the exception appropriately
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public int getUserId(String userName) throws ClassNotFoundException, SQLException {
        int userId = -1;  // Default value if user is not found

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);

            // Assuming you have a 'user' table with a primary key column 'id'
            String selectQuery = "SELECT id FROM users WHERE name = ?";
//            System.out.println("ok");
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, userName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        userId = resultSet.getInt("id");
                    }
                }
            }

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return userId;
    }

    /**
     * @return @throws java.lang.ClassNotFoundException
     * @throws java.lang.ClassNotFoundException
     * @brief get data from the Database method
     * @retunr results as ResultSet
     */
    public ObservableList<User> getDataFromTable() throws ClassNotFoundException {
        ObservableList<User> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseTableName);
            while (rs.next()) {
                // read the result set
                String name = rs.getString("name");
                String password = rs.getString("password");
                Timestamp registeredDateTime = rs.getTimestamp("registeredDateTime");
                result.add(new User(name, password, registeredDateTime));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    public ObservableList<UserFile> getUserFiles(String username) throws ClassNotFoundException {
        ObservableList<UserFile> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);

            // Assuming there's a 'user_files' table with columns 'file_id', 'file_path', 'file_name', 'file_type', 'file_size', and 'uploaded_time'
            ResultSet rs = statement.executeQuery("SELECT * FROM user_files WHERE user_id = (SELECT id FROM users WHERE name = '" + username + "')");

            while (rs.next()) {
                // Read the result set
                int fileId = rs.getInt("id");
                String filePath = rs.getString("file_path");
                String fileName1 = rs.getString("file_name");
                String fileType = rs.getString("file_type");
                long fileSize = rs.getLong("file_size");
                Timestamp uploadedTime = rs.getTimestamp("uploaded_time");

                result.add(new UserFile(fileId, filePath, fileName1, fileType, fileSize, uploadedTime));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Close the connection
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    /**
     * @throws java.security.spec.InvalidKeySpecException
     * @throws java.lang.ClassNotFoundException
     * @brief decode password method
     * @param user name as type String
     * @param pass plain password of type String
     * @return true if the credentials are valid, otherwise false
     */
    public boolean validateUser(String user, String pass) throws InvalidKeySpecException, ClassNotFoundException {
        Boolean flag = false;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select name, password from " + this.dataBaseTableName);
            String inPass = generateSecurePassword(pass);
            // Let's iterate through the java ResultSet
            while (rs.next()) {
                if (user.equals(rs.getString("name")) && rs.getString("password").equals(inPass)) {
                    flag = true;
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return flag;
    }

    public int validateUserTOLogin(String user, String pass) throws InvalidKeySpecException, ClassNotFoundException {
        int flag = 0;
        //0 = user
        //1 = admin
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select name, password, user_type from " + this.dataBaseTableName);
            String inPass = generateSecurePassword(pass);
            // Let's iterate through the java ResultSet
            while (rs.next()) {
                if (user.equals(rs.getString("name")) && rs.getString("password").equals(inPass)) {
                    if ("admin".equals(rs.getString("user_type"))) {
                        flag = 1;
                        break;
                    } else if ("user".equals(rs.getString("user_type"))) {
                        flag = 2;
                        break;
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return flag;
    }

    public boolean validateUserTORegister(String user) throws InvalidKeySpecException, ClassNotFoundException {
        boolean flag = false;
        //false = new user
        //true = registered previousely
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select name from " + this.dataBaseTableName);
            // Let's iterate through the java ResultSet
            while (rs.next()) {
                if (user.equals(rs.getString("name"))) {
                    flag = true;
                    break;
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return flag;
    }

    private String getSaltvalue(int length) {
        StringBuilder finalval = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            finalval.append(characters.charAt(random.nextInt(characters.length())));
        }

        return new String(finalval);
    }

    /* Method to generate the hash value */
    private byte[] hash(char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keylength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public String generateSecurePassword(String password) throws InvalidKeySpecException {
        String finalval = null;

        byte[] securePassword = hash(password.toCharArray(), saltValue.getBytes());

        finalval = Base64.getEncoder().encodeToString(securePassword);

        return finalval;
    }

    /**
     * @brief delete a user from the database
     * @param username the username of the user to be deleted
     */
    public void deleteUser(String username) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);

            // Assuming you have a 'users' table with a primary key column 'id'
            String deleteQuery = "DELETE FROM " + dataBaseTableName + " WHERE name = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void deleteFile(UserFile selectedFile) throws SQLException, ClassNotFoundException {
        Connection connection1 = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection1 = DriverManager.getConnection(fileName);
            connection1.setAutoCommit(false);

            String deleteQuery = "DELETE FROM user_files WHERE id = ?";

            try (PreparedStatement preparedStatement = connection1.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, selectedFile.getFileId()); // Assuming fileId is the primary key
                preparedStatement.executeUpdate();
            }

            connection1.commit();
        } finally {
            if (connection1 != null) {
                connection1.close();
            }
        }
    }

    public void updateUser(String oldUsername, String newUsername, String newPassword) throws InvalidKeySpecException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);

// Assuming you have a 'users' table with columns 'name', 'password', and 'registeredDateTime'
            String updateQuery = "UPDATE " + dataBaseTableName + " SET name = ?, password = ?, registeredDateTime = current_timestamp WHERE name = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                // Setting values for the prepared statement
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, generateSecurePassword(newPassword));
                preparedStatement.setString(3, oldUsername);

                // Executing the query
                preparedStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @brief get table name
     * @return table name as String
     */
    public String getTableName() {
        return this.dataBaseTableName;
    }

    /**
     * @brief print a message on screen method
     * @param message of type String
     */
    public void log(String message) {
        System.out.println(message);

    }

    ObservableList<UserLoad> getAllUsers() {
        ObservableList<UserLoad> users = FXCollections.observableArrayList();

        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);

            ResultSet rs = statement.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                int userId = rs.getInt("id");
                String username = rs.getString("name");

                // Add more properties as needed based on your user table structure
                UserLoad user = new UserLoad(userId, username);
                users.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Close the connection
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return users;

    }

}
