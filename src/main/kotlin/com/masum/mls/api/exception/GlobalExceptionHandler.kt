package com.masum.mls.api.exception

import com.masum.mls.api.dto.ApiResponse
import com.masum.mls.api.dto.ErrorDetail
import com.masum.mls.api.util.buildMeta
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    // ✅ Custom App Exception
    @ExceptionHandler(AppException::class)
    fun handleAppException(
        ex: AppException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        val errorCode = ex.errorCode

        return ResponseEntity
            .status(errorCode.status)
            .body(
                ApiResponse(
                    success = false,
                    message = ex.message ?: errorCode.message,
                    code = errorCode.code,
                    data = null,
                    metaData = buildMeta(request),
                )
            )
    }

    // ✅ Validation (@Valid body)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
        ): ResponseEntity<ApiResponse<Nothing>> {
        val errors = ex.bindingResult.allErrors.map {
            val fieldError = it as FieldError

            ErrorDetail(
                field = fieldError.field,
                message = fieldError.defaultMessage ?: "invalid",
                rejectedValue = fieldError.rejectedValue
            )
        }

        val errorCode = ErrorCode.VALIDATION_ERROR

        return ResponseEntity
            .status(errorCode.status)
            .body(
                ApiResponse(
                    success = false,
                    message = "Validation failed",
                    code = errorCode.code,
                    errors = errors,
                    metaData = buildMeta(request),
                )
            )
    }

    // ✅ Validation (query param / path variable)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraint(
        ex: ConstraintViolationException,
        request: HttpServletRequest
        ): ResponseEntity<ApiResponse<Nothing>> {
        val errors = ex.constraintViolations.map {
            ErrorDetail(
                field = it.propertyPath.toString(),
                message = it.message,
                rejectedValue = it.invalidValue
            )
        }

        val errorCode = ErrorCode.VALIDATION_ERROR

        return ResponseEntity
            .status(errorCode.status)
            .body(
                ApiResponse(
                    success = false,
                    message = "Validation failed",
                    code = errorCode.code,
                    errors = errors,
                    metaData = buildMeta(request),
                )
            )
    }

    // ✅ Fallback (Unhandled error)
    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception, request: HttpServletRequest): ResponseEntity<ApiResponse<Nothing>> {
        ex.printStackTrace() // FIXME nanti ganti logger

        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR

        return ResponseEntity
            .status(errorCode.status)
            .body(
                ApiResponse(
                    success = false,
                    message = errorCode.message,
                    code = errorCode.code,
                    metaData = buildMeta(request),
                )
            )
    }
}