package database.LZW;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.GeneralMethods;
import database.BitReader;

/**
 * <h2> LZW decoder </h2>
 * <ul>
 *     <li>BIT_LENGTH is the fixed amount of bits used to each value in the dictionary.</li>
 * </ul>
 */
public class LZWDecoder {

    public static int bitLength;

    /***
     * Decode the file content using the LZW algorithm with ASCII characters of 0 to 254
     * as the initial dictionary and returns the decoded string.
     * 
     * @param name file to decode
     * @return decoded string
     * @throws IOException
     */
    public static String decode(String name, long version) throws IOException {
        File arq = new File(("./db/LZW/" + name + version  + ".db"));
        
        int maxTableSize = (int) Math.pow(2, bitLength);

        List<Integer> compressedValues = new ArrayList<>();
        int tableSize = 255;

        BitReader bitReader = new BitReader(arq);
        byte[] bits = bitReader.getBits();
        int c = 0;
        while (c < bits.length) {
            // read bitLength bits, parses it as an integer and adds it to the list
            if ((c + bitLength) > bits.length)
                break;
            else
                compressedValues.add(GeneralMethods.getBitsValue(bits, c, (c += bitLength)));
        }

        Map<Integer, String> table = new HashMap<>();
        for (int i = 0; i < 255; i++)
            table.put(i, String.valueOf((char) i));

        String compressedValueDecoded = table.get(compressedValues.remove(0));
        StringBuilder decodedString = new StringBuilder(compressedValueDecoded);

        String valueFromTable = null;
        for (int key : compressedValues) {
            if (table.containsKey(key))
                valueFromTable = table.get(key);
            else if (key == tableSize)
                valueFromTable = compressedValueDecoded + compressedValueDecoded.charAt(0);

            decodedString.append(valueFromTable);

            if (tableSize < maxTableSize)
                table.put(tableSize++, compressedValueDecoded + valueFromTable.charAt(0));

            compressedValueDecoded = valueFromTable;
        }
        bitReader.close();
        return decodedString.toString();
    }
}