package ru.jenyaiu90.ylingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import ru.jenyaiu90.ylingua.entity.Theme;

@Dao
public interface Themes
{
	@Insert
	void insert(Theme... themes);

	@Delete
	void delete(Theme... themes);

	@Update
	void update(Theme... themes);
}
