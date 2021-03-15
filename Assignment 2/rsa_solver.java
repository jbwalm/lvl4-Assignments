import java.util.*;
import java.math.BigInteger;

public class rsa_solver{

    public static void main(String[] args){
        // Magic Numbers, sorry...
        BigInteger n = new BigInteger("96552920362271189");
        BigInteger e = new BigInteger("4260983");
        BigInteger c = new BigInteger("33882695944926540");
        BigInteger p = new BigInteger("2");
        BigInteger m = new BigInteger("0");
        BigInteger q, temp, primeMinOne;
        
        boolean searching = true;
        while(searching){
            //get first prime value set.
            q = n.divide(p);
            primeMinOne = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

            // gets a message.
            temp = potMessage(primeMinOne, e, n, c);

            //checks that message is worth continuing.
            if(m.compareTo(temp) != 0){
                m = temp;

                // Tests cipher against known cipher.
                searching = testCipher(m, c, e, n);
                if(!searching){
                    System.out.println(p + " and " + q);
                }
            }

            //get next prime and keep searching.
            p = p.nextProbablePrime();
        }

        System.out.println("Message: " + m);
    }

    // Generate a possible decrypted message.
    public static BigInteger potMessage(BigInteger primeMinOne, BigInteger e, BigInteger n, BigInteger c){
        BigInteger m = new BigInteger("0");
        BigInteger d;

        if(e.gcd(primeMinOne).equals(BigInteger.ONE)){
            //Creat decryption key create potential message.
            d = e.modInverse(primeMinOne);
            m = c.modPow(d, n);
        }

        return m;
    }

    //create cipher text from m, compare to known cipher text.
    public static boolean testCipher(BigInteger message, BigInteger cipher, BigInteger e, BigInteger n){
        boolean searching = true;

        // generates possible cipher, compares to known cipher.
        if(cipher.equals(message.modPow(e, n))){
            searching = false;
        }

        return searching;
    }

}