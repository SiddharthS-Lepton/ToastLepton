package com.example.lepton;

public class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence placeName;
        public CharSequence description;

        public PlaceAutocomplete(CharSequence placeId, CharSequence placeName, CharSequence description) {
            this.placeId = placeId;
            this.placeName=placeName;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }