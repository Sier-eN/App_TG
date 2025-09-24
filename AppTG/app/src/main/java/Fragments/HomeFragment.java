package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptg.CustomCalendar.CustomCalendarView;
import com.example.apptg.CustomCalendar.OnDateSelectedListener;
import com.example.apptg.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    CustomCalendarView customCalendarView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        customCalendarView = view.findViewById(R.id.custom_calendar_view);

        customCalendarView.setDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(ArrayList<String> selectedDates) {
                Log.e(TAG,"onDateSelected" + selectedDates.toString());
            }
        });
        return view;
    }
}