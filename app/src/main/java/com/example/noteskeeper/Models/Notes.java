package com.example.noteskeeper.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")

public class Notes implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    int id=0;

    @ColumnInfo(name="title")
    String title="";

    @ColumnInfo(name="notes")
    String notes="";

    @ColumnInfo(name="date")
    String date="";

    @ColumnInfo(name="pinned")
    boolean pinned=false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

}
