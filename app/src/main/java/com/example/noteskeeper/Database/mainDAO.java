package com.example.noteskeeper.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.noteskeeper.Models.Notes;

import java.util.List;

@Dao
public interface mainDAO
{
    @Insert(onConflict = REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAll();

    @Query("UPDATE notes SET title= :title,notes= :notes WHERE id= :id")
    void update(int id,String title,String notes);

    @Delete
    void delete(Notes notes);

    @Query("UPDATE notes Set pinned= :pin WHERE id= :id")
    void pin(int id,boolean pin);
}
