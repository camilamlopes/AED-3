
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

        try {

            dos.writeInt(l1.id);
            dos.writeUTF(l1.name);
            dos.writeInt(l1.pages);

            dos.writeInt(l2.id);
            dos.writeUTF(l2.name);
            dos.writeInt(l2.pages);
            dos.close();

            System.out.println("===========================");
            l3.id = dis.readInt();
            l3.name = dis.readUTF();
            l3.pages = dis.readInt();

            l4.id = dis.readInt();
            l4.name = dis.readUTF();
            l4.pages = dis.readInt();
            dis.close();

            System.out.println(l3);
            System.out.println(l4);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
