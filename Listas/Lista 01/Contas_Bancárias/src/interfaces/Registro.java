package interfaces;
import java.io.IOException;

/* Entidade de Registro -> template */
public interface Registro {
    public void setId(int id);
    public int getId();

    public byte[] toByteArray() throws IOException;
    public void fromByteArray(byte[] ba) throws IOException;
	public String toString();
    
    // criar conta bancária
    // realizar transferência
    // ler um registro
    // atualizar um registro
    // deletar um registro
    // realizar ordenação do arquivo

}
