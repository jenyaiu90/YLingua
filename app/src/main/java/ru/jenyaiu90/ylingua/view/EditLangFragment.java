package ru.jenyaiu90.ylingua.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.adapter.LanguagesAdapter;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Language;

public class EditLangFragment extends Fragment
{
	private ProgressBar loadingBP;
	private View view;

	private MainActivity activity;
	private boolean isLangLoaded;

	public EditLangFragment(MainActivity activity)
	{
		this.activity = activity;
		isLangLoaded = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_list, container, false);
		this.view = view;

		loadingBP = view.findViewById(R.id.loadingPB);

		view.findViewById(R.id.addBT).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_lang,
						null);
				new AlertDialog.Builder(getContext())
						.setTitle(R.string.add_lang)
						.setView(dialogView)
						.setPositiveButton(R.string.add, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								EditText langCodeET = dialogView.findViewById(R.id.langCodeET);
								EditText langNameET = dialogView.findViewById(R.id.langNameET);

								if (!langCodeET.getText().toString().isEmpty() &&
									!langNameET.getText().toString().isEmpty())
								{
									loadingBP.setVisibility(View.VISIBLE);
									addLanguage(langCodeET.getText().toString(),
												langNameET.getText().toString());
								}
							}
						})
						.setNegativeButton(R.string.cancel, null)
						.create()
						.show();
			}
		});
		((Button)view.findViewById(R.id.addBT)).setText(R.string.add_lang);

		loadLanguages();

		return view;
	}

	private void addLanguage(@NonNull final String code, @NonNull final String name)
	{
		loadingBP.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					Database.get(getContext()).languages().insert(new Language(code, name));
					loadLanguages();
				}
				catch (SQLiteConstraintException e)
				{
					getActivity().runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							Toast.makeText(getContext(), R.string.land_not_added,
									Toast.LENGTH_LONG).show();
						}
					});
				}
				finally
				{
					loadingBP.setVisibility(View.INVISIBLE);
				}
			}
		}.start();
	}

	public void loadLanguages()
	{
		loadingBP.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				List<Language> list = Database.get(getContext()).languages().getAll();
				Language[] array = new Language[list.size()];
				list.toArray(array);
				final LanguagesAdapter adapter =
						new LanguagesAdapter(EditLangFragment.this, array);
				getActivity().runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						((ListView)view.findViewById(R.id.listLV)).setAdapter(adapter);
						loadingBP.setVisibility(View.INVISIBLE);
						if (isLangLoaded)
						{
							activity.loadLang();
						}
						else
						{
							isLangLoaded = true;
						}
					}
				});
			}
		}.start();
	}
}