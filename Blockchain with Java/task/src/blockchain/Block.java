package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Block {
    private int id;
    private final long timestamp;
    private long time;
    private long magicNum;
    private String previousHash;
    private String hash;
    private int minerId;
    private String nChangeMessage;
    public List<Message> messages;
    public List<Transaction> tx;
    public String  minerName;

    public Block(List<Message> msgs, List<Transaction> tx, String minerName) {
        this.minerName = minerName;
        this.id = 1;
        this.timestamp = new Date().getTime();
        this.previousHash = "0";
        this.hash = StringUtil.applySha256(getStringInput());
        this.messages = new ArrayList<>(msgs);
        this.tx = new ArrayList<>(tx);
    }

    public String getNChangeMessage() {
        return nChangeMessage;
    }
    public String getMinerName() {
        return minerName;
    }

    public void setMinerName(String minerName) {
        this.minerName = minerName;
    }

    public void setNChangeMessage(String nChangeMessage) {
        this.nChangeMessage = nChangeMessage;
    }
    public String getStringInput() {
        return this.id + this.timestamp + this.previousHash + this.magicNum + this.messages + this.tx;
    }

    public void setMinerId(int minerId) {
        this.minerId = minerId;
    }

    public int getId() {
        return id;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public String getPreviousHash() {
        return previousHash;
    }

    private void calculateHash() {
        this.hash = StringUtil.applySha256(getStringInput());
    }

    public String getHash() {
        return hash;
    }

    public void setId(int newVal) {
        this.id = newVal;
    }

    public void setPreviousHash(String newVal) {
        this.previousHash = newVal;
    }

    public String zerosToString(int numberOfZeros) {
        return "0".repeat(numberOfZeros);
    }


    public boolean mineBlock(int numberOfZeros, BlockChain blockChain, int expectedSize) {
        long startTime = System.currentTimeMillis();
        this.magicNum = 0;
        String zeros = zerosToString(numberOfZeros);

        while (!Thread.currentThread().isInterrupted()) {
            if (magicNum % 500 == 0) {
                if (blockChain.getSize() != expectedSize) {
                    return false;
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }

            calculateHash();
            if (hash.startsWith(zeros)) {
                long endTime = System.currentTimeMillis();
                setTime((endTime - startTime) / 1000);
                return true;
            }
            magicNum++;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Block:\n" +
                "Created by: " + minerName + "\n" +
                minerName + " gets 100 VC\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timestamp + "\n" +
                "Magic number: " + magicNum + "\n" +
                "Hash of the previous block:\n" +
                previousHash + "\n" +
                "Hash of the block:\n" +
                hash + "\n" +
                "Block data:\n" +
                (tx.isEmpty() ? "No transactions\n" :
                        tx.stream()
                                .map(Transaction::toString)
                                .collect(Collectors.joining("\n")) + "\n") +
                "Block was generating for " + time + " seconds\n" +
                nChangeMessage;
    }

}