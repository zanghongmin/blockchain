package top.zanghongmin.blockchain.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import top.zanghongmin.blockchain.core.QueryHead;
import top.zanghongmin.blockchain.core.ReturnT;
import top.zanghongmin.blockchain.dto.RegisterNodeRequest;
import top.zanghongmin.blockchain.dto.TransactionsnewRequest;
import top.zanghongmin.blockchain.service.APIService;

import javax.validation.Valid;

@Api(value = "区块链API", description = "区块链API")
@Controller
@RequestMapping("/api")
public class APIController {
    @Autowired
    private APIService apiService;

    @ApiOperation(value = "向区块中创建一个新的交易")
    @PostMapping(value="/transactions/new",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnT<String> transactionsnew(@Valid @ModelAttribute TransactionsnewRequest transactionsnewRequest){
        int index = apiService.new_transaction(transactionsnewRequest.getSender(),transactionsnewRequest.getRecipient(),transactionsnewRequest.getAmount());
        return new ReturnT("Transaction will be added to Block (" + index + ")").setCommonHeader(transactionsnewRequest);
    }

    /**
     * 挖矿端点要完成3件事情：
     *     计算工作量证明；
     *     添加一笔包含1个币的奖励矿工的交易；
     *     向区块链中添加一个新的区块。
     * @param queryHead
     * @return
     */
    @ApiOperation(value = "让服务器挖矿一个新的区块")
    @GetMapping(value="/mine",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnT<String> mine(@Valid @ModelAttribute QueryHead queryHead){
        return apiService.mine(queryHead);
    }

    @ApiOperation(value = "返回整个区块链。")
    @GetMapping(value="/chain",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnT<String> chain(@Valid @ModelAttribute QueryHead queryHead){
        return apiService.chain(queryHead);
    }

    @ApiOperation(value = "用来添加相邻节点")
    @GetMapping(value="/nodes/register",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnT<String> register_node(@Valid @ModelAttribute RegisterNodeRequest registerNodeRequest){
        return apiService.register_node(registerNodeRequest);
    }

    @ApiOperation(value = "用来解决共识冲突问题")
    @GetMapping(value="/nodes/resolve",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnT<String> resolve_conflicts(@Valid @ModelAttribute QueryHead queryHead){

        Boolean replaced = apiService.resolve_conflicts();
        if(replaced){
            return new ReturnT("Our chain was replaced").setCommonHeader(queryHead);
        }else{
            return new ReturnT("Our chain is authoritative").setCommonHeader(queryHead);
        }
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value="/test",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnT<String> chtestain(@Valid @ModelAttribute QueryHead queryHead){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://127.0.0.1:8080/api/chain?signature=123456&source=FrontEnd&transeq=123456789",String.class);
        return new ReturnT(responseEntity.getBody()).setCommonHeader(queryHead);
    }

}



