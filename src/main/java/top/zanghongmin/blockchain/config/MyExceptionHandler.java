package top.zanghongmin.blockchain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import top.zanghongmin.blockchain.core.ReturnT;
import top.zanghongmin.blockchain.core.ReturnTEnum;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Component
public class MyExceptionHandler extends ResponseEntityExceptionHandler   {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ObjectMapper objectMapper;

    /**
    * 参数检验 全局统一处理
    */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        String source = request.getParameter("source");
        String transeq = request.getParameter("transeq");
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ReturnT(ReturnTEnum.BAD_REQUEST.getCode(), result.getAllErrors().get(0).getDefaultMessage()).setCommonHeader(source, transeq));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ReturnT(ReturnTEnum.BAD_REQUEST, null).setCommonHeader(source, transeq));
        }

    /**
    * 统一异常处理
    */
    @ExceptionHandler(value = Exception.class)
    public String defaultErrorHandler(HttpServletRequest request, Exception ex) throws Exception {
        String source = request.getParameter("source");
        String transeq = request.getParameter("transeq");
        ReturnT errorResult = new ReturnT(ReturnT.FAIL_CODE, ex.getMessage()).setCommonHeader(source, transeq);
        logger.error("统一异常处理,来源系统:{},请求流水号:{},请求URI:{},请求方式:{},异常内容:{}", source, transeq, request.getRequestURI(), request.getMethod(), objectMapper.writeValueAsString(errorResult));
        return objectMapper.writeValueAsString(errorResult);
    }
}
