package top.zanghongmin.blockchain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import top.zanghongmin.blockchain.core.QueryHead;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@ApiModel(value="检验参数合法性")
public class TransactionsnewRequest extends QueryHead implements Serializable {

    private static final long serialVersionUID = 42L;

    @ApiParam(name= "amount", value = "amount", defaultValue = "1",required = true)
    @NotNull(message = "记录id不能为空")
    private Long amount;

    @ApiParam(name= "recipient", value = "recipient", defaultValue = "recipient01",required = true)
    @NotNull(message = "recipient不能为空")
    private String recipient;

    @ApiParam(name= "sender", value = "sender", defaultValue = "sender01",required = true)
    @NotNull(message = "sender不能为空")
    private String sender;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}