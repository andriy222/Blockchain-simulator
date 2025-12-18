package blockchain;

public class TransactionSender extends  Thread {
    private  BlockChain blockChain;
    private  Wallet wallet;
    private User user;
   public TransactionSender(BlockChain blockChain, Wallet wallet, User user) {
       this.wallet = wallet;
       this.blockChain = blockChain;
       this.user = user;
   }

    @Override
    public void run() {

        Object[][] transactions = {
                {"Alice", 10},
                {"Bob", 5},
                {"Charlie", 7}
        };

        for (Object[] tx : transactions) {
            String receiverName = (String) tx[0];
            int amount = (int) tx[1];
            if (wallet.getBalance(blockChain) < amount) break;
            try {
                Transaction transaction = wallet.createTransaction(receiverName, amount);
                blockChain.addTx(transaction, user);
            } catch (Exception e) {
                System.out.println("Failed to create transaction: " + e.getMessage());
                break;
            }


            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
