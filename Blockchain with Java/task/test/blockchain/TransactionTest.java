package blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private User alice;
    private Wallet aliceWallet;

    @BeforeEach
    void setUp() throws Exception {
        alice = new User("Alice");
        aliceWallet = new Wallet(alice);
    }

    @Test
    void testTransactionCreation() throws Exception {
        Transaction tx = aliceWallet.createTransaction("Bob", 50);

        assertNotNull(tx);
        assertEquals("Alice", tx.getSenderName());
        assertEquals("Bob", tx.getReceiverName());
        assertEquals(50, tx.getAmount());
    }

    @Test
    void testTransactionHasSignature() throws Exception {
        Transaction tx = aliceWallet.createTransaction("Bob", 50);
        assertNotNull(tx.getSignature());
        assertTrue(tx.getSignature().length > 0);
    }

    @Test
    void testTransactionHasPublicKey() throws Exception {
        Transaction tx = aliceWallet.createTransaction("Bob", 50);
        assertNotNull(tx.getSenderPublicKey());
        assertEquals(alice.getPublicKey(), tx.getSenderPublicKey());
    }

    @Test
    void testTransactionToString() throws Exception {
        Transaction tx = aliceWallet.createTransaction("Bob", 50);
        String expected = "Alice sent 50 VC to Bob";
        assertEquals(expected, tx.toString());
    }

    @Test
    void testDifferentTransactionsHaveDifferentSignatures() throws Exception {
        Transaction tx1 = aliceWallet.createTransaction("Bob", 30);
        Transaction tx2 = aliceWallet.createTransaction("Charlie", 40);

        assertNotEquals(tx1.getSignature(), tx2.getSignature());
    }
}
