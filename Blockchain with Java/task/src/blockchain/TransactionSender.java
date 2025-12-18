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

            if (balance < 10) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            // Pick a random receiver
            String receiver = receivers[(int) (Math.random() * receivers.length)];
            if (receiver.equals(user.getName())) {
                continue;
            }

            balance = wallet.getBalance(blockChain);

            // Keep at least half of the balance
            int availableBalance = balance / 2;
            if (availableBalance < 5) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            int maxAmount = Math.min(20, availableBalance / 2);
            int amount = (int) (Math.random() * maxAmount) + 1;

            try {
                Transaction transaction = wallet.createTransaction(receiver, amount);
                blockChain.addTx(transaction, user);
            } catch (Exception e) {
                System.out.println("Failed to create transaction: " + e.getMessage());
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

}
