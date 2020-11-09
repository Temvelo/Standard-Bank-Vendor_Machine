package za.co.standardbank.vendingmachine.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private int status;
    private String message;
    private String debugMessage;
    private List<String> errors;

    private ApiError() {
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status.value();
        this.message = status.getReasonPhrase();
    }

    public ApiError(HttpStatus status, String message) {
        this();
        this.status = status.value();
        this.message = message;
    }
}
