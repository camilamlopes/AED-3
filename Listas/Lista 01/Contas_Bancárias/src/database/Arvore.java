package database;

import java.io.*;
import java.util.*;

class Node {
    // variáveis importantes para o nó
    protected int n;                // Número de elementos presentes na página
    protected int order;            // Ordem
    protected int max_keys;         // Número máximo de elementos no nó
    protected int max_children;     // Número máximo de filhos

    protected int[] key_id;         // Chave primária
    protected long[] key_pos;       // Chave secundária

    protected long next;            // próxima folha quando a página for uma folha
    protected long[] children;      // vetor de ponteiros para filhos  

    protected int REGISTER_TAM;     // os elementos são de tamanho fixo
    protected int ELEMENT_TAM;      // o nó terá um tamanho fixo

    /**
     * Construtor do Nó
     */
    public Node () {
        this(5);
    }

    /**
     * Construtor do Nó
     * @param o
     */
    public Node (int o) {
        this.order = o;
        this.max_keys = o - 1;
        this.max_children =  o;

        this.next = -1;
        this.n = 0;

        this.key_id = new int[max_keys];
        this.key_pos = new long[max_keys];
        this.children = new long[max_children];


        for(int i = 0; i < max_keys; i++){
            key_id[i] = 0;
            key_pos[i] = 0;
            children[i] = -1;
        }

        children[max_children-1] = -1;


        /*
         * n    p1    e1    p2    e2    p3    e3    p4    e4    p5      ponteiro para próximo            
         * n                        -> int          -> 4 bytes
         * elemento                 -> int + long   -> 12 bytes
         * ponteiro de filho        -> long         -> 8 bytes
         * ponteiro para o próximo  -> long         -> 8 bytes
        */
        REGISTER_TAM = 12;
        ELEMENT_TAM = 4 + (max_keys * REGISTER_TAM) + (max_children * 8) + 8;
    }
    /**
     * Retorna um vetor de bytes que representa o nó
     * @return
     * @throws IOException
     */
    protected byte[] toByteArray() throws IOException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Quantidade de elementos presentes
        dos.writeInt(this.n);

        // Escreve todos os elementos
        int i=0;
        for(;i < n; i++){
            dos.writeLong(this.children[i]);
            dos.writeInt(this.key_id[i]);
            dos.writeLong(this.key_pos[i]);
        }
        dos.writeLong(this.children[i]);

        // Completar com registros vazios
        byte[] null_register = new byte[REGISTER_TAM];
        
        for(; i < max_keys; i++){
            dos.write(null_register);
            dos.writeLong(children[i+1]);
        }

        // Escreve ponteiro para o próximo nó
        dos.writeLong(this.next);

        return baos.toByteArray();
    }

    /**
     * Lê byte e insere nos elementos do nó
     * @param b
     * @throws IOException
     */
    protected void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        // Lê a quantidade de elementos no nó
        this.n = dis.readInt();

        // Lê todos os elementos (incluindo os vazios)
        int i = 0;  
        for(; i < max_keys; i++) {
            this.children[i] = dis.readLong();
            this.key_id[i] = dis.readInt();
            this.key_pos[i] = dis.readLong();
        }
        this.children[i] = dis.readLong();

        this.next = dis.readLong();
    }


}

public class Arvore {
    // variáveis importantes para a árvore
    protected int order;            // ordem da árvore
    protected int max_keys;         // Número máximo de elementos para cada nó (ordem - 1)
    protected int max_children;     // Número máximo de filhos para cada nó (igual a ordem)

    // variáveis globais auxiliares usadas nos métodos recursivos
    protected int key_idAux;        
    protected long key_posAux;
    protected long aux;
    protected boolean parent_leaf;
    protected boolean smaller;

    // variáveis importantes para o índicce
    private RandomAccessFile rac;
    private String save_location;

    /**
     * Construtor nulo
     * @param fn
     */
    public Arvore (String fn){
        this(5, fn);
    }
    
