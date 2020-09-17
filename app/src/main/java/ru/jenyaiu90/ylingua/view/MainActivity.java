package ru.jenyaiu90.ylingua.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.adapter.MainFragmentPagerAdapter;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Language;

public class MainActivity extends AppCompatActivity
{
	private Button editLangBT;
	private FrameLayout mainFL;

	private boolean editingLang;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editLangBT = findViewById(R.id.editLangBT);
		mainFL = findViewById(R.id.mainFL);

		editingLang = false;

		editLangBT.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (editingLang)
				{
					editLangBT.setText(R.string.edit_lang);
					loadFragment(new MainFragment(), false);
				}
				else
				{
					editLangBT.setText(R.string.back);
					loadFragment(new EditLangFragment(), false);
				}
				editingLang = !editingLang;
			}
		});

		loadFragment(new MainFragment(), false);
	}

	private void loadFragment(Fragment fragment, boolean addToStack)
	{
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.mainFL, fragment);
		if (addToStack)
		{
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	/*public void start(boolean withLearned)
	{
		if (lang1 == null || lang2 == null || lang1.equals(lang2))
		{
			Toast.makeText(MainActivity.this, R.string.chose_lang,
					Toast.LENGTH_LONG).show();
		}
		else
		{
			training(withLearned);
		}
	}*/

	/*public void training(boolean withLearned)
	{
		loadFragment(new TrainingFragment(
				MainActivity.this, new Pair<>(lang1, lang2), withLearned),
				false);
	}*/

	/*private void openDictionary()
	{
		if (lang1 == null || lang2 == null || lang1.equals(lang2))
		{
			Toast.makeText(MainActivity.this, R.string.chose_lang, Toast.LENGTH_LONG).show();
			loadFragment(new StartFragment(MainActivity.this), false);
		}
		else
		{
			loadFragment(new DictionaryFragment(lang1, lang2), false);
		}
	}*/

	@Override
	public void onBackPressed()
	{
		if (editingLang)
		{
			editLangBT.callOnClick();
		}
		else
		{
			super.onBackPressed();
		}
	}
}