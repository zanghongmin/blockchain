package top.zanghongmin.blockchain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;
import top.zanghongmin.blockchain.core.QueryHead;
import top.zanghongmin.blockchain.core.ReturnT;
import top.zanghongmin.blockchain.dto.Block;
import top.zanghongmin.blockchain.dto.RegisterNodeRequest;
import top.zanghongmin.blockchain.dto.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 实现InitializingBean，重写afterPropertiesSet方法
 */
@Service
public class APIService implements InitializingBean {


    private List<Block> chain;
    private List<Transaction> current_transactions;
    private Set<String> nodes;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;



    /**
     * 用来部署共识机制，解决冲突，确保每一个节点都获得正确的链。
     * 当一个节点和其它节点拥有不同链时，就会产生冲突，为了解决这个问题，我们采用了最长有效链的原则。
     * 换句话，网络中的最长链就是实际的区块链。使用该机制，网络中的节点就能够达成一致。
     *         neighbours = self.nodes
     *         new_chain = None
     *
     *         # We're only looking for chains longer than ours
     *         max_length = len(self.chain)
     *
     *         # Grab and verify the chains from all the nodes in our network
     *         for node in neighbours:
     *             response = requests.get(f'http://{node}/chain')
     *
     *             if response.status_code == 200:
     *                 length = response.json()['length']
     *                 chain = response.json()['chain']
     *
     *                 # Check if the length is longer and the chain is valid
     *                 if length > max_length and self.valid_chain(chain):
     *                     max_length = length
     *                     new_chain = chain
     *
     *         # Replace our chain if we discovered a new, valid chain longer than ours
     *         if new_chain:
     *             self.chain = new_chain
     *             return True
     *
     *         return False
     */
    public Boolean resolve_conflicts(){
        Set<String>  neighbours = nodes;
        List<Block> new_chain = null;
        int max_length = chain.size();
        for(String neighbour:neighbours){
            String httpadress = neighbour + "/chain?signature=123456&source=FrontEnd&transeq=123456789";
            ReturnT returnT = restTemplate.getForObject(httpadress,ReturnT.class);
            if (returnT!=null&&returnT.getCode()==200){

                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Block.class);
                List<Block> rchain  = objectMapper.convertValue(returnT.getData(),listType);
                int length = rchain.size();
                if(length>max_length && valid_chain(rchain)){
                    max_length = length;
                    new_chain = rchain;
                }
            }
        }
        if(new_chain!=null){
            chain = new_chain;
            return true;
        }

