package me.karishnu.hicycle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import me.karishnu.hicycle.network.APIClient;
import me.karishnu.hicycle.network.AcademicsAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private AcademicsAPIService academicsAPIService;
    private List<Cycle> cycleList;
    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        academicsAPIService = APIClient.getClient();

        refresh = (Button) findViewById(R.id.bt_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCycles();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style));

        updateCycles();
    }

    ProgressDialog progress;

    private void updateCycles(){

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setMessage("Searching for bikes nearby.");

        progress.show();

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        mMap.clear();

        Call<CyclesResponse> cyclesResponseCall = academicsAPIService.getCycles();
        cyclesResponseCall.enqueue(new Callback<CyclesResponse>() {
            @Override
            public void onResponse(Call<CyclesResponse> call, Response<CyclesResponse> response) {
                cycleList = response.body().getCycles();

                if(cycleList.isEmpty()){
                    progress.dismiss();
                    Toast.makeText(MapsActivity.this, "No bokes found nearby!", Toast.LENGTH_LONG).show();
                }
                else {

                    for (Cycle cycle : cycleList) {
                        LatLng sydney = new LatLng(Double.parseDouble(cycle.getCoodX()), Double.parseDouble(cycle.getCoodY()));
                        Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                        marker.setTag(cycle.getCycleId());
                        builder.include(sydney);
                    }

                    LatLngBounds bounds = builder.build();

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;
                    int padding = (int) (width * 0.20);

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                    progress.dismiss();

                    mMap.animateCamera(cu);
                }
            }

            @Override
            public void onFailure(Call<CyclesResponse> call, Throwable t) {
                progress.dismiss();
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String marker_id = (String) marker.getTag();
        for(Cycle cycle: cycleList){
            if(cycle.getCycleId().equals(marker_id)){
                Intent intent = new Intent(this, CycleActivity.class);
                intent.putExtra("lat", cycle.getCoodX());
                intent.putExtra("lon", cycle.getCoodY());
                intent.putExtra("cycle_id", marker_id);
                startActivity(intent);
            }
        }
        return true;
    }
}
