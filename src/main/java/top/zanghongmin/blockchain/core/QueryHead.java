package top.zanghongmin.blockchain.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 统一请求对象
 */
@ApiModel(value="公共请求头")
public class QueryHead implements Serializable {
	public static final long serialVersionUID = 42L;

	@ApiParam(name= "source", value = "来源系统", defaultValue = "FrontEnd",required = true)
	@NotBlank(message = "source来源系统不能为空")
	private String source;
	@ApiParam(name= "transeq", value = "请求流水号", defaultValue = "123456789",required = true)
    @NotBlank(message = "transeq请求流水号不能为空")
	private String transeq;
	@ApiParam(name= "signature", value = "签名字符串", defaultValue = "123456",required = true)
	private String signature;

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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}


}
