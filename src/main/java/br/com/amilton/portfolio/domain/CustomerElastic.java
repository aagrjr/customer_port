package br.com.amilton.portfolio.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "customers")
public class CustomerElastic {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "gender")
    private GenderEnum gender;

    @Field(type = FieldType.Date, format = DateFormat.year_month_day, name = "birthDate")
    private LocalDate birthDate;

    @Field(type = FieldType.Text, name = "nickname")
    private String nickname;

    @Field(type = FieldType.Text, name = "email")
    private String email;
}
