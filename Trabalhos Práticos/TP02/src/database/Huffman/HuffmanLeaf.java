package database.Huffman;

class HuffmanLeaf extends HuffmanTree {
    public final char value;

    /**
     * Leaf constructor. The leaf is simply a huffman tree with only one node: the leaf.
     * @param frequency the amount of times the symbol repeat itself
     * @param symbol the symbol
     */
    public HuffmanLeaf(int frequency, char symbol) {
        super(frequency);
        value = symbol;
    }
}