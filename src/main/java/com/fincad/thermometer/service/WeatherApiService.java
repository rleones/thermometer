package com.fincad.thermometer.service;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by roberto on 06/05/17.
 */
public interface WeatherApiService {

    Double getWeatherConditions(String location) throws IOException, JSONException;

}
