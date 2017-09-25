package me.karishnu.hicycle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.CountDownTimer;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import me.karishnu.hicycle.network.APIClient;
import me.karishnu.hicycle.network.AcademicsAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CycleActivity extends AppCompatActivity implements View.OnClickListener {

    TextView address, status;
    CircleImageView imageView;
    Button submit;
    int status_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        status_code = 0;

        address = (TextView) findViewById(R.id.tv_address);
        status = (TextView) findViewById(R.id.tv_status);
        imageView = (CircleImageView) findViewById(R.id.iv_map);
        submit = (Button) findViewById(R.id.bt_submit);

        submit.setOnClickListener(this);

        Glide.with(this)
                .load("https://maps.googleapis.com/maps/api/staticmap?center="+getIntent().getStringExtra("lat")+","+getIntent().getStringExtra("lon")+"&zoom=19&size=400x400&maptype=roadmap&markers=color:green%7C"+getIntent().getStringExtra("lat")+","+getIntent().getStringExtra("lon"))
                .into(imageView);

        address.setText(getAddress(Double.parseDouble(getIntent().getStringExtra("lat")), Double.parseDouble(getIntent().getStringExtra("lon"))));
    }

    private String getAddress(Double lat, Double lon){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
            return addressList.get(0).getAddressLine(0);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    AcademicsAPIService academicsAPIService;
    SharedPreferences preference;

    @Override
    public void onClick(View view) {
        academicsAPIService = APIClient.getClient();
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        if(status_code==0) {
            Call<CycleResponse> cycleResponseCall = academicsAPIService.bookCycle(getIntent().getStringExtra("cycle_id"), preference.getString("token_id", "none"));
            cycleResponseCall.enqueue(new Callback<CycleResponse>() {
                @Override
                public void onResponse(Call<CycleResponse> call, Response<CycleResponse> response) {
                    if (response.body().getCycle().getStatus().equals("booked")) {
                        submit.setText("UNLOCK CYCLE");

                        CountDownTimer countDownTimer = new CountDownTimer(5 * 60 * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                String text = String.format(Locale.getDefault(), "%02d:%02d",
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                                status.setText(text);
                            }

                            @Override
                            public void onFinish() {
                                finish();
                            }
                        };

                        countDownTimer.start();
                        status_code = 1;
                    }
                }

                @Override
                public void onFailure(Call<CycleResponse> call, Throwable t) {

                }
            });
        }
        else if(status_code == 1){

            Intent intent = new Intent(this, QRActivity.class);
            startActivityForResult(intent, 1);

        }
        else if(status_code == 2){
            Call<CycleResponse> cycleResponseCall = academicsAPIService.lockCycle(getIntent().getStringExtra("cycle_id"), preference.getString("token_id", "none"));
            cycleResponseCall.enqueue(new Callback<CycleResponse>() {
                @Override
                public void onResponse(Call<CycleResponse> call, Response<CycleResponse> response) {
                    if (response.body().getCycle().getStatus().equals("locked")) {
                        finish();

                        status_code = 2;
                    }
                }

                @Override
                public void onFailure(Call<CycleResponse> call, Throwable t) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");

                Call<CycleResponse> cycleResponseCall = academicsAPIService.unlockCycle(result, preference.getString("token_id", "none"));
                cycleResponseCall.enqueue(new Callback<CycleResponse>() {
                    @Override
                    public void onResponse(Call<CycleResponse> call, Response<CycleResponse> response) {
                        if (response.body().getCycle().getStatus().equals("unlocked")) {
                            submit.setText("FINISH RIDE");

                            status_code = 2;
                        }
                    }

                    @Override
                    public void onFailure(Call<CycleResponse> call, Throwable t) {

                    }
                });
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}
