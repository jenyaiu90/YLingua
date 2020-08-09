package ru.jenyaiu90.ylingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.jenyaiu90.ylingua.entity.Word;

@Dao
public interface Words
{
	@Query("SELECT * FROM words WHERE id = :id")
	Word getById(int id);

	@Query("SELECT * FROM words WHERE word = :word AND language = :language")
	List<Word> getWord(String word, String language);

	@Query("SELECT MAX(id) FROM words")
	int getLastId();

	@Query("SELECT COUNT(*) FROM words")
	int count();

	@Insert
	void insert(Word... words);

	@Delete
	void delete(Word... words);

	@Update
	void update(Word... words);
}
