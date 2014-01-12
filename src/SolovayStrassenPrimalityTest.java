import java.math.BigInteger;
import java.util.Random;

/**
 * Solovay-Strassen primality test implementation.
 * 
 * This is not full performance implementation nor it is fully tested.
 * 
 * @author Deximat
 * 
 */
public class SolovayStrassenPrimalityTest {

	// number of iterations will increase probability of correct answer
	private int numberOfIterations = 100;
	private Random randomGenerator = new Random();

	private boolean isPrime(BigInteger numberToTest) {
		// Let's check some base cases

		// zero and one are not prime numbers
		if (numberToTest.equals(BigInteger.valueOf(0))
				|| numberToTest.equals(BigInteger.valueOf(1))) {
			return false;
		}

		// two is only even prime number
		if (numberToTest.equals(BigInteger.valueOf(2))) {
			return true;
		}

		// all other prime numbers are not primes
		if (numberToTest.mod(BigInteger.valueOf(2)).equals(
				BigInteger.valueOf(0))) {
			return false;
		}
		BigInteger numberToTestMinusOne = numberToTest.subtract(BigInteger.ONE);
		// this will always be integer because it is odd number - 1 and then divided by 2
		BigInteger numberToTestMinusOneDiv2 = numberToTestMinusOne
				.divide(BigInteger.valueOf(2));
		// if we reach here we should determine if odd number is prime or not
		for (int i = 0; i < numberOfIterations; i++) {
			// we need a random big int
			BigInteger randomNumber = new BigInteger(numberToTest.bitLength(),
					randomGenerator);
			// random number must be in following range [1, numberToTest - 1]
			randomNumber = randomNumber.mod(numberToTestMinusOne).add(BigInteger.ONE);
			// calculate jacobi
			BigInteger jacobiByMod = calculateJacobi(randomNumber, numberToTest)
					.add(numberToTest).mod(numberToTest);
			// this could be implemented in log n
			BigInteger y = modPowerDummy(randomNumber,
					numberToTestMinusOneDiv2, numberToTest);
			// check for Euler equality
			if (jacobiByMod.equals(BigInteger.ZERO) || !jacobiByMod.equals(y)) {
				return false;
			}
		}
		return true;
	}

	private BigInteger modPowerDummy(BigInteger base,
			BigInteger exponent, BigInteger mod) {
		BigInteger solution = BigInteger.ONE;
		for (BigInteger counter = BigInteger.ZERO; counter.compareTo(exponent) != 0; counter = counter.add(BigInteger.ONE)) {
			solution = solution.multiply(base).mod(mod);
		}
		return solution;
	}

	private BigInteger calculateJacobi(BigInteger a,
			BigInteger b) {
		BigInteger solution = BigInteger.ONE;
		
		while (!a.equals(BigInteger.ZERO)) {
			
			// reduce a to 2^x and some odd k
			while ( a.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
				a = a.divide(BigInteger.valueOf(2));
				// reducing to power of two doesn't change anything but sign
				// only if following rule is satisfied
				BigInteger bMod8 = b.mod(BigInteger.valueOf(8));
				if (bMod8.equals(BigInteger.valueOf(3)) || bMod8.equals(BigInteger.valueOf(5))) {
					solution = solution.negate();
				}
			}
			
			// now we have a without any two factors
			
			// use rule for swapping a and b
			BigInteger temp = a;
			a = b;
			b = temp;
			
			// if we swapped a and b we should invert sign of solution if next rule is satisfied
			if (a.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) && 
					b.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
				solution = solution.negate();
			}
			
			// reduce by moduo of nominator
			a = a.mod(b);
		}
		
		if (b.equals(BigInteger.ONE)) {
			return solution;
		}
		
		return BigInteger.ZERO;
	}

	public static void main(String[] args) {
		SolovayStrassenPrimalityTest primality = new SolovayStrassenPrimalityTest();
		System.out.println(primality.isPrime(BigInteger.valueOf(432432431)));
	}
}
