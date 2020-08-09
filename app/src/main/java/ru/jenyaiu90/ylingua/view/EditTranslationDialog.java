package ru.jenyaiu90.ylingua.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import ru.jenyaiu90.ylingua.R;
import ru.jenyaiu90.ylingua.database.Database;
import ru.jenyaiu90.ylingua.database.Words;
import ru.jenyaiu90.ylingua.entity.Translation;
import ru.jenyaiu90.ylingua.entity.Word;

public class EditTranslationDialog extends DialogFragment
{
	private Translation translation;
	private Pair<String, String> lang;
	private View view;

	private DictionaryFragment fragment;

	private EditText word1ET, word2ET;
	private ProgressBar translationPB;

	public EditTranslationDialog(@Nullable Translation translation,
								 @NonNull Pair<String, String> lang,
								 @NonNull DictionaryFragment fragment)
	{
		this.translation = translation;
		this.lang = lang;
		this.fragment = fragment;
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_translation,
				null);
		((TextView)view.findViewById(R.id.lang1TV)).setText(lang.first);
		((TextView)view.findViewById(R.id.lang2TV)).setText(lang.second);
		word1ET = view.findViewById(R.id.word1ET);
		word2ET = view.findViewById(R.id.word2ET);
		translationPB = view.findViewById(R.id.translationPB);
		if (translation != null)
		{
			translationPB.setVisibility(View.VISIBLE);
			new Thread()
			{
				@Override
				public void run()
				{
					final String word1, word2;
					Words words = Database.get(getContext()).words();
					word1 = words.getById(translation.getWord1()).getWord();
					word2 = words.getById(translation.getWord2()).getWord();
					getActivity().runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							word1ET.setText(word1);
							word2ET.setText(word2);
							translationPB.setVisibility(View.INVISIBLE);
						}
					});
				}
			}.start();
		}
		return new AlertDialog.Builder(getContext())
				.setTitle(R.string.edit_translation)
				.setView(view)
				.setPositiveButton(R.string.save, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						if (!word1ET.getText().toString().isEmpty() &&
							!word2ET.getText().toString().isEmpty())
						{
							saveTranslation(new Pair<>(word1ET.getText().toString(),
													   word2ET.getText().toString()));
						}
						else
						{
							Toast.makeText(getContext(), R.string.enter_words, Toast.LENGTH_LONG)
									.show();
						}
					}
				})
				.setNeutralButton(R.string.delete, translation != null ?
						new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						new AlertDialog.Builder(getContext())
								.setMessage(R.string.translation_delete_sure)
								.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										deleteTranslation();
									}
								})
								.setNegativeButton(R.string.no, null)
								.create()
								.show();
					}
				} : null)
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	public void saveTranslation(@NonNull final Pair<String, String> words)
	{
		translationPB.setVisibility(View.VISIBLE);
		if (translation != null)
		{
			new Thread()
			{
				@Override
				public void run()
				{
					Database db = Database.get(getContext());
					if (db.translations().getWithWord(translation.getWord1()).size() > 1)
					{
						List<Word> word = db.words().getWord(words.first, lang.first);
						if (word.isEmpty())
						{
							int id = db.words().count() > 0 ? db.words().getLastId() + 1 : 1;
							db.words().insert(new Word(id, words.first, lang.first));
							translation.setWord1(id);
						}
						else
						{
							translation.setWord1(word.get(0).getId());
						}
					}
					else
					{
						db.words().update(
								new Word(translation.getWord1(), words.first, lang.first));
					}

					if (db.translations().getWithWord(translation.getWord2()).size() > 1)
					{
						List<Word> word = db.words().getWord(words.second, lang.second);
						if (word.isEmpty())
						{
							int id = db.words().count() > 0 ? db.words().getLastId() + 1 : 1;
							db.words().insert(new Word(id, words.second, lang.second));
							translation.setWord2(id);
						}
						else
						{
							translation.setWord2(word.get(0).getId());
						}
					}
					else
					{
						db.words().update(
								new Word(translation.getWord2(), words.second, lang.second));
					}

					db.translations().update(translation);

					getActivity().runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							translationPB.setVisibility(View.INVISIBLE);
							fragment.loadWords();
						}
					});
				}
			}.start();
		}
		else
		{
			new Thread()
			{
				@Override
				public void run()
				{
					int id1, id2;
					Database db = Database.get(getContext());

					List<Word> fWord = db.words().getWord(words.first, lang.first);
					if (fWord.isEmpty())
					{
						id1 = db.words().count() > 0 ? db.words().getLastId() + 1 : 1;
						db.words().insert(new Word(id1, words.first, lang.first));
					}
					else
					{
						id1 = fWord.get(0).getId();
					}

					List<Word> sWord = db.words().getWord(words.second, lang.second);
					if (sWord.isEmpty())
					{
						id2 = db.words().count() > 0 ? db.words().getLastId() + 1 : 1;
						db.words().insert(new Word(id2, words.second, lang.second));
					}
					else
					{
						id2 = sWord.get(0).getId();
					}

					if (lang.first.compareTo(lang.second) > 0)
					{
						int tmp = id1;
						id1 = id2;
						id2 = tmp;
					}

					if (db.translations().getTranslation(id1, id2).isEmpty())
					{
						translation = new Translation(db.translations().count() > 0 ?
													  db.translations().getLastId() + 1 :
													  2, id1, id2, false);
						db.translations().insert(translation);
					}

					getActivity().runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							translationPB.setVisibility(View.INVISIBLE);
							fragment.loadWords();
						}
					});
				}
			}.start();
		}
	}

	private void deleteTranslation()
	{
		translationPB.setVisibility(View.VISIBLE);
		new Thread()
		{
			@Override
			public void run()
			{
				Database.get(getContext()).translations().delete(translation);
				fragment.getActivity().runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						translationPB.setVisibility(View.INVISIBLE);
						fragment.loadWords();
					}
				});
			}
		}.start();
	}
}
