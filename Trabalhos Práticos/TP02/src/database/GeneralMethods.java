package database;

public class GeneralMethods {

    /**
     * Get integer value of BIT array (written as a BYTE array) given the
     * starting position (inclusive) and the exclusive end.
     * 
     * @param bits BIT array as BYTE array
     * @param start starting position inclusive
     * @param end end exclusive
     * @return integer value of bits [start : end - 1]
     * @throws IndexOutOfBoundsException
     */
    public static int getBitsValue(byte[] bits, int start, int end) throws IndexOutOfBoundsException {
        if (start > end || start > bits.length || end > bits.length || start < 0) {
            throw new IndexOutOfBoundsException();
        }
        int result = 0, pos = 0;
        for (int i = end - 1; i >= start; i--) {
            result += bits[i] * (int) Math.pow(2, pos++);
        }
        return result;
    }
}
