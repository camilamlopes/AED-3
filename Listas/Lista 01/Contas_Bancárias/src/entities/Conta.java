package entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

import interfaces.*;
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
    private String getNomeUsuario() {
        return nomeUsuario;
    }

    private void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    private String getNomePessoa() {
        return nomePessoa;
    }

    private void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    private String getCidade() {
        return cidade;
    }

    private void setCidade(String cidade) {
        this.cidade = cidade;
    }

    private String getSenha() {
        return senha;
    }

    private void setSenha(String senha) {
        this.senha = senha;
    }

    private List getEmail() {
        return email;
    }

    private void setEmail(List<String> email) {
        this.email = email;
    }

    private int getQnt_email() {
        return qnt_email;
    }

    private void setQnt_email(int qnt_email) {
        this.qnt_email = qnt_email;
    }

    private char[] getCpf() {
        return cpf;
    }

    private void setCpf(char[] cpf) {
        this.cpf = cpf;
    }

    private int getTransfRealizadas() {
        return transfRealizadas;
    }

    private void setTransfRealizadas(int transfRealizadas) {
        this.transfRealizadas = transfRealizadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private float getSaldoConta() {
        return saldoConta;
    }

    private void setSaldoConta(float saldoConta) {
        this.saldoConta = saldoConta;
    }

    /**
     * Formata as variáveis para uma única string e a retorna
     * @return String
     */
    public String toString() {
        String str = new String();
        int i;

        str += "id: " + this.id + " | username: " + this.nomeUsuario + " | " + this.qnt_email + " email(s): ";

        for(i = 0; i < this.qnt_email - 1; i++) {        
            str += email.get(i) + ", ";
        }
        
        str += email.get(i);

        str += " | name: " + this.nomePessoa + " | password: " + this.senha + "| CPF: ";

        for(i = 0; i < this.cpf.length; i++) {
            str += this.cpf[i];
        }

        str += " | city: " + this.cidade + " | tranferências realizadas: " + this.transfRealizadas + " | saldo: " + this.saldoConta;

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
