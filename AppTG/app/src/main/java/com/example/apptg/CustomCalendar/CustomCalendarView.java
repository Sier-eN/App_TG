package com.example.apptg.CustomCalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.apptg.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class CustomCalendarView extends LinearLayout {
    private static final String TAG = "CustomCalendarView";

    private TextView tvThangHienTai;
    private ImageView ivThangTruoc, ivThangSau;
    private GridLayout gridCalendarDays;
    private LocalDate thangHienTai;
    private Set<LocalDate> selectedDate;
    private OnDateSelectedListener dateSelectedListener;

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMMM yyyy");

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_calendar_view, this, true);

        tvThangHienTai = findViewById(R.id.thangnam);
        ivThangTruoc = findViewById(R.id.thangtruoc);
        ivThangSau = findViewById(R.id.thangsau);
        gridCalendarDays = findViewById(R.id.grid_calendar_day);

        thangHienTai = LocalDate.now().withDayOfMonth(1);

        // chọn ngày hôm nay
        selectedDate = new HashSet<>();
        selectedDate.add(LocalDate.now());

        ivThangTruoc.setOnClickListener(v -> {
            thangHienTai = thangHienTai.minusMonths(1);
            setupCalender();
        });
        ivThangSau.setOnClickListener(v -> {
            thangHienTai = thangHienTai.plusMonths(1);
            setupCalender();
        });

        setupCalender();
    }


    private void setupCalender() {
        gridCalendarDays.removeAllViews();
        tvThangHienTai.setText(thangHienTai.format(monthFormat));


        int firstDayOfWeek = thangHienTai.getDayOfWeek().getValue() % 7;

        // Thêm ngày rỗng bắt đầu
        for (int i = 0; i < firstDayOfWeek; i++) {
            TextView emptyDay = new TextView(getContext(), null, 0, R.style.CalendarDate);
            emptyDay.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f)
            ));
            emptyDay.setText(".");
            gridCalendarDays.addView(emptyDay);
        }

        //Thêm Ngày Chính Xác
        for (int day = 1; day <= thangHienTai.lengthOfMonth(); day++) {
            final LocalDate date = thangHienTai.withDayOfMonth(day);
            TextView dayview = new TextView(getContext(), null, 0, R.style.CalendarDate);
            dayview.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f)
            ));

            dayview.setText(String.valueOf(day));
            dayview.setTag(date);

            if (selectedDate.contains(date)) {
                dayview.setSelected(true);
            } else {
                dayview.setSelected(false);
            }

            dayview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalDate clickedDate = (LocalDate) v.getTag();
                    if (selectedDate.contains(clickedDate)) {
                        selectedDate.remove(clickedDate);
                        v.setSelected(false);
                    } else {
                        selectedDate.add(clickedDate);
                        v.setSelected(true);
                    }

                    if (dateSelectedListener != null) {
                        dateSelectedListener.onDateSelected(getSelectedDates());
                    }
                }
            });

            gridCalendarDays.addView(dayview);
        }
        // Thêm Ngày rỗng kết thúc
        int totalCells = firstDayOfWeek + thangHienTai.lengthOfMonth();

        for(int i = totalCells; i < 42; i++){
            TextView emptyDay = new TextView(getContext(), null, 0, R.style.CalendarDate);
            emptyDay.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f)
            ));
            emptyDay.setText(".");
            gridCalendarDays.addView(emptyDay);
        }
    }



    public void setSelectedDate(ArrayList<String>date){
        selectedDate.clear();
        for (String dateStr: date){
            try {
                selectedDate.add(LocalDate.parse(dateStr,dateFormat));
            }catch (Exception e){
                Log.e(TAG, "setSelectedDates: ");
            }

        }
        setupCalender();
    }

    public ArrayList<String> getSelectedDates() {
        ArrayList<String> dates = new ArrayList<>();
        for (LocalDate date : selectedDate) {
            dates.add(date.format(dateFormat));
        }

        return dates;
    }

    public void setDateSelectedListener(OnDateSelectedListener dateSelectedListener) {
        this.dateSelectedListener = dateSelectedListener;
    }
}
