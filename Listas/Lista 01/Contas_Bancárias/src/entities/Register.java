package entities;
import java.io.IOException;
import java.util.List;

/* Entidade de Registro -> template */
public interface Register {
    public String getNomeUsuario();
    public String getNomePessoa();
    public void setNomePessoa(String nomePessoa);
    public String getCidade();
    public void setCidade(String cidade);
    public String getSenha();
    public void setSenha(String senha);
    public List<String> getEmail();
    public void setEmail(List<String> email);
    public int getQnt_email();
    public void setQnt_email(int qnt_email);
    public char[] getCpf();
    public int getTransfRealizadas();
    public void setTransfRealizadas(int transfRealizadas);
    public int getId();
    public void setId(int id);
    public float getSaldoConta();
    public void setSaldoConta(float saldoConta);


    public byte[] toByteArray() throws IOException;
    public void fromByteArray(byte[] ba) throws IOException;
	public String toString();
}