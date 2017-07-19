package com.fincad.thermometer.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Created by roberto on 06/05/17.
 */
@Service
public class OpenWeatherMapApiServiceImpl implements WeatherApiService {

    private static final String APPID_HEADER = "x-api-key";
    private static final String apiKey = "1fedc1fc3aa951f781503336ddafb724"; // APIKey from OpenWeatherMap
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private static final String baseOwmUrl = "http://api.openweathermap.org/data/2.5/";
    private static final Double KELVIN = 273.15;

    @Override
    public Double getWeatherConditions(String location) throws IOException, JSONException {
        JSONObject json = doQuery(String.format("/weather?q=%s", location.replaceAll(" ", "%20")));

        JSONObject main = (JSONObject) json.get("main");

        return (Double) main.get("temp") - KELVIN;
    }

    private JSONObject doQuery (String subUrl) throws JSONException, IOException {
        String responseBody = null;
        HttpGet httpget = new HttpGet (this.baseOwmUrl + subUrl);
        if (this.apiKey != null) {
            httpget.addHeader (APPID_HEADER, this.apiKey);
        }

        HttpResponse response = this.httpClient.execute (httpget);
        InputStream contentStream = null;
        try {
            StatusLine statusLine = response.getStatusLine ();
            if (statusLine == null) {
                throw new IOException (
                        String.format ("Unable to get a response from OWM server"));
            }
            int statusCode = statusLine.getStatusCode ();
            if (statusCode < 200 && statusCode >= 300) {
                throw new IOException (
                        String.format ("OWM server responded with status code %d: %s", statusCode, statusLine));
            }
			/* Read the response content */
            HttpEntity responseEntity = response.getEntity ();
            contentStream = responseEntity.getContent ();
            Reader isReader = new InputStreamReader(contentStream);
            int contentSize = (int) responseEntity.getContentLength ();
            if (contentSize < 0)
                contentSize = 8*1024;
            StringWriter strWriter = new StringWriter (contentSize);
            char[] buffer = new char[8*1024];
            int n = 0;
            while ((n = isReader.read(buffer)) != -1) {
                strWriter.write(buffer, 0, n);
            }
            responseBody = strWriter.toString ();
            contentStream.close ();
        } catch (IOException e) {
            throw e;
        } catch (RuntimeException re) {
            httpget.abort ();
            throw re;
        } finally {
            if (contentStream != null)
                contentStream.close ();
        }
        return new JSONObject (responseBody);
    }

}
