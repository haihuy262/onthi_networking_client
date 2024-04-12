package huynhph30022.fpoly.ontap_client.screen.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import huynhph30022.fpoly.ontap_client.R;
import huynhph30022.fpoly.ontap_client.api.ApiService;
import huynhph30022.fpoly.ontap_client.models.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailStudent extends AppCompatActivity {
    private String id;
    private TextView tvMaSinhVien, tvHoTen, tvEmail, tvDiem, tvDiaChi;
    private ImageView imgAnh;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgAnh = findViewById(R.id.imageDetail);
        tvHoTen = findViewById(R.id.nameDetail);
        tvMaSinhVien = findViewById(R.id.codeDetail);
        tvEmail = findViewById(R.id.emailDetail);
        tvDiem = findViewById(R.id.diemDetail);
        tvDiaChi = findViewById(R.id.addressDetail);
        progressBar = findViewById(R.id.progress_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            getDetailStudent(id);
        }
    }

    private void getDetailStudent(String id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService.API_SERVICE.getStudentID(id).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(@NonNull Call<Student> call, @NonNull Response<Student> response) {
                if (response.isSuccessful()) {
                    Student student = response.body();
                    assert student != null;

                    String image = student.getAnh();
                    String mssv = student.getMasv();
                    String hoTen = student.getHoten();
                    String diaChi = student.getDiachi();
                    String email = student.getEmail();
                    String diem = student.getDiem();

                    Glide.with(DetailStudent.this).load(image).into(imgAnh);
                    tvMaSinhVien.setText(mssv);
                    tvHoTen.setText(hoTen);
                    tvDiaChi.setText(diaChi);
                    tvEmail.setText(email);
                    tvDiem.setText(diem);

                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Student> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}