    /**
     * Construtor
     * @param o
     * @param fn
     */
    public Arvore (int o, String fn){
        this.order = o;
        this.max_children = o;
        this. max_keys = o - 1;
        this.save_location = fn;

        try {
            rac = new RandomAccessFile(save_location, "rw");
            
            // Define uma árvore vazia 
            if(rac.length() < 8) 
                rac.writeLong(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Testa se a árvore está vazia
     * @return
     */
    private boolean empty () {
        long root = -1;
        
        try {
            rac.seek(0);
            root = rac.readLong();
            
            rac.seek(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (root == -1);
    }

    /* CREATE */
    /**
     * Método de criar recursivo
     * @param id
     * @param pointer
     */
    public void create (int id, long pointer) {

        try {
            // Valida as chaves
            if(id <= 0 || pointer < 0) {
                throw new Exception("Ocorreu algum erro com a chave ou o ponteiro!");
            }

            // Carrega a raiz (posição da raiz)
            rac.seek(0);
            long node = rac.readLong();

            // Prevenção devido o funcionamento do java
            key_idAux = id;
            key_posAux = pointer;

            // Se houver crescimento será criado um novo nó 
            // e mantido um ponteira para essa página
            aux = -1;
            parent_leaf = false;

            // Chamada para a inserção do novo elemento
            create(node);

            // Testa se é necessario a criação de uma nova raiz
            if (parent_leaf) {
                Node newNode = new Node(order);     // Novo nó que será a raiz
                
                newNode.n = 1;
                newNode.key_id[0] = key_idAux;
                newNode.key_pos[0] = key_posAux;

                //            novo nó
                //          ↙        ↘
                //  raiz antiga     novo elemento
                newNode.children[0] = node;         
                newNode.children[1] = aux; 

                // Procura memória livre e insere a nova memória
                rac.seek(rac.length());
                long root = rac.getFilePointer();
                rac.write(newNode.toByteArray());
                
                // Determina a posição raiz
                rac.seek(0);
                rac.writeLong(root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método de criar recursivo
     * @param node
     */
    private void create (long node) {

        try {
            // Verifica se o nó passou a ser um filho de uma folha
            // obs.: nesse caso inicializa as variáveis globais de controle
            if(node == - 1) {
                parent_leaf = true;
                aux = -1;
            } 
            
            else {
                // Leitura do nó
                Node no = new Node(order);
                byte[] buffer = new byte[no.ELEMENT_TAM];

                rac.seek(node);
                rac.read(buffer);
                no.fromByteArray(buffer);

                // Busca o próximo ponteiro de descida
                // obs.: devido o funcionamento da árvore B+ é utilizado + de uma chave
                int i = 0;
                for(; i < no.n && (key_idAux > no.key_id[i] || (key_idAux == no.key_id[i]) && key_posAux > no.key_pos[i]); i++);
            
                // Testa se o registro já existe em uma folha
                if(i < no.n && no.children[i] == -1 && key_posAux > no.key_pos[i]) {
                    parent_leaf = false;
                    throw new Exception("O registro já é existente!");
                }

                /**
                 * Busca recursiva
                 * ocorrerá uma busca até achar uma vaga em um nó
                 * até o filho de um nó folha
                 */
                if(i == no.n || key_idAux < no.key_id[i] || (key_idAux == no.key_id[i]) && key_posAux < no.key_pos[i]){
                    create(no.children[i]);
                } else {
                    create(no.children[i+1]);
                }

                // obs.: a inclusão pode ter sido resolvida se:
                //          - o par de chaves já existia (inclusão inválida)
                //          - o novo elemento coube em uma página existente
                if(parent_leaf) {
                    // Se tiver espaço no nó é inserido o novo elemento
                    if(no.n < max_keys) {
                        toRight(no, i, -1);     // empurra todos os elementos à direita
                        insert(no, i);
                        
                        // Insere o nó atualizado no arquivo
                        rac.seek(node);
                        rac.write(no.toByteArray());

                        parent_leaf = false;    // encerra o processo de crescimento
                    } 
                    // Se o elemento não couber no nó, ocorrerá a divisão
                    // obs.: o elemento do meio será promovido
                    else {
                        Node nn = new Node(order);  // novo nó
                        int meio = max_keys/2;

                        // Copia a metade a direita dos elementos para o novo nó
                        for(int j = 0; j < (max_keys - meio); j++) {
                            // Bloco de cópia
                            nn.key_id[j] = no.key_id[j + meio];
                            nn.key_pos[j] = no.key_id[j + meio];
                            nn.children[j + 1] = no.children[j + meio + 1];

                            // Limpa o espaço liberado
                            no.key_id[j + meio] = 0;
                            no.key_id[j + meio] = 0;
                            no.children[j + meio + 1] = -1;
                            
                        }

                        // Atualiza o tamanho
                        nn.children[0] = no.children[meio];
                        nn.n = max_keys - meio;
                        no.n = meio;


                        // Primeiro Caso - Novo registro fica à esquerda
                        if (i <= meio) {
                            toRight(no, i, meio);
                            insert(no, i);

                            // se o nó for uma folha
                            // seleciona o primeiro elemento da página da direita para ser promovido
                            if(no.children[0] == -1) {
                                key_idAux = nn.key_id[0];
                                key_posAux = nn.key_pos[0];
                            } 
                            // se não for folha, promove o maior elemento da página à esquerda
                            // o remove do nó
                            else {
                                key_idAux = no.key_id[no.n - 1];
                                key_posAux = no.key_pos[no.n - 1];
                                no.key_id[no.n - 1] = 0;
                                no.key_pos[no.n - 1] = 0;
                                no.children[no.n] = -1;
                            }
                        }

                        // Segundo Caso - Novo registro fica à direita
                        else {
                            insert(nn,toLeft(nn, meio));

                            // Seleciona o primeiro elemento da página à direita
                            key_idAux = nn.key_id[0];
                            key_posAux = nn.key_pos[0];

                            // se não for folha
                            // remove o elemento promovido do nó 
                            if(no.children[0] != -1) {
                                int j = 0;
                                for(; j < nn.n - 1; j++) {
                                    nn.key_id[j] = nn.key_id[j+1];
                                    nn.key_pos[j] = nn.key_id[j+1];
                                    nn.children[j] = nn.children[j+1];
                                }
                                nn.children[j] = nn.children[j+1];

                                //apaga o último elemento
                                nn.key_id[j] = 0;
                                nn.key_pos[j] = 0;
                                nn.children[j] = -1;
                                nn.n--;
                            }
                        }


                        // Se o nó era uma folha e apontava para outra folha,
                        // o apontador é atualizado para o novo nó
                        if(no.children[0] == -1) {
                            nn.next = no.next;
                            no.next = rac.length();
                        }

                        // Insere os nós no aquivo
                        aux = rac.length();
                        rac.seek(aux);
                        rac.write(nn.toByteArray());
                        rac.seek(node);
                        rac.write(no.toByteArray());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }

    /* READ */
    /**
     * Método de busca recursiva
     * @param id
     * @return
     */
    public long read (int id) {
        long root = -1;

        try {
            // identifica primeiro se a árvore está vazia
            if(empty())
                throw new Exception("A árvore está vazia!");
            else
                root = rac.readLong();

        } catch (Exception e) {
            e.printStackTrace();
        }
            return read(id, root);
    }

    /**
     * Método de busca recursiva 
     * retorna a posição do elemento pesquisado se existir
     * se não existir retornará -1
     * @param id
     * @param node
     * @return
     */
    public long read (int id, long node) {
        long pos = -1;

        // Identifica se a busca está em um filho de folha
        if(node == -1)
            return pos;

        try {
            Node no = new Node(this.order);
            byte[] buffer = new byte[no.ELEMENT_TAM];

            // Formata o nó passado como referência
            rac.seek(node);
            rac.read(buffer);
            no.fromByteArray(buffer);

            // Se posiciona dentro do nó
            int i = 0;
            for(;i < no.n && id > no.key_id[i]; i++);
            
            // Primeiro - chave encontrada  //
            // Condições:
            //      - id for igual à chave
            //      - i for menor que o número de elementos no nó
            //      - nó for uma folha
            // obs.: em uma árvore B+, todas as chaves válidas estão nas folhas
            if(id == no.key_id[i] && i < no.n && no.children[0] == -1) {
                pos = no.key_pos[i];
            } 

            // Segundo - chave pode estar na próxima folha  //
            // Condições:
            //      - i for igual ao número de filhos
            //      - nó for uma folha
            else if (i == no.n && no.children[0] == -1) {
                
                // Testa se há próxima folha
                if(no.next != -1) {
                    // Lê a próxima folha
                    rac.seek(no.next);
                    rac.read(buffer);
                    no.fromByteArray(buffer);

                    // Testa se a chave é a primeira da próxima folha
                    i = 0;
                    if(id == no.key_id[0]) 
                        pos = no.key_pos[0];
                    
                }
                // Se não tiver próxima folha retorna valor vazio
                else
                    return -1;
            } 
            
            // Parte recursiva //
            // Se a chave ainda não foi encontrada, 
            // continuar senguindo para as folhas
            else if (i == no.n || id <= no.key_id[i]) {
                pos = read(id, no.children[i]);
            } else {
                pos = read(id, no.children[i+1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return pos;
    }


    /* UPDATE */

    /* DELETE */
    public void delete (int id) {
        

        try {
            // Encontra a raiz da árvore
            rac.seek(0);
            long root;
            root = rac.readLong();
            
            // Chama recursivamente a exclusão de registro
            // 
            boolean deleted = delete(id, root);

            // 
            if(deleted && smaller) {

            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private boolean delete (int id, long node) {
        boolean deleted = false;
        
        if(node == -1){
            smaller = false;
            return false;
        }

        try {
            Node no = new Node();
            byte[] buffer = new byte[no.ELEMENT_TAM];

            // Formata o nó passado como referência
            rac.seek(node);
            rac.read(buffer);
            no.fromByteArray(buffer);

            // Se posiciona dentro do nó
            int i = 0;
            long pointer = read(id);
            
            if(pointer == -1){
                return false;
            }
                
            for(;i < no.n && (id > no.key_id[i] || (id == no.key_id[i] && pointer > no.key_pos[0])); i++);
            
            // CASO 1 //
            // Se chave a ser removida estiver em um nó folha
            // Condições:
            //      - id for igual à chave
            //      - i for menor que o número de elementos no nó
            //      - nó for uma folha
            // obs.: em uma árvore B+, todas as chaves válidas estão nas folhas
            if(id == no.key_id[i] && i < no.n && no.children[0] == -1 && pointer == no.key_pos[i]) {
                int j = i;
                for(; j < no.n - 1; j++) {
                    no.key_id[j] = no.key_id[j + 1];
                    no.key_pos[j] = no.key_pos[j + 1];
                }

                no.n--;

                no.key_id[no.n] = 0;
                no.key_pos[no.n] = 0;

                smaller = no.n < max_keys/2;
                return true;
            } 

            if(i == no.n || id < no.key_id[i] || (id == no.key_id[i] && pointer < no.key_pos[i])) 
                deleted = delete(id, no.children[i]);
            else 
                deleted = delete(id, no.children[++i]);
            
            
            // Testa se há necessidade de fusão de páginas
            if(smaller) {
                // Lê o nó filho que ficou com menos elementos
                // do que o mínimo necessário (média)
                long child_pointer = no.children[i];
                Node noC = new Node();
                rac.seek(child_pointer);
                rac.read(buffer);
                noC.fromByteArray(buffer);

                // Cria um nó para o irmão
                long sibling_pointer;
                Node noS = new Node();





                // Verifica e tenta fusão com o irmão esquerdo
                if(i > 0) {
                    // Lê o irmão esquerdo
                    sibling_pointer = no.children[i - 1];
                    rac.seek(sibling_pointer);
                    rac.read(buffer);
                    noS.fromByteArray(buffer);

                    // Verifica se o irmão pode doar registro
                    if(noS.n > max_keys/2) {
                        // Move todos os elementos do filho à direita
                        // aumentando uma posição à esquerda
                        toRight(noC, 0, -1);
                        noC.children[1] = noC.children[0];
                        noC.n++;

                        // Se for folha, copia o elemento do irmão
                        if(noC.children[0] == -1) {
                            noC.key_id[0] = noS.key_id[noS.n - 1];
                            noC.key_pos[0] = noS.key_pos[noS.n - 1];
                        } 
                        
                        // Se não for folha rotaciona os elementos
                        // e desce o pai
                        else {
                            noC.key_id[0] = no.key_id[i - 1];
                            noC.key_pos[0] = no.key_pos[i - 1];
                        }
                        
                        // Copia o elemento do irmão para o pai
                        no.key_id[i - 1] = noS.key_id[noS.n - 1];
                        no.key_pos[i - 1] = noS.key_pos[noS.n - 1];


                        //  Reduz o elemento do irmão
                        noC.children[0] = noS.children[noS.n];
                        noS.n--;
                        smaller = false;
                    }
                }

                


            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return deleted;
    }



    /**
     * Empurra todos os elementos para a direita e insere novo elemento
     * @param no
     * @param j
     */
    private void toRight (Node no, int i, int option) {
        // Empurra todos os elementos para a direita
        // inicia do último elemento
        
        if(option == -1) {
            for(int j = no.n; j > i; j--) {
                no.key_id[j] = no.key_id[j - 1];
                no.key_pos[j] = no.key_id[j - 1];
                no.children[j+1] = no.children[j];
            }
        } else {
            for(int j = option; j > 0 && j > i; j--) {
                no.key_id[j] = no.key_id[j - 1];
                no.key_pos[j] = no.key_id[j - 1];
                no.children[j+1] = no.children[j];
            }
        }
        
         
    }

    /**
     * Empurra todos os elementos para a esquerda e insere novo elemento
     * @param no
     * @param midle
     */
    private int toLeft (Node no, int midle) {
        int i = max_keys - midle;
        // arreda a esquerda
        for(; i > 0 && (key_idAux < no.key_id[i - 1] || (key_idAux == no.key_id[i - 1] && key_posAux < no.key_pos[i - 1])); i--){
            no.key_id[i] = no.key_id[i - 1];
            no.key_pos[i] = no.key_id[i - 1];
            no.children[i+1] = no.children[i];
        }

        return i;
    }

    /**
     * 
     * @param no
     * @param i
     */
    private void insert (Node no, int i) {
        // Insere o novo elemento
        no.key_id[i] = key_idAux;
        no.key_pos[i] = key_posAux;
        no.children[i+1] = aux;
        no.n++;
    }
    private void swap () {

    }
}
