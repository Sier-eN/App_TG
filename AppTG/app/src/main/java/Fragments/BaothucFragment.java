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

    public BaothucFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baothuc, container, false);

        // Ánh xạ
        rvBaoThuc = view.findViewById(R.id.rv_baothuc);
        ivAdd = view.findViewById(R.id.img_add);

        // Database
        dbHelper = new DatabaseHelper(getContext());
        baoThucList = dbHelper.getAllBaoThuc();

        // Adapter
        adapter = new BaoThucAdapter(getContext(), baoThucList);
        rvBaoThuc.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBaoThuc.setAdapter(adapter);

        // Nút thêm báo thức
        ivAdd.setOnClickListener(v -> {
            ThemBaoThucBottomSheet bottomSheet = new ThemBaoThucBottomSheet();
            bottomSheet.show(getParentFragmentManager(), "ThemBaoThucBottomSheet");

            // Lắng nghe khi BottomSheet đóng và refresh dữ liệu
            getParentFragmentManager().setFragmentResultListener("refresh_baothuc", this, (requestKey, bundle) -> refreshData());
        });
        adapter.setOnItemClickListener(baoThuc -> {
            ThemBaoThucBottomSheet bottomSheet = new ThemBaoThucBottomSheet();
            bottomSheet.setBaoThuc(baoThuc); // truyền dữ liệu báo thức cần sửa
            bottomSheet.show(getParentFragmentManager(), "ThemBaoThucBottomSheet");

            getParentFragmentManager().setFragmentResultListener("refresh_baothuc", this,
                    (requestKey, bundle) -> refreshData());
        });


        return view;

    }

    private void refreshData() {
        baoThucList.clear();
        baoThucList.addAll(dbHelper.getAllBaoThuc());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            view.setBackgroundResource(R.drawable.bg_bottomsheet);
        }
    }
}
