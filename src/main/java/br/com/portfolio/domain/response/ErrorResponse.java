package br.com.portfolio.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(description = "Represents all error responses in service.")
public class ErrorResponse implements Serializable {

	@ApiModelProperty("An important information about this error (Ex. field name).")
	private String error;

	@ApiModelProperty("Business Error code, that vary due to the context.")
	private String errorCode;

	@ApiModelProperty("A basic description about what happened.")
	private String errorDescription;

	public static ErrorResponse as(String description) {
		return new ErrorResponse().description(description);
	}

	@ApiModelProperty("An inner body, if exists.")
	public ErrorResponse code(String code) {
		this.errorCode = code;
		return this;
	}

	public ErrorResponse tag(String tag) {
		this.error = tag;
		return this;
	}

	public ErrorResponse description(String description) {
		this.errorDescription = description;
		return this;
	}
}

