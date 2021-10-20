package br.com.portfolio.domain.payload;

import br.com.portfolio.domain.GenderEnum;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCustomerPayload {

    @ApiModelProperty(value = "Customer's name.", required = true)
    @Size(max = 120, message = "{Customer.name.size}")
    @NotBlank(message = "{Customer.name.notBlank}")
    private String name;
    
    @ApiModelProperty(value = "Customer's name.", required = true, example = "MALE/FEMALE")
    private GenderEnum gender;

    @ApiModelProperty(value = "Customer's nickname.")
    @Size(max = 120, message = "{Customer.nickname.size}")
    private String nickname;

    @ApiModelProperty(value = "Customer's associated e-mail address, following requirements.", required = true)
    @Size(max = 80, message = "{Customer.email.size}")
    @Email(message = "{Customer.email.isInvalid}")
    @NotBlank(message = "{Customer.email.notBlank}")
    private String email;

    @ApiModelProperty(value = "Customer's address.", required = true)
    private String address;
}
