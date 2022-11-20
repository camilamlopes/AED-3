package entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Entidade Conta do Usuário
 * @author Camila Moreira Lopes
 * @version 1.0
 */

public class Conta implements Register {
    /******** VARIÁVEIS PROTEGIDAS ********/
    // ------------- string ------------- //
    protected String nomeUsuario;           
    protected String nomePessoa;            
    protected String cidade;                
    protected String senha;

    // -------- array of string --------- //
    protected List<String> email;               
    private int qnt_email = 0;
    
    // --------- array of char ---------- //
    protected char[] cpf;    
    
    // -------------- int --------------- //
    protected int transfRealizadas;             // variável correspondente à quantidade de transferências realizadas
    protected int id;                  

    // ------------- float -------------- //
    protected float saldoConta;             
    

    /**
     * Construtor sem nenhum parâmetro
     */
    public Conta () {
        this.nomeUsuario = " ";
        this.nomePessoa = " ";
        this.cidade = " ";
        this.senha = " ";
        this.email = new ArrayList<String>();
        this.qnt_email = 0;
        this.cpf = new char[11];
        
        this.id = -1;
        this.saldoConta = 0;
        
    } // end of void constructor

    /**
     * Construtor com parâmetros
     * @param id
     * @param nomePessoa
     * @param email
     * @param nomeUsuario
     * @param senha
     * @param cpf
     * @param cidade
     * @param transfRealizadas
     * @param saldoConta
     */
    
    public Conta(int id, String nomePessoa, List<String> email, String nomeUsuario, String senha, char[] cpf, String cidade,
                 int transfRealizadas, float saldoConta) {
        this.nomeUsuario = nomeUsuario;
        this.nomePessoa = nomePessoa;
        this.cidade = cidade;
        this.senha = senha;
        this.email = email;
        this.qnt_email = email.size();
        this.cpf = cpf;
        
        this.id = id;
        this.saldoConta = saldoConta;
    } //end of full constructor

    /********** GETTERS E SETTERS **********/
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public int getQnt_email() {
        return qnt_email;
    }

    public void setQnt_email(int qnt_email) {
        this.qnt_email = qnt_email;
    }

    public char[] getCpf() {
        return cpf;
    }

    public int getTransfRealizadas() {
        return transfRealizadas;
    }

    public void setTransfRealizadas(int transfRealizadas) {
        this.transfRealizadas = transfRealizadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getSaldoConta() {
        return saldoConta;
    }

    public void setSaldoConta(float saldoConta) {
        this.saldoConta = saldoConta;
    }

    /**
     * Formata as variáveis para uma única string e a retorna
     * @return String
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String str = new String();
        int i;

        sb.append(this.id).append(' ');
        sb.append(this.nomeUsuario).append(' ');
        sb.append(this.qnt_email).append(' ');

        if(this.qnt_email > 0){
            for(i = 0; i < this.qnt_email - 1; i++) {  
                sb.append(email.get(i)).append(' ');
            }
        
            sb.append(email.get(i)).append(' ');
        }
            
        sb.append(this.nomePessoa).append(' ');
        sb.append(this.senha).append(' ');

        for(i = 0; i < this.cpf.length; i++) {
            sb.append(this.cpf[i]).append(' ');
        }

        sb.append(this.cidade).append(' ');
        sb.append(this.transfRealizadas).append(' ');
        sb.append(this.saldoConta).append(' ');

        str = sb.toString();
        return str;
    }   // end of toString
    


    /**
     * 
     * @return array of byte
     * @throws IOException
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        int i;
        dos.writeInt(this.id);
        dos.writeUTF(this.nomePessoa);
        
        dos.writeInt(this.qnt_email);
        for(i = 0; i < this.qnt_email; i++) dos.writeUTF(email.get(i));
        
        dos.writeUTF(this.nomeUsuario);
        dos.writeUTF(this.senha);

        for(i = 0; i < 11; i++) dos.writeChar(this.cpf[i]);

        dos.writeUTF(this.cidade);
        dos.writeInt(this.transfRealizadas);
        dos.writeFloat(this.saldoConta);

        return baos.toByteArray();
    }   // end of toByteArray

    /**
     * 
     * @param b
     * @throws IOException
     */
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.nomePessoa = dis.readUTF();
        
        this.qnt_email = dis.readInt();
        this.email = new ArrayList<String>();
        for(int i = 0; i < this.qnt_email; i++) email.add(dis.readUTF());

        this.nomeUsuario = dis.readUTF();
        this.senha = dis.readUTF();

        for(int i = 0; i < 11; i++) this.cpf[i] = dis.readChar();
        
        this.cidade = dis.readUTF();
        this.transfRealizadas = dis.readInt();
        this.saldoConta = dis.readFloat();
    }   //end of fromByteArray
}
