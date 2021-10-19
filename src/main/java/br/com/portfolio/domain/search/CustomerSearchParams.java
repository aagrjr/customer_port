package br.com.portfolio.domain.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchParams {

    @ApiModelProperty("Search by customer's name")
    private String name;

    @ApiModelProperty("Search by customer's document Number")
    private String documentNumber;
}
