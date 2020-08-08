package ru.jenyaiu90.ylingua.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Translation;

public class DictionaryAdapter extends ArrayAdapter<Translation>
{
	View[] views;

	public DictionaryAdapter(Context context, Translation[] array)
	{
		super(context, R.layout.adapter_dictionary, array);
		views = new View[array.length];
	}

	@Override
	@NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_dictionary,
											  null);
											  //parent, false);
		}
		views[position] = convertView;

		((TextView)convertView.findViewById(R.id.firstTV)).setText(
				Database.get(getContext()).words().getById(getItem(position).getWord1()).getWord());
		((TextView)convertView.findViewById(R.id.secondTV)).setText(
				Database.get(getContext()).words().getById(getItem(position).getWord2()).getWord());

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
					//TODO: Change word
				}
			}
		});

		return convertView;
	}
}
