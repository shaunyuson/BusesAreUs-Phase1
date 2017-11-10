package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Bus;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.exception.RouteException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Parser for bus data
public class BusParser {
    /**
     * Parse buses from JSON response produced by TransLink query.  All parsed buses are
     * added to the given stop.  Bus location data that is missing any of the required
     * fields (RouteNo, Latitude, Longitude, Destination, RecordedTime) is silently
     * ignored and not added to stop. Bus that is on route that does not pass through
     * this stop is silently ignored and not added to stop.
     *
     * @param stop            stop to which parsed buses are to be added
     * @param jsonResponse    the JSON response produced by Translink
     * @throws JSONException  when:
     * <ul>
     *     <li>JSON response does not have expected format (JSON syntax problem)</li>
     *     <li>JSON response is not a JSON array</li>
     * </ul>
     */
    public static void parseBuses(Stop stop, String jsonResponse) throws JSONException {
        // TODO: implement this method
        JSONArray buses = new JSONArray(jsonResponse);

        for (int i = 0; i < buses.length(); i++) {
            JSONObject bus = buses.getJSONObject(i);
            Bus b;
            try {
                b = parseBus(bus);
            } catch (JSONException e) {
                continue;
            }
            try {
                stop.addBus(b);
            } catch (RouteException e) {
                // do nothing
            }
        }
    }

    /**
     * Parses bus parsed from JSON object
     * Fields: (RouteNo, Latitude, Longitude, Destination, RecordedTime)
     * @param bus  a JSON object representing a bus
     * @throws JSONException when bus doesn't have complete fields
     */
    private static Bus parseBus(JSONObject bus) throws JSONException {
        String RouteNo = bus.getString("RouteNo");
        Double Latitude = bus.getDouble("Latitude");
        Double Longitude = bus.getDouble("Longitude");
        String Destination = bus.getString("Destination");
        String RecordedTime = bus.getString("RecordedTime");

        return new Bus(RouteManager.getInstance().getRouteWithNumber(RouteNo), Latitude,
                Longitude, Destination, RecordedTime);
    }
}
