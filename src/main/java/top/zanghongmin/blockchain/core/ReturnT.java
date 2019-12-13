package top.zanghongmin.blockchain.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;

/**
 * 统一返回对象
 */
//@JsonInclude(value=JsonInclude.Include.NON_ABSENT)
@ApiModel(value="统一返回对象")
public class ReturnT<T> implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ReturnT.class);
    public static final long serialVersionUID = 42L;
    public static final int FAIL_CODE = ReturnTEnum.HTTP_ERROR_500.getCode();
    public static final ReturnT<String> SUCCESS = new ReturnT<String>(null);
    public static final ReturnT<String> FAIL = new ReturnT<String>(ReturnTEnum.HTTP_ERROR_500,null);

    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                jsonGenerator.writeString("");
            }
        });
    }


	@ApiModelProperty(name= "code", value = "返回状态码 200为成功，其他为错误，具体看状态码表",example = "200",required = true)
	private int code;
    @ApiModelProperty(name= "msg", value = "返回状态描述",required = true)
	private String msg;
    @ApiModelProperty(name= "source", value = "来源系统，该值为请求头source字段",required = true)
    private String source;
    @ApiModelProperty(name= "transeq", value = "请求流水号，该值为请求头transeq字段",required = true)
    private String transeq;
    @ApiModelProperty(name= "data", value = "具体的返回内容",required = true)
	private T data;

    public ReturnT(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

	public ReturnT(ReturnTEnum resultStatusCode, T data) {
		this.code = resultStatusCode.getCode();
		this.msg = resultStatusCode.getMsg();
		this.data = data;
	}

	public ReturnT(T data) {
		this.code =  ReturnTEnum.OK.getCode();
        this.msg =  ReturnTEnum.OK.getMsg();
		this.data = data;
	}
    public ReturnT() {
    }

    /**
     * 设置通用返回头
     */
    public ReturnT setCommonHeader(QueryHead queryHead) {
        this.source = queryHead.getSource();
        this.transeq = queryHead.getTranseq();
        if(this.code==ReturnTEnum.OK.getCode()){
            try {
                logger.info("返回成功结果,来源系统:{},请求流水号:{},状态码:{},状态描述:{},返回内容:{}",this.source,this.transeq,this.code,this.msg,objectMapper.writeValueAsString(this.data));
            } catch (JsonProcessingException e) {
                logger.error("返回成功结果日志解析失败:{}",e.getMessage());
            }
        }else{
            try {
                logger.info("返回失败结果,来源系统:{},请求流水号:{},状态码:{},状态描述:{},返回内容:{}",this.source,this.transeq,this.code,this.msg,objectMapper.writeValueAsString(this.data));
            } catch (JsonProcessingException e) {
                logger.error("返回成功结果日志解析失败:{}",e.getMessage());
            }
        }
        return this;
    }
    /**
     * 设置通用返回头
     */
    public ReturnT setCommonHeader(String source, String transeq) {
        QueryHead queryHead = new QueryHead();
        queryHead.setSource(source);
        queryHead.setTranseq(transeq);
        return setCommonHeader(queryHead);
    }


	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTranseq() {
        return transeq;
    }

    public void setTranseq(String transeq) {
        this.transeq = transeq;
    }



}
