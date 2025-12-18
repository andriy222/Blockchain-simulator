package blockchain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() throws Exception {
        User user = new User("Alice");
        assertEquals("Alice", user.getName());
        assertNotNull(user.getPublicKey());
        assertNotNull(user.getPrivateKey());
    }

    @Test
    void testDifferentUsersHaveDifferentKeys() throws Exception {
        User user1 = new User("Alice");
        User user2 = new User("Bob");

        assertNotEquals(user1.getPublicKey(), user2.getPublicKey());
        assertNotEquals(user1.getPrivateKey(), user2.getPrivateKey());
    }

    @Test
    void testSetName() throws Exception {
        User user = new User("Alice");
        user.setName("Bob");
        assertEquals("Bob", user.getName());
    }
}
