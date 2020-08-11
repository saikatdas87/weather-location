package com.saikat.project.weatherlocation.model.external;

public class LocationResponse {
    private boolean isArray;
    private Class<? extends GeoCodeResponse[]> arrayType;

    public boolean isArray() {
        return isArray;
    }

    public Class<? extends GeoCodeResponse[]> getArrayType() {
        return arrayType;
    }

    public Class<? extends GeoCodeResponse> getNonArrayType() {
        return nonArrayType;
    }

    private Class<? extends GeoCodeResponse> nonArrayType;

    public LocationResponse(boolean isArray, Class<? extends GeoCodeResponse[]> arrayType, Class<? extends GeoCodeResponse> nonArrayType) {
        this.isArray = isArray;
        this.arrayType = arrayType;
        this.nonArrayType = nonArrayType;
    }
}
