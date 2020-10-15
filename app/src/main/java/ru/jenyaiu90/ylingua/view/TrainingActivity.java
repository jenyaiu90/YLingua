package ru.jenyaiu90.ylingua.view;

import android.os.Bundle;
import android.util.Pair;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import ru.jenyaiu90.ylingua.R;

public class TrainingActivity extends AppCompatActivity
{
	public static final String LANG1 = "lang_1";
	public static final String LANG2 = "lang_2";
	public static final String LEARNED = "learned";

	private FrameLayout trainingFL;

	private Pair<String, String> langs;
	private boolean withLearned;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training);

		langs = new Pair<>(getIntent().getStringExtra(LANG1), getIntent().getStringExtra(LANG2));
		withLearned = getIntent().getBooleanExtra(LEARNED, false);

		trainingFL = findViewById(R.id.trainingFL);
		training();
	}

	public void training()
	{
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.trainingFL, new TrainingFragment(
				TrainingActivity.this, langs, withLearned));
		transaction.commit();
	}
}
