package ru.jenyaiu90.ylingua.view;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.adapter.MainFragmentPagerAdapter;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Language;

public class MainFragment extends Fragment
{
	private ProgressBar langPB;
	private Spinner lang1SP, lang2SP;
	private TabLayout modeTL;
	private ViewPager fragmentVP;

	private String lang1, lang2;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_main, container, false);

		langPB = view.findViewById(R.id.langPB);
		lang1SP = view.findViewById(R.id.lang1SP);
		lang2SP = view.findViewById(R.id.lang2SP);
		modeTL = view.findViewById(R.id.modeTL);
		fragmentVP = view.findViewById(R.id.fragmentVP);

		langPB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				List<Language> languages = Database.get(getContext()).languages().getAll();
				String[] langs = new String[languages.size()];
				for (int i = 0; i < languages.size(); i++)
				{
					langs[i] = languages.get(i).getCode();
				}
				final ArrayAdapter<String> adapter =
						new ArrayAdapter<>(getContext(),
								android.R.layout.simple_spinner_item, langs);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				lang1 = null;
				lang2 = null;
				getActivity().runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						lang1SP.setAdapter(adapter);
						lang2SP.setAdapter(adapter);
						langPB.setVisibility(View.INVISIBLE);
					}
				});
			}
		}.start();

		fragmentVP.setAdapter(new MainFragmentPagerAdapter(
				getFragmentManager(), MainFragment.this));
		modeTL.setupWithViewPager(fragmentVP);

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
		lang2SP.setOnItemSelectedListener(spinnerLstn);

		return view;
	}

	public Pair<String, String> getLangs()
	{
		return new Pair<>(lang1, lang2);
	}
}
