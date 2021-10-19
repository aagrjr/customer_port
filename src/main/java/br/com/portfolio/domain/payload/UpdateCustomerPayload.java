package br.com.portfolio.domain.payload;

import br.com.portfolio.domain.GenderEnum;
import lombok.Data;

@Data
public class UpdateCustomerPayload {
    private String name;
    private GenderEnum gender;
    private String nickname;
    private String email;
}
