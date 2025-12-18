package blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    private User alice;
    private Wallet aliceWallet;
    private BlockChain blockChain;

    @BeforeEach
    void setUp() throws Exception {
        alice = new User("Alice");
        aliceWallet = new Wallet(alice);
        blockChain = new BlockChain();
    }

    @Test
    void testWalletCreation() {
        assertNotNull(aliceWallet);
    }

    @Test
    void testCreateTransaction() throws Exception {
        Transaction tx = aliceWallet.createTransaction("Bob", 10);

        assertNotNull(tx);
        assertEquals("Alice", tx.getSenderName());
        assertEquals("Bob", tx.getReceiverName());
        assertEquals(10, tx.getAmount());
        assertNotNull(tx.getSignature());
        assertNotNull(tx.getSenderPublicKey());
    }

    @Test
    void testGetBalance() {
        int balance = aliceWallet.getBalance(blockChain);
        assertEquals(100, balance);
    }

    @Test
    void testGetBalanceAfterTransaction() throws Exception {
        User bob = new User("Bob");
        Transaction tx = aliceWallet.createTransaction("Bob", 50);
        blockChain.addTx(tx, alice);

        int balance = aliceWallet.getBalance(blockChain);
        assertEquals(50, balance);
    }
}
