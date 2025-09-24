package javaclass;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.apptg.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Adapter.ColorAdapter;
import Database.DatabaseHelper;
import item.EventItem;

public class AddEditEventBottomSheet extends BottomSheetDialogFragment {

    private EditText etTitle;
    private TextView tvDate;
    private ImageView imgSave, imgDelete;
    private RecyclerView rvColors;
    private ColorAdapter colorAdapter;
    private List<String> colorList;
    private String selectedColor = "#FF0000";
    private String selectedDateIso = ""; // yyyy-MM-dd
    private DatabaseHelper db;
    private EventItem editingEvent; // nếu null => insert
    private String preselectedDate;


    public interface OnChangeListener {
        void onChanged(); // gọi để HomeFragment reload dữ liệu
    }
    private OnChangeListener changeListener;

    public AddEditEventBottomSheet(OnChangeListener l) { this.changeListener = l; }

    // Nếu bạn cần khởi tạo với event để edit, gọi setEditingEvent(...)
    public void setEditingEvent(EventItem e) { this.editingEvent = e; }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_bottom_sheet_event, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // set height ~ 80% của màn hình
        View view = getView();
        if (view == null) return;
        View parent = (View) view.getParent();
        parent.getLayoutParams().height = (int) (getScreenHeight() * 0.80);
        BottomSheetBehavior.from(parent).setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getScreenHeight() {
        WindowManager wm = requireActivity().getWindowManager();
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        return size.y;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        db = new DatabaseHelper(requireContext());
        etTitle = v.findViewById(R.id.et_title);
        tvDate = v.findViewById(R.id.tv_date);
        imgSave = v.findViewById(R.id.img_save);
        imgDelete = v.findViewById(R.id.img_delete);
        rvColors = v.findViewById(R.id.rv_colors);

        // prepare color list (bạn có thể thay hex khác)
        colorList = new ArrayList<>();
        colorList.add("#F44336"); // red
        colorList.add("#E91E63"); // pink
        colorList.add("#9C27B0"); // purple
        colorList.add("#3F51B5"); // indigo
        colorList.add("#2196F3"); // blue
        colorList.add("#03A9F4"); // light blue
        colorList.add("#009688"); // teal
        colorList.add("#4CAF50"); // green
        colorList.add("#FF9800"); // orange
        colorList.add("#FFC107"); // amber
        colorList.add("#795548"); // brown
        colorList.add("#607D8B"); // blue grey
        // ... thêm nếu muốn

        colorAdapter = new ColorAdapter(requireContext(), colorList, (hex, pos) -> {
            selectedColor = hex;
        });
        rvColors.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvColors.setAdapter(colorAdapter);

        // Date pick
        tvDate.setOnClickListener(vv -> showDatePicker());
        v.findViewById(R.id.btn_pick_date).setOnClickListener(vv -> showDatePicker());

        // nếu editingEvent != null thì load data
        if (editingEvent != null) {
            etTitle.setText(editingEvent.getTitle());
            selectedDateIso = editingEvent.getDateIso();
            tvDate.setText(selectedDateIso);
            selectedColor = editingEvent.getColorHex();
            int pos = colorList.indexOf(selectedColor);
            if (pos >= 0) colorAdapter.setSelectedPos(pos);
            imgDelete.setVisibility(View.VISIBLE);
        } else {
            imgDelete.setVisibility(View.GONE);
        }

        imgSave.setOnClickListener(vv -> {
            String title = etTitle.getText().toString().trim();
            if (title.isEmpty()) { etTitle.setError("Nhập tên"); return; }
            if (selectedDateIso.isEmpty()) { tvDate.setError("Chọn ngày"); return; }
            if (editingEvent == null) {
                EventItem e = new EventItem(title, selectedDateIso, selectedColor);
                db.insertEvent(e);
            } else {
                editingEvent.setTitle(title);
                editingEvent.setDateIso(selectedDateIso);
                editingEvent.setColorHex(selectedColor);
                db.updateEvent(editingEvent);
            }
            if (changeListener != null) changeListener.onChanged();
            dismiss();
        });

        imgDelete.setOnClickListener(vv -> {
            if (editingEvent != null) {
                db.deleteEvent(editingEvent.getId());
                if (changeListener != null) changeListener.onChanged();
            }
            dismiss();
        });
    }
    public void setPreselectedDate(String dateIso) {
        this.preselectedDate = dateIso;
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dp = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            // month is 0-based
            String mm = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
            String dd = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
            selectedDateIso = String.format("%04d-%s-%s", year, mm, dd);
            tvDate.setText(selectedDateIso);
        }, y, m, d);
        dp.show();
    }
}
