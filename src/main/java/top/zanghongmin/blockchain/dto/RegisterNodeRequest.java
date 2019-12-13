package top.zanghongmin.blockchain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import top.zanghongmin.blockchain.core.QueryHead;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@ApiModel(value="检验参数合法性")
public class RegisterNodeRequest extends QueryHead implements Serializable {

    private static final long serialVersionUID = 42L;

    @ApiParam(name= "address", value = "address", defaultValue = "http://127.0.0.1:8080",required = true)
    @NotNull(message = "address不能为空")
    private String address;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}