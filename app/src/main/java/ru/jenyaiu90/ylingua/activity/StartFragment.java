package ru.jenyaiu90.ylingua.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.jenyaiu90.ylingua.R;

public class StartFragment extends Fragment
{
    private MainActivity activity;

    public StartFragment(MainActivity activity)
    {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        view.findViewById(R.id.startBT).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activity.start();
            }
        });
        return view;
    }
}