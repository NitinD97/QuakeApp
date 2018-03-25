package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */
    private static final String REQUEST_URL= "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */



    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes() {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try
        {

            //  Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            String baseJsonResponse = getJsonFromUrl(REQUEST_URL);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for(int i=0;i<earthquakeArray.length();i++)
            {
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                String magnitude = properties.getString("mag");
                String location = properties.getString("place");
                String time = properties.getString("time");

                Earthquake earthquake=new Earthquake(magnitude,location,time);
                earthquakes.add(earthquake);
            }


        }
        catch (JSONException e)
        {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }



        // Return the list of earthquakes
        return earthquakes;
    }

    private String readFromStream throws IOException (InputStream is)
    {
        StringBuilder output = new StringBuilder();
        if(is != null)
        {
            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (line != null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public String getJsonFromUrl throws IOException(String REQUEST_URL)
    {
        HttpURLConnection urlConnection=null;
        JSONObject finalObject;
        String jsonStream=" ";
        InputStream inputStream=null;
        if(REQUEST_URL == null)
        {
            return jsonStream;
        }

        try{
            URL url= new url(REQUEST_URL);
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000);     /* milliseconds*/
            urlConnection.getConnectTimeout(1500);  /* milliseconds*/
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonStream = readFromStream(inputStream);
            }
        }catch (IOException e){

        }

    }

}