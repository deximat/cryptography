import java.math.BigInteger;
import java.util.Random;


public class MillerRabinPrimalityTest {
	private int numberOfIterations = 100;
	private Random randomGenerator = new Random();
	
	private boolean isPrime(BigInteger numberToTest) {
		// zero and one are not prime numbers
		if (numberToTest.equals(BigInteger.valueOf(0))
				|| numberToTest.equals(BigInteger.valueOf(1))) {
			return false;
		}

		// two is only even prime number
		if (numberToTest.equals(BigInteger.valueOf(2))) {
			return true;
		}
		
		// all other even numbers are not primes
		if (numberToTest.mod(BigInteger.valueOf(2)).equals(
				BigInteger.valueOf(0))) {
			return false;
		}
		
		
		BigInteger numberToTestMinusOne = numberToTest.subtract(BigInteger.ONE);
		// we need to factor out S from prime = 2^ks + 1
		BigInteger s = numberToTestMinusOne;
		
		while (s.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
			s = s.divide(BigInteger.valueOf(2));
		}
		
		for (int i = 0; i < numberOfIterations; i++) {
			BigInteger a = new BigInteger(numberToTestMinusOne.bitCount(), randomGenerator);
			a = a.mod(numberToTestMinusOne).add(BigInteger.ONE);
			BigInteger temp = s;
			BigInteger mod = a.modPow(temp, numberToTest);
			while (!temp.equals(numberToTestMinusOne) && !mod.equals(BigInteger.ONE) && !mod.equals(numberToTestMinusOne)) {
				mod = mod.multiply(mod).mod(numberToTest);
				temp = temp.multiply(BigInteger.valueOf(2));
			}
			if (!mod.equals(numberToTestMinusOne) && temp.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
				return false;
			}
		}
        
        
		return true;
	}
	
	public static void main(String[] args) {
		
		MillerRabinPrimalityTest primalityTest = new MillerRabinPrimalityTest();
		for (int i = 0; i < 100; i++) {
			BigInteger numberToTest = BigInteger.valueOf(i);
			if (primalityTest.isPrime(numberToTest) != numberToTest.isProbablePrime(100) ) {
				System.out.print("[BANG!]");
			}
			System.out.println("" + i +"  is prime by MRPT: " + primalityTest.isPrime(numberToTest) + " java implementation: " + numberToTest.isProbablePrime(100));
		}
	}
}
