package blockchain;
import java.security.PublicKey;

public class Transaction {
    private final String senderName;
    private final String receiverName;
    private final int amount;
    private final byte[] signature;
    private final PublicKey senderPublicKey;

    public Transaction(String senderName, String receiverName, int amount,
                       byte[] signature, PublicKey senderPublicKey) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.amount = amount;
        this.signature = signature;
        this.senderPublicKey = senderPublicKey;
    }

    public String getSenderName() {
        return senderName;
    }

    public byte[] getSignature() {
        return signature;
    }

    public int getAmount() {
        return amount;
    }

    public PublicKey getSenderPublicKey() {
        return senderPublicKey;
    }

    public String getReceiverName() {
        return receiverName;
    }

    @Override
    public String toString() {
        return senderName + " sent " + amount + " VC to " + receiverName;
    }
}