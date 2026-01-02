package com.lixin.vehical_management.constants;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    @JsonIgnore
    CommonMessage commonMessage;
    int code;
    String message;
    T data;

    /**
     * Set <code>CommonMessage.SUCCESS</code> by default
     */
    public BaseResponse() {
        this.commonMessage = CommonMessage.SUCCESS;
        this.code = this.commonMessage.code;
        this.message = this.commonMessage.message;
    }

    /**
     * Set <code>CommonMessage.SUCCESS</code> by default
     */
    public BaseResponse(T data) {
        this();
        this.data = data;
    }

    /**
     * Freely customize response code & message without conforming to CommonMessage enum
     */
    public BaseResponse(int code, String message) {
        this(code, message, null);
    }

    /**
     * Freely customize response code & message without conforming to CommonMessage enum
     */
    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(CommonMessage commonMessage) {
        this(commonMessage, null);
    }

    public BaseResponse(CommonMessage commonMessage, T data) {
        this.commonMessage = commonMessage;
        this.code = commonMessage.code;
        this.message = commonMessage.message;
        this.data = data;
    }

    public static <T> BaseResponse<T> succeeded(T data) {
        return new BaseResponse<>(data);
    }

    public static <T> BaseResponse<T> failed(T data) {
        return new BaseResponse<>(CommonMessage.FAILED, data);
    }
}

