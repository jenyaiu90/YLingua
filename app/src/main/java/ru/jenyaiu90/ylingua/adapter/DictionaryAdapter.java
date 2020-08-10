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
import ru.jenyaiu90.ylingua.view.DictionaryFragment;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Translation;
import ru.jenyaiu90.ylingua.view.EditTranslationDialog;

public class DictionaryAdapter extends ArrayAdapter<Translation>
{
	private View[] views;
	private CheckBox[] learned1CBs, learned2CBs;
	@NonNull
	private DictionaryFragment fragment;
	private Translation[] array;
	private Pair<String, String> lang;

	public DictionaryAdapter(@NonNull DictionaryFragment fragment, Translation[] array,
							 @NonNull Pair<String, String> lang)
	{
		super(fragment.getContext(), R.layout.adapter_dictionary, array);
		views = new View[array.length];
		learned1CBs = new CheckBox[array.length];
		learned2CBs = new CheckBox[array.length];
		this.fragment = fragment;
		this.array = array;
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
				fragment.getActivity().runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						((TextView)views[position].findViewById(R.id.firstTV)).setText(first);
						((TextView)views[position].findViewById(R.id.secondTV)).setText(second);
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
				switchLearned(v, 1);
			}
		});
		learned2CBs[position].setChecked(getItem(position).getLearned2());
		learned2CBs[position].setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switchLearned(v, 2);
			}
		});

		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int i;
				for (i = 0; i < views.length; i++)
				{
					if (views[i] == v)
					{
						break;
					}
				}

				if (i < views.length)
				{
					EditTranslationDialog dialog =
							new EditTranslationDialog(array[i], lang, fragment);
					dialog.show(fragment.getFragmentManager(), null);
				}
			}
		});

		return convertView;
	}

	private void switchLearned(final View v, final int n)
	{
		final CheckBox[] arr = n == 1 ? learned1CBs : learned2CBs;
		fragment.loadingPB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				int i;
				for (i = 0; i < arr.length; i++)
				{
					if (v == arr[i])
					{
						break;
					}
				}
				if (i < arr.length)
				{
					if (n == 1)
					{
						Database.setTranslationLearned(getContext(), n, array[i].getWord1(),
													   lang, !array[i].getLearned1());
					}
					else
					{
						Database.setTranslationLearned(getContext(), n, array[i].getWord2(),
													   lang, !array[i].getLearned2());
					}

					fragment.getActivity().runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							fragment.loadWords();
						}
					});
				}
			}
		}.start();
	}
}
