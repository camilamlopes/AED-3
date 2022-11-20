package database.Huffman;

public class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency;

    /**
     * Huffman tree constructor.
     * @param frequency
     */
    public HuffmanTree(int frequency) {
        this.frequency = frequency;
    }

    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}