        return false;
    }

    private Boolean valid_chain(List<Block> otherchain){
        Block last_block = otherchain.get(0);
        int current_index = 1;
        while (current_index < otherchain.size()){
            Block block = otherchain.get(current_index);
            if(!block.getPrevious_hash().equals(hash(last_block))){
                return false;
            }
            if(!valid_proof(last_block.getProof(),block.getProof())){
                return false;
            }
            last_block = block;
            current_index++;
        }
        return true;
    }


    /**
     * 用来以URLs格式接受新的节点列表；
     * @param registerNodeRequest
     * @return
     */
    public ReturnT<String> register_node(RegisterNodeRequest registerNodeRequest){
        nodes.add(registerNodeRequest.getAddress());
        return  new ReturnT(nodes).setCommonHeader(registerNodeRequest);
    }

    /**
     * 挖矿端点要完成3件事情：
     *     计算工作量证明；
     *     添加一笔包含1个币的奖励矿工的交易；
     *     向区块链中添加一个新的区块。
     * @param queryHead
     * @return
     */
    public ReturnT<String> mine(QueryHead queryHead){
        Block last_block = chain.get(chain.size()-1);
        long last_proof = last_block.getProof();
        long proof = proof_of_work(last_proof);
        new_transaction("0", "we", 1);
        String previous_hash =hash(last_block);
        Block block = new_block(proof, previous_hash);
        return  new ReturnT(block).setCommonHeader(queryHead);
    }

    /**
     * 返回整个区块链
     * @param queryHead
     * @return
     */
    public ReturnT<String> chain(QueryHead queryHead){
        return new ReturnT(chain).setCommonHeader(queryHead);
    }

    /**
     * 向区块中添加交易
     * 需要一种方式来向区块添加交易。new_transaction()方法就是负责实现该目的，而且非常直观：
     * 在new_transaction()添加交易后，它返回接收添加交易的区块的索引，即下一个被挖矿出来的区块的索引。
     * @return
     */
    public int new_transaction(String sender, String recipient, long amount){
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setRecipient(recipient);
        transaction.setSender(sender);
        current_transactions.add(transaction);
        return chain.get(chain.size()-1).getIndex()+1;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        chain = new ArrayList<>();
        current_transactions=new ArrayList<>();
        nodes = new HashSet<>();
        new_block(100, "1");
    }
    /**
     * 创建区块
     * 当Blockchain实例化时，我们需要添加创始区块 – 不包含之前区块信息的第一个区块。我们还需要向创始区块添加一个证明，它就是挖矿或工作量证明（PoW）。
     * 除了在构造器中创建创始区块，我们还需要具体化new_block(), new_transaction() 及hash()方法。
     * @return
     */
    public Block new_block(long proof, String previous_hash){
        Block block = new Block();
        block.setIndex(chain.size()+1);
        block.setTimestamp(System.currentTimeMillis());
        block.setTransactions(current_transactions);
        block.setProof(proof);
        block.setPrevious_hash(previous_hash==null?hash(chain.get(chain.size()-1)):previous_hash);
        current_transactions = new ArrayList<>();
        chain.add(block);
        return block;
    }

    /**
     * hash
     *  block_string = json.dumps(block, sort_keys=True).encode()
     *  return hashlib.sha256(block_string).hexdigest()
     * @param block
     * @return
     */
    public String hash(Block block){
        return DigestUtils.md5DigestAsHex(block.toString().getBytes());
    }


    /**
     * 理解工作量证明
     *
     * 工作量证明PoW机制用于在区块链中创建或挖矿新的区块。PoW的目的是为了找到一个解决问题的数字。该数字必须很难找到，但是又很容易验证 – 网络中的任何人都可以验证。这是PoW背后的核心思想。
     *
     * 举例说明。
     *
     * 假定我们定义整数x 乘以整数y 的哈希值必须要以 0 结尾。因此hash(x * y) = ac23dc...0 。简化期间，我们取x = 5。并在Python中部署：
     *
     * from hashlib import sha256
     * x = 5
     * y = 0  # We don't know what y should be yet...
     * while sha256(f'{x*y}'.encode()).hexdigest()[-1] != "0":
     *     y += 1
     * print(f'The solution is y = {y}')
     *
     * 此时计算结果为 y = 21。因为此时它们乘积的哈希值是以 0结尾。
     *
     * hash(5 * 21) = 1253e9373e...5e3600155e860
     *
     * 比特币中PoW算法为Hashcash。与我们上面所举例的本质上是一样的。通常，其难度是由字符串中搜索的字符数决定。
     * 部署基本的工作量证明
     * 在我们的区块链中，规则与上面举例的类似：
     *     找到一个数字p，当它与之前区块的哈希再次哈希后的数值前4位都是0。
     * @param last_proof
     * @return
     */
    public long proof_of_work(long last_proof){
        long proof = 0;
        while(!valid_proof(last_proof, proof)){
            proof += 1;
        }
        return proof;
    }

    /**
     * 验证工作量证明
     * @param last_proof
     * @param proof
     * @return
     */
    public Boolean valid_proof(long last_proof, long proof){
        String guess = last_proof+""+proof;
        return  DigestUtils.md5DigestAsHex(guess.getBytes()).substring(0,1).equals("0");
    }



}
