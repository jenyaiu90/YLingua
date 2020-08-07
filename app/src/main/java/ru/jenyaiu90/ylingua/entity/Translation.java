package ru.jenyaiu90.ylingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "translations",
		foreignKeys = {
			@ForeignKey(entity = Word.class, parentColumns = "id", childColumns = "word1",
						onDelete = CASCADE, onUpdate = CASCADE),
			@ForeignKey(entity = Word.class, parentColumns = "id", childColumns = "word2",
						onDelete = CASCADE, onUpdate = CASCADE)
		})
public class Translation
{
	@PrimaryKey
	private int id;
	private int word1, word2;

	public Translation(int id, int word1, int word2)
	{
		this.id = id;
		this.word1 = word1;
		this.word2 = word2;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getWord1()
	{
		return word1;
	}

	public void setWord1(int word1)
	{
		this.word1 = word1;
	}

	public int getWord2()
	{
		return word2;
	}

	public void setWord2(int word2)
	{
		this.word2 = word2;
	}

	@Override
	@NonNull
	public String toString()
	{
		//Todo: Return words, not their id`s
		return String.format("[%s] %s : %s [%s]", word1, word1, word2, word2);
	}
}
