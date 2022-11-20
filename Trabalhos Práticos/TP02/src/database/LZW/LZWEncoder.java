package database.LZW;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.BitWriter;

/**
 * <h2> LZW encoder </h2>
 * <ul>
 *     <li>BIT_LENGTH is the fixed amount of bits used to each value in the dictionary.</li>
 * </ul>
 */
public class LZWEncoder {
    public static int bitLength;

    /***
     * Encode the string using the LZW algorithm with ASCII characters of 0 to 254 (inclusive)
     * as the initial dictionary and writes the result to a new file.
     * 
     * @param name new file path to write encoded string
     * @param string string to be encoded
     * @return amount of bits after encoding
     * @throws IOException
     */
    public static int encode(String name, String string) throws IOException {
        // create file
        File directory = new File("./db/LZW/");
        long lastVersion = directory.list().length + 1;
        File arq = new File(("./db/LZW/" + name + lastVersion  + ".db"));

        int maxTableSize = (int) Math.pow(2, bitLength);
        int tableSize = 255;

        Map<String, Integer> table = new HashMap<>();
        for (int i = 0; i < 255; i++)
            table.put(String.valueOf((char) i), i);

        String initialString = "";
        List<Integer> encodedValues = new ArrayList<>();

        for (char symbol : string.toCharArray()) {
            String concatenatedSymbol = initialString + symbol;
            if (table.containsKey(concatenatedSymbol))
                initialString = concatenatedSymbol;
            else {
                encodedValues.add(table.get(initialString));

                if (tableSize < maxTableSize)
                    table.put(concatenatedSymbol, tableSize++);
                initialString = String.valueOf(symbol);
            }
        }
        if (!initialString.equals(""))
            encodedValues.add(table.get(initialString));

        createLZWFile(arq, encodedValues);
        return encodedValues.size() * bitLength; // size (in bits) after compression
    }

    /***
     * Create .lzw file and write bits of encodedValues in it with bitLength fixed length.
     * 
     * @param fileToWrite file to write encoded values
     * @param encodedValues integers to be written in filepath file
     * @throws IOException
     */
    private static void createLZWFile(File fileToWrite, List<Integer> encodedValues) throws IOException {

        BitWriter bitWriter = new BitWriter(fileToWrite);
        try {
            byte[] bytesCode;
            String aux;
            for (Integer value : encodedValues) {
                aux = Integer.toBinaryString(value);
                
                StringBuilder binString = new StringBuilder();

                binString.append("0".repeat(Math.max(0, (bitLength - aux.length()))));
                binString.append(aux);

                bytesCode = new byte[bitLength];
                for (int i = 0; i < bitLength; i++)
                    bytesCode[i] = binString.charAt(i) == '0' ? (byte) 0 : (byte) 1;
                bitWriter.write(bytesCode);
            }
            bitWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}