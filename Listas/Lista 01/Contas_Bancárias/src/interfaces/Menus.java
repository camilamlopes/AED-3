package interfaces;
import os_procesess.*;

public class Menus {
    private Contas conta;

    public Menus() {
        try {
            conta = new Contas();
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
            System.out.println("1.) Acessar conta");
            System.out.println("2.) Criar nova conta");
            System.out.println("\n -------------------------");
            System.out.println("\n0.) Acessar conta");
            System.out.print("\nInsira sua opção: ");

            int option = IO.readInt();

            switch (option) {
                case 0:
                    flag = false;
                    break;
                case 1:
                    conta.read();
                    break;
                case 2:
                    conta.create();
                    break;
                default:
                    System.out.println("Opção inválida!");
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
