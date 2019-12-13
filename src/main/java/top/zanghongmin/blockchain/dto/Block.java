package top.zanghongmin.blockchain.dto;

import java.util.List;

/*
block = {
    'index': 1,
    'timestamp': 1506057125.900785,
    'transactions': [
        {
            'sender': "8527147fe1f5426f9dd545de4b27ee00",
            'recipient': "a77f5cdfa2934df3954a5c7c7da5df1f",
            'amount': 5,
        }
    ],
    'proof': 324984774000,
    'previous_hash': "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
}
 */
public class Block {
    private Integer index;
    private Long timestamp;
    private List<Transaction> transactions;
    private Long proof;
    private String previous_hash;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Long getProof() {
        return proof;
    }

    public void setProof(Long proof) {
        this.proof = proof;
    }

    public String getPrevious_hash() {
        return previous_hash;
    }

    public void setPrevious_hash(String previous_hash) {
        this.previous_hash = previous_hash;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for(Transaction transaction:transactions){
            sb.append(transaction.toString());
        }

        return "Block{" +
                "index=" + index +
                ", timestamp=" + timestamp +
                ", transactions=" + sb +
                ", proof=" + proof +
                ", previous_hash='" + previous_hash + '\'' +
                '}';
    }
}
