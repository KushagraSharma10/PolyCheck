import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PolynomialCheck {

    // Function to evaluate polynomial at a given x
    public static BigInteger evaluatePolynomial(BigInteger x, List<BigInteger[]> points, int k) {
        BigInteger result = BigInteger.ZERO;
        BigInteger mod = BigInteger.valueOf(Long.MAX_VALUE);  // Use the same mod as in interpolation

        // Iterate through each point to calculate Lagrange basis polynomials
        for (int i = 0; i < k; i++) {
            BigInteger xi = points.get(i)[0];
            BigInteger yi = points.get(i)[1];

            BigInteger basis = BigInteger.ONE;

            // Compute the basis polynomial for this point
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = points.get(j)[0];

                    // Numerator: x - xj
                    BigInteger numerator = x.subtract(xj);

                    // Denominator: xi - xj
                    BigInteger denominator = xi.subtract(xj);

                    // Modular inverse of denominator
                    BigInteger denominatorInv = denominator.modInverse(mod);

                    // Basis term = basis * (numerator * denominatorInv) % mod
                    basis = basis.multiply(numerator).multiply(denominatorInv).mod(mod);
                }
            }

            // Add the contribution of the current point
            result = result.add(yi.multiply(basis)).mod(mod);
        }

        return result.mod(mod);  // Return result mod M
    }

    // Function to find incorrect roots
    public static List<Integer> findIncorrectRoots(List<BigInteger[]> points, int k) {
        List<Integer> incorrectRoots = new ArrayList<>();

        for (BigInteger[] point : points) {
            BigInteger x = point[0];
            BigInteger y = point[1];
            BigInteger evaluatedY = evaluatePolynomial(x, points, k);

            if (!y.equals(evaluatedY)) {
                incorrectRoots.add(x.intValue());
            }
        }

        return incorrectRoots;
    }

    public static void main(String[] args) {
        // Second test case points
        List<BigInteger[]> points2 = List.of(
                new BigInteger[]{BigInteger.ONE, decodeValue("28735619723837", 10)},
                new BigInteger[]{BigInteger.valueOf(2), decodeValue("1A228867F0CA", 16)},
                new BigInteger[]{BigInteger.valueOf(3), decodeValue("32811A4AA0B7B", 12)},
                new BigInteger[]{BigInteger.valueOf(4), decodeValue("917978721331A", 11)},
                new BigInteger[]{BigInteger.valueOf(5), decodeValue("1A22886782E1", 16)},
                new BigInteger[]{BigInteger.valueOf(6), decodeValue("28735619654702", 10)},
                new BigInteger[]{BigInteger.valueOf(7), decodeValue("71AB5070CC4B", 14)},
                new BigInteger[]{BigInteger.valueOf(8), decodeValue("122662581541670", 9)},
                new BigInteger[]{BigInteger.valueOf(9), decodeValue("642121030037605", 8)}
        );

        List<Integer> incorrectRoots = findIncorrectRoots(points2, 6);

        System.out.println("Incorrect roots: " + incorrectRoots);
    }

    // Function to decode the value based on the given base
    public static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);  // Use BigInteger for large numbers
    }
}

