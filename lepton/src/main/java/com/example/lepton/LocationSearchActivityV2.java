package com.example.lepton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.lepton.adapters.PlaceAdapter;

import com.example.leptontest.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;





import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationSearchActivityV2 extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private final String TAG = LocationSearchActivityV2.class.getSimpleName();
    private PlaceAdapter mPlaceArrayAdapter;
    private EditText et_search;
    private ListView lv_locations;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    ArrayList<PlaceAutocomplete> mResultList=new ArrayList<>();
   private PlacesClient placesClient;
    AutocompleteSessionToken token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_searchv2);
        if (!com.google.android.libraries.places.api.Places.isInitialized())
        com.google.android.libraries.places.api.Places.initialize(this,getString(R.string.google_maps_key));
        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        token = AutocompleteSessionToken.newInstance();
        et_search = findViewById(R.id.et_search);
        lv_locations = findViewById(R.id.lv_locations);



        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s!=null && s.length()>2)
                getPrediction(s);
                else {
                    mResultList.clear();
                    for (int i=0;i<mResultList.size();i++){
                        Log.i(TAG,"Result "+mResultList.get(i).placeName);
                    }
                    mPlaceArrayAdapter = new PlaceAdapter(mResultList,LocationSearchActivityV2.this);
                    lv_locations.setAdapter(mPlaceArrayAdapter);
                }
//                mPlaceArrayAdapter.getFilter().filter(s);

            }
        });

        lv_locations.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceAdapter(mResultList,LocationSearchActivityV2.this);
        lv_locations.setAdapter(mPlaceArrayAdapter);
     /*   mPlaceArrayAdapter = new PlaceArrayAdapterV2(mResultList,this);
        lv_locations.setAdapter(mPlaceArrayAdapter);*/

        // mPlaceArrayAdapter.getFilter().filter();
    }

    private void getPrediction(Editable constraint) {


        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                //   .setLocationBias(mBounds)
                //   .setLocationRestriction(bounds)
                // .setOrigin(new LatLng(-33.8749937,151.2041382))
              //  .setCountries("IN")
                //.setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build();


        // Create a new PlacesClient instance

            mResultList.clear();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (com.google.android.libraries.places.api.model.AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                mResultList.add(new PlaceAutocomplete(prediction.getPlaceId(),prediction.getPrimaryText(null), prediction.getSecondaryText(new StyleSpan(Typeface.BOLD))));
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());

              //  mPlaceArrayAdapter.notifyDataSetChanged();
            }
            mPlaceArrayAdapter = new PlaceAdapter(mResultList,LocationSearchActivityV2.this);
            lv_locations.setAdapter(mPlaceArrayAdapter);

   /*         mPlaceArrayAdapter = new PlaceAdapter(mResultList,this);
            lv_locations.setAdapter(mPlaceArrayAdapter);*/

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceAutocomplete item = mResultList.get(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i("Location", "Selected: " + item.description);
            final List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,
                    com.google.android.libraries.places.api.model.Place.Field.NAME
            ,com.google.android.libraries.places.api.model.Place.Field.LAT_LNG
            );

// Construct a request object, passing the place ID and fields array.
            final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                com.google.android.libraries.places.api.model.Place place = response.getPlace();
                Log.i("name", place.getName().toString());
                Log.i("coordinates", place.getLatLng().toString());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latt",place.getLatLng().latitude);
                resultIntent.putExtra("lng",place.getLatLng().longitude);
                resultIntent.putExtra("searched_text",place.getName());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                Log.i(TAG, "Place found: " + place.getName());
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();
                    // TODO: Handle error with given status code.
                }
            });

           /* PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            //showToast(""+item.description);
            Log.i("Location", "Fetching details for ID: " + item.placeId);*/
        }
    };




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
