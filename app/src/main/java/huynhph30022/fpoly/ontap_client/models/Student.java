package huynhph30022.fpoly.ontap_client.models;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("_id")
    private String id;
    @SerializedName("ma_sinh_vien")
    private String masv;
    @SerializedName("ho_ten")
    private String hoten;
    @SerializedName("email")
    private String email;
    @SerializedName("dia_chi")
    private String diachi;
    @SerializedName("diem")
    private String diem;
    @SerializedName("anh_sinh_vien")
    private String anh;

    public Student() {
    }

    public Student(String id, String masv, String hoten, String email, String diachi, String diem, String anh) {
        this.id = id;
        this.masv = masv;
        this.hoten = hoten;
        this.email = email;
        this.diachi = diachi;
        this.diem = diem;
        this.anh = anh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getDiem() {
        return diem;
    }

    public void setDiem(String diem) {
        this.diem = diem;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }
}
