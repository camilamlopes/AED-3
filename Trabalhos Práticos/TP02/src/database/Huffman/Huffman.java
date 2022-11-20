/**
 * 
 * Students: Arthur Rodrigues S. Quadros e Camila Moreira Lopes
 */

package database.Huffman;

import database.*;
import java.io.*;
import java.util.*;

public class Huffman {
    private HashMap<Character, String> index;

    public Huffman() {
        index = new HashMap<Character, String>();
    } 

    public void buildFileCodeIndex(String toDecode, String name , long lastVersion) {
        // definir a frequencia de cada caractere
        int[] charFrequency = new int[256];
        for(char c :toDecode.toCharArray())
            charFrequency[c]++;
            
        HuffmanTree hT = Huffman.buildTree(charFrequency);
        buildCodeIndex(hT, "");

        //
        try {
            RandomAccessFile arq = new RandomAccessFile(("./db/Huffman/" + name + "Index" + lastVersion  + ".huff"), "rw");
            String value;
            for(char ch : index.keySet()) {
                value = index.get(ch);
                
                arq.writeUTF(value);
                arq.writeChar(ch);
            }

            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildCodeIndex (HuffmanTree n, String code) {
        if (n != null) {
            if(!(n instanceof HuffmanLeaf)) {
                HuffmanNode node = (HuffmanNode) n;
                
                buildCodeIndex(node.left, code + '0');
                buildCodeIndex(node.right, code + '1');
            } 
            else {
                HuffmanLeaf leaf = (HuffmanLeaf) n;
                index.put(leaf.value, code);
            }
        }
    }

    private void readCodeIndex (String name, long version) {

        try {
            RandomAccessFile arq = new RandomAccessFile(("./db/Huffman/" + name + "Index" + version  + ".huff"), "rw");
            String value;
            char ch;

            while(true){
                try{
                    value = arq.readUTF();
                    ch = arq.readChar();
                    index.put(ch, value);
                } catch(EOFException e){
                   break;
                }
            }

            arq.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * Build huffman tree given the frequencies of each symbol.
     * 
     * @param charFreqs frequencies
     * @return huffman tree built
     */
    public static HuffmanTree buildTree(int[] charFreqs) {
        // Cria uma Fila de Prioridade
        
        // A Fila ser criado pela ordem de frequncia da letra no texto
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        
        // Criar as Folhas da arvore para cada letra 
        for (int i = 0; i < charFreqs.length; i++) {
            if (charFreqs[i] > 0)
                trees.offer(new HuffmanLeaf(charFreqs[i], (char) i)); // Insere os elementos, n folha, na fila de prioridade
        }
        
        // Percorre todos os elementos da fila
        // Criando a arvore binaria de baixo para cima
        while (trees.size() > 1) {
            // Pega os dois nos com menor frequencia
            HuffmanTree a = trees.poll(); // poll - Retorna o proximo n da Fila ou NULL se no tem mais
            HuffmanTree b = trees.poll(); // poll - Retorna o proximo n da Fila ou NULL se no tem mais

            // Criar os ns da rvore binria
            trees.offer(new HuffmanNode(a, b));
        }

        // Retorna o primeiro no da rvore
        return trees.poll();
    }


    /**
     * 
     * @param tree huffman tree built
     * @param encode string to be encoded
     * @param name file to write encoded string
     * @return
     * @throws Exception
     */
    public String encode(String encode, String name) throws Exception{
        
        File directory = new File("./db/Huffman/");
        long lastVersion = directory.list().length + 1;
        File arq = new File(("./db/Huffman/" + name + lastVersion  + ".db"));
        BitWriter bitWriter = new BitWriter(arq);

        StringBuilder sb = new StringBuilder();       
        buildFileCodeIndex(encode, name, lastVersion);

        byte[] bytesCode;
        String str;

        for(char ch : encode.toCharArray()) {
            str = index.get(ch);
            sb.append(str);

            assert str != null;
            bytesCode = new byte[str.length()];
            for (int i = 0; i < str.length(); i++)
                bytesCode[i] = (str.charAt(i) == '0' ? (byte) 0 : (byte) 1);
            
            bitWriter.write(bytesCode); 
        }
        bitWriter.close();
        return sb.toString();
    }


     /**
     * Decode string given the huffman tree built.
     * 
     * @param tree huffman tree built
     * @param encodedBitsLength length of encoded file in bits
     * @param f file to decode
     * @return decoded file
     */
    public String decode(String name, long version) {

        File directory = new File("./db/Huffman/");
        long lastVersion = directory.list().length + 1;
        File arq = new File(("./db/Huffman/" + name + lastVersion  + ".db"));

        readCodeIndex(name, version);

        StringBuilder decodeText = new StringBuilder();
        try {
            byte[] bitsToDecode = new BitReader(arq).getBits();
            


        } catch (IOException e) {
            e.printStackTrace();
        }

        return decodeText.toString(); // Retorna o texto Decodificado
    }
}
