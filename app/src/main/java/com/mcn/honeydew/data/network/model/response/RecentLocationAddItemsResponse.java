package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gkumar on 9/3/18.
 */

public class RecentLocationAddItemsResponse {

        @SerializedName("html_attributions")
        @Expose
        private List<Object> htmlAttributions = null;
        @SerializedName("results")
        @Expose
        private List<LocationResult> results = null;
        @SerializedName("status")
        @Expose
        private String status;

        public List<Object> getHtmlAttributions() {
            return htmlAttributions;
        }

        public void setHtmlAttributions(List<Object> htmlAttributions) {
            this.htmlAttributions = htmlAttributions;
        }

        public List<LocationResult> getResults() {
            return results;
        }

        public void setResults(List<LocationResult> results) {
            this.results = results;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }




    public class Geometry {

        @SerializedName("location")
        @Expose
        private Location location;
        @SerializedName("viewport")
        @Expose
        private Viewport viewport;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Viewport getViewport() {
            return viewport;
        }

        public void setViewport(Viewport viewport) {
            this.viewport = viewport;
        }

    }

    public class Location {

        @SerializedName("lat")
        @Expose
        private float lat;
        @SerializedName("lng")
        @Expose
        private float lng;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

    }


    public class Northeast {

        @SerializedName("lat")
        @Expose
        private float lat;
        @SerializedName("lng")
        @Expose
        private float lng;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

    }


    public class OpeningHours {

        @SerializedName("open_now")
        @Expose
        private boolean openNow;
        @SerializedName("weekday_text")
        @Expose
        private List<Object> weekdayText = null;

        public boolean isOpenNow() {
            return openNow;
        }

        public void setOpenNow(boolean openNow) {
            this.openNow = openNow;
        }

        public List<Object> getWeekdayText() {
            return weekdayText;
        }

        public void setWeekdayText(List<Object> weekdayText) {
            this.weekdayText = weekdayText;
        }

    }


    public class Photo {

        @SerializedName("height")
        @Expose
        private int height;
        @SerializedName("html_attributions")
        @Expose
        private List<String> htmlAttributions = null;
        @SerializedName("photo_reference")
        @Expose
        private String photoReference;
        @SerializedName("width")
        @Expose
        private int width;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<String> getHtmlAttributions() {
            return htmlAttributions;
        }

        public void setHtmlAttributions(List<String> htmlAttributions) {
            this.htmlAttributions = htmlAttributions;
        }

        public String getPhotoReference() {
            return photoReference;
        }

        public void setPhotoReference(String photoReference) {
            this.photoReference = photoReference;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

    }


    public class LocationResult {

        @SerializedName("formatted_address")
        @Expose
        private String formattedAddress;
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;
        @SerializedName("icon")
        @Expose
        private String icon;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("photos")
        @Expose
        private List<Photo> photos = null;
        @SerializedName("place_id")
        @Expose
        private String placeId;
        @SerializedName("rating")
        @Expose
        private Double rating;
        @SerializedName("reference")
        @Expose
        private String reference;
        @SerializedName("types")
        @Expose
        private List<String> types = null;
        @SerializedName("opening_hours")
        @Expose
        private OpeningHours openingHours;

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public void setFormattedAddress(String formattedAddress) {
            this.formattedAddress = formattedAddress;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public void setOpeningHours(OpeningHours openingHours) {
            this.openingHours = openingHours;
        }

    }


    public class Southwest {

        @SerializedName("lat")
        @Expose
        private float lat;
        @SerializedName("lng")
        @Expose
        private float lng;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

    }


    public class Viewport {

        @SerializedName("northeast")
        @Expose
        private Northeast northeast;
        @SerializedName("southwest")
        @Expose
        private Southwest southwest;

        public Northeast getNortheast() {
            return northeast;
        }

        public void setNortheast(Northeast northeast) {
            this.northeast = northeast;
        }

        public Southwest getSouthwest() {
            return southwest;
        }

        public void setSouthwest(Southwest southwest) {
            this.southwest = southwest;
        }

    }
}