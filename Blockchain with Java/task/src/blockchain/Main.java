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


            Miner[] miners = new Miner[3];
            miners[0] = new Miner(1, miner1.getName(), blockChain);
            miners[1] = new Miner(2, miner2.getName(), blockChain);
            miners[2] = new Miner(3, miner3.getName(), blockChain);

            for (Miner miner : miners) {
                miner.start();
            }


            Thread.sleep(50);


            TransactionSender sender1 = new TransactionSender(blockChain, aliceWallet, alice);
            TransactionSender sender2 = new TransactionSender(blockChain, bobWallet, bob);
            TransactionSender sender3 = new TransactionSender(blockChain, charlieWallet, charlie);

            sender1.start();
            sender2.start();
            sender3.start();


            while (blockChain.getSize() < 15) {
                Thread.sleep(100);
            }

            for (Miner miner : miners) {
                miner.join();
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