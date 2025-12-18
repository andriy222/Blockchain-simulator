package blockchain;

import java.security.*;

class MessageSender extends Thread {
    private final BlockChain blockChain;
    private final User user;

    public MessageSender(User user, BlockChain blockChain) throws Exception {
        this.user = user;
        this.blockChain = blockChain;
    }

    public void run() {
        String[] messages = {
                "Hey, I'm first!",
                "It's not fair!",
                "You're welcome :)",
                "Nice chat!",
                "Thank you!",
                "Great blockchain!",
                "Amazing!",
                "So cool!"
        };

        for (String msg : messages) {
            try {
                byte[] signature = SignatureUtil.sign(msg, user.getPrivateKey());
                Message message = new Message(user.getName(), msg, signature, user.getPublicKey());
                blockChain.addMessage(message);
                Thread.sleep(10);
            } catch (Exception e) {
                break;
            }

            if (blockChain.getSize() >= 5) {
                break;
            }
        }
    }
}