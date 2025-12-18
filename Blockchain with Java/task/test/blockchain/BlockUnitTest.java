package blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class BlockUnitTest {

    private Block block;
    private BlockChain blockChain;

    @BeforeEach
    void setUp() {
        blockChain = new BlockChain();
        ArrayList<Message> messages = new ArrayList<>();
        ArrayList<Transaction> transactions = new ArrayList<>();
        block = new Block(messages, transactions, "miner1");
    }

    @Test
    void testBlockCreation() {
        assertNotNull(block);
        assertEquals(1, block.getId());
        assertEquals("0", block.getPreviousHash());
        assertEquals("miner1", block.getMinerName());
    }

    @Test
    void testSetId() {
        block.setId(5);
        assertEquals(5, block.getId());
    }

    @Test
    void testSetPreviousHash() {
        block.setPreviousHash("abc123");
        assertEquals("abc123", block.getPreviousHash());
    }

    @Test
    void testSetMinerName() {
        block.setMinerName("miner2");
        assertEquals("miner2", block.getMinerName());
    }

    @Test
    void testGetHash() {
        assertNotNull(block.getHash());
        assertEquals(64, block.getHash().length());
    }

    @Test
    void testSetTime() {
        block.setTime(10);
        assertEquals(10, block.getTime());
    }

    @Test
    void testSetNChangeMessage() {
        block.setNChangeMessage("N was increased to 1");
        assertEquals("N was increased to 1", block.getNChangeMessage());
    }

    @Test
    void testZerosToString() {
        assertEquals("", block.zerosToString(0));
        assertEquals("0", block.zerosToString(1));
        assertEquals("000", block.zerosToString(3));
    }

    @Test
    void testGetStringInput() {
        String input = block.getStringInput();
        assertNotNull(input);
        assertTrue(input.length() > 0);
    }

    @Test
    void testToString() {
        block.setTime(5);
        block.setNChangeMessage("N stays the same");
        String blockString = block.toString();

        assertTrue(blockString.contains("Block:"));
        assertTrue(blockString.contains("Created by: miner1"));
        assertTrue(blockString.contains("miner1 gets 100 VC"));
        assertTrue(blockString.contains("Id: 1"));
        assertTrue(blockString.contains("Block was generating for 5 seconds"));
        assertTrue(blockString.contains("N stays the same"));
    }

    @Test
    void testBlockWithTransactions() throws Exception {
        User alice = new User("Alice");
        User bob = new User("Bob");
        Wallet aliceWallet = new Wallet(alice);

        ArrayList<Transaction> transactions = new ArrayList<>();
        Transaction tx = aliceWallet.createTransaction("Bob", 50);
        transactions.add(tx);

        Block blockWithTx = new Block(new ArrayList<>(), transactions, "miner1");
        String blockString = blockWithTx.toString();

        assertTrue(blockString.contains("Alice sent 50 VC to Bob"));
    }

    @Test
    void testBlockWithNoTransactions() {
        String blockString = block.toString();
        assertTrue(blockString.contains("No transactions"));
    }
}
