package ru.bookstore.POJO;

import java.sql.Date;

/**
 * Created by Johnny D on 07.10.2014.
 */
public class History {
    private int id = 0;
    private int client_id = 0;
    private int book_id = 0;
    private Date date = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
