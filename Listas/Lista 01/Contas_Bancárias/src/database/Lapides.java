package database;
import java.io.RandomAccessFile;

public class Lapides {
    String save_location;
    RandomAccessFile rac;

    Lapides (String save_location) {
        try {
            this.save_location = save_location;
            rac = new RandomAccessFile(this.save_location, "rw");

            if(rac.length() == 0) 
                rac.writeLong(-1);
            

            rac.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create_grave (long register_size, long location_in_db) {
        try {
            rac = new RandomAccessFile(this.save_location, "rw");
            
            // last_grave armazena o ultimo campo lapide livre para inserir
            long last_grave = rac.readLong();
            
            // Inicializador
            // se o primeiro long do indice for -1, entao esse índice esta sem campos lapide
            // fazendo com que os valores recebidos sejam inserido no final
            if(last_grave == -1) {
                rac.seek(rac.length());
                
                rac.writeLong(register_size);
                rac.writeLong(location_in_db);
            }

            // se o primeiro long do indice nao for -1, entao existe um campo lapide livre,
            // fazendo com que os valores recebidos sejam inseridos nesse campo livre e nao no final
            else {
                // sobrescrevendo o campo lapide
                rac.seek(last_grave);
                rac.writeLong(register_size);

                
                long current_pos = rac.getFilePointer();
                long next_spot = rac.readLong();

                rac.seek(0);
                rac.writeLong(next_spot);

                rac.seek(current_pos);
                rac.writeLong(location_in_db);
            }

            rac.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public long search_space (long register_size) {
        long best_candidate_value = -1;
        long best_candidate_location_file = -1; // localizacao da lapide no arquivo de dados
        long best_candidate_location_index = -1; // localizacao do byte inicial do melhor candidato no index
       
        long candidate_size = -1;
        boolean flag = true;


        try {
            rac = new RandomAccessFile(this.save_location, "rw");

            while (flag && (rac.getFilePointer() <= rac.length() - 1)) {
                candidate_size = rac.readLong();
                
                if (100 * register_size/candidate_size >= 80 && 100 * register_size/candidate_size <= 100 ) {
    
                    if (register_size == candidate_size) {
                        best_candidate_value = 100;
                        best_candidate_location_file = rac.readLong();
                        flag = false;

                        best_candidate_location_index = rac.getFilePointer()-16;
                    } 
                    else if(100*register_size/candidate_size > best_candidate_value) {
                        best_candidate_value = 100*register_size/candidate_size;
                        best_candidate_location_file = rac.readLong();

                        best_candidate_location_index = rac.getFilePointer()-16;
                    }
    
    
                } else {
                    rac.readLong();
                }
            }

            if(best_candidate_location_index != -1) 
                delete_grave(best_candidate_location_index);

            
                rac.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return best_candidate_location_file;
    }


    public void delete_grave (long location) {
        try {
            rac.seek(0);

            long last_grave_pos = rac.readLong();
            
            rac.seek(0);
            rac.writeLong(location);

            rac.seek(location);
            rac.writeLong(-1);
            rac.writeLong(last_grave_pos);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
 }


