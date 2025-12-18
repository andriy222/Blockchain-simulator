package blockchain;

public class Main {
    public static void main(String[] args) {
        try {
            BlockChain blockChain = new BlockChain();

            // Create users
            User miner1 = new User("miner1");
            User miner2 = new User("miner2");
            User miner3 = new User("miner3");
            User miner4 = new User("miner4");
            User miner5 = new User("miner5");
            User miner6 = new User("miner6");
            User miner7 = new User("miner7");
            User miner8 = new User("miner8");
            User miner9 = new User("miner9");
            User alice = new User("Alice");
            User bob = new User("Bob");
            User charlie = new User("Charlie");
            User nick = new User("Nick");
            User shopOwner = new User("ShopOwner");
            User worker1 = new User("Worker1");
            User worker2 = new User("Worker2");
            User carShop = new User("CarShop");

            // Create wallets
            Wallet aliceWallet = new Wallet(alice);
            Wallet bobWallet = new Wallet(bob);
            Wallet charlieWallet = new Wallet(charlie);
            Wallet nickWallet = new Wallet(nick);
            Wallet shopWallet = new Wallet(shopOwner);
            Wallet worker1Wallet = new Wallet(worker1);
            Wallet worker2Wallet = new Wallet(worker2);
            Wallet carShopWallet = new Wallet(carShop);
            Wallet miner1Wallet = new Wallet(miner1);
            Wallet miner2Wallet = new Wallet(miner2);
            Wallet miner3Wallet = new Wallet(miner3);
            Wallet miner4Wallet = new Wallet(miner4);
            Wallet miner5Wallet = new Wallet(miner5);
            Wallet miner6Wallet = new Wallet(miner6);
            Wallet miner7Wallet = new Wallet(miner7);
            Wallet miner8Wallet = new Wallet(miner8);
            Wallet miner9Wallet = new Wallet(miner9);


            // Create miners
            Miner[] miners = new Miner[9];
            miners[0] = new Miner(1, miner1.getName(), blockChain);
            miners[1] = new Miner(2, miner2.getName(), blockChain);
            miners[2] = new Miner(3, miner3.getName(), blockChain);
            miners[3] = new Miner(4, miner4.getName(), blockChain);
            miners[4] = new Miner(5, miner5.getName(), blockChain);
            miners[5] = new Miner(6, miner6.getName(), blockChain);
            miners[6] = new Miner(7, miner7.getName(), blockChain);
            miners[7] = new Miner(8, miner8.getName(), blockChain);
            miners[8] = new Miner(9, miner9.getName(), blockChain);

            // Create transaction senders
            TransactionSender sender1 = new TransactionSender(blockChain, aliceWallet, alice);
            TransactionSender sender2 = new TransactionSender(blockChain, bobWallet, bob);
            TransactionSender sender3 = new TransactionSender(blockChain, charlieWallet, charlie);
            TransactionSender sender4 = new TransactionSender(blockChain, nickWallet, nick);
            TransactionSender sender5 = new TransactionSender(blockChain, worker1Wallet, worker1);
            TransactionSender sender6 = new TransactionSender(blockChain, worker2Wallet, worker2);
            TransactionSender sender7 = new TransactionSender(blockChain, carShopWallet, carShop);
            TransactionSender minerSender1 = new TransactionSender(blockChain, miner1Wallet, miner1);
            TransactionSender minerSender2 = new TransactionSender(blockChain, miner2Wallet, miner2);
            TransactionSender minerSender3 = new TransactionSender(blockChain, miner3Wallet, miner3);
            TransactionSender minerSender7 = new TransactionSender(blockChain, miner7Wallet, miner7);
            TransactionSender minerSender9 = new TransactionSender(blockChain, miner9Wallet, miner9);

            // Start transaction senders first
            sender1.start();
            sender2.start();
            sender3.start();
            sender4.start();
            sender5.start();
            sender6.start();
            sender7.start();
            minerSender1.start();
            minerSender2.start();
            minerSender3.start();
            minerSender7.start();
            minerSender9.start();

            // Wait for some transactions to be created
            Thread.sleep(50);

            // Start miners
            for (Miner miner : miners) {
                miner.start();
            }


            while (blockChain.getSize() < 15) {
                Thread.sleep(100);
            }

            for (Miner miner : miners) {
                miner.interrupt();
            }

            for (Miner miner : miners) {
                miner.join(1000);
            }


            for (Block block : blockChain.getBlockArrayList()) {
                System.out.println(block);
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}