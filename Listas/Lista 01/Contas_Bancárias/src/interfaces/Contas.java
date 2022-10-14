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

            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    //update
    //delete
}
