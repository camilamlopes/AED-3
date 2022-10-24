package interfaces;

import entities.Conta;
import os_procesess.*;

public class Menus {
    private Contas account;
    private Conta entity_account;

    public Menus() {
        try {
            entity_account = new Conta();
            account = new Contas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    /* Método da tela de menu inicial */
    public void telaInicial() {
        boolean flag = true;

        while(flag) {
            Console.clearConsole();

            System.out.println("UAIBANK 1.0");
            System.out.println("===========================================\n");
            System.out.println("Menu\n");
            System.out.println("1.) Acessar informações da conta");
            System.out.println("2.) Update account");
            System.out.println("3.) Deletar Conta");
            System.out.println("5.) Fazer transferência");
            System.out.println("\n -------------------------");
            System.out.println("\n0.) Exit");
            System.out.print("\nInsert your option: ");

            int option = IO.readInt();

            switch (option) {
                case 0:
                    flag = false;
                    break;
                case 1:
                    account.read();
                    break;
                case 2:
                    account.update(entity_account);
                    break;
                default:
                    System.out.println("Invalid Option!");
                    break;
            }

            
        } // end of while
    } // end of Login menu

    /* Método da tela de login */
    public void login() {
        boolean flag = true;

        while(flag) {
            Console.clearConsole();

            System.out.println("UAIBANK 1.0");
            System.out.println("===========================================\n");
            System.out.println("Menu Inicial\n");
            System.out.println("1.) Entrar em uma conta");
            System.out.println("2.) Criar uma conta");
            System.out.println("\n -------------------------");
            System.out.println("\n0.) Exit");
            System.out.print("\nInsert your option: ");

            int option = IO.readInt();

            switch (option) {
                case 0:
                    flag = false;
                    break;
                case 1:
                    entity_account = account.login_read();
                    telaInicial();
                    option = 0;
                    break;
                case 2:
                    account.create();
                    break;
                default:
                    System.out.println("Invalid Option!");
                    break;
            }

            
        } // end of while


    }

    /* Método da tela de menus */
}
