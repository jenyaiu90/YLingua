package ru.jenyaiu90.ylingua.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Language;

public class MainActivity extends AppCompatActivity
{
	private ProgressBar mainPB;
	private Spinner lang1SP, lang2SP;
	private ImageButton optionsIB, langsIB, trainingIB, dictionaryIB, themesIB;
	private CheckBox learnedCB;

	private int easterCounter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mainPB = findViewById(R.id.mainPB);
		lang1SP = findViewById(R.id.lang1SP);
		lang2SP = findViewById(R.id.lang2SP);
		optionsIB = findViewById(R.id.optionsIB);
		langsIB = findViewById(R.id.langsIB);
		trainingIB = findViewById(R.id.trainingIB);
		dictionaryIB = findViewById(R.id.dictionaryIB);
		themesIB = findViewById(R.id.themesIB);
		learnedCB = findViewById(R.id.learnedCB);

		setLangs();

		mainPB.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (easterCounter >= 16)
				{
					Toast.makeText(MainActivity.this, "Мама, я тебя люблю!",
							Toast.LENGTH_LONG).show();
				}
				else
				{
					easterCounter++;
				}
			}
		});

		optionsIB.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent optionsI = new Intent(MainActivity.this, OptionsActivity.class);
				startActivity(optionsI);
			}
		});

		langsIB.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent editLanguagesI = new Intent(MainActivity.this,
						EditLanguagesActivity.class);
				startActivity(editLanguagesI);
			}
		});

		trainingIB.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String  lang1 = lang1SP.getSelectedItem() == null ? "" :
								lang1SP.getSelectedItem().toString(),
						lang2 = lang2SP.getSelectedItem() == null ? "" :
								lang2SP.getSelectedItem().toString();
				if (lang1.isEmpty() || lang2.isEmpty() || lang1.equals(lang2))
				{
					Toast.makeText(MainActivity.this, R.string.select_lang_wrong_msg,
							Toast.LENGTH_LONG).show();
				}
				else
				{
					Intent trainingI = new Intent(MainActivity.this,
							TrainingActivity.class);
					trainingI.putExtra(DictionaryActivity.LANG1,
							lang1SP.getSelectedItem().toString());
					trainingI.putExtra(DictionaryActivity.LANG2,
							lang2SP.getSelectedItem().toString());
					trainingI.putExtra(TrainingActivity.LEARNED,
							learnedCB.isChecked());
					startActivity(trainingI);
				}
			}
		});

		dictionaryIB.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String  lang1 = lang1SP.getSelectedItem() == null ? "" :
								lang1SP.getSelectedItem().toString(),
						lang2 = lang2SP.getSelectedItem() == null ? "" :
								lang2SP.getSelectedItem().toString();
				if (lang1.isEmpty() || lang2.isEmpty() || lang1.equals(lang2))
				{
					Toast.makeText(MainActivity.this, R.string.select_lang_wrong_msg,
							Toast.LENGTH_LONG).show();
				}
				else
				{
					Intent dictionaryI = new Intent(MainActivity.this,
							DictionaryActivity.class);
					dictionaryI.putExtra(DictionaryActivity.LANG1,
							lang1SP.getSelectedItem().toString());
					dictionaryI.putExtra(DictionaryActivity.LANG2,
							lang2SP.getSelectedItem().toString());
					startActivity(dictionaryI);
				}
			}
		});

		themesIB.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent themesI = new Intent(MainActivity.this, ThemesActivity.class);
				startActivity(themesI);
			}
		});
	}

	private void setLangs()
	{
		mainPB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				List<Language> languages = Database.get(MainActivity.this).languages().getAll();
				String[] langs = new String[languages.size()];
				for (int i = 0; i < languages.size(); i++)
				{
					langs[i] = languages.get(i).getCode();
				}
				final ArrayAdapter<String> adapter =
						new ArrayAdapter<>(MainActivity.this,
								android.R.layout.simple_spinner_item, langs);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						lang1SP.setAdapter(adapter);
						lang2SP.setAdapter(adapter);
						mainPB.setVisibility(View.INVISIBLE);
					}
				});
			}
		}.start();

		/* DELETE if lang1SP.getSelectedItem().toString() will word

		AdapterView.OnItemSelectedListener spinnerLstn = new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (parent == lang1SP)
				{
					lang1 = (String)parent.getItemAtPosition(position);
				}
				else
				{
					lang2 = (String)parent.getItemAtPosition(position);
				}

				MainFragmentPagerAdapter.getStartFragment().load();
				MainFragmentPagerAdapter.getDictionaryFragment().setLangs(new Pair<>(lang1, lang2));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		};
		lang1SP.setOnItemSelectedListener(spinnerLstn);
		lang2SP.setOnItemSelectedListener(spinnerLstn);*/
	}
}