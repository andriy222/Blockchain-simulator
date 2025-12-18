package blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockChain {
    private final List<Block> blockArrayList = new ArrayList<>();
    private final List<Message> pendingMessages = new ArrayList<>();
    private List<Message> currentBlockMessages = new ArrayList<>();
    private final List<Transaction> pendingTxs = new ArrayList<>();
    private List<Transaction> currentBlockTxs = new ArrayList<>();
    private int numberOfZeros = 0;  // Start with difficulty 0, will increase automatically
    private int currentBlockId = 0;


    public int getSize() {
        return this.blockArrayList.size();
    }

    public List<Block> getBlockArrayList() {
        return blockArrayList;
    }

    /**
     * Calculate the balance of a user
     * Everyone starts with 100 VC
     * Miners get 100 VC for each block they create
     * @param user The user to calculate balance for
     * @return The current balance
     */
    public synchronized int getBalance(User user) {
        int balance = 100; // Starting balance

        // Process all confirmed transactions in the blockchain
        for (Block block : blockArrayList) {
            // Miners get 100 VC reward for creating a block
            if (Objects.equals(block.getMinerName(), user.getName())) {
                balance += 100;
            }

            // Process all transactions in the block
            for (Transaction tx : block.tx) {
                if (Objects.equals(tx.getSenderName(), user.getName())) {
                    balance -= tx.getAmount();
                }
                if (Objects.equals(tx.getReceiverName(), user.getName())) {
                    balance += tx.getAmount();
                }
            }
        }

        // Account for pending transactions to prevent double-spending
        for (Transaction tx : pendingTxs) {
            if (Objects.equals(tx.getSenderName(), user.getName())) {
                balance -= tx.getAmount();
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

    /**
     * Add a transaction to the pending transactions pool
     * Validates signature and checks if sender has sufficient balance
     * @param tx The transaction to add
     * @param sender The user sending the transaction
     */
    public synchronized void addTx(Transaction tx, User sender) throws Exception {
        // Verify transaction signature
        String txData = tx.getSenderName() + tx.getReceiverName() + tx.getAmount();
        boolean validSignature = SignatureUtil.verify(txData, tx.getSignature(), tx.getSenderPublicKey());

        if (!validSignature) {
            System.out.println("Invalid signature! Transaction rejected from " + tx.getSenderName());
            return;
        }

        // Check if sender has sufficient balance
        int balance = getBalance(sender);
        if (tx.getAmount() > balance) {
            System.out.println("Insufficient balance! " + tx.getSenderName() +
                             " has " + balance + " VC but tried to send " + tx.getAmount() + " VC");
            return;
        }

        // Add to pending transactions pool
        pendingTxs.add(tx);
    }

    /**
     * Add a validated block to the blockchain
     * Validates block ID, previous hash, and proof of work
     * @param block The block to add
     */
    public synchronized void addBlock(Block block) {
        // Validate block ID (must be sequential)
        if (block.getId() != blockArrayList.size() + 1) {
            return;
        }

        // Validate previous hash (must match last block's hash)
        if (!block.getPreviousHash().equals(getLastHash())) {
            return;
        }

        // Validate proof of work (hash must start with required zeros)
        String zeros = "0".repeat(numberOfZeros);
        if (!block.getHash().startsWith(zeros)) {
            return;
        }

        // Add block to chain
        blockArrayList.add(block);

        // Clear pending data as it's now included in the block
        pendingMessages.clear();
        currentBlockMessages.clear();
        pendingTxs.clear();
        currentBlockTxs.clear();
        currentBlockId = 0;

        // Adjust mining difficulty based on block generation time
        adjustDifficulty(block);
    }


    /**
     * Create the next block for mining
     * Includes pending transactions and messages
     * @param minerName The name of the miner creating this block
     * @return A new block ready to be mined
     */
    public synchronized Block createNextBlock(String minerName) {
        int blockId = blockArrayList.size() + 1;

        // Snapshot pending transactions for this block
        if (blockId != currentBlockId) {
            currentBlockId = blockId;
            currentBlockMessages = new ArrayList<>(pendingMessages);
            currentBlockTxs = new ArrayList<>(pendingTxs);
        }

        // Create new block with current pending data
        Block block = new Block(
                new ArrayList<>(currentBlockMessages),
                new ArrayList<>(currentBlockTxs),
                minerName
        );
        block.setId(blockId);
        block.setPreviousHash(getLastHash());
        return block;
    }

    /**
     * Adjust mining difficulty based on block generation time
     * If blocks are mined too quickly (< 2s), increase difficulty
     * If blocks are mined too slowly (> 5s), decrease difficulty
     * @param block The block that was just added
     */
    private void adjustDifficulty(Block block) {
        long generationTime = block.getTime();

        if (generationTime < 2 && numberOfZeros < 3) {
            numberOfZeros++;
            block.setNChangeMessage("N was increased to " + numberOfZeros);
        } else if (generationTime > 5 && numberOfZeros > 0) {
            numberOfZeros--;
            block.setNChangeMessage("N was decreased to " + numberOfZeros);
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