package com.example.lepton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.lepton.adapters.PlaceAdapterr;
import com.example.lepton.appconstants.AppConfig;
import com.example.leptontest.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

/*import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;*/


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.Places;
/*import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.PlaceBuffer;
import com.google.android.libraries.places.compat.Places;*/


public class LocationSearchActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private final String TAG = LocationSearchActivity.class.getSimpleName();
    private PlaceAdapterr mPlaceArrayAdapter;
    private EditText et_search;
    private ListView lv_locations;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        et_search = findViewById(R.id.et_search);
        lv_locations = findViewById(R.id.lv_locations);

        mGoogleApiClient = new GoogleApiClient.Builder(LocationSearchActivity.this)
      //          .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();

      /*  if (!Places.isInitialized())
            com.google.android.libraries.places.api.Places.initialize(this,getString(R.string.google_maps_key));
        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
*/

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
           //     mPlaceArrayAdapter.getFilter().filter(s);

            }
        });

    //    lv_locations.setOnItemClickListener(mAutocompleteClickListener);

    /*    mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, BOUNDS_INDIA, null);
        lv_locations.setAdapter(mPlaceArrayAdapter);*/


        // mPlaceArrayAdapter.getFilter().filter();
    }

    @Override
    public void onConnected(Bundle bundle) {
     //   mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i("Location", "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Location", "Google Places API connection failed with error code: "+ connectionResult.getErrorCode());
        // alertDialog(getString(R.string.error),"Google Places API connection failed with error code:" + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
     //   mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e("Location", "Google Places API connection suspended.");
    }

    /*  private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i("Location", "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            //showToast(""+item.description);
            Log.i("Location", "Fetching details for ID: " + item.placeId);
        }
    };*/

   /* private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("Location", "Place query did not complete. Error: " + places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            Log.i("name", place.getName().toString());
            Log.i("coordinates", place.getLatLng().toString());
            Intent resultIntent = new Intent();
            resultIntent.putExtra(AppConfig.LAT,place.getLatLng().latitude);
            resultIntent.putExtra(AppConfig.LNG,place.getLatLng().longitude);
            resultIntent.putExtra(AppConfig.SEARCHED_TEXT,place.getName());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    };*/

}
