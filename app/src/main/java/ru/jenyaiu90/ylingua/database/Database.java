package ru.jenyaiu90.ylingua.database;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

import ru.jenyaiu90.ylingua.entity.Language;
import ru.jenyaiu90.ylingua.entity.Translation;
import ru.jenyaiu90.ylingua.entity.Word;

@androidx.room.Database(entities = {Language.class, Word.class, Translation.class}, version = 2)
public abstract class Database extends RoomDatabase
{
	// Migrations
	private static final Migration MIGRATION_1_2 = new Migration(1, 2)
	{
		@Override
		public void migrate(@NonNull SupportSQLiteDatabase database)
		{
			database.execSQL("ALTER TABLE translations ADD COLUMN learned2 INTEGER NOT NULL DEFAULT 0");
			database.execSQL("UPDATE translations SET learned2 = 0");
		}
	};

	public abstract Languages languages();
	public abstract Translations translations();
	public abstract Words words();

	private static final String DB_NAME = "ylingua.db";
	private static volatile Database INSTANCE = null;

	public synchronized static Database get(Context context)
	{
		if (INSTANCE == null)
		{
			INSTANCE = create(context, false);
		}
		return INSTANCE;
	}

	private static Database create(Context context, boolean memoryOnly)
	{
		RoomDatabase.Builder<Database> builder;
		if (memoryOnly)
		{
			builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), Database.class);
		}
		else
		{
			builder = Room.databaseBuilder(context.getApplicationContext(),
										   Database.class, DB_NAME);
		}
		return builder
				.addMigrations(MIGRATION_1_2)
				.build();
	}

	public static void setTranslationLearned(Context context, int n, int word,
											 Pair<String, String> lang, boolean isLearned)
	{
		List<Translation> translations =
				get(context).translations().getForLang(lang.first, lang.second);
		if (n == 1)
		{
			for (Translation i : translations)
			{
				if (i.getWord1() == word)
				{
					i.setLearned1(isLearned);
					get(context).translations().update(i);
				}
			}
		}
		else
		{
			for (Translation i : translations)
			{
				if (i.getWord2() == word)
				{
					i.setLearned2(isLearned);
					get(context).translations().update(i);
				}
			}
		}
	}
}
