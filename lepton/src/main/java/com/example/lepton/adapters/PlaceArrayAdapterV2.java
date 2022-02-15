package com.example.lepton.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lepton.PlaceAutocomplete;
import com.example.leptontest.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;

public class PlaceArrayAdapterV2 extends ArrayAdapter<PlaceAutocomplete> implements Filterable {
    private static final String TAG = "PlaceArrayAdapter";
    private GoogleApiClient mGoogleApiClient;
  //  private AutocompleteFilter mPlaceFilter;
    private LatLngBounds mBounds;
    private ArrayList<PlaceAutocomplete> mResultList;
    private LayoutInflater mLayoutInflater;
    /**
     * Constructor
     *  @param context  Context
     * @param resource Layout resource
     * @param bounds   Used to specify the search bounds
     * @param filter   Used to specify place types
     */
    public PlaceArrayAdapterV2(Context context, int resource, LatLngBounds bounds, ArrayList<PlaceAutocomplete> filter) {
        super(context, resource);
        mBounds = bounds;
        mResultList = filter;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            mGoogleApiClient = null;
        } else {
            mGoogleApiClient = googleApiClient;
        }
    }

    @Override
    public int getCount() {
        if(mResultList == null)return 0;
        else return mResultList.size();
    }

    @Override
    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            /*mView = mLayoutInflater.inflate(R.layout.location_search_itemv2, null);
            mViewHolder.tv_title = mView.findViewById(R.id.tv_title);
            mViewHolder.tv_sub = mView.findViewById(R.id.tv_sub);*/

        } else {
            mViewHolder = (ViewHolder) mView.getTag();
        }
        if(mResultList != null) {
            PlaceAutocomplete beatInfoBean = mResultList.get(position);
            mViewHolder.tv_title.setText(Html.fromHtml(beatInfoBean.description.toString() != null ? beatInfoBean.description.toString() : ""));
            mViewHolder.tv_sub.setText(Html.fromHtml(beatInfoBean.placeId.toString() != null ? beatInfoBean.placeId.toString() : ""));
            mView.setTag(R.integer.data, beatInfoBean);
        }
        mView.setTag(mViewHolder);
        return mView;
    }

    private ArrayList<PlaceAutocomplete> getPredictions(CharSequence constraint) {
        if (mGoogleApiClient != null) {
            Log.i(TAG, "Executing autocomplete query for: " + constraint);
            com.google.android.libraries.places.api.Places.initialize(getContext(),"AIzaSyD8UCZGteY5MO-H-fTekzVBX1o4MxxBvmI");
            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
            // Use the builder to create a FindAutocompletePredictionsRequest.
            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    // Call either setLocationBias() OR setLocationRestriction().
                 //   .setLocationBias(mBounds)
                    //   .setLocationRestriction(bounds)
                   // .setOrigin(new LatLng(-33.8749937,151.2041382))
                    //.setCountries("AU", "NZ")
                    //.setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(constraint.toString())
                    .build();



            // Create a new PlacesClient instance
            ArrayList resultList = new ArrayList<>();
            if(mResultList==null){
                mResultList=new ArrayList<>();
            }else
            mResultList.clear();
            PlacesClient placesClient = com.google.android.libraries.places.api.Places.createClient(getContext());
            placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                for (com.google.android.libraries.places.api.model.AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                    mResultList.add(new PlaceAutocomplete("",prediction.getSecondaryText(null), prediction.getPrimaryText(new StyleSpan(Typeface.BOLD))));
                    Log.i(TAG, prediction.getPlaceId());
                    Log.i(TAG, prediction.getPrimaryText(null).toString());
                }
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                }
            });

           /* PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, constraint.toString(), null, null);
            // Wait for predictions, set the timeout.
            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(getContext(), "Error: " + status.toString(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error getting place predictions: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount() + " predictions.");
            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(), prediction.getPrimaryText(new StyleSpan(Typeface.BOLD))));
            }
            // Buffer release
            autocompletePredictions.release();*/
            return resultList;
        }
        Log.e(TAG, "Google API client is not connected.");
        return null;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    // Query the autocomplete API for the entered constraint
                    mResultList = getPredictions(constraint);
                    if (mResultList != null) {
                        // Results
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

  /*  public class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }*/

    private class ViewHolder {
        ImageView iv_image;
        TextView tv_title,tv_sub;

    }
}
