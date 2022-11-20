package database;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class BitReader extends RandomAccessFile {
    private static final byte mask = 0x01;
    File f;

    /**
     * Bit reader constructor.
     * @param f file to be read
     * @throws IOException
     */
    public BitReader(File f) throws IOException {
        super(f, "r");
        this.f = f;
    }

    /* escreve do final para o inicio de cada byte sequencialmente no arquivo */
    /**
     * Get each BIT in the file as an array of BYTES because the smallest variable
     * usable in Java is a byte.
     * @return byte array where each byte is one bit of the file
     * @throws IOException
     */
    public byte[] getBits() throws IOException {
        byte[] bits = new byte[((int) length()) * 8];
        byte currentByte, maskAppliedResult, counter;
        long l = 0;

        seek(0);

        while (l < length()) {
            currentByte = readByte();
            for (counter = 0; counter < 8; counter++) {
                maskAppliedResult = (byte) (currentByte & mask);
                bits[((int) l * 8) + 7 - counter] = maskAppliedResult;
                currentByte >>>= 1; // shift-right-logical (>>>=) and not arithmetical (>>=)
            }
            l++;
        }
        return bits;
    }
}