package huynhph30022.fpoly.ontap_client.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import huynhph30022.fpoly.ontap_client.models.Student;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    Gson GSON = new GsonBuilder().setLenient().create();
    ApiService API_SERVICE = new Retrofit.Builder()
            .baseUrl(LinkApi.linkUrl + "/api/sinhvien/")
            .addConverterFactory(GsonConverterFactory.create(GSON))
            .build()
            .create(ApiService.class);

    @GET("list")
    Call<List<Student>> getAllStudent();

    @GET("list/{id}")
    Call<Student> getStudentID(@Path("id") String id);

    @POST("add")
    Call<Student> postStudent(@Body Student student);

    @PUT("edit/{id}")
    Call<Student> putStudent(@Path("id") String id, @Body Student student);

    @DELETE("delete/{id}")
    Call<Student> deleteStudent(@Path("id") String id);
}
