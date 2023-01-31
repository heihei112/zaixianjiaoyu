package com.atguigu.guli.service.base.handler;

import com.atguigu.guli.common.base.utils.ExceptionUtils;
import com.atguigu.guli.common.base.result.R;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public R error(Exception e){
        log.error(ExceptionUtils.getMessage(e));
        e.printStackTrace();
        return R.error();
    }

    @ExceptionHandler({BadSqlGrammarException.class})
    @ResponseBody
    public R sqlError(BadSqlGrammarException e){
        log.error(ExceptionUtils.getMessage(e));
        e.printStackTrace();
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }
    @ExceptionHandler({GuliException.class})
    @ResponseBody
    public R error(GuliException e){
        log.error(ExceptionUtils.getMessage(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
