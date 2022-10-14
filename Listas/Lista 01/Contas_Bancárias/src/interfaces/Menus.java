package interfaces;
import os_procesess.*;

public class Menus {
    private Contas account;

    public Menus() {
        try {
            account = new Contas();
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }

    /* Método da tela de menu inicial */
    public void telaInicial() {
        boolean flag = true;

        while(flag) {
            Console.clearConsole();

            System.out.println("UAIBANK 1.0");
            System.out.println("===========================================\n");
            System.out.println("Login Menu\n");
            System.out.println("1.) Access account");
            System.out.println("2.) Update account");
            System.out.println("3.) Create a new account");
            System.out.println("4.) Delete account");
            System.out.println("5.) Transfer money to another account");
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
                    account.create();
                    break;
                default:
                    System.out.println("Invalid Option!");
                    break;
            }

            
        } // end of while
    } // end of Login menu

    /* Método da tela de login */
    public void login() {
        //inserir ou nome de usuário


    }

    /* Método da tela de menus */
}
