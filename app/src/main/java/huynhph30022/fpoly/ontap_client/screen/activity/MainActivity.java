package huynhph30022.fpoly.ontap_client.screen.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import huynhph30022.fpoly.ontap_client.R;
import huynhph30022.fpoly.ontap_client.adapter.AdapterStudent;
import huynhph30022.fpoly.ontap_client.api.ApiService;
import huynhph30022.fpoly.ontap_client.api.LinkApi;
import huynhph30022.fpoly.ontap_client.models.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterStudent.StudentInterface {
    private RecyclerView rcv_main;
    private final ArrayList<Student> list = new ArrayList<>();
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUI();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListStudent();
    }

    private void initUI() {
        progressBar = findViewById(R.id.progressBar);
        rcv_main = findViewById(R.id.rcv_main);
        fab = findViewById(R.id.fab_main);
    }


    private void setLayout(ArrayList<Student> list) {
        AdapterStudent adapterStudent = new AdapterStudent(MainActivity.this);
        adapterStudent.setStudentClickListener(MainActivity.this);
        adapterStudent.setListStudent(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcv_main.setLayoutManager(layoutManager);
        rcv_main.setAdapter(adapterStudent);
    }

    @SuppressLint("SetTextI18n")
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_student, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        AppCompatButton btnCancel = view.findViewById(R.id.btnCancel);
        AppCompatButton btnAdd = view.findViewById(R.id.btnAdd);
        EditText edMaSinhVien = view.findViewById(R.id.masinhvien);
        EditText edHoTen = view.findViewById(R.id.hoten);
        EditText edEmail = view.findViewById(R.id.email);
        EditText edDiaChi = view.findViewById(R.id.diachi);
        EditText edDiem = view.findViewById(R.id.diem);
        EditText edLinkAnh = view.findViewById(R.id.linkanh);
        TextView tvTieuDe = view.findViewById(R.id.tv01);

        tvTieuDe.setText("Add Student");
        btnCancel.setText("Cancel");
        btnAdd.setText("Add");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = "Vui lòng điền trường này!";
                boolean isValid;

                String maSinhVien = edMaSinhVien.getText().toString();
                String hoTen = edHoTen.getText().toString();
                String email = edEmail.getText().toString();
                String diaChi = edDiaChi.getText().toString();
                String diem = edDiem.getText().toString();
                String linkAnh = edLinkAnh.getText().toString();

                isValid = validateFields(edMaSinhVien, error);
                isValid = validateFields(edHoTen, error) && isValid;
                isValid = validateFields(edEmail, error) && isValid;
                isValid = validateFields(edDiaChi, error) && isValid;
                isValid = validateScoreField(edDiem, error) && isValid;
                isValid = validateFields(edLinkAnh, error) && isValid;

                if (isValid) {
                    postStudent(maSinhVien, hoTen, email, diaChi, diem, linkAnh);
                    dialog.dismiss();
                }

            }
        });
    }

    private boolean validateFields(EditText editText, String error) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError(error);
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    private boolean validateScoreField(EditText editText, String error) {
        String input = editText.getText().toString();
        if (TextUtils.isEmpty(input)) {
            editText.setError(error);
            return false;
        } else {
            try {
                float score = Float.parseFloat(input);
                if (score < 0 || score > 10) {
                    editText.setError("Điểm phải nằm trong khoảng từ 0 đến 10");
                    return false;
                } else {
                    editText.setError(null);
                    return true;
                }
            } catch (NumberFormatException e) {
                editText.setError("Điểm phải là một số");
                return false;
            }
        }
    }

    private void getListStudent() {
        progressBar.setVisibility(View.VISIBLE);

        ApiService.API_SERVICE.getAllStudent().enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(@NonNull Call<List<Student>> call, @NonNull Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    assert response.body() != null;
                    list.addAll(response.body());
                    setLayout(list);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Student>> call, @NonNull Throwable t) {
                Log.d(LinkApi.TAG, "Lỗi getStudent" + t.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void postStudent(String maSinhVien, String hoTen, String email, String diaChi, String diem, String linkAnh) {
        Student newStudent = new Student();
        newStudent.setMasv(maSinhVien);
        newStudent.setHoten(hoTen);
        newStudent.setEmail(email);
        newStudent.setDiachi(diaChi);
        newStudent.setDiem(diem);
        newStudent.setAnh(linkAnh);

        ApiService.API_SERVICE.postStudent(newStudent).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(@NonNull Call<Student> call, @NonNull Response<Student> response) {
                if (response.isSuccessful()) {
                    getListStudent();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Student> call, @NonNull Throwable t) {
                Log.d(LinkApi.TAG, "Lỗi postStudent" + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void clickDelete(String id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService.API_SERVICE.deleteStudent(id).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(@NonNull Call<Student> call, @NonNull Response<Student> response) {
                if (response.isSuccessful()) {
                    getListStudent();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Student> call, @NonNull Throwable t) {
                Log.d(LinkApi.TAG, "Lỗi updateStudent" + t.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void clickUpdate(String id, Student student) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService.API_SERVICE.putStudent(id, student).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(@NonNull Call<Student> call, @NonNull Response<Student> response) {
                if (response.isSuccessful()) {
                    getListStudent();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Student> call, @NonNull Throwable t) {
                Log.d(LinkApi.TAG, "Lỗi updateStudent" + t.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}