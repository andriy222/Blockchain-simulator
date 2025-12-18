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
        String[] receivers = {"Alice", "Bob", "Charlie", "Nick", "miner1", "miner2", "miner3",
                             "miner7", "miner9", "ShopOwner", "Worker1", "Worker2", "CarShop"};

        while (blockChain.getSize() < 15 && !Thread.currentThread().isInterrupted()) {
            try {
                int balance = wallet.getBalance(blockChain);

                if (balance < 5) {
                    Thread.sleep(100);
                    continue;
                }

                String receiver = receivers[(int) (Math.random() * receivers.length)];
                if (receiver.equals(user.getName())) {
                    Thread.sleep(10);
                    continue;
                }

                int availableBalance = (int) (balance * 0.6);
                if (availableBalance < 2) {
                    Thread.sleep(100);
                    continue;
                }

                int maxAmount = Math.min(20, availableBalance);
                int amount = (int) (Math.random() * (maxAmount - 1)) + 1;

                try {
                    Transaction transaction = wallet.createTransaction(receiver, amount);
                    blockChain.addTx(transaction, user);
                } catch (Exception e) {
                    System.out.println("Failed to create transaction: " + e.getMessage());
                }

                Thread.sleep((long) (Math.random() * 30) + 10);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

}
