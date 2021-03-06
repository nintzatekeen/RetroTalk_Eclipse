/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Category;
import beans.User;
import beans.ForumThread;
import beans.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author ACER
 */
public class DaoRetro {

    private static final BasicDataSource ds = new BasicDataSource();
    private static PreparedStatement psAvailableUser;
    private static PreparedStatement psInsertUser;
    private static PreparedStatement psGetUserByUsername;
    private static PreparedStatement psGetThreads;
    private static PreparedStatement psGetUserById;
    private static PreparedStatement psGetCategoryById; 
    private static PreparedStatement psGetMessages;
    private static PreparedStatement psGetThreadById;
    private static PreparedStatement psGetMessageById;

    static {
        //Para el driver nuevo
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost/foro?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        //para el driver de netbeans 8
//        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        ds.setUrl("jdbc:mysql://localhost/foro");

        ds.setUsername("root");
        ds.setPassword("");
        try {
            Connection cn = ds.getConnection();

            String sqlAvailableUser = "select id from user where username = ?";
            String sqlInsertUser = "insert into user (username, password, email, date) values (?,?,?,now())";
            String sqlGetUserByUsername = "select id, username, password, email, bio, avatar, date from user where username=?";
            String sqlGetThreads = "select id, title, user, category from thread where category = ? order by id limit ?,50";
            String sqlGetUserById = "select id, username, password, email, bio, avatar, date from user where id=?";
            String sqlGetCategoryById = "select id, name, description from category where id = ?";
            String sqlGetMessages = "select id, content, user, thread, date, quote from message where thread = ? limit ?,50";
            String sqlGetThreadById = "select id, title, user, category from thread where id = ?";
            String sqlGetMessageById = "select id, content, user, thread, date, quote from message where id = ?";
            psAvailableUser = cn.prepareStatement(sqlAvailableUser);
            psInsertUser = cn.prepareStatement(sqlInsertUser);
            psGetUserByUsername = cn.prepareStatement(sqlGetUserByUsername);
            psGetThreads = cn.prepareStatement(sqlGetThreads);
            psGetUserById = cn.prepareStatement(sqlGetUserById);
            psGetCategoryById = cn.prepareStatement(sqlGetCategoryById);
            psGetMessages = cn.prepareStatement(sqlGetMessages);
            psGetThreadById = cn.prepareStatement(sqlGetThreadById);
            psGetMessageById = cn.prepareStatement(sqlGetMessageById);
        } catch (SQLException ex) {
        }
    }

    public static List<Category> getCategories() {
        String sql = "select id, name, description from category";
        ArrayList<Category> list = new ArrayList<Category>();
        try (Connection cn = ds.getConnection();
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Category category = new Category(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"));
                list.add(category);
            }
        } catch (SQLException ex) {
            System.err.println("Error en getCategories: " + ex.getMessage());
        }
        return list;
    }

    public static boolean isUserAvailable(String user) {
        try {
            psAvailableUser.setString(1, user);
            ResultSet rs = psAvailableUser.executeQuery();
            boolean ret = !rs.next();
            rs.close();
            return ret;
        } catch (SQLException ex) {
            System.err.println("Error en isUserAvailable: " + ex.getMessage());
        }
        return false;
    }

    public static void insertUser(User user) {
        try {
            psInsertUser.setString(1, user.getUsername());
            psInsertUser.setString(2, user.getPassword());
            psInsertUser.setString(3, user.getEmail());
            psInsertUser.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error en insertUser: " + ex.getMessage());
        }
    }

