package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {

    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop             stop to which parsed arrivals are to be added
     * @param jsonResponse    the JSON response produced by Translink
     * @throws JSONException  when:
     * <ul>
     *     <li>JSON response does not have expected format (JSON syntax problem)</li>
     *     <li>JSON response is not an array</li>
     * </ul>
     * @throws ArrivalsDataMissingException  when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse)
            throws JSONException, ArrivalsDataMissingException {
        // TODO: Task 4: Implement this method
        int arrivalCount = 0;
        JSONArray arrivals = new JSONArray(jsonResponse);
        for (int i = 0; i < arrivals.length(); i++) {
            JSONObject arrival = arrivals.getJSONObject(i);
            Route r = RouteManager.getInstance().getRouteWithNumber(arrival.getString("RouteNo"));
            JSONArray schedules = arrival.getJSONArray("Schedules");
            for (int j = 0; j < schedules.length(); j++) {
                try {
                    JSONObject schedule = schedules.getJSONObject(j);
                    int countdown = schedule.getInt("ExpectedCountdown");
                    String status = schedule.getString("ScheduleStatus");
                    String destination = schedule.getString("Destination");
                    Arrival a = new Arrival(countdown, destination, r);
                    a.setStatus(status);
                    stop.addArrival(a);
                    arrivalCount++;
                } catch (JSONException e) {
                    // nothing
                }
            }
        }
        if (arrivalCount == 0) {
            throw new ArrivalsDataMissingException();
        }
    }
}
