import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import entities.Conta;

public class App {
    public static void main(String[] args) throws Exception {
        String[] email_1 = {"lopes.camilamoreira@gmail.com", "camilamoeriral2001@gmail.com", "camilaml2001@gmail.com"};
        String[] email_2 = {"exemplo@dominio.com", "exemplo@gmail.com"};
        char[] cpf = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};

        Conta c1 = new Conta(0, "Camila Moreira Lopes", email_1, "camila_lopes", "123456", cpf, "Belo Horizonte",
        0, 0);
        System.out.println(c1);
        Conta c2 = new Conta(1, "Nome Pessoa", email_2, "username", "123456", cpf, "Cidade",
        100, 999);       
        System.out.println(c2);

        FileOutputStream fos = new FileOutputStream("banco.db");
        DataOutputStream dos = new DataOutputStream(fos);

        FileInputStream fis = new FileInputStream("banco.db");
        DataInputStream dis = new DataInputStream(fis);

        Conta c3 = new Conta();
        Conta c4 = new Conta();

        byte[] b;
        int len;

        try {

            b = c1.toByteArray();
            dos.writeInt(b.length);
            dos.write(b);

            b = c2.toByteArray();
            dos.writeInt(b.length);
            dos.write(b);

            dos.close();

            len = dis.readInt();
            b = new byte[len];
            dis.read(b);
            c3.fromByteArray(b);
            c3.setId(3);
            System.out.println(c3);

            len = dis.readInt();
            b = new byte[len];
            dis.read(b);
            c4.fromByteArray(b);
            c4.setId(4);
            System.out.println(c4);

            dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
