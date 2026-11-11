package com.lunar.navigator.service;

import com.lunar.navigator.model.CandidateSiteDTO;
import com.lunar.navigator.model.SiteTelemetryDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LunarEphemerisService {

    private static final Map<String, Map<String, Object>> SITES = Map.of(
        "shackleton", Map.of("name", "Shackleton Crater Rim", "lat", -89.9, "lon", 0.0, "elev", 1.2, "horizonMask", 1.5),
        "connecting-ridge", Map.of("name", "Connecting Ridge", "lat", -89.4, "lon", 222.0, "elev", 0.8, "horizonMask", 2.1),
        "malapert", Map.of("name", "Malapert Mountain", "lat", -86.0, "lon", 2.7, "elev", 5.0, "horizonMask", -0.5),
        "haworth", Map.of("name", "Haworth Crater Rim", "lat", -87.4, "lon", -5.1, "elev", -0.5, "horizonMask", 3.2)
    );

    public List<CandidateSiteDTO> getAllCandidateSites() {
        List<CandidateSiteDTO> list = new ArrayList<>();
        SITES.forEach((id, data) -> {
            list.add(new CandidateSiteDTO(
                id,
                (String) data.get("name"),
                (double) data.get("lat"),
                (double) data.get("lon"),
                (double) data.get("elev")
            ));
        });
        return list;
    }

    public SiteTelemetryDTO calculateTelemetry(String siteId, String dateStr, int hour) {
        Map<String, Object> site = SITES.getOrDefault(siteId.toLowerCase(), SITES.get("shackleton"));

        double lat = (double) site.get("lat");
        double lon = (double) site.get("lon");
        double elev = (double) site.get("elev");
        double horizonMask = (double) site.get("horizonMask");

        LocalDate date = (dateStr != null && !dateStr.isEmpty()) ? LocalDate.parse(dateStr) : LocalDate.now();
        LocalDateTime dt = date.atTime(hour, 0);
        long epochSeconds = dt.toEpochSecond(ZoneOffset.UTC);

        double dayOffset = (epochSeconds - 1700000000L) / 86400.0;
        double subSolarLat = 1.54 * Math.sin(Math.toRadians((dayOffset / 29.53) * 360.0));
        double subSolarLon = ((dayOffset / 29.53) * 360.0) % 360.0;

        double subEarthLat = 6.68 * Math.sin(Math.toRadians((dayOffset / 27.21) * 360.0));
        double subEarthLon = 8.02 * Math.sin(Math.toRadians((dayOffset / 27.55) * 360.0));

        double sunElevation = calculateElevationAngle(lat, lon, subSolarLat, subSolarLon);
        double sunAzimuth = calculateAzimuthAngle(lat, lon, subSolarLat, subSolarLon);

        double earthElevation = calculateElevationAngle(lat, lon, subEarthLat, subEarthLon);
        double earthAzimuth = calculateAzimuthAngle(lat, lon, subEarthLat, subEarthLon);

        boolean solarPower = sunElevation > horizonMask;
        boolean commActive = earthElevation > 0.5;
        double lineOfSightMargin = Math.round((earthElevation - 0.5) * 100.0) / 100.0;
        int score = calculateSuitabilityScore(sunElevation, earthElevation, horizonMask, elev);

        SiteTelemetryDTO dto = new SiteTelemetryDTO();
        dto.setSiteId(siteId);
        dto.setSiteName((String) site.get("name"));
        dto.setTimestampISO(dt.toInstant(ZoneOffset.UTC).toString());
        dto.setLatitude(lat);
        dto.setLongitude(lon);
        dto.setElevationKm(elev);
        dto.setSunElevationAngle(Math.round(sunElevation * 100.0) / 100.0);
        dto.setSunAzimuthAngle(Math.round(sunAzimuth * 100.0) / 100.0);
        dto.setEarthElevationAngle(Math.round(earthElevation * 100.0) / 100.0);
        dto.setEarthAzimuthAngle(Math.round(earthAzimuth * 100.0) / 100.0);
        dto.setSolarPowerAvailable(solarPower);
        dto.setDirectToEarthCommActive(commActive);
        dto.setLineOfSightMarginDeg(lineOfSightMargin);
        dto.setSuitabilityScore(score);

        List<SiteTelemetryDTO.HourlyWindow> forecast = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            LocalDateTime fDt = date.atTime(h, 0);
            long fEpoch = fDt.toEpochSecond(ZoneOffset.UTC);
            double fDayOffset = (fEpoch - 1700000000L) / 86400.0;

            double fSubSolarLat = 1.54 * Math.sin(Math.toRadians((fDayOffset / 29.53) * 360.0));
            double fSubSolarLon = ((fDayOffset / 29.53) * 360.0) % 360.0;
            double fSubEarthLat = 6.68 * Math.sin(Math.toRadians((fDayOffset / 27.21) * 360.0));
            double fSubEarthLon = 8.02 * Math.sin(Math.toRadians((fDayOffset / 27.55) * 360.0));

            double fSunElev = calculateElevationAngle(lat, lon, fSubSolarLat, fSubSolarLon);
            double fEarthElev = calculateElevationAngle(lat, lon, fSubEarthLat, fSubEarthLon);

            forecast.add(new SiteTelemetryDTO.HourlyWindow(
                h,
                Math.round(fSunElev * 10.0) / 10.0,
                Math.round(fEarthElev * 10.0) / 10.0,
                fSunElev > horizonMask,
                fEarthElev > 0.5
            ));
        }
        dto.setDayForecast(forecast);

        return dto;
    }

    private double calculateElevationAngle(double siteLat, double siteLon, double subLat, double subLon) {
        double phi1 = Math.toRadians(siteLat);
        double phi2 = Math.toRadians(subLat);
        double deltaLambda = Math.toRadians(subLon - siteLon);

        double zenithCos = Math.sin(phi1) * Math.sin(phi2) + Math.cos(phi1) * Math.cos(phi2) * Math.cos(deltaLambda);
        zenithCos = Math.max(-1.0, Math.min(1.0, zenithCos));

        double zenithAngleRad = Math.acos(zenithCos);
        return 90.0 - Math.toDegrees(zenithAngleRad);
    }

    private double calculateAzimuthAngle(double siteLat, double siteLon, double subLat, double subLon) {
        double phi1 = Math.toRadians(siteLat);
        double phi2 = Math.toRadians(subLat);
        double deltaLambda = Math.toRadians(subLon - siteLon);

        double y = Math.sin(deltaLambda) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(deltaLambda);

        double azRad = Math.atan2(y, x);
        return (Math.toDegrees(azRad) + 360.0) % 360.0;
    }

    private int calculateSuitabilityScore(double sunElev, double earthElev, double horizonMask, double siteElevKm) {
        double sunFactor = Math.max(0.0, (sunElev - horizonMask) * 8.0);
        double earthFactor = Math.max(0.0, earthElev * 6.0);
        double elevationBonus = Math.max(0.0, siteElevKm * 3.0);

        int total = (int) Math.round(sunFactor + earthFactor + elevationBonus + 40.0);
        return Math.max(5, Math.min(100, total));
    }
}