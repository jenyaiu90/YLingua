package ru.jenyaiu90.ylingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ru.jenyaiu90.ylingua.entity.Language;

@Dao
public interface Languages
{
	@Query("SELECT * FROM languages WHERE code = :code")
	Language get(String code);

	@Insert
	void insert(Language... languages);

	@Delete
	void delete(Language... languages);

	@Update
	void update(Language... languages);
}
