package ru.jenyaiu90.ylingua.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.view.EditLangFragment;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Language;

public class LanguagesAdapter extends ArrayAdapter<Language>
{
	private ImageButton[] deleteViews;
	private EditLangFragment fragment;

	public LanguagesAdapter(@NonNull EditLangFragment fragment, Language[] array)
	{
		super(fragment.getContext(), R.layout.adapter_languages, array);
		deleteViews = new ImageButton[array.length];
		this.fragment = fragment;
	}

	@Override
	@NonNull
	public View getView(final int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_languages,
											  null);
		}
		deleteViews[position] = convertView.findViewById(R.id.deleteIB);

		((TextView)convertView.findViewById(R.id.langCodeTV)).setText(getItem(position).getCode());
		((TextView)convertView.findViewById(R.id.langTV)).setText(getItem(position).getName());
		deleteViews[position].setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				new AlertDialog.Builder(getContext())
						.setMessage(R.string.lang_delete_sure)
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								if (position < deleteViews.length)
								{
									deleteLanguage(getItem(position));
								}
							}
						})
						.setNegativeButton(R.string.no, null)
						.create()
						.show();
			}
		});

		return convertView;
	}

	private void deleteLanguage(final Language language)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				Database.get(getContext()).languages().delete(language);
				fragment.getActivity().runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						fragment.loadLanguages();
					}
				});
			}
		}.start();
	}
}
