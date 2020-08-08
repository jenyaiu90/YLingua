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

	@Query("SELECT * FROM translations " +
			"LEFT JOIN words AS words1 ON words1.id = word1 " +
			"LEFT JOIN words AS words2 ON words2.id = word2 " +
			"WHERE words1.language = :language1 AND words2.language = :language2 " +
			"OR words1.language = :language2 AND words2.language = :language1 " +
			"ORDER BY words1.word, words2.word")
	List<Translation> getForLang(@NonNull String language1, @NonNull String language2);

	@Insert
	void insert(Translation... translations);

	@Delete
	void delete(Translation... translations);

	@Update
	void update(Translation... translations);
}
