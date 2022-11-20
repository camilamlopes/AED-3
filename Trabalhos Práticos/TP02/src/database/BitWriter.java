package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitWriter extends FileOutputStream {
    byte buffer;
    byte pos;

    /**
     * Bit writer constructor.
     * @param file file to be written in
     * @throws FileNotFoundException
     */
    public BitWriter(File file) throws FileNotFoundException {
        super(file);
        buffer = 0x00;
        pos = 0x00;
    }

    /**
     * Write a single bit to the buffer and flushes it if completed.
     * @param bit a byte with a value of 0 or 1 representing a bit
     * @throws IOException
     */
    public void write(byte bit) throws IOException {
        if (!(bit == 0 || bit == 1))
            throw new RuntimeException("ERROR: A bit can only be a 1 or 0.");

        buffer = (byte) (buffer << 1);
        buffer |= bit;

        if (++pos == 8)
            flushByte();
    }

    public void write(byte[] bits) throws IOException {
        for (byte bit : bits)
            write(bit);
    }

    public void close() throws IOException {
        if (pos != 0) {
            buffer = (byte) (buffer << (8 - pos));
            flushByte();
        }
        super.close();
    }

    private void flushByte() throws IOException {
        super.write(buffer);
        buffer = 0x00;
        pos = 0;
    }
}