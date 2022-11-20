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
            System.out.println("===================================================================\n");
            System.out.println("Menu\n");
            System.out.println("1.) Acessar informações da conta");
            System.out.println("2.) Atualizar Conta");
            System.out.println("3.) Deletar Conta");
            System.out.println("4.) Fazer transferência");
            System.out.println("5.) Depositar dinheiro");
            System.out.println("6.) Sacar dinheiro");
            System.out.println("\n -------------------------------");
            System.out.println("\n0.) Exit");
            System.out.print("\nInsira sua opção: ");

            int option = IO.readInt();

            switch (option) {
                case 0:
                    System.out.print("\nObrigada por usar o UAIBANK!\n");
                    flag = false;
                    Console.clearConsole();
                    break;
                case 1:
                    account.read();
                    break;
                case 2:
                    account.update(entity_account);
                    break;
                case 3:
                    account.delete(entity_account);
                    flag = false;
                    break;
                case 4:
                    account.transferencia(entity_account);
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

            System.out.println("UAIBANK 1.0");
            System.out.println("==================================================================\n");
            System.out.println("Menu Inicial\n");
            System.out.println("1.) Entrar em uma conta");
            System.out.println("2.) Criar uma conta");
            System.out.println("3.) Comprimir arquivo de dados");
            System.out.println("4.) Descomprimir arquivo de dados");
            System.out.println("\n --------------------------------");
            System.out.println("\n0.) Exit");
            System.out.print("\nInsira sua opção: ");

            int option = IO.readInt();

            switch (option) {
                case 0:
                    flag = false;
                    Console.clearConsole();
                    break;
                case 1:
                    entity_account = account.login_read();
                    break;
                case 2:
                    account.create();
                    break;
                case 3:
                    account.compactAccount();
                    break;
                case 4:
                    account.descompactAccount();
                    break;
                default:
                    System.out.println("Invalid Option!");
                    break;
            }

            
        } // end of while


    }

    /* Método da tela de menus */
}
