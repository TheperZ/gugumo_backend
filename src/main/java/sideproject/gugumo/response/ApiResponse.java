package sideproject.gugumo.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    private String status;
    private T data;
    private String message;

    @Builder
    public ApiResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> createSuccess(T data) {
//        return new ApiResponse<>(SUCCESS_STATUS, data, null);
        return ApiResponse.<T>builder()
                .status(SUCCESS_STATUS)
                .data(data)
                .message(null)
                .build();
    }

    public static <T> ApiResponse<T> createSuccess() {

        //return new ApiResponse<>(SUCCESS_STATUS, null, null);
        return ApiResponse.<T>builder()
                .status(SUCCESS_STATUS)
                .data(null)
                .message(null)
                .build();
    }

    public static <T> ApiResponse<T> createFail(T data, String failMessage) {
//        return new ApiResponse<>(FAIL_STATUS, data, failMessage);
        return ApiResponse.<T>builder()
                .status(FAIL_STATUS)
                .data(data)
                .message(failMessage)
                .build();
    }

    public static <T> ApiResponse<T> createFail(String failMessage) {
//        return new ApiResponse<>(FAIL_STATUS, null, failMessage);
        return ApiResponse.<T>builder()
                .status(FAIL_STATUS)
                .data(null)
                .message(failMessage)
                .build();
    }

}



