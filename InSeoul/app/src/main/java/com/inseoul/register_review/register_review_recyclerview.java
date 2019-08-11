package com.inseoul.register_review;

public class register_review_recyclerview {
    private String placeName;
    private String placeCategory;
    private String placeScore;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceCategory() {
        return placeCategory;
    }

    public void setPlaceCategory(String placeCategory) {
        this.placeCategory = placeCategory;
    }

    public String getPlaceScore() {
        return placeScore;
    }

    public void setPlaceScore(String placeScore) {
        this.placeScore = placeScore;
    }

    public register_review_recyclerview(String placeName, String placeCategory, String placeScore) {
        this.placeName = placeName;
        this.placeCategory = placeCategory;
        this.placeScore = placeScore;
    }
}
