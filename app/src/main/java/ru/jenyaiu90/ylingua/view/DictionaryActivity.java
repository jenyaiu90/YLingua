package ru.jenyaiu90.ylingua.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.adapter.DictionaryAdapter;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Translation;

public class DictionaryActivity extends AppCompatActivity
{
	public static final String LANG1 = "lang_1";
	public static final String LANG2 = "lang_2";

	private Button addWordBT;
	private ProgressBar dictionaryPB;
	private ListView wordsListLV;

	private Pair<String, String> langs;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionary);

		addWordBT = findViewById(R.id.addWordBT);
		dictionaryPB = findViewById(R.id.dictionaryPB);
		wordsListLV = findViewById(R.id.wordsListLV);

		langs = new Pair<>(getIntent().getStringExtra(LANG1), getIntent().getStringExtra(LANG2));

		addWordBT.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EditTranslationDialog dialog = new EditTranslationDialog(
						null, langs, new Runnable()
				{
					@Override
					public void run()
					{
						loadWords();
					}
				});
				dialog.show(getSupportFragmentManager(), null);
			}
		});
		loadWords();
	}

	private void loadWords()
	{
		dictionaryPB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				List<Translation> list = Database.get(DictionaryActivity.this).translations()
						.getForLang(langs.first, langs.second);
				Translation[] array = new Translation[list.size()];
				list.toArray(array);
				final DictionaryAdapter adapter = new DictionaryAdapter(array, langs);
				DictionaryActivity.this.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						if (wordsListLV.getChildCount() > 0)
						{
							final int firstVisiblePosition = wordsListLV.getFirstVisiblePosition();
							View tmp = wordsListLV.getChildAt(0);
							int topEdge = tmp.getTop();
							wordsListLV.setAdapter(adapter);
							wordsListLV.post(new Runnable()
							{
								@Override
								public void run()
								{
									wordsListLV.setSelectionFromTop(firstVisiblePosition, 0);
								}
							});
							wordsListLV.scrollBy(0, -topEdge);
						}
						else
						{
							wordsListLV.setAdapter(adapter);
						}
						dictionaryPB.setVisibility(View.INVISIBLE);
					}
				});
			}
		}.start();
	}
}
