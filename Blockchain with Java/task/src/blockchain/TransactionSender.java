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
        String[] receivers = {"Alice", "Bob", "Charlie", "miner1", "miner2", "miner3", "ShopOwner"};

        while (blockChain.getSize() < 15) {
            int balance = wallet.getBalance(blockChain);

            if (balance < 5) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            for (String receiver : receivers) {
                if (receiver.equals(user.getName())) continue;

                int amount = (int) (Math.random() * Math.min(30, balance / 2)) + 1;
                balance = wallet.getBalance(blockChain);

                if (balance < amount) break;

                try {
                    Transaction transaction = wallet.createTransaction(receiver, amount);
                    blockChain.addTx(transaction, user);
                } catch (Exception e) {
                    System.out.println("Failed to create transaction: " + e.getMessage());
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

}
