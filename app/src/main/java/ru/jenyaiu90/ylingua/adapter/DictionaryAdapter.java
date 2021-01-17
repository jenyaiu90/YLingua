package ru.jenyaiu90.ylingua.adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.view.DictionaryActivity;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Translation;
import ru.jenyaiu90.ylingua.view.EditTranslationDialog;

public class DictionaryAdapter extends ArrayAdapter<Translation>
{
	private View[] views;
	private CheckBox[] learned1CBs, learned2CBs;
	@NonNull
	private DictionaryActivity activity;
	private Pair<String, String> lang;

	public DictionaryAdapter(@NonNull DictionaryActivity activity, Translation[] array,
							 @NonNull Pair<String, String> lang)
	{
		super(activity, R.layout.adapter_dictionary, array);
		views = new View[array.length];
		learned1CBs = new CheckBox[array.length];
		learned2CBs = new CheckBox[array.length];
		this.activity = activity;
		this.lang = lang;
	}

	@Override
	@NonNull
	public View getView(final int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_dictionary,
											  null);
											  //parent, false);
		}
		views[position] = convertView;
		learned1CBs[position] = convertView.findViewById(R.id.learned1CB);
		learned2CBs[position] = convertView.findViewById(R.id.learned2CB);

		new Thread()
		{
			@Override
			public void run()
			{
				final String first = Database.get(getContext()).words().getById(getItem(position)
						.getWord1()).getWord();
				final String second = Database.get(getContext()).words().getById(getItem(position)
						.getWord2()).getWord();
				activity.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						((TextView)views[position].findViewById(R.id.word1TV)).setText(first);
						((TextView)views[position].findViewById(R.id.word2TV)).setText(second);
					}
				});
			}
		}.start();

		learned1CBs[position].setChecked(getItem(position).getLearned1());
		learned1CBs[position].setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switchLearned(position, 1);
			}
		});
		learned2CBs[position].setChecked(getItem(position).getLearned2());
		learned2CBs[position].setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switchLearned(position, 2);
			}
		});

		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (position < views.length)
				{
					EditTranslationDialog dialog =
							new EditTranslationDialog(getItem(position), lang, activity);
					dialog.show(activity.getSupportFragmentManager(), null);
				}
			}
		});

		return convertView;
	}

	private void switchLearned(final int position, final int n)
	{
		final CheckBox[] arr = n == 1 ? learned1CBs : learned2CBs;
		activity.dictionaryPB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				if (position < arr.length && position >= 0)
				{
					if (n == 1)
					{
						Database.setTranslationLearned(getContext(), n, getItem(position).getWord1(),
													   lang, !getItem(position).getLearned1());
					}
					else
					{
						Database.setTranslationLearned(getContext(), n, getItem(position).getWord2(),
													   lang, !getItem(position).getLearned2());
					}

					activity.runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							activity.loadWords();
						}
					});
				}
			}
		}.start();
	}
}
