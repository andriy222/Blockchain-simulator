package blockchain;

import java.security.*;


public class Message {
    private final String text;
    private final String senderName;
    private final byte[] signature;
    private final PublicKey publicKey;

    public Message(String senderName, String text, byte[] signature, PublicKey publicKey) {
        this.senderName = senderName;
        this.text = text;
        this.signature = signature;
        this.publicKey = publicKey;
    }

    public String getText() {
        return text;
    }

    public String getSenderName() {
        return senderName;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString() {
        return senderName + ": " + text;
    }
}