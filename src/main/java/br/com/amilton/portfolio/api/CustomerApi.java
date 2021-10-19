package br.com.amilton.portfolio.api;


import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import br.com.amilton.portfolio.domain.payload.CreateCustomerPayload;
import br.com.amilton.portfolio.domain.payload.UpdateCustomerPayload;
import br.com.amilton.portfolio.domain.response.CustomerResponse;
import br.com.amilton.portfolio.domain.response.ErrorResponse;
import br.com.amilton.portfolio.domain.search.CustomerSearchParams;
import br.com.amilton.portfolio.swagger.resource.ApiPageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Person Api")
public interface CustomerApi {

    @ApiOperation(value = "Create new Price")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Invalid payload value(s)"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 409, message = "Price already exists"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    CustomerResponse create(@ApiParam(required = true) @Valid CreateCustomerPayload payload);

    @ApiOperation(value = "Update Price")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 400, message = "Invalid id value or payload value(s)"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Price not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    CustomerResponse update(@ApiParam(value = "Price hexadecimal id", required = true) ObjectId id, @ApiParam(required = true) @Valid UpdateCustomerPayload payload);

    @ApiOperation(value = "Find Price by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Invalid id value"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Price not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    CustomerResponse findById(@ApiParam(value = "Price hexadecimal id", required = true) ObjectId id);

    @ApiOperation(value = "Delete Price by id")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 400, message = "Invalid id value"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Price not found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    void delete(@ApiParam(value = "Price hexadecimal id", required = true) ObjectId id);

    @ApiOperation(value = "Finds all Prices", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of Price returned with success", response = CustomerResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Invalid parameter value was sent", response = ErrorResponse.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "An unexpected error occurred", response = ErrorResponse.class)})
    @ApiPageable
    Page<CustomerResponse> findAll(@ApiIgnore @PageableDefault(direction = Sort.Direction.DESC, sort = "id") Pageable pageable, CustomerSearchParams search);

}
