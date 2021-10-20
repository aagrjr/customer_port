package br.com.portfolio.service;

import static net.logstash.logback.argument.StructuredArguments.kv;

import br.com.portfolio.exception.AddressNotFoundException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeoLocationService {

    private final String geolocationKey;

    public GeoLocationService(@Value("${local.geolocation.key}") final String geolocationKey) {
        this.geolocationKey = geolocationKey;
    }

    public List<Double> getLatLongByAddress(String address) {
        var context = new GeoApiContext.Builder().apiKey(geolocationKey).build();
        try {
            GeocodingResult[] request = GeocodingApi.newRequest(context).address(address).await();
            LatLng location = request[0].geometry.location;
            log.info("Found custom location to be: {}", kv("location", request[0].formattedAddress));
            return Arrays.asList(location.lat, location.lng);
        } catch (InterruptedException | IOException | ApiException e) {
            Thread.currentThread().interrupt();
            throw new AddressNotFoundException();
        }
    }
}
