/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.Date;

/**
 *
 * @author ACER
 */
public class Message {
    private int id;
    private String content;
    private User user;
    private Date date;
    private ForumThread thread;
    private Message quote;

    public Message() {
    }

    public Message(int id, String content, User user, Date date, ForumThread thread) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.date = date;
        this.thread = thread;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ForumThread getThread() {
        return thread;
    }

    public void setThread(ForumThread thread) {
        this.thread = thread;
    }

    public Message getQuote() {
        return quote;
    }

    public void setQuote(Message quote) {
        this.quote = quote;
    }
    
    
}
