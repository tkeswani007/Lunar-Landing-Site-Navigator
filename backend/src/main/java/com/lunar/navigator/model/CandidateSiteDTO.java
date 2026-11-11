package com.lunar.navigator.model;

public class CandidateSiteDTO {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private double elevationKm;

    public CandidateSiteDTO() {}

    public CandidateSiteDTO(String id, String name, double latitude, double longitude, double elevationKm) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevationKm = elevationKm;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public double getElevationKm() { return elevationKm; }
    public void setElevationKm(double elevationKm) { this.elevationKm = elevationKm; }
}