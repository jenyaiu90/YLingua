package ru.jenyaiu90.ylingua.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.adapter.LanguagesAdapter;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Language;

public class EditLanguagesActivity extends Activity
{
	private Button addLangBT;
	private ProgressBar editLangsPB;
	private ListView langsListLV;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_languages);

		addLangBT = findViewById(R.id.addLangBT);
		editLangsPB = findViewById(R.id.editLangsPB);
		langsListLV = findViewById(R.id.langsListLV);

		addLangBT.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final View dialogView = LayoutInflater.from(EditLanguagesActivity.this).
						inflate(R.layout.dialog_add_lang, null);
				new AlertDialog.Builder(EditLanguagesActivity.this)
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
									addLanguage(langCodeET.getText().toString(),
											langNameET.getText().toString());
								}
								else
								{
									Toast.makeText(EditLanguagesActivity.this,
											R.string.add_lang_no_name_msg, Toast.LENGTH_LONG)
											.show();
								}
							}
						})
						.setNegativeButton(R.string.cancel, null)
						.create()
						.show();
			}
		});
	}

	private void addLanguage(@NonNull final String code, @NonNull final String name)
	{
		editLangsPB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					Database.get(EditLanguagesActivity.this).languages()
							.insert(new Language(code, name));
				}
				catch (SQLiteConstraintException e)
				{
					EditLanguagesActivity.this.runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							Toast.makeText(EditLanguagesActivity.this,
									R.string.land_not_added,
									Toast.LENGTH_LONG).show();
						}
					});
				}
				finally
				{
					EditLanguagesActivity.this.runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							editLangsPB.setVisibility(View.INVISIBLE);
						}
					});
				}
			}
		}.start();
		loadLangs();
	}

	private void loadLangs()
	{
		editLangsPB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				List<Language> list = Database.get(EditLanguagesActivity.this)
						.languages().getAll();
				Language[] array = new Language[list.size()];
				list.toArray(array);
				final LanguagesAdapter adapter = new LanguagesAdapter(array);
				EditLanguagesActivity.this.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						if (langsListLV.getChildCount() > 0)
						{
							final int firstVisiblePosition = langsListLV.getFirstVisiblePosition();
							View tmp = langsListLV.getChildAt(0);
							int topEdge = tmp.getTop();
							langsListLV.setAdapter(adapter);
							langsListLV.post(new Runnable()
							{
								@Override
								public void run()
								{
									langsListLV.setSelectionFromTop(firstVisiblePosition, 0);
								}
							});
							langsListLV.scrollBy(0, -topEdge);
						}
						else
						{
							langsListLV.setAdapter(adapter);
						}
						editLangsPB.setVisibility(View.INVISIBLE);
					}
				});
			}
		}.start();
	}
}
