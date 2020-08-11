package com.saikat.project.weatherlocation.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class TempParam {
    private double temp;

    @JsonProperty("main")
    public void setMain(Map<String, Object> main) {
        setTemp(Double.parseDouble(main.get("temp").toString()));
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
