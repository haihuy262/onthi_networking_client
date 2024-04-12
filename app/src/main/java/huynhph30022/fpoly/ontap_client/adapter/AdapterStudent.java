package huynhph30022.fpoly.ontap_client.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import huynhph30022.fpoly.ontap_client.R;
import huynhph30022.fpoly.ontap_client.models.Student;
import huynhph30022.fpoly.ontap_client.screen.activity.DetailStudent;

public class AdapterStudent extends RecyclerView.Adapter<AdapterStudent.ViewStudentHolder> {
    private final Context context;
    private ArrayList<Student> listStudent;
    private StudentInterface studentInterface;

    public AdapterStudent(Context context) {
        this.context = context;
    }

    public void setListStudent(ArrayList<Student> listStudent) {
        this.listStudent = listStudent;
        notifyItemInserted(0);
    }

    public void setStudentClickListener(StudentInterface listener) {
        this.studentInterface = listener;
    }

    public interface StudentInterface {
        void clickDelete(String id);

        void clickUpdate(String id, Student student);
    }

    @NonNull
    @Override
    public ViewStudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rcv_main, parent, false);
        return new ViewStudentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewStudentHolder holder, int position) {
        Student objStudent = listStudent.get(position);
        if (objStudent == null) {
            return;
        }
        holder.tvCode.setText(objStudent.getMasv());
        holder.tvName.setText(objStudent.getHoten());
        holder.tvAddress.setText(objStudent.getDiachi());
        String image = objStudent.getAnh();
        Glide.with(context).load(image).into(holder.imgAvatar);

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUpdate(objStudent.getId(), objStudent.getMasv(), objStudent.getHoten(), objStudent.getEmail(), objStudent.getDiachi(), objStudent.getDiem(), objStudent.getAnh());
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDelete(objStudent.getId());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailStudent.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", objStudent.getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listStudent != null) {
            return listStudent.size();
        }
        return 0;
    }

    public static class ViewStudentHolder extends RecyclerView.ViewHolder {
        protected TextView tvName, tvAddress, tvCode;
        protected ImageView imgAvatar, imgEdit, imgDelete;

        public ViewStudentHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvAddress = itemView.findViewById(R.id.address);
            tvCode = itemView.findViewById(R.id.code);
            imgAvatar = itemView.findViewById(R.id.avatar);
            imgEdit = itemView.findViewById(R.id.edit);
            imgDelete = itemView.findViewById(R.id.delete);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showDialogUpdate(String id, String maSinhVien, String hoTen, String email, String diaChi, String diem, String linkAnh) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_student, null);
        builder.setView(view);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        AppCompatButton btnCancel = view.findViewById(R.id.btnCancel);
        AppCompatButton btnUpdate = view.findViewById(R.id.btnAdd);
        EditText edMaSinhVien = view.findViewById(R.id.masinhvien);
        EditText edHoTen = view.findViewById(R.id.hoten);
        EditText edEmail = view.findViewById(R.id.email);
        EditText edDiaChi = view.findViewById(R.id.diachi);
        EditText edDiem = view.findViewById(R.id.diem);
        EditText edLinkAnh = view.findViewById(R.id.linkanh);
        TextView tvTieuDe = view.findViewById(R.id.tv01);

        tvTieuDe.setText("Update Student");
        btnCancel.setText("Cancel");
        btnUpdate.setText("Update");

        edMaSinhVien.setText(maSinhVien);
        edHoTen.setText(hoTen);
        edEmail.setText(email);
        edDiaChi.setText(diaChi);
        edDiem.setText(diem);
        edLinkAnh.setText(linkAnh);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
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
                    Student student = new Student(id, maSinhVien, hoTen, email, diaChi, diem, linkAnh);
                    studentInterface.clickUpdate(id, student);
                    dialog.dismiss();
                }

            }
        });
    }

    private void showDialogDelete(String id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle("Thông báo");
        mBuilder.setMessage("Bạn có muốn xóa hay không?");
        mBuilder.setNegativeButton("NO", null);

        mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                studentInterface.clickDelete(id);
            }
        });
        mBuilder.show();
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
}
