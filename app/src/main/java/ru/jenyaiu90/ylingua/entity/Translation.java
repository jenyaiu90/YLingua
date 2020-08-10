package ru.jenyaiu90.ylingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Locale;

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
	private boolean learned1, learned2;

	public Translation(int id, int word1, int word2, boolean learned1, boolean learned2)
	{
		this.id = id;
		this.word1 = word1;
		this.word2 = word2;
		this.learned1 = learned1;
		this.learned2 = learned2;
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

	public boolean getLearned1()
	{
		return learned1;
	}

	public void setLearned1(boolean learned1)
	{
		this.learned1 = learned1;
	}

	public boolean getLearned2()
	{
		return learned2;
	}

	public void setLearned2(boolean learned2)
	{
		this.learned2 = learned2;
	}

	@Override
	@NonNull
	public String toString()
	{
		return String.format(Locale.getDefault(),
				"(%b) %d : %d (%b)", learned1, word1, word2, learned2);
	}
}
