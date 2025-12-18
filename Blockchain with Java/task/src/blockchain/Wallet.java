package blockchain;

public class Wallet {
    private final User owner;

    public Wallet(User owner) {
        this.owner = owner;
    }

    public Transaction createTransaction(String receiver, int amount) throws Exception {
        try {
            String txData = owner.getName() + receiver + amount;
            byte[] signature = SignatureUtil.sign(txData, owner.getPrivateKey());
            return new Transaction(owner.getName(), receiver,amount, signature, owner.getPublicKey());
        } catch (Exception e) {
            throw new Exception("Transaction Failed");
        }
    }

    public int getBalance(BlockChain blockChain) {
            return blockChain.getBalance(owner);
    }
}
