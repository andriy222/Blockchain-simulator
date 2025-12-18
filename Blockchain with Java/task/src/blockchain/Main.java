package blockchain;

public class Main {
    public static void main(String[] args) {
        try {
            BlockChain blockChain = new BlockChain();

            User miner1 = new User("miner1");
            User miner2 = new User("miner2");
            User miner3 = new User("miner3");
            User alice = new User("Alice");
            User bob = new User("Bob");
            User charlie = new User("Charlie");
            User shopOwner = new User("ShopOwner");


            Wallet aliceWallet = new Wallet(alice);
            Wallet bobWallet = new Wallet(bob);
            Wallet charlieWallet = new Wallet(charlie);
            Wallet shopWallet = new Wallet(shopOwner);
            Wallet miner1Wallet = new Wallet(miner1);
            Wallet miner2Wallet = new Wallet(miner2);
            Wallet miner3Wallet = new Wallet(miner3);


            Miner[] miners = new Miner[3];
            miners[0] = new Miner(1, miner1.getName(), blockChain);
            miners[1] = new Miner(2, miner2.getName(), blockChain);
            miners[2] = new Miner(3, miner3.getName(), blockChain);

            TransactionSender sender1 = new TransactionSender(blockChain, aliceWallet, alice);
            TransactionSender sender2 = new TransactionSender(blockChain, bobWallet, bob);
            TransactionSender sender3 = new TransactionSender(blockChain, charlieWallet, charlie);
            TransactionSender minerSender1 = new TransactionSender(blockChain, miner1Wallet, miner1);
            TransactionSender minerSender2 = new TransactionSender(blockChain, miner2Wallet, miner2);
            TransactionSender minerSender3 = new TransactionSender(blockChain, miner3Wallet, miner3);

            sender1.start();
            sender2.start();
            sender3.start();

            Thread.sleep(300);

            for (Miner miner : miners) {
                miner.start();
            }

            minerSender1.start();
            minerSender2.start();
            minerSender3.start();


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