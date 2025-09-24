package javaclass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import com.example.apptg.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import Database.DatabaseHelper;
import item.BaoThuc;

public class ThemBaoThucBottomSheet extends BottomSheetDialogFragment {

    private TimePicker timePicker;
    private ToggleButton tbT2, tbT3, tbT4, tbT5, tbT6, tbT7, tbCN;
    private ImageView imgLuu, imgXoa;
    private CardView cvXoa;
    private DatabaseHelper dbHelper;
    private BaoThuc baoThuc;

    private boolean isViewCreated = false; // để lazy load

    // Singleton pattern: tái sử dụng instance
    public static ThemBaoThucBottomSheet newInstance() {
        return new ThemBaoThucBottomSheet();
    }

    public void setBaoThuc(BaoThuc baoThuc) {
        this.baoThuc = baoThuc;
        if (isViewCreated) {
            loadBaoThucData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_thembaothuc, container, false);

        // Ánh xạ view
        timePicker = view.findViewById(R.id.timePicker);
        tbT2 = view.findViewById(R.id.tbT2);
        tbT3 = view.findViewById(R.id.tbT3);
        tbT4 = view.findViewById(R.id.tbT4);
        tbT5 = view.findViewById(R.id.tbT5);
        tbT6 = view.findViewById(R.id.tbT6);
        tbT7 = view.findViewById(R.id.tbT7);
        tbCN = view.findViewById(R.id.tbCN);
        imgLuu = view.findViewById(R.id.imgLuu);
        imgXoa = view.findViewById(R.id.imgXoa);
        cvXoa = view.findViewById(R.id.cvXoa);

        timePicker.setIs24HourView(true);
        dbHelper = new DatabaseHelper(getContext());

        // Lắng nghe nút Lưu
        imgLuu.setOnClickListener(v -> saveBaoThuc());

        // Lắng nghe nút Xóa
        imgXoa.setOnClickListener(v -> deleteBaoThuc());

        isViewCreated = true;

        // Lazy load dữ liệu nếu đã set trước đó
        if (baoThuc != null) {
            loadBaoThucData();
        } else {
            imgXoa.setVisibility(View.GONE);
            cvXoa.setVisibility(View.GONE);
        }

        return view;
    }

    private void loadBaoThucData() {
        if (baoThuc == null) return;

        timePicker.setHour(baoThuc.getH());
        timePicker.setMinute(baoThuc.getM());

        tbT2.setChecked(baoThuc.getT2() == 1);
        tbT3.setChecked(baoThuc.getT3() == 1);
        tbT4.setChecked(baoThuc.getT4() == 1);
        tbT5.setChecked(baoThuc.getT5() == 1);
        tbT6.setChecked(baoThuc.getT6() == 1);
        tbT7.setChecked(baoThuc.getT7() == 1);
        tbCN.setChecked(baoThuc.getCn() == 1);

        imgXoa.setVisibility(View.VISIBLE);
        cvXoa.setVisibility(View.VISIBLE);
    }

    private void saveBaoThuc() {
        int h = timePicker.getHour();
        int m = timePicker.getMinute();
        int t2 = tbT2.isChecked() ? 1 : 0;
        int t3 = tbT3.isChecked() ? 1 : 0;
        int t4 = tbT4.isChecked() ? 1 : 0;
        int t5 = tbT5.isChecked() ? 1 : 0;
        int t6 = tbT6.isChecked() ? 1 : 0;
        int t7 = tbT7.isChecked() ? 1 : 0;
        int cn = tbCN.isChecked() ? 1 : 0;

        if (baoThuc != null) {
            // Update báo thức
            baoThuc.setH(h);
            baoThuc.setM(m);
            baoThuc.setT2(t2);
            baoThuc.setT3(t3);
            baoThuc.setT4(t4);
            baoThuc.setT5(t5);
            baoThuc.setT6(t6);
            baoThuc.setT7(t7);
            baoThuc.setCn(cn);
            baoThuc.setBat(1);
            dbHelper.updateBaoThuc(baoThuc);

            javaclass.AlarmCanceler.huyBaoThuc(getContext(), baoThuc);
            javaclass.AlarmScheduler.datBaoThuc(getContext(), baoThuc);
        } else {
            BaoThuc newBaoThuc = new BaoThuc(h, m, t2, t3, t4, t5, t6, t7, cn, 1);
            long id = dbHelper.insertBaoThuc(newBaoThuc);
            newBaoThuc.setId((int) id);
            javaclass.AlarmScheduler.datBaoThuc(getContext(), newBaoThuc);
        }

        getParentFragmentManager().setFragmentResult("refresh_baothuc", new Bundle());
        dismiss();
    }

    private void deleteBaoThuc() {
        if (baoThuc != null) {
            dbHelper.deleteBaoThuc(baoThuc.getId());
            javaclass.AlarmCanceler.huyBaoThuc(getContext(), baoThuc);
            getParentFragmentManager().setFragmentResult("refresh_baothuc", new Bundle());
            dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            view.getLayoutParams().height = (int)(getResources().getDisplayMetrics().heightPixels * 0.75);
        }
    }
}
