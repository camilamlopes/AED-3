package database;
import java.io.*;
import java.lang.reflect.*;
import interfaces.Registro;
import entities.*;

public class CRUD <T extends Registro> {
    private RandomAccessFile rac;
    private Lapides lixo;
    private Constructor<T> constructor;
    
    public CRUD(String entidade, Constructor<T> constructor) throws Exception {
        try {
            this.constructor = constructor;
            this.lixo = new Lapides("../db/" + entidade + "_Lixo.db");
            // mover o ponteiro para o ínicio do arquivo
            rac = new RandomAccessFile("../db/" + entidade + ".db", "rw");
            
            // ler ultimo id se existir
            if (rac.length() < 12) {
                rac.writeInt(0);
                rac.writeLong(-1);
                
            } // end of if 


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create (T entidade) {

        try {
            // mover o ponteiro para inicio do arquivo(cabeçalho)
            rac.seek(0);
            
            // ler últimoID
            int id = rac.readInt();
            entidade.setId(id+1);

            // atualização do último id
            rac.seek(0);
            rac.write(entidade.getId());

            // criar registro para o objeto
            byte[] register = entidade.toByteArray();
            long register_lenght = register.length;

            // procurar posição
            long free_pos = lixo.search_space(register_lenght);
            if(free_pos == -1)
                free_pos = rac.length();

            //
            rac.seek(free_pos);
            rac.writeChar(' ');
            rac.writeLong(register_lenght);
            
            //indices

            //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T read (int id) {
        T entidade = null;

        try {
            // mover o ponteiro para o primeiro registro
            rac.seek(0);
            int last_id = rac.readInt();

            if(last_id == 0) {
                // não existe
            }

            int current_id = 0;
            long current_pos;
            long register_size;
            
            rac.seek(6);
            while (current_id < last_id && id > current_id) {
                
                register_size = rac.readLong();
                current_pos = rac.getFilePointer();
                current_id = rac.readInt();

                if(current_id == id)
                    last_id = 0;
                else
                    rac.seek(current_pos + register_size + 2);
            }

            rac.seek(current_pos - 10);


        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    
}

