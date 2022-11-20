package database.Huffman;

class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // sub-rvores

    /**
     * Huffman node constructor. The node is simply a huffman tree with pointers to
     * the left and to the right.
     * @param left left huffman tree
     * @param right right huffman tree
     */
    public HuffmanNode(HuffmanTree left, HuffmanTree right) {
        super(left.frequency + right.frequency);
        this.left = left;
        this.right = right;
    }
}