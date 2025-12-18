package blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockChain {
    private final List<Block> blockArrayList = new ArrayList<>();
    private final List<Message> pendingMessages = new ArrayList<>();
    private List<Message> currentBlockMessages = new ArrayList<>();
    private final List<Transaction> pendingTxs =new ArrayList<>();
    private List<Transaction> currentBlockTxs = new ArrayList<>();
    private   Miner miner;
    private int numberOfZeros = 2;
    private int currentBlockId = 0;


    public int getSize() {
        return this.blockArrayList.size();
    }

    public List<Block> getBlockArrayList() {
        return blockArrayList;
    }

    public int getBalance(User user) {
        int balance = 100;  // ← Локальна змінна!

        for(Block block : blockArrayList) {
            if(Objects.equals(block.getMinerName(), user.getName())) {
                balance += 100;
            }
            for (Transaction tx : block.tx) {  // ← НЕ currentBlockTxs, а block.tx!
                if(Objects.equals(tx.getSenderName(), user.getName())) {
                    balance -= tx.getAmount();
                }
                if(Objects.equals(tx.getReceiverName(), user.getName())) {
                    balance += tx.getAmount();
                }
            }
        }
        return balance;
    }

    public String getLastHash() {
        if (blockArrayList.isEmpty()) {
            return "0";
        }
        return blockArrayList.getLast().getHash();
    }

    public int getCurrentN() {
        return this.numberOfZeros;
    }

    public synchronized void addMessage(Message message)  throws Exception{
        try {
            boolean valid = SignatureUtil.verify(
                    message.getText(),
                    message.getSignature(),
                    message.getPublicKey()
            );

            if (valid) {
                pendingMessages.add(message);
            } else {
                throw new Exception ("Invalid signature! Message rejected from " + message.getSenderName());
            }
        } catch (Exception e) {
                throw new Exception("Failed add Message");
        }
    }

    public synchronized void addTx(Transaction tx, User sender) throws Exception {
        String txData = tx.getSenderName() + tx.getReceiverName() + tx.getAmount();

        boolean validSignature = SignatureUtil.verify(txData, tx.getSignature(), tx.getSenderPublicKey());
        if (!validSignature) {
            System.out.println("Invalid signature! Transaction rejected from " + tx.getSenderName());
            return;
        }

        int balance = getBalance(sender);
        if (tx.getAmount() > balance) {
            System.out.println("Insufficient balance! " + tx.getSenderName() + " has " + balance + " VC but tried to send " + tx.getAmount() + " VC");
            return;
        }

        pendingTxs.add(tx);
    }

    public synchronized void addBlock(Block block) {

        if (block.getId() != blockArrayList.size() + 1) {
            return;
        }

        if (!block.getPreviousHash().equals(getLastHash())) {
            return;
        }

        String zeros = "0".repeat(numberOfZeros);
        if (!block.getHash().startsWith(zeros)) {
            return;
        }

        blockArrayList.add(block);
        pendingMessages.clear();
        currentBlockMessages.clear();
        pendingTxs.clear();
        currentBlockTxs.clear();
        currentBlockId = 0;

        adjustDifficulty(block);
    }


    public synchronized Block createNextBlock(String minerName) {
        int blockId = blockArrayList.size() + 1;

        if (blockId != currentBlockId) {
            currentBlockId = blockId;
            currentBlockMessages = new ArrayList<>(pendingMessages);
            currentBlockTxs = new ArrayList<>(pendingTxs);
        }

        Block block = new Block(
                new ArrayList<>(currentBlockMessages),
                new ArrayList<>(currentBlockTxs),
                minerName
        );
        block.setId(blockId);
        block.setPreviousHash(getLastHash());
        return block;
    }

    private void adjustDifficulty(Block block) {
        long generationTime = block.getTime();

        if (generationTime < 5 && numberOfZeros < 4) {
            numberOfZeros++;
            block.setNChangeMessage("N was increased to " + numberOfZeros);
        } else if (generationTime > 15 && numberOfZeros > 1) {
            numberOfZeros--;
            block.setNChangeMessage("N was decreased by 1");
        } else {
            block.setNChangeMessage("N stays the same");
        }
    }

    public Boolean validate() {
        for (Block block : blockArrayList) {
            if (!Objects.equals(block.getHash(), StringUtil.applySha256(block.getStringInput()))) {
                return false;
            }
        }
        for (int i = 1; i < blockArrayList.size(); i++) {
            Block currentBlock = blockArrayList.get(i);
            Block previousBlock = blockArrayList.get(i - 1);
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }
}