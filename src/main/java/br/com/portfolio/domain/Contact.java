package br.com.portfolio.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    private String address;
    private List<Double> coordinates;
    @Builder.Default
    private String type = "Point";
}