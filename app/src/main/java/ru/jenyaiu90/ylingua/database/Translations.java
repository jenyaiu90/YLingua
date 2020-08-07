package ru.jenyaiu90.ylingua.database;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.jenyaiu90.ylingua.entity.Language;
import ru.jenyaiu90.ylingua.entity.Translation;

@Dao
public interface Translations
{
	@Query("SELECT * FROM translations WHERE id = :id")
	Translation getById(int id);

	@Query("SELECT * FROM translations WHERE" +
			"(word1 IN (SELECT word FROM words WHERE language = :language1) AND " +
			"word2 IN (SELECT word FROM words WHERE language = :language2)) OR " +
			"(word1 IN (SELECT word FROM words WHERE language = :language2) AND " +
			"word2 IN (SELECT word FROM words WHERE language = :language1))")
	List<Translation> getForLang(@NonNull String language1, @NonNull String language2);

	@Insert
	void insert(Translation... translations);

	@Delete
	void delete(Translation... translations);

	@Update
	void update(Translation... translations);
}
