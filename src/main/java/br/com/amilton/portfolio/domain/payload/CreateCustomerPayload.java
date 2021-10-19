package br.com.amilton.portfolio.domain.payload;

import br.com.amilton.portfolio.domain.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCustomerPayload {
    @ApiModelProperty(value = "Customer's name.", required = false)
    @Size(max = 120, message = "{Customer.name.size}")
    @NotBlank(message = "{Customer.name.notBlank}")
    private String name;
    private GenderEnum gender;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;
    private String documentNumber;
    private String nickname;
    private String email;


}
