package interfaces;

import entities.*;
import java.util.*;
import java.io.File;
import database.CRUD;
import database.Huffman.*;
import database.LZW.LZWDecoder;
import database.LZW.LZWEncoder;
import os_procesess.*;

public class Contas{
    private CRUD<Conta> dados;

    public Contas() {
        try {
            dados = new CRUD<>("Contas", Conta.class.getConstructor());
        } catch (Exception e) {
            System.out.print("ERRO!: ");
            e.printStackTrace();
        }
    }

    
    //create
    public void create() {
        try {
            String nomePessoa;       
            String nomeUsuario;
            String cidade;
            String senha;
    
            List<String> emails = new ArrayList<String>();
    
            char[] cpf;
    
            float saldo;
    
            // cadastro do nome
            System.out.print("Insira seu nome completo: ");
            nomePessoa = IO.readString();
    
            // cadastro do cpf
            System.out.print("\n Insira seu cpf: ");
            cpf = (IO.readString()).toCharArray();
    
            // cadastro da cidade
            System.out.print("\n Insira o nome da cidade que mora: ");
            cidade = IO.readString();
    
            // entrada do(s) email(s)
            do {
                System.out.print("Insira seu email: ");
                emails.add(IO.readString());
            } while(Console.ask("Gostaria de inserir mais um email?"));
    
            // cadastro do nome de usuario
            System.out.print("\n Insira seu nome de usuario: ");
            nomeUsuario = IO.readString();
    
            // cadastro de senha
            System.out.print("Insira sua senha: ");
            senha = IO.readString();
    
            // ------------------------------ fim do cadastro do usuário ------------------------------ //
    
            // entrada do valor inicial do saldo
            System.out.println("Parabéns! Você criou sua conta no melhor banco mineiro!");
            System.out.print("Insira o valor inicial do saldo de sua conta: ");
            saldo = IO.readFloat();


            Conta c = new Conta(-1,nomePessoa, emails, nomeUsuario, senha, cpf, cidade,0, saldo);
            dados.create(c);


        } catch (Exception e) {
            System.out.println("ERRO AO CRIAR UMA NOVA CONTA: ");
            e.printStackTrace();
        }

    }

