package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptg.R;

import java.util.List;

import Adapter.BaoThucAdapter;
import Database.DatabaseHelper;
import item.BaoThuc;
import javaclass.ThemBaoThucBottomSheet;

public class BaothucFragment extends Fragment {

    private RecyclerView rvBaoThuc;
    private ImageView ivAdd;
    private BaoThucAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<BaoThuc> baoThucList;
    private ThemBaoThucBottomSheet bottomSheet; // reuse

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baothuc, container, false);

        rvBaoThuc = view.findViewById(R.id.rv_baothuc);
        ivAdd = view.findViewById(R.id.img_add);

        dbHelper = new DatabaseHelper(getContext());
        baoThucList = dbHelper.getAllBaoThuc();

        adapter = new BaoThucAdapter(getContext(), baoThucList);
        rvBaoThuc.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBaoThuc.setAdapter(adapter);

        // Tạo 1 instance BottomSheet dùng chung
        bottomSheet = ThemBaoThucBottomSheet.newInstance();

        ivAdd.setOnClickListener(v -> {
            bottomSheet.setBaoThuc(null); // Thêm mới
            bottomSheet.show(getParentFragmentManager(), "ThemBaoThucBottomSheet");
        });

        adapter.setOnItemClickListener(baoThuc -> {
            bottomSheet.setBaoThuc(baoThuc); // sửa
            bottomSheet.show(getParentFragmentManager(), "ThemBaoThucBottomSheet");
        });

        // Lắng nghe refresh dữ liệu chung
        getParentFragmentManager().setFragmentResultListener(
                "refresh_baothuc", this,
                (requestKey, bundle) -> refreshData()
        );

        return view;
    }

    private void refreshData() {
        baoThucList.clear();
        baoThucList.addAll(dbHelper.getAllBaoThuc());
        adapter.notifyDataSetChanged();
    }
}

