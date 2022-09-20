
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class App {
    public static void main(String[] args) throws Exception {
        Livro l1 = new Livro(1, "O nome do vento", 600);
        System.out.println(l1);
        Livro l2 = new Livro(2, "A Origem", 300);
        System.out.println(l2);

        FileOutputStream fos = new FileOutputStream("../db/banco.db");
        DataOutputStream dos = new DataOutputStream(fos);

        FileInputStream fis = new FileInputStream("../db/banco.db");
        DataInputStream dis = new DataInputStream(fis);

        Livro l3 = new Livro();
        Livro l4 = new Livro();

        byte[] b;
        int len;
        try {

            b = l1.toByteArray();
            dos.writeInt(b.length);
            dos.write(b);
            b = l2.toByteArray();
            dos.writeInt(b.length);
            dos.write(b);

            dos.close();

            len = dis.readInt();
            b = new byte[len];
            dis.read(b);
            l3.fromByteArray(b);
            System.out.println(l3);

            len = dis.readInt();
            b = new byte[len];
            dis.read(b);
            l4.fromByteArray(b);
            System.out.println(l4);

            dis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
