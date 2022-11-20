package main;

import java.io.*;
import java.util.*;

import entities.*;
import database.CRUD;

public class App {
    
    public static void main(String[] args) throws Exception {
        List<String> email_1 = new ArrayList<String>();
        email_1.add("lopes.camilamoreira@gmail.com");
        email_1.add("camilamoeriral2001@gmail.com"); 
        email_1.add("camilaml2001@gmail.com");


        List<String> email_2 = new ArrayList<String>();
        email_2.add("exemplo@dominio.com"); 
        email_2.add("exemplo@gmail.com");
        char[] cpf = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};

        float saldo = 12345;
        
        
        CRUD<Conta> dados = new CRUD<>("Contas", Conta.class.getConstructor());
        

        Conta c1 = new Conta(-1, "Camila Moreira Lopes", email_1, "camila_lopes", "123456", cpf, "Belo Horizonte", 53, saldo);

        Conta c2 = new Conta(-1, "Nome Pessoa", email_2, "username", "123456", cpf, "Cidade", 100, saldo);   
            
        dados.create(c1);
        dados.create(c2);
        
        

        c2 = dados.read(1);
        if(c2 == null || c2.getId() == -1)
            System.out.println("A conta não existe! Não foi possível encontrar uma conta com esse id: 2");
        else 
            System.out.println("Funcionou");

        dados.delete(1);

        c2 = dados.read(1);
        if(c2 == null || c2.getId() == -1)
            System.out.println("A conta não existe! Não foi possível encontrar uma conta com esse id: 2");
        else 
            System.out.println("Funcionou");


        dados.create(c1);

        c2 = new Conta(c1.getId(), "Camila Lopes", email_1, "camila_lopes", "123456", cpf, "Belo Horizonte", 0, saldo);

        dados.update(c2);

        c2 = new Conta(c1.getId(), "Camila Moreira Lopes Trindade", email_1, "camila_lopes", "123456", cpf, "Belo Horizonte", 0, saldo);

        dados.update(c2);
    }
}