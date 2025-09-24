package Fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptg.CustomCalendar.CustomCalendarView;
import com.example.apptg.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.EventAdapter;
import Database.DatabaseHelper;
import item.EventItem;
import javaclass.AddEditEventBottomSheet;

public class HomeFragment extends Fragment {
    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private DatabaseHelper db;
    private List<EventItem> eventList = new ArrayList<>();
    private View imgAdd;
    private CustomCalendarView calendarView;

    public HomeFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        db = new DatabaseHelper(requireContext());

        rvEvents = v.findViewById(R.id.rv_events);
        imgAdd = v.findViewById(R.id.img_add);
        calendarView = v.findViewById(R.id.custom_calendar_view);

        adapter = new EventAdapter(requireContext(), eventList, item -> {
            AddEditEventBottomSheet b = new AddEditEventBottomSheet(this::reloadAndRefreshCalendar);
            b.setEditingEvent(item);
            b.show(getParentFragmentManager(), "edit_event");
        });
        rvEvents.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvEvents.setAdapter(adapter);

        // nút add
        imgAdd.setOnClickListener(xx -> {
            AddEditEventBottomSheet b = new AddEditEventBottomSheet(this::reloadAndRefreshCalendar);
            b.show(getParentFragmentManager(), "add_event");
        });

        // chọn ngày trên calendar
        calendarView.setDateSelectedListener(selectedDates -> {
            if (selectedDates == null || selectedDates.isEmpty()) return;
            String dateIso = selectedDates.get(0);

            List<EventItem> events = db.getEventsByDate(dateIso);
            AddEditEventBottomSheet b = new AddEditEventBottomSheet(this::reloadAndRefreshCalendar);

            if (events != null && !events.isEmpty()) {
                b.setEditingEvent(events.get(0));
            } else {
                b.setPreselectedDate(dateIso); // cần có trong AddEditEventBottomSheet
            }

            b.show(getParentFragmentManager(), "event_sheet");
        });

        reloadAndRefreshCalendar();
    }

    private void reloadAndRefreshCalendar() {
        eventList = db.getAllEvents();
        adapter.setItems(eventList);

        calendarView.clearAllMarks();

        for (EventItem e : eventList) {
            String dateIso = e.getDateIso();
            String hex = e.getColorHex();
            int bgColor = Color.parseColor(hex);

            boolean isDark = isColorDark(bgColor);
            int textColor = isDark
                    ? requireContext().getResources().getColor(R.color.mautrangnhathon)
                    : requireContext().getResources().getColor(R.color.maunensanghon);

            calendarView.setDayColor(dateIso, bgColor, textColor);
        }
    }

    private boolean isColorDark(int color) {
        double r = Color.red(color) / 255.0;
        double g = Color.green(color) / 255.0;
        double b = Color.blue(color) / 255.0;
        double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b;
        return luminance < 0.5;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadAndRefreshCalendar();
    }
}
