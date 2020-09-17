package ru.jenyaiu90.ylingua.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import ru.jenyaiu90.ylingua.R;

public class StartFragment extends Fragment
{
	private MainFragment fragment;
	private FrameLayout startFL;

	public StartFragment(MainFragment fragment)
	{
		this.fragment = fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
							 Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.layout_start, container, false);
		startFL = view.findViewById(R.id.startFL);

		view.findViewById(R.id.startBT).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Pair<String, String> langs = fragment.getLangs();
				if (langs.first == null || langs.second == null || langs.first.equals(langs.second))
				{
					Toast.makeText(getContext(), R.string.chose_lang,
							Toast.LENGTH_LONG).show();
				}
				else
				{
					boolean withLearned =
							((CheckBox)view.findViewById(R.id.withLearnedCB)).isChecked();
					training(withLearned);
				}
			}
		});
		return view;
	}

	public void training(boolean withLearned)
	{
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.replace(R.id.startFL, new TrainingFragment(
				StartFragment.this, fragment.getLangs(), withLearned));
		transaction.commit();
	}
}