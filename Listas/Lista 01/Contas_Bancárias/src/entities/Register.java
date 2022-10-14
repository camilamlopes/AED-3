package entities;
import java.io.IOException;

/* Entidade de Registro -> template */
public interface Register {
    public void setId(int id);
    public int getId();

    public byte[] toByteArray() throws IOException;
    public void fromByteArray(byte[] ba) throws IOException;
	public String toString();
}