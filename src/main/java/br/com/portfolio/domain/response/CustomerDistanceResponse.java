package br.com.portfolio.domain.response;

import br.com.portfolio.domain.Customer;
import java.text.DecimalFormat;
import lombok.Getter;

@Getter
public class CustomerDistanceResponse extends CustomerResponse {

    private String distance;

    public CustomerDistanceResponse(Customer customer, double distance) {
        super(customer);
        this.distance = formatDistance(distance);
    }

    private String formatDistance(double distance) {
        DecimalFormat df = new DecimalFormat("#.###");
        return df.format(distance);
    }
}
