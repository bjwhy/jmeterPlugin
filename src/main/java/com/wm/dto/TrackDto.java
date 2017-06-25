package com.wm.dto;

/**
 * Created by peekaboo on 2016/3/21.
 */
public class TrackDto {

    String[] lons;
    String[] lats;
    String[] sysTimes;
    String[] gpsTimes;
    String[] localTimes;
    String userId;
    String trackId;

    public String[] getLons() {
        return lons;
    }

    public void setLons(String[] lons) {
        this.lons = lons;
    }

    public String[] getLats() {
        return lats;
    }

    public void setLats(String[] lats) {
        this.lats = lats;
    }

    public String[] getSysTimes() {
        return sysTimes;
    }

    public void setSysTimes(String[] sysTimes) {
        this.sysTimes = sysTimes;
    }

    public String[] getGpsTimes() {
        return gpsTimes;
    }

    public void setGpsTimes(String[] gpsTimes) {
        this.gpsTimes = gpsTimes;
    }

    public String[] getLocalTimes() {
        return localTimes;
    }

    public void setLocalTimes(String[] localTimes) {
        this.localTimes = localTimes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
}
