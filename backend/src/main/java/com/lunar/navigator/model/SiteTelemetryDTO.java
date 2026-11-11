package com.lunar.navigator.model;

import java.util.List;

public class SiteTelemetryDTO {
    private String siteId;
    private String siteName;
    private String timestampISO;
    private double latitude;
    private double longitude;
    private double elevationKm;
    private double sunElevationAngle;
    private double sunAzimuthAngle;
    private double earthElevationAngle;
    private double earthAzimuthAngle;
    private boolean isSolarPowerAvailable;
    private boolean isDirectToEarthCommActive;
    private double lineOfSightMarginDeg;
    private int suitabilityScore;
    private List<HourlyWindow> dayForecast;

    public static class HourlyWindow {
        private int hour;
        private double sunElevation;
        private double earthElevation;
        private boolean power;
        private boolean comm;

        public HourlyWindow() {}

        public HourlyWindow(int hour, double sunElevation, double earthElevation, boolean power, boolean comm) {
            this.hour = hour;
            this.sunElevation = sunElevation;
            this.earthElevation = earthElevation;
            this.power = power;
            this.comm = comm;
        }

        public int getHour() { return hour; }
        public void setHour(int hour) { this.hour = hour; }
        public double getSunElevation() { return sunElevation; }
        public void setSunElevation(double sunElevation) { this.sunElevation = sunElevation; }
        public double getEarthElevation() { return earthElevation; }
        public void setEarthElevation(double earthElevation) { this.earthElevation = earthElevation; }
        public boolean isPower() { return power; }
        public void setPower(boolean power) { this.power = power; }
        public boolean isComm() { return comm; }
        public void setComm(boolean comm) { this.comm = comm; }
    }

    public SiteTelemetryDTO() {}

    public String getSiteId() { return siteId; }
    public void setSiteId(String siteId) { this.siteId = siteId; }
    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }
    public String getTimestampISO() { return timestampISO; }
    public void setTimestampISO(String timestampISO) { this.timestampISO = timestampISO; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public double getElevationKm() { return elevationKm; }
    public void setElevationKm(double elevationKm) { this.elevationKm = elevationKm; }
    public double getSunElevationAngle() { return sunElevationAngle; }
    public void setSunElevationAngle(double sunElevationAngle) { this.sunElevationAngle = sunElevationAngle; }
    public double getSunAzimuthAngle() { return sunAzimuthAngle; }
    public void setSunAzimuthAngle(double sunAzimuthAngle) { this.sunAzimuthAngle = sunAzimuthAngle; }
    public double getEarthElevationAngle() { return earthElevationAngle; }
    public void setEarthElevationAngle(double earthElevationAngle) { this.earthElevationAngle = earthElevationAngle; }
    public double getEarthAzimuthAngle() { return earthAzimuthAngle; }
    public void setEarthAzimuthAngle(double earthAzimuthAngle) { this.earthAzimuthAngle = earthAzimuthAngle; }
    public boolean isSolarPowerAvailable() { return isSolarPowerAvailable; }
    public void setSolarPowerAvailable(boolean solarPowerAvailable) { isSolarPowerAvailable = solarPowerAvailable; }
    public boolean isDirectToEarthCommActive() { return isDirectToEarthCommActive; }
    public void setDirectToEarthCommActive(boolean directToEarthCommActive) { isDirectToEarthCommActive = directToEarthCommActive; }
    public double getLineOfSightMarginDeg() { return lineOfSightMarginDeg; }
    public void setLineOfSightMarginDeg(double lineOfSightMarginDeg) { this.lineOfSightMarginDeg = lineOfSightMarginDeg; }
    public int getSuitabilityScore() { return suitabilityScore; }
    public void setSuitabilityScore(int suitabilityScore) { this.suitabilityScore = suitabilityScore; }
    public List<HourlyWindow> getDayForecast() { return dayForecast; }
    public void setDayForecast(List<HourlyWindow> dayForecast) { this.dayForecast = dayForecast; }
}