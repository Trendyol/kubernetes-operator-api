package com.trendyol.kubernetesoperatorapi.infra.common;

import com.trendyol.kubernetesoperatorapi.domain.exception.BusinessException;
import com.trendyol.kubernetesoperatorapi.domain.exception.ClientApiBusinessException;
import com.trendyol.kubernetesoperatorapi.domain.exception.ClientApiInternalServerException;
import com.trendyol.kubernetesoperatorapi.domain.exception.ClientApiReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerExceptionHandler {

    private static final Locale TR = new Locale("tr");
    private static final String UNEXPECTED_ERROR = "Beklenmeyen bir hata oluştu";

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception ex) {
        var errorDetailDTO = getErrorDetailTO(INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR);

        var errorDTO = ErrorDTO.builder()
                .exception(ex.getClass().getCanonicalName())
                .errorDetail(List.of(errorDetailDTO))
                .build();
        log.error("Exception Caused By: {}", ex.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException matme) {
        var errorDetailDTO = getErrorDetailTO(BAD_REQUEST, matme.getMessage());

        var errorDTO = ErrorDTO.builder()
                .exception(matme.getClass().getCanonicalName())
                .errorDetail(List.of(errorDetailDTO))
                .build();

        log.error("Enum could not found. Caused By: {}", errorDTO);
        return new ResponseEntity<>(errorDTO, BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDTO> handleMissingServletRequestParameterException(MissingServletRequestParameterException msrpe) {
        var errorDetailDTO = getErrorDetailTO(BAD_REQUEST, msrpe.getMessage());

        var errorDTO = ErrorDTO.builder()
                .exception(msrpe.getClass().getCanonicalName())
                .errorDetail(List.of(errorDetailDTO))
                .build();

        log.error("Request parameter could not found. Caused By: {}", errorDTO);
        return new ResponseEntity<>(errorDTO, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException manve) {
        var errorDTO = ErrorDTO.builder()
                .exception(manve.getClass().getCanonicalName())
                .build();

        prepareBindingResult(manve.getBindingResult(), errorDTO);
        log.error("Field validation failed. Caused By: {}", manve.getMessage());
        return new ResponseEntity<>(errorDTO, BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleGeneralBusinessException(BusinessException be) {
        var errorDetailDTO = getErrorDetailTO(BAD_REQUEST, be.getMessage());

        var errorDTO = ErrorDTO.builder()
                .exception(be.getClass().getCanonicalName())
                .errorDetail(List.of(errorDetailDTO))
                .build();
        log.error("GeneralBusinessException Caused By: {}", be.getMessage());
        return new ResponseEntity<>(errorDTO, BAD_REQUEST);
    }

    @ExceptionHandler(ClientApiBusinessException.class)
    public ResponseEntity<ErrorDTO> handleClientApiBusinessException(ClientApiBusinessException cabe) {
        var errorDTO = new ErrorDTO();
        errorDTO.setException(cabe.getException());

        prepareHystrixErrorDetail(cabe.getBaseResponse(), errorDTO);
        log.error("Client Api Business Exception Caused By: {}", cabe.getException());
        return new ResponseEntity<>(errorDTO, BAD_REQUEST);
    }

    @ExceptionHandler(ClientApiReadTimeoutException.class)
    public ResponseEntity<ErrorDTO> handleClientReadTimeoutException(ClientApiReadTimeoutException carte) {
        var errorDTO = new ErrorDTO();
        errorDTO.setException(carte.getException());

        prepareHystrixErrorDetail(carte.getBaseResponse(), errorDTO);
        log.warn("Client Api Read Timeout Exception Caused By: {}", carte.getMessage());
        return new ResponseEntity<>(errorDTO, BAD_REQUEST);
    }

    @ExceptionHandler(ClientApiInternalServerException.class)
    public ResponseEntity<ErrorDTO> handleClientApiInternalServerException(ClientApiInternalServerException caise) {
        var errorDTO = new ErrorDTO();
        errorDTO.setException(caise.getException());

        prepareHystrixErrorDetail(caise.getBaseResponse(), errorDTO);
        log.warn("Client Api Internal Server Exception Caused By: {}", caise.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void prepareHystrixErrorDetail(BaseResponse baseResponse, ErrorDTO errorDTO) {
        baseResponse.getErrorDetailModel().forEach(errorDetail -> {
            var fieldError = new ErrorDetailDTO();
            fieldError.setMessage(getMessage(errorDetail.getKey(), errorDetail.getArgs(), errorDetail.getMessage()));
            fieldError.setKey(errorDetail.getKey());
            fieldError.setArgs(errorDetail.getArgs());
            errorDTO.setErrorDetail(List.of(fieldError));
        });
    }

    private String getMessage(String key, Object[] args, String defaultMessage) {
        return Optional.of(getMessage(() -> messageSource.getMessage(key, args, TR)))
                .filter(StringUtils::isNotBlank)
                .orElse(defaultMessage);
    }

    private String getMessage(Supplier<String> supplier) {
        String message = StringUtils.EMPTY;
        try {
            message = supplier.get();
        } catch (Exception exception) {
            log.warn("Exception occurred : ", exception);
        }
        return message;
    }

    private void prepareBindingResult(BindingResult bindingResult, ErrorDTO errorDTO) {
        bindingResult.getFieldErrors().forEach(i -> {
            var errorDetailDTO = new ErrorDetailDTO();
            errorDetailDTO.setMessage(getMessage(i.getDefaultMessage(), i.getArguments(), StringUtils.EMPTY));
            errorDetailDTO.setKey(i.getDefaultMessage());
            errorDTO.setErrorDetail(List.of(errorDetailDTO));
        });
    }

    private ErrorDetailDTO getErrorDetailTO(HttpStatus httpStatus, String message) {
        return ErrorDetailDTO.builder()
                .message(message)
                .errorCode(String.valueOf(httpStatus.value()))
                .build();
    }
}