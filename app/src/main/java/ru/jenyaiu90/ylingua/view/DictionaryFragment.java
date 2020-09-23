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
import android.widget.Toast;

import java.util.List;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.adapter.DictionaryAdapter;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Translation;

public class DictionaryFragment extends Fragment
{
	private Pair<String, String> langs;

	public ProgressBar loadingPB;
	private ListView listLV;

	private View view;

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
		listLV = view.findViewById(R.id.listLV);

		view.findViewById(R.id.addBT).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (langs == null || langs.first == null || langs.second == null || langs.first.equals(langs.second))
				{
					Toast.makeText(getContext(), R.string.chose_lang, Toast.LENGTH_LONG).show();
				}
				else
				{
					EditTranslationDialog dialog = new EditTranslationDialog(
							null, langs, DictionaryFragment.this);
					dialog.show(getFragmentManager(), null);
				}
			}
		});
		((Button)view.findViewById(R.id.addBT)).setText(R.string.add_word);

		return view;
	}

	public void loadWords()
	{
		loadingPB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				List<Translation> list = Database.get(getContext()).translations()
						.getForLang(langs.first, langs.second);
				Translation[] array = new Translation[list.size()];
				list.toArray(array);
				final DictionaryAdapter adapter = new DictionaryAdapter(
						DictionaryFragment.this, array, langs);
				getActivity().runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						if (listLV.getChildCount() > 0)
						{
							final int firstVisiblePosition = listLV.getFirstVisiblePosition();
							View tmp = listLV.getChildAt(0);
							int topEdge = tmp.getTop();
							listLV.setAdapter(adapter);
							listLV.post(new Runnable()
							{
								@Override
								public void run()
								{
									listLV.setSelectionFromTop(firstVisiblePosition, 0);
								}
							});
							listLV.scrollBy(0, -topEdge);
						}
						else
						{
							listLV.setAdapter(adapter);
						}
						loadingPB.setVisibility(View.INVISIBLE);
					}
				});
			}
		}.start();
	}

	public void setLangs(Pair<String, String> langs)
	{
		this.langs = langs;
		if (langs.first != null && langs.second != null && !langs.first.equals(langs.second))
		{
			loadWords();
		}
		else
		{
			listLV.setAdapter(null);
		}
	}
}