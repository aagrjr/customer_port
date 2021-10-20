package br.com.portfolio.service;

import br.com.portfolio.exception.AddressNotFoundException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationService {

    private String geolocationKey;

    public GeoLocationService(@Value("${local.geolocation.key}") final String geolocationKey) {
        this.geolocationKey = geolocationKey;
    }

    public List<Double> getLatLongByAddress(String address) {
        var context = new GeoApiContext.Builder().apiKey(geolocationKey).build();
        try {
            GeocodingResult[] request = GeocodingApi.newRequest(context).address(address).await();
            LatLng location = request[0].geometry.location;
            System.out.println("Found custom location to be: " + request[0].formattedAddress);
            return Arrays.asList(location.lat, location.lng);
        } catch (Exception e) {
            throw new AddressNotFoundException();
        }
    }
}
