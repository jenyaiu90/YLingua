package ru.jenyaiu90.ylingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "themes_translations",
		foreignKeys =
		{
				@ForeignKey(entity = Theme.class,
						parentColumns = "id", childColumns = "theme",
						onDelete = CASCADE, onUpdate = CASCADE),
				@ForeignKey(entity = Translation.class,
						parentColumns = "id", childColumns = "translation",
						onDelete = CASCADE, onUpdate = CASCADE)
		})
public class ThemeTranslation
{
	@PrimaryKey
	private int id;
	private int theme;
	private int translation;

	public ThemeTranslation(int id, int theme, int translation)
	{
		this.id = id;
		this.theme = theme;
		this.translation = translation;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getTheme()
	{
		return theme;
	}

	public void setTheme(int theme)
	{
		this.theme = theme;
	}

	public int getTranslation()
	{
		return translation;
	}

	public void setTranslation(int translation)
	{
		this.translation = translation;
	}

	@Override
	@NonNull
	public String toString()
	{
		return String.format("%d : %d", theme, translation);
	}
}
