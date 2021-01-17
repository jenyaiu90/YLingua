package ru.jenyaiu90.ylingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "themes")
public class Theme
{
	@PrimaryKey
	private int id;

	@NonNull
	private String name;

	private String description;

	public Theme(int id, @NonNull String name, String description)
	{
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@NonNull
	public String getName()
	{
		return name;
	}

	public void setName(@NonNull String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@Override
	@NonNull
	public String toString()
	{
		return String.format("%d: %n", id, name);
	}
}
