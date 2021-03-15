import java.math.BigInteger.*;
import java.math.*;
import java.util.*;

public class difhel{

    public static void main(String[] args){

        BigInteger p = new BigInteger("4639310264362860937");
        BigInteger g = new BigInteger("19");
        BigInteger a = new BigInteger("32609727098235");
        BigInteger b = new BigInteger("326097270981479");

        BigInteger alice = g.modPow(a, p);
        BigInteger bob = g.modPow(b, p);
        System.out.println("Value calculated by Alice to send to Bob: " + alice.toString());
        System.out.println("Value calculated by Bob to send to Alice: " + bob.toString());

        BigInteger bob_result = alice.modPow(b, p);
        BigInteger alice_result = bob.modPow(a, p);

        System.out.println("Shared secret (g**ab):\nComputer by Bob:");
        System.out.println(bob_result.toString());
        System.out.println("Computer by Alice: ");
        System.out.println(alice_result.toString());
        return;
    }

}