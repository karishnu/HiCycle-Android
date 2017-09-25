package me.karishnu.hicycle.network;

import me.karishnu.hicycle.CycleResponse;
import me.karishnu.hicycle.CyclesResponse;
import me.karishnu.hicycle.ProfileResponse;
import me.karishnu.hicycle.SignUp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AcademicsAPIService {

    @POST("/users/signup")
    Call<ProfileResponse> signup(@Body SignUp signUp);

    @GET("/cycle/all")
    Call<CyclesResponse> getCycles();

    @GET("/cycle_auth/book")
    Call<CycleResponse> bookCycle(@Query("cycle_id") String cycle_id, @Header("token_id") String token_id);

    @GET("/cycle_auth/unlock")
    Call<CycleResponse> unlockCycle(@Query("cycle_id") String cycle_id, @Header("token_id") String token_id);

    @GET("/cycle_auth/lock")
    Call<CycleResponse> lockCycle(@Query("cycle_id") String cycle_id, @Header("token_id") String token_id);
}
