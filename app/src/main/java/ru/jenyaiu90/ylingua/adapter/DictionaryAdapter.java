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
	private CheckBox[] learnedCBs;
	private DictionaryFragment fragment;
	private Translation[] array;
	private Pair<String, String> lang;

	public DictionaryAdapter(@NonNull DictionaryFragment fragment, Translation[] array,
							 @NonNull Pair<String, String> lang)
	{
		super(fragment.getContext(), R.layout.adapter_dictionary, array);
		views = new View[array.length];
		learnedCBs = new CheckBox[array.length];
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
		learnedCBs[position] = convertView.findViewById(R.id.learnedCB);

		new Thread()
		{
			@Override
			public void run()
			{
				((TextView)views[position].findViewById(R.id.firstTV)).setText(
						Database.get(getContext()).words().getById(getItem(position).getWord1())
								.getWord());
				((TextView)views[position].findViewById(R.id.secondTV)).setText(
						Database.get(getContext()).words().getById(getItem(position).getWord2())
								.getWord());
			}
		}.start();

		learnedCBs[position].setChecked(getItem(position).getLearned());
		learnedCBs[position].setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switchLearned(v);
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

	private void switchLearned(final View v)
	{
		fragment.loadingPB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				int i;
				for (i = 0; i < learnedCBs.length; i++)
				{
					if (v == learnedCBs[i])
					{
						break;
					}
				}
				if (i < learnedCBs.length)
				{
					array[i].setLearned(!array[i].getLearned());
					Database.get(getContext()).translations().update(array[i]);
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
