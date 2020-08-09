package ru.jenyaiu90.ylingua.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.entity.Translation;

public class TrainingFragment extends Fragment
{
	private boolean withLearned;
	private Pair<String, String> lang;
	private View view;

	private MainActivity activity;

	private ConstraintLayout trainingCL;
	private TextView wordTV;
	private ProgressBar wordPB;
	private EditText translationET;
	private Button okBT;
	private CheckBox learnedCB;

	private String word;
	private LinkedList<String> translations;

	public TrainingFragment(@NonNull MainActivity activity,
							@NonNull Pair<String, String> lang, boolean withLearned)
	{
		this.activity = activity;
		this.withLearned = withLearned;
		this.lang = lang;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_training, container, false);

		trainingCL = view.findViewById(R.id.trainingCL);
		wordTV = view.findViewById(R.id.wordTV);
		wordPB = view.findViewById(R.id.wordPB);
		translationET = view.findViewById(R.id.translationET);
		okBT = view.findViewById(R.id.okBT);
		learnedCB = view.findViewById(R.id.learnedCB);

		loadQuestion();

		return view;
	}

	private void loadQuestion()
	{
		learnedCB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				Database db = Database.get(getContext());
				List<Translation> translationList;
				if (withLearned)
				{
					translationList = db.translations().getForLang(lang.first, lang.second);
				}
				else
				{
					translationList = db.translations().getForLangNotLearned(lang.first, lang.second);
				}
				if (translationList.isEmpty())
				{
					getActivity().runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							trainingCL.setBackground(
									getResources().getDrawable(R.drawable.bad_card));
							wordTV.setText(R.string.error);
							wordTV.setTextColor(Color.GRAY);
							translationET.setEnabled(false);
							okBT.setEnabled(false);
							wordPB.setVisibility(View.INVISIBLE);
							Toast.makeText(getContext(), R.string.no_words, Toast.LENGTH_LONG)
									.show();
						}
					});
					return;
				}
				int i = (int)(Math.random() * translationList.size());
				boolean isFirst = Math.random() >= 0.5;

				translations = new LinkedList<>();
				if (isFirst)
				{
					word = db.words().getById(translationList.get(i).getWord1()).getWord();
					for (Translation j : translationList)
					{
						if (j.getWord1() == translationList.get(i).getWord1())
						{
							translations.add(db.words().getById(j.getWord2()).getWord());
						}
					}
				}
				else
				{
					word = db.words().getById(translationList.get(i).getWord2()).getWord();
					for (Translation j : translationList)
					{
						if (j.getWord2() == translationList.get(i).getWord2())
						{
							translations.add(db.words().getById(j.getWord1()).getWord());
						}
					}
				}

				getActivity().runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						wordTV.setText(word);
						okBT.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								if (translations.contains(translationET.getText().toString()))
								{
									trainingCL.setBackground(getResources()
											.getDrawable(R.drawable.true_card));
									learnedCB.setVisibility(View.VISIBLE);
									learnedCB.setEnabled(true);
								}
								else
								{
									trainingCL.setBackground(getResources()
											.getDrawable(R.drawable.false_card));
								}
								okBT.setText(R.string.next);
								okBT.setOnClickListener(new View.OnClickListener()
								{
									@Override
									public void onClick(View v)
									{
										activity.training();
									}
								});
							}
						});
						wordPB.setVisibility(View.INVISIBLE);
					}
				});
			}
		}.start();
	}
}