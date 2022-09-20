import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Camila Moreira Lopes
 * @version 1.0
 */
class Conta implements Listagem {
    /******** VARIÁVEIS PROTEGIDAS ********/
    // ------------- string ------------- //
    protected String nomeUsuario;           
    protected String nomePessoa;            
    protected String cidade;                
    protected String senha;                 
    // -------- array of string --------- //
    protected String[] email;               
    // --------- array of char ---------- //
    protected char[] cpf = new char[11];    
    // -------------- int --------------- //
    protected int transfRealizadas;             // variável correspondente à quantidade de transferências realizadas
    protected int idConta;                  
    // ------------- float -------------- //
    protected float saldoConta;             


    /**
     * Construtor sem nenhum parâmetro
     */
    public Conta () {
        this.nomeUsuario = "";
        this.nomePessoa = "";
        this.cidade = "";
        this.senha = "";
        this.transfRealizadas = 0;
        this.idConta = -1;
        this.saldoConta = 0;
    }

    /**
     * Construtor com parâmetros
     * @param idConta
     * @param nomePessoa
     * @param email
     * @param nomeUsuario
     * @param senha
     * @param cpf
     * @param cidade
     * @param transfRealizadas
     * @param saldoConta
     */
    public Conta(int idConta, String nomePessoa, String[] email, String nomeUsuario, String senha, char[] cpf, String cidade,
            int transfRealizadas, float saldoConta) {
        this.nomeUsuario = nomeUsuario;
        this.nomePessoa = nomePessoa;
        this.cidade = cidade;
        this.senha = senha;
        this.email = email;
        this.cpf = cpf;
        this.transfRealizadas = transfRealizadas;
        this.idConta = idConta;
        this.saldoConta = saldoConta;
    }



    /**
     * Formata as variáveis para uma única string e a retorna
     * @return String
     */
    public String toString() {
        return "id: " + this.idConta + " | username: " + this.nomeUsuario + " | name: " + this.nomePessoa + " | city: " + this.cidade +
                " | password: " + this.senha + " | tranferências realizadas: " + this.transfRealizadas + " | saldo: " + this.saldoConta;
    }
    

    /**
     * 
     * @return array of byte
     * @throws IOException
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.idConta);
        dos.writeUTF(this.nomePessoa);
        // dos.write (this.email);
        dos.writeUTF(this.nomeUsuario);
        dos.writeUTF(this.senha);
        // dos.write (this.cpf);
        dos.writeUTF(this.cidade);
        dos.writeInt(this.transfRealizadas);
        dos.writeFloat(this.saldoConta);

        return baos.toByteArray();
    }

    /**
     * 
     * @param b
     * @throws IOException
     */
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.idConta = dis.readInt();
        this.nomePessoa = dis.readUTF();
        // this.email = dis.read ();
        this.nomeUsuario = dis.readUTF();
        this.senha = dis.readUTF();
        // this.cpf = dis.read ();
        this.cidade = dis.readUTF();
        this.transfRealizadas = dis.readInt();
        this.saldoConta = dis.readFloat();
    }
}
