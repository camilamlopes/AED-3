package database;
import java.io.*;
import java.lang.reflect.*;
import entities.*;

public class CRUD <T extends Register> {
    public RandomAccessFile rac;
    private Lapides lixo;
    private Constructor<T> constructor;
    
    /**
     * Construtor especial de classe generalizada
     * @param entidade
     * @param constructor
     * @throws Exception
     */
    public CRUD(String entidade, Constructor<T> constructor) throws Exception {
        try {
            this.constructor = constructor;
            this.lixo = new Lapides("./db/" + entidade + "_Lixo.db");
            // mover o ponteiro para o ínicio do arquivo
            rac = new RandomAccessFile("./db/" + entidade + ".db", "rw");
            
            // define o primeiro valor
            if (rac.length() < 12) {
                rac.writeInt(0);
                rac.writeLong(0);
                
            } // end of if 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria um novo registro no arquivo
     * @param entidade
    */
    public void create (T entidade) {

        try {
            // mover o ponteiro para inicio do arquivo(cabeçalho)
            rac.seek(0);
            
            // ler últimoID
            int id = rac.readInt();
            entidade.setId(++id);
            Long qnt = rac.readLong();

            // atualização do último id
            rac.seek(0);
            rac.writeInt(id);

            // criar registro para o objeto
            byte[] register = entidade.toByteArray();
            long register_lenght = register.length;

            // procurar posição
            long free_pos = lixo.search_space(register_lenght);
            if(free_pos == -1) {
                free_pos = rac.length();
                
                rac.seek(free_pos);
                rac.writeChar(' ');
                rac.writeLong(register_lenght);
                rac.write(register);
            } 
            // se houver posição livre dentre as lápides
            else {
                rac.seek(free_pos);
                rac.writeChar(' ');
                rac.skipBytes(8);
                rac.write(register);
            }            
            
            //
            rac.seek(4);
            rac.writeLong((++qnt));

            //indices

            //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T read (int id) {
        T entity = null;

        try {
            // mover o ponteiro para o primeiro registro
            rac.seek(0);
            int last_id = rac.readInt();

            if(last_id == 0) 
                throw new Exception("O banco de dados está vazio!");
            
            char grave;
            int current_id = 0;
            long current_pos = rac.length();
            long register_size;
            boolean flag = true;
            
            rac.seek(12);
            do {
                grave = rac.readChar();
                register_size = rac.readLong();
                current_pos = rac.getFilePointer();
                current_id = rac.readInt();

                if(current_id == id){
                    flag = false;
                }

                rac.skipBytes((int) register_size - 2);

            } while (flag && (rac.getFilePointer() <= rac.length() - 1));

            rac.seek(current_pos);
            byte[] data = new byte[(int) register_size];
            rac.read(data);

            entity = this.constructor.newInstance();

            if(flag == false && grave == ' ')
                entity.fromByteArray(data);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return entity;
    }
    
    public T read (String username) {
        T entity = null;

        try {
            // mover o ponteiro para o primeiro registro
            rac.seek(0);
            int last_id = rac.readInt();

            if(last_id == 0) 
                throw new Exception("O banco de dados está vazio!");
            
            char grave;
            //long current_pos = 0;
            long register_size;
            boolean flag = true;

            String name;
            entity = this.constructor.newInstance();
            byte[] data = new byte[4];

            rac.seek(12);
            do{
                
                grave = rac.readChar();
                register_size = rac.readLong();

                if(grave == ' '){
                    data = new byte[(int) register_size];
                    rac.read(data);
                    entity.fromByteArray(data);

                    name = entity.getNomeUsuario();

                    if(username.equals(name))
                        flag = false;
                }

                rac.skipBytes((int) register_size - 2);

            } while (flag && (rac.getFilePointer() <= rac.length() - 1));
            

            if(flag != false)
                entity = this.constructor.newInstance();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }

    public void update(T entity) {
        int id = entity.getId();

        try {
            // mover o ponteiro para o primeiro registro
            rac.seek(0);
            int last_id = rac.readInt();

            if(last_id == 0) 
                throw new Exception("O banco de dados está vazio!");
            
            char grave;
            int current_id = 0;
            long current_pos = 0;
            long register_size;
            boolean flag = true;
            
            rac.seek(12);
            do{
                
                grave = rac.readChar();
                register_size = rac.readLong();
                current_pos = rac.getFilePointer();
                current_id = rac.readInt();
                if(current_id == id){
                    flag = false;
                }

                rac.skipBytes((int) register_size - 2);

            } while (flag && (rac.getFilePointer() <= rac.length() - 1));

            if(flag == false && grave == ' ')
                delete(id);
            else
                throw new Exception("Essa conta não existe!");
            
            create(entity);

            rac.seek(current_pos);
            rac.writeInt(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete (int id) {
        try {
            // mover o ponteiro para o primeiro registro
            rac.seek(0);
            int last_id = rac.readInt();

            if(last_id == 0) 
                throw new Exception("O banco de dados está vazio!");
            if(id <= 0 )
                throw new Exception("O ID inserido é inválido!");

            last_id = (int) rac.readLong() - 1;

            char grave;
            int current_id = 0;
            long current_pos = 0;
            long register_size;
            boolean flag = true;
            
            rac.seek(12);
            do{
                current_pos = rac.getFilePointer();
                grave = rac.readChar();
                register_size = rac.readLong();
                current_id = rac.readInt();
                
                if(current_id == id){
                    flag = false;
                }

                rac.skipBytes((int) register_size - 2);

            } while (flag && (rac.getFilePointer() <= rac.length() - 1));
            
            rac.seek(current_pos);
            if(flag == false && grave == ' '){
                rac.writeChar('*');
                long size = rac.readLong(); 
                lixo.create_grave(size, current_pos);

                rac.seek(4);
                rac.writeLong((long) last_id);
            }
                
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        int i = 0, h = -1;
        long len = -1;

        try {
            rac.seek(0);
            h = rac.readInt();
            len = rac.readLong();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sb.append(h).append(' ').append(len).append(' ');

        char grave;
        long register_size;
        T entity = null;
        byte[] data;

        
        try {
            while (i < len) {
                grave = rac.readChar();
                register_size = rac.readLong();
                
                data = new byte[(int) register_size];
                rac.read(data);
                entity = this.constructor.newInstance();
                entity.fromByteArray(data);

                sb.append(entity.toString()).append(' ');

                if (grave == ' ') i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}

