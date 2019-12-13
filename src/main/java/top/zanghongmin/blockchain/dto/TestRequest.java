package top.zanghongmin.blockchain.dto;

import top.zanghongmin.blockchain.core.QueryHead;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


@ApiModel(value="检验参数合法性")
public class TestRequest extends QueryHead implements Serializable {

    private static final long serialVersionUID = 42L;

    @ApiParam(name= "id", value = "id", defaultValue = "1",required = true)
    @NotNull(message = "记录id不能为空")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}