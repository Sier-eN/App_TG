package javaclass;

import android.content.ContentValues;
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

import com.example.apptg.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import Database.DatabaseHelper;
import item.BaoThuc;

public class ThemBaoThucBottomSheet extends BottomSheetDialogFragment {

    private TimePicker timePicker;
    private ToggleButton tbT2, tbT3, tbT4, tbT5, tbT6, tbT7, tbCN;
    private ImageView imgLuu,imgXoa;
    private DatabaseHelper dbHelper;
    private BaoThuc baoThuc;
    private CardView cvXoa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_thembaothuc, container, false);

        // Ánh xạ
        timePicker = view.findViewById(R.id.timePicker);
        tbT2 = view.findViewById(R.id.tbT2);
        tbT3 = view.findViewById(R.id.tbT3);
        tbT4 = view.findViewById(R.id.tbT4);
        tbT5 = view.findViewById(R.id.tbT5);
        tbT6 = view.findViewById(R.id.tbT6);
        tbT7 = view.findViewById(R.id.tbT7);
        tbCN = view.findViewById(R.id.tbCN);
        imgLuu = view.findViewById(R.id.imgLuu);

        // ➋ Nút Xóa (chỉ hiện khi sửa)
        imgXoa = view.findViewById(R.id.imgXoa);
        cvXoa = view.findViewById(R.id.cvXoa);
        imgXoa.setVisibility(baoThuc != null ? View.VISIBLE : View.GONE);
        cvXoa.setVisibility(baoThuc != null ? View.VISIBLE : View.GONE);

        timePicker.setIs24HourView(true);
        dbHelper = new DatabaseHelper(getContext());

        // ➌ Nếu là sửa, điền dữ liệu cũ
        if (baoThuc != null) {
            timePicker.setHour(baoThuc.getH());
            timePicker.setMinute(baoThuc.getM());

            tbT2.setChecked(baoThuc.getT2() == 1);
            tbT3.setChecked(baoThuc.getT3() == 1);
            tbT4.setChecked(baoThuc.getT4() == 1);
            tbT5.setChecked(baoThuc.getT5() == 1);
            tbT6.setChecked(baoThuc.getT6() == 1);
            tbT7.setChecked(baoThuc.getT7() == 1);
            tbCN.setChecked(baoThuc.getCn() == 1);
        }

        // ➍ Lưu (Insert hoặc Update)
        imgLuu.setOnClickListener(v -> {
            int h = timePicker.getHour();
            int m = timePicker.getMinute();
            int t2 = tbT2.isChecked() ? 1 : 0;
            int t3 = tbT3.isChecked() ? 1 : 0;
            int t4 = tbT4.isChecked() ? 1 : 0;
            int t5 = tbT5.isChecked() ? 1 : 0;
            int t6 = tbT6.isChecked() ? 1 : 0;
            int t7 = tbT7.isChecked() ? 1 : 0;
            int cn = tbCN.isChecked() ? 1 : 0;

            if(baoThuc != null){
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
                baoThuc.setBat(1); // bật báo thức
                dbHelper.updateBaoThuc(baoThuc);

                // Hủy báo thức cũ và đặt lại mới
                javaclass.AlarmCanceler.huyBaoThuc(getContext(), baoThuc);
                javaclass.AlarmScheduler.datBaoThuc(getContext(), baoThuc);

            } else {
                // Thêm mới
                BaoThuc newBaoThuc = new BaoThuc(h, m, t2, t3, t4, t5, t6, t7, cn, 1);
                long id = dbHelper.insertBaoThuc(newBaoThuc);
                newBaoThuc.setId((int) id);

                // Đặt báo thức
                javaclass.AlarmScheduler.datBaoThuc(getContext(), newBaoThuc);
            }

            getParentFragmentManager().setFragmentResult("refresh_baothuc", new Bundle());
            dismiss();
        });


        // ➎ Xóa
        imgXoa.setOnClickListener(v -> {
            if(baoThuc != null){
                dbHelper.deleteBaoThuc(baoThuc.getId());
                // Hủy luôn báo thức
                javaclass.AlarmCanceler.huyBaoThuc(getContext(), baoThuc);
                getParentFragmentManager().setFragmentResult("refresh_baothuc", new Bundle());
                dismiss();
            }
        });


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if(view != null){
            view.getLayoutParams().height = (int)(getResources().getDisplayMetrics().heightPixels * 0.75);
        }
    }

    public void setBaoThuc(BaoThuc baoThuc) {
        this.baoThuc = baoThuc;
    }
}
