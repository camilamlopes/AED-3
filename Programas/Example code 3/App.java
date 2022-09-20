import java.io.RandomAccessFile;

public class App {
    public static void main(String[] args) throws Exception {
        Livro l1 = new Livro(1, "O nome do vento", 600);
        System.out.println(l1);
        Livro l2 = new Livro(2, "A Origem", 300);
        System.out.println(l2);

        RandomAccessFile arq = new RandomAccessFile("../db/banco.db", "rw");
        Livro l3 = new Livro();
        Livro l4 = new Livro();

        byte[] b;
        int len;
        try {

            b = l1.toByteArray();
            arq.writeInt(b.length);
            arq.write(b);
            b = l2.toByteArray();
            arq.writeInt(b.length);
            arq.write(b);

            arq.seek(0);
            len = arq.readInt();
            b = new byte[len];
            arq.read(b);
            l3.fromByteArray(b);
            System.out.println(l3);

            len = arq.readInt();
            b = new byte[len];
            arq.read(b);
            l4.fromByteArray(b);
            System.out.println(l4);

            arq.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
