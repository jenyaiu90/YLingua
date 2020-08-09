package ru.jenyaiu90.ylingua.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.adapter.DictionaryAdapter;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Translation;

public class DictionaryFragment extends Fragment
{
	private Pair<String, String> lang;

	public ProgressBar loadingPB;

	private View view;

	private ListAdapter listAdapter;

	public DictionaryFragment(@NonNull String lang1, @NonNull String lang2)
	{
		lang = new Pair<>(lang1, lang2);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_list, container, false);
		this.view = view;

		loadingPB = view.findViewById(R.id.loadingPB);

		view.findViewById(R.id.addBT).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EditTranslationDialog dialog = new EditTranslationDialog(null, lang,
						DictionaryFragment.this);
				dialog.show(getFragmentManager(), null);
			}
		});
		((Button)view.findViewById(R.id.addBT)).setText(R.string.add_word);

		loadWords();

		return view;
	}

	public void loadWords()
	{
		loadingPB.setVisibility(View.VISIBLE);

		Thread thread = new Thread()
		{
			@Override
			public void run()
			{
				List<Translation> list = Database.get(getContext()).translations()
						.getForLang(lang.first, lang.second);
				Translation[] array = new Translation[list.size()];
				list.toArray(array);
				DictionaryAdapter adapter = new DictionaryAdapter(
						DictionaryFragment.this, array, lang);
				listAdapter = adapter;
				loadingPB.setVisibility(View.INVISIBLE);
			}
		};
		thread.start();
		try
		{
			thread.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		((ListView)view.findViewById(R.id.listLV)).setAdapter(listAdapter);
	}
}