    //read
    public Conta read () {
        Conta c = new Conta();
        try {
            // obter id
            System.out.print("Insira o ID que procura: ");
            int id = IO.readInt();

            c = dados.read(id);

            if(c == null || c.getId() == -1)
                throw new Exception("A conta não existe! Não foi possível encontrar uma conta com esse id: " + id);
            
            System.out.println(c.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    // login read
    public Conta login_read () {
        Conta c = new Conta();
        try {
            // obter id
            System.out.print("Insira seu nome de usuário: ");
            String user = IO.readString();
            
            c = dados.read(user);

            if(c == null || c.getId() == -1)
                throw new Exception("A conta não existe! Não foi possível encontrar uma conta com esse id: " + user);
            
            
            boolean flag = true;

            while (flag) {
                System.out.print("Insira sua senha: ");
                String password = IO.readString();

                if(password.equals(c.getSenha()))
                    flag = false;
                else 
                    System.out.println("Senha incorreta!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    //update
    public void update (Conta c) {
        try {   
            // update do nome?
            if(Console.ask("Gostaria de modificar seu nome?")){
                System.out.print("Insira seu nome completo: ");
                c.setNomePessoa(IO.readString());
            }
    
            // cadastro da cidade
            if(Console.ask("Gostaria de modificar a cidade em que mora?")){
                System.out.print("\n Insira o nome da cidade que mora: ");
                c.setCidade(IO.readString());
            }
            
            // entrada do(s) email(s)
            if(Console.ask("Gostaria de modificar seus e-mails?")){
                List<String> emails = new ArrayList<String>();
                System.out.println("Insira todos os seus emails relacionados a erra conta!");
                
                do {
                    System.out.print("Insira seu email: ");
                    emails.add(IO.readString());
                } while(Console.ask("Gostaria de inserir mais um email?"));

                c.setEmail(emails);
            }
    
            // cadastro de senha
            if(Console.ask("Gostaria de modificar sua senha?")){
                System.out.print("Insira sua senha: ");
                c.setSenha(IO.readString());
            }
            
            
            dados.update(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //delete
    public void delete (Conta c) {
        try {
            // obter id
            dados.delete(c.getId());

            System.out.print("O UAIBANK te deseja o melhor! Agradecemos por uso de nossos serviços!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // trasnferencia
    public void transferencia (Conta c) {
        // obter conta para quem está transferindo
        System.out.println("Insira o id da conta que está transferindo: ");
        int id = IO.readInt();

        System.out.println("Insira o valor que deseja transferir: ");
        float valor = IO.readFloat();

        try {
            Conta r = dados.read(id);
            
            c.setSaldoConta(c.getSaldoConta() - valor);
            r.setSaldoConta(r.getSaldoConta() + valor);
            

            c.setTransfRealizadas(c.getTransfRealizadas() + 1);
            r.setTransfRealizadas(r.getTransfRealizadas() + 1);

            dados.update(c);
            dados.update(r);

            //
            /*File rac = new File("./db/HT.txt");
            int lastT;

            if(rac.length() == 0)
                lastT = 0;
            else 
                lastT =  IO.readInt(rac); 

            rac.seek(0);
            rac.writeInt(++lastT);

            rac.seek(rac.readLong());
            rac.writeInt(lastT);
            rac.writeInt(c.getId());
            rac.writeInt(id);
            rac.writeFloat(valor);

            rac.close();*/

            System.out.print("Transferência realizada!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // histórico
    public void compactAccount () {
        System.out.println("==================================================================\n");

        try {
            long bflib = dados.rac.length() * 8;
            String bfToString = dados.toString();

            // tempo inicial
            double start, end;

            // using Huffman
            System.out.println("\nComprimindo o arquivo de dados com Huffman...");
            
            start = new Date().getTime();
            Huffman h = new Huffman();
            String encoded = h.encode(bfToString, "Contas");
            end = new Date().getTime();

            System.out.println("\nCompressão sucedida!");
            System.out.println("Tamanho do arquivo original: " + bflib);

            long flib = encoded.length();
            float tc = (float)flib/bflib;
            float fc = (float)bflib/flib;
            
            System.out.println("\nHUFFMAN");
            System.out.println("\t- Tempo de execução: " + (end - start) + " segundos");
            System.out.println("\t- Tamanho final em bits: " + flib);
            System.out.println("\t- Taxa de Compressão: " + tc);
            System.out.println("\t- Fator de Compressão: " + fc);
            System.out.println("\t- Percentual de Redução: " + (100 * (1 - tc)));

            System.out.println("\n\n");


            // using LZW
            
            System.out.println("\nComprimindo o arquivo de dados com LZW...");
            
            start = new Date().getTime();
            int lzwEncoderLength = LZWEncoder.encode(bfToString, "Contas");
            end = new Date().getTime();

            System.out.println("\nCompressão sucedida!");
            System.out.println("Tamanho do arquivo original: " + bflib);

            tc = (float) lzwEncoderLength / bflib;
            fc = (float) bflib / lzwEncoderLength;
            
            System.out.println("\nLZW");
            System.out.println("\t- Tempo de execução: " + (end - start) + " segundos");
            System.out.println("\t- Tamanho final em bits: " + lzwEncoderLength);
            System.out.println("\t- Taxa de Compressão: " + tc);
            System.out.println("\t- Fator de Compressão: " + fc);
            System.out.println("\t- Percentual de Redução: " + (100 * (1 - tc)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n==================================================================\n");
    }

    public void descompactAccount () {
        System.out.println("==================================================================\n");

        try {
            

            System.out.println("\nInsira a versão que deseja descompactar: ");
            double version = IO.readDouble();

            File arq = new File(("./db/Huffman/Contas" + version  + ".db"));
            long bflib = arq.length() * 8;

            // tempo inicial
            double start, end;

            // using Huffman
            System.out.println("\nDescomprimindo o arquivo de dados com Huffman...");
            
            start = new Date().getTime();
            Huffman h = new Huffman();
            String decoded = h.decode("Contas", (long)version, bflib);            
            end = new Date().getTime();

            System.out.println("\nDescompressão sucedida!");
            System.out.println("Tamanho do arquivo codificado: " + bflib);

            long flib = decoded.length();
            float tc = (float)flib/bflib;
            float fc = (float)bflib/flib;
            
            System.out.println("\nHUFFMAN");
            System.out.println("\t- Tempo de execução: " + (end - start) + " segundos");
            System.out.println("\t- Tamanho do arquivo descompactado: " + flib);
            System.out.println("\t- Taxa de Compressão: " + tc);
            System.out.println("\t- Fator de Compressão: " + fc);
            System.out.println("\t- Perccentual de Redução: " + (100 * (1 - tc)));

            // using LZW

            arq = new File(("./db/LZW/Contas" + version  + ".db"));
            bflib = arq.length() * 8;

            System.out.println("\nDescomprimindo o arquivo de dados com LZW...");
            
            start = new Date().getTime();
            decoded = LZWDecoder.decode("Contas", (long) version);            
            end = new Date().getTime();

            System.out.println("\nDescompressão sucedida!");
            System.out.println("Tamanho do arquivo codificado: " + bflib);

            flib = decoded.length();
            tc = (float) flib / bflib;
            fc = (float) bflib / flib;
            
            System.out.println("\nLZW");
            System.out.println("\t- Tempo de execução: " + (end - start) + " segundos");
            System.out.println("\t- Tamanho do arquivo descompactado: " + flib);
            System.out.println("\t- Taxa de Compressão: " + tc);
            System.out.println("\t- Fator de Compressão: " + fc);
            System.out.println("\t- Perccentual de Redução: " + (100 * (1 - tc)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n==================================================================\n");
    }
}
