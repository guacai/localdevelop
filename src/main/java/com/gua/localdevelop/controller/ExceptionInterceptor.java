package com.gua.localdevelop.controller;
import com.gua.localdevelop.contants.ErrorCode;
import com.gua.localdevelop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class ExceptionInterceptor {

    /**
     * 处理所有未捕获的异常,防止堆栈信息暴露服务器细节
     *
     * @param ex service层发生的异常
     * @return ResultVOVO
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    ResultVO handleRootException(Exception ex) {
        log.error("handleRootException Error ! ", ex);
        return ResultVO.error(ErrorCode.ARGS_EXCEPTION);
    }



    /**
     * 参数异常
     * @param ex 未处理的参数异常
     * @return ResultVOVO
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(OK)
    ResultVO handleMissingParamsException(MissingServletRequestParameterException ex) {
        log.warn("handleMissingParamsException  ! ", ex);
        return ResultVO.error(ErrorCode.ARGS_EXCEPTION);
    }



    /**
     * 参数无效，如JSON请求参数违反约束
     * @param exception 未处理异常
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(OK)
    ResultVO handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("handleMethodArgumentNotValidException  ! ", exception);
        StringBuffer stringBuffer = new StringBuffer();
        for (FieldError error : exception.getFieldErrors()) {
            stringBuffer.append(error.getDefaultMessage()).append(";");
        }
        return ResultVO.error(ErrorCode.ARGS_EXCEPTION);
    }

}

