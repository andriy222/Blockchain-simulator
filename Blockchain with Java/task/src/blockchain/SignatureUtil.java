package blockchain;

import java.security.*;

public class SignatureUtil {

    public static byte[] sign(String msg, PrivateKey privateKey) throws Exception {
        Signature signature  = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(msg.getBytes("UTF-8"));
        return signature.sign();
    }

    public  static boolean verify(String msg, byte[] sign, PublicKey publicKey) throws  Exception {
        Signature signature  = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(msg.getBytes("UTF-8"));
        return signature.verify(sign);
    }
}
