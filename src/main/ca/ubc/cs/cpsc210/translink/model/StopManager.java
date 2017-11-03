package ca.ubc.cs.cpsc210.translink.model;

import ca.ubc.cs.cpsc210.translink.model.exception.StopException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Manages all bus stops.
 *
 * Singleton pattern applied to ensure only a single instance of this class that
 * is globally accessible throughout application.
 */
// TODO: Task 2: Complete all the methods of this class

public class StopManager implements Iterable<Stop> {
    public static final int RADIUS = 10000;
    private static StopManager instance;
    // Use this field to hold all of the stops.
    // Do not change this field or its type, as the iterator method depends on it
    private Map<Integer, Stop> stopMap;
    private Stop selected;

    /**
     * Constructs stop manager with empty collection of stops and null as the selected stop
     */
    private StopManager() {
        stopMap = new HashMap<>();
        selected = null;
    }

    /**
     * Gets one and only instance of this class
     *
     * @return  instance of class
     */
    public static StopManager getInstance() {
        // Do not modify the implementation of this method!
        if(instance == null) {
            instance = new StopManager();
        }

        return instance;
    }

    public Stop getSelected() {
        return selected;
    }

    /**
     * Get stop with given number, creating it and adding it to the collection of all stops if necessary.
     * If it is necessary to create a new stop, then provide it with an empty string as its name,
     * and a default location somewhere in the lower mainland as its location.
     *
     * In this case, the correct name and location of the stop will be provided later
     *
     * @param number  the number of this stop
     *
     * @return  stop with given number
     */
    public Stop getStopWithNumber(int number) {
        if (stopMap.keySet().contains(number)) {
            return stopMap.get(number);
        }
        else {
            Stop s = new Stop(number, "", new LatLon(-49.2, 123.2));
            stopMap.put(number, s);
            return s;
        }
    }

    /**
     * Get stop with given number, creating it and adding it to the collection of all stops if necessary,
     * using the given name and location
     *
     * @param number  the number of this stop
     * @param name   the name of this stop
     * @param locn   the location of this stop
     *
     * @return  stop with given number
     */
    public Stop getStopWithNumber(int number, String name, LatLon locn) {
        if (stopMap.keySet().contains(number)) {
            return stopMap.get(number);
        }
        else {
            Stop s = new Stop(number, name, locn);;
            stopMap.put(number, s);
            return s;
        }
    }

    /**
     * Set the stop selected by user
     *
     * @param selected   stop selected by user
     * @throws StopException when stop manager doesn't contain selected stop
     */
    public void setSelected(Stop selected) throws StopException {
        if (stopMap.containsKey(selected.getNumber())) {
            this.selected = stopMap.get(selected.getNumber());
        }
        else {
            throw new StopException("No such stop: " + selected.getNumber() + " " + selected.getName());

        }
    }

    /**
     * Clear selected stop (selected stop is null)
     */
    public void clearSelectedStop() {
        stopMap.put(selected.getNumber(), null);
        this.selected = null;
    }

    /**
     * Get number of stops managed
     *
     * @return  number of stops added to manager
     */
    public int getNumStops() {
        return stopMap.size();
    }

    /**
     * Remove all stops from stop manager
     */
    public void clearStops() {
        stopMap.clear();
    }

    /**
     * Find nearest stop to given point.  Returns null if no stop is closer than RADIUS metres.
     *
     * @param pt  point to which nearest stop is sought
     * @return    stop closest to pt but less than RADIUS away; null if no stop is within RADIUS metres of pt
     */
    public Stop findNearestTo(LatLon pt) {
        double ptLat, ptLon;
        ptLat = pt.getLatitude();
        ptLon = pt.getLongitude();
        Stop near = null;
        double nearDist = RADIUS*RADIUS;
        for (int i : stopMap.keySet()) {
            double ptTempLat = stopMap.get(i).getLocn().getLatitude();
            double ptTempLon = stopMap.get(i).getLocn().getLongitude();
            double latDist = ptTempLat - ptLat;
            latDist = latDist*latDist;
            double lonDist = ptTempLon - ptLon;
            lonDist = lonDist*lonDist;
            double dist = latDist + lonDist;
            if ((dist <= (RADIUS*RADIUS))&&(dist <= nearDist)) {
                near = stopMap.get(i);
                nearDist = dist;
            }
        }
        return near;
    }

    @Override
    public Iterator<Stop> iterator() {
        // Do not modify the implementation of this method!
        return stopMap.values().iterator();
    }
}