    public static User getUserByUsername(String username) {
        try {
            psGetUserByUsername.setString(1, username);
            ResultSet rs = psGetUserByUsername.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setAvatar(rs.getString("avatar"));
                user.setBio(rs.getString("bio"));
                user.setDate(rs.getDate("date"));
                user.setEmail(rs.getString("email"));
                user.setId(rs.getInt("id"));
                user.setPassword(rs.getString("password"));
                user.setUsername(rs.getString("username"));
                return user;
            }
        } catch (SQLException ex) {
            System.err.println("Error en getUserByUsername: " + ex.getMessage());
        }
        return null;
    }
    
    public static User getUserById (Integer id) {
        try {
            psGetUserById.setInt(1, id);
            ResultSet rs = psGetUserById.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setAvatar(rs.getString("avatar"));
                user.setBio(rs.getString("bio"));
                user.setDate(rs.getDate("date"));
                user.setEmail(rs.getString("email"));
                user.setId(rs.getInt("id"));
                user.setPassword(rs.getString("password"));
                user.setUsername(rs.getString("username"));
                return user;
            }
        } catch (SQLException ex) {
            System.err.println("Error en getUserById: " + ex.getMessage());
        }
        return null;
    }
    
    public static Category getCategoryById (Integer id) {
        try {
            psGetCategoryById.setInt(1, id);
            ResultSet rs = psGetCategoryById.executeQuery();
           if (rs.next())
               return new Category(rs.getInt("id")
                       , rs.getString("name")
                       , rs.getString("description"));
        } catch (SQLException ex) {
            System.err.println("Error en getCategoryById: " + ex.getMessage());
        }
        return null;
    }
    
    public static ForumThread getThreadById (Integer id) {
        try {
            psGetThreadById.setInt(1, id);
            ResultSet rs = psGetThreadById.executeQuery();
            if (rs.next())
                return new ForumThread(rs.getInt("id")
                        , rs.getString("title")
                        , getUserById(rs.getInt("user"))
                        , getCategoryById(rs.getInt("category")));
        } catch (SQLException ex) {
            System.err.println("Error en getThreadById: " + ex.getMessage());
        }
        return null;
    }

    public static Collection<ForumThread> getThreads(Integer categoryId, Integer page) {
        ArrayList<ForumThread> list = new ArrayList<ForumThread>();
        try {
            Integer msgFrom = page * 50;
            psGetThreads.setInt(1, categoryId);
            psGetThreads.setInt(2, msgFrom);
            ResultSet rs = psGetThreads.executeQuery();
            while (rs.next()) {
                ForumThread thread = new ForumThread(rs.getInt("id")
                        ,rs.getString("title"),
                        getUserById(rs.getInt("user")),
                        getCategoryById(rs.getInt("category")));
                list.add(thread);
            }
            if (!list.isEmpty())
                return list;
        } catch (SQLException ex) {
            System.err.println("Error en getThreads: " + ex.getMessage());
        }
        return null;
    }
    
    public static Message getMessageById (Integer id) {
        try {
            psGetMessageById.setInt(1, id);
            ResultSet rs = psGetMessageById.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("id")
                        , rs.getString("content")
                        , getUserById(rs.getInt("user"))
                        , rs.getDate("date")
                        , getThreadById(rs.getInt("thread")));
            }
        } catch (SQLException ex) {
            System.err.println("Error en getMessageById: " + ex.getMessage());
        }
        return null;
    }
    
    public static Collection<Message> getMessages (Integer threadId, Integer page) {
        try {
            psGetMessages.setInt(1, threadId);
            psGetMessages.setInt(2, page*50);
            ResultSet rs = psGetMessages.executeQuery();
            ArrayList<Message> list = new ArrayList<Message>();
            while (rs.next()) {
                Message msg = new Message(rs.getInt("id")
                        , rs.getString("content")
                        , getUserById(rs.getInt("user"))
                        , rs.getDate("date")
                        , getThreadById(rs.getInt("thread")));
                if(rs.getInt("quote")!=0) {
                    Message quoted = getMessageById(rs.getInt("quote"));
                }
                list.add(msg);
            }
            if (!list.isEmpty())
                return list;
        } catch (SQLException ex) {
            System.err.println("Error en getMessages: " + ex.getMessage());
        }
        return null;
    }
}
