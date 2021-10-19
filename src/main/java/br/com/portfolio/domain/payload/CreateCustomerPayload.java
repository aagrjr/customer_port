package br.com.portfolio.domain.payload;

import br.com.portfolio.component.ValidBirthDate;
import br.com.portfolio.domain.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class CreateCustomerPayload {

    @ApiModelProperty(value = "Customer's name.", required = true)
    @Size(max = 120, message = "{Customer.name.size}")
    @NotBlank(message = "{Customer.name.notBlank}")
    private String name;
    @ApiModelProperty(value = "Customer's name.", required = true, example = "MALE/FEMALE")
    private GenderEnum gender;

    @ApiModelProperty(value = "Customer's birth date, following rules about user's age.")
    @ValidBirthDate(value = 0)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @ApiModelProperty(value = "Customer's document number. May be CPF or PASSPORT.", required = true)
    @NotBlank(message = "{Customer.documentNumber.notBlank}")
    @CPF(message = "{Customer.documentNumber.isInvalid}")
    private String documentNumber;

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
