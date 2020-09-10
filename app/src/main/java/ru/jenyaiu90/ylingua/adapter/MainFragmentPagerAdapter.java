package ru.jenyaiu90.ylingua.adapter;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.view.DictionaryFragment;
import ru.jenyaiu90.ylingua.view.MainActivity;
import ru.jenyaiu90.ylingua.view.StartFragment;
import ru.jenyaiu90.ylingua.view.TrainingFragment;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter
{
	public static final int PAGES_COUNT = 2;
	private static final int[] TITLES = { R.string.training, R.string.dictionary };
	private final MainActivity activity;

	public MainFragmentPagerAdapter(FragmentManager manager, MainActivity activity)
	{
		super(manager);
		this.activity = activity;
	}

	@Override
	public int getCount()
	{
		return PAGES_COUNT;
	}

	@Override
	@NonNull
	public Fragment getItem(int position)
	{
		switch (position)
		{
			case 0:
				return new StartFragment(activity);
			case 1:
				Pair<String, String> langs = activity.getLangs();
				return new DictionaryFragment(langs.first, langs.second);
			default:
				throw new IndexOutOfBoundsException("There are only " + PAGES_COUNT + " pages.");
		}
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return activity.getResources().getString(TITLES[position]);
	}
}
