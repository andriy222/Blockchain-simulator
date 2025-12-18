package blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class BlockChainUnitTest {

    private BlockChain blockChain;
    private User alice;
    private User bob;
    private User miner;
    private Wallet aliceWallet;
    private Wallet bobWallet;

    @BeforeEach
    void setUp() throws Exception {
        blockChain = new BlockChain();
        alice = new User("Alice");
        bob = new User("Bob");
        miner = new User("miner1");
        aliceWallet = new Wallet(alice);
        bobWallet = new Wallet(bob);
    }

    @Test
    void testBlockChainCreation() {
        assertNotNull(blockChain);
        assertEquals(0, blockChain.getSize());
    }

    @Test
    void testInitialBalance() {
        assertEquals(100, blockChain.getBalance(alice));
        assertEquals(100, blockChain.getBalance(bob));
    }

    @Test
    void testGetLastHashWhenEmpty() {
        assertEquals("0", blockChain.getLastHash());
    }

    @Test
    void testCreateNextBlock() {
        Block block = blockChain.createNextBlock("miner1");
        assertNotNull(block);
        assertEquals(1, block.getId());
        assertEquals("0", block.getPreviousHash());
    }

    @Test
    void testAddTransaction() throws Exception {
        Transaction tx = aliceWallet.createTransaction("Bob", 50);
        blockChain.addTx(tx, alice);

        int aliceBalance = blockChain.getBalance(alice);
        assertEquals(50, aliceBalance);
    }

    @Test
    void testRejectTransactionWithInsufficientBalance() throws Exception {
        Transaction tx = aliceWallet.createTransaction("Bob", 150);
        blockChain.addTx(tx, alice);

        int aliceBalance = blockChain.getBalance(alice);
        assertEquals(100, aliceBalance);
    }

    @Test
    void testAddBlock() {
        Block block = blockChain.createNextBlock("miner1");
        block.setId(1);
        block.setPreviousHash("0");
        blockChain.addBlock(block);

        assertEquals(1, blockChain.getSize());
    }

    @Test
    void testMinerRewardAfterBlockCreation() {
        Block block = blockChain.createNextBlock("miner1");
        block.setId(1);
        block.setPreviousHash("0");
        blockChain.addBlock(block);

        int minerBalance = blockChain.getBalance(miner);
        assertEquals(200, minerBalance);
    }

    @Test
    void testBalanceAfterTransaction() throws Exception {
        Transaction tx = aliceWallet.createTransaction("Bob", 30);
        blockChain.addTx(tx, alice);

        Block block = blockChain.createNextBlock("miner1");
        block.setId(1);
        block.setPreviousHash("0");
        blockChain.addBlock(block);

        assertEquals(70, blockChain.getBalance(alice));
        assertEquals(130, blockChain.getBalance(bob));
    }

    @Test
    void testBlockValidation() {
        Block validBlock = blockChain.createNextBlock("miner1");
        validBlock.setId(1);
        validBlock.setPreviousHash("0");
        blockChain.addBlock(validBlock);
        assertEquals(1, blockChain.getSize());

        Block invalidBlock = blockChain.createNextBlock("miner1");
        invalidBlock.setId(5);
        invalidBlock.setPreviousHash("wrong_hash");
        blockChain.addBlock(invalidBlock);
        assertEquals(1, blockChain.getSize());
    }

    @Test
    void testGetBlockArrayList() {
        assertEquals(0, blockChain.getBlockArrayList().size());

        Block block = blockChain.createNextBlock("miner1");
        block.setId(1);
        block.setPreviousHash("0");
        blockChain.addBlock(block);

        assertEquals(1, blockChain.getBlockArrayList().size());
    }

    @Test
    void testValidateEmptyBlockchain() {
        assertTrue(blockChain.validate());
    }

    @Test
    void testValidateWithOneBlock() {
        Block block = blockChain.createNextBlock("miner1");
        block.setId(1);
        block.setPreviousHash("0");
        blockChain.addBlock(block);

        assertTrue(blockChain.validate());
    }

    @Test
    void testGetCurrentN() {
        int n = blockChain.getCurrentN();
        assertTrue(n >= 0);
    }
}
