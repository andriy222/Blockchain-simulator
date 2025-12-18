package blockchain;

public class Miner extends Thread {
    private final int id;
    private final BlockChain blockChain;
    private final String miner;
    public Miner(int id, String miner, BlockChain blockChain) {
        this.miner = miner;
        this.id = id;
        this.blockChain = blockChain;

    }

    public String getMiner() {
        return miner;
    }

    public void run() {
        while (blockChain.getSize() < 5) {
            int currentSize = blockChain.getSize();

            Block block = blockChain.createNextBlock(getMiner());
            block.setMinerId(this.id);
            block.setMinerName(this.getMiner());
            boolean mined = block.mineBlock(blockChain.getCurrentN(), blockChain, currentSize);

            if (mined) {
                blockChain.addBlock(block);
            }
        }
    }
}