package fr.funixgaming.api.core.utils.string;

public class StringComparators {

    /**
     * This method works by performing a bitwise XOR of each corresponding byte in the two arrays, and ORing the results together.
     * If the result is non-zero, it means that the arrays are not equal, and the method returns false. If the result is zero,
     * it means that the arrays are equal, and the method returns true.
     * The key characteristic of this method is that the time it takes to execute is constant and does not depend on the contents of
     * the arrays, making it timing-safe.
     * Generated by ChatGPT
     * @param a first byte array
     * @param b second byte array
     * @return returns if the arrays are equals in safe time way
     */
    public static boolean timingSafeEqual(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }

        return result == 0;
    }

}
