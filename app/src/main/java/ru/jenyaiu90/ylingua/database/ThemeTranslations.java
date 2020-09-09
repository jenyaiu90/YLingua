package ru.jenyaiu90.ylingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import ru.jenyaiu90.ylingua.entity.ThemeTranslation;

@Dao
public interface ThemeTranslations
{
	@Insert
	void insert(ThemeTranslation... themeTranslations);

	@Delete
	void delete(ThemeTranslation... themeTranslations);

	@Update
	void update(ThemeTranslation... themeTranslations);
}
