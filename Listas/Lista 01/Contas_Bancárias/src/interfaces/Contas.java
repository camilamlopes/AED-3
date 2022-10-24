package interfaces;
import entities.*;
import database.CRUD;
import java.util.*;
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
    public void read () {
        try {
            // obter id
            System.out.print("Insira o ID que procura: ");
            int id = IO.readInt();

            Conta c = dados.read(id);

            if(c == null || c.getId() == -1)
                throw new Exception("A conta não existe! Não foi possível encontrar uma conta com esse id: " + id);
            
            System.out.println(c.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void delete (){
        try {
            // obter id
            System.out.print("Insira o ID que gostaria de deletar: ");
            int id = IO.readInt();

            dados.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
