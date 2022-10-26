import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
    private final ArrayList<Account> accounts = new ArrayList<>();
    private final ArrayList<String> commands = new ArrayList<>();

    public void initializeCommands() {
        commands.add("withdraw");
        commands.add("deposit");
        commands.add("changepin");
        commands.add("transfer");
        commands.add("balance");
        commands.add("logout");
        commands.add("exit");
    }

    public void initializeAccounts() {
        accounts.add(new Account("sezermehmed", new CreditCard("1234",0)));
        accounts.add(new Account("ivanivanov", new CreditCard("0000",1000)));
        accounts.add(new Account("georgigeorgiev", new CreditCard("9876",500)));
    }

    public boolean isAccountInSystem(String name) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getName().equals(name)) return true;
        }
        return false;
    }

    public void run() {
        initializeAccounts();
        initializeCommands();
        Scanner scanner = new Scanner(System.in);
        String command = "";
        double amount;
        while (true) {
            System.out.println("Welcome to the ATM application! Please enter your name: ");
            command = scanner.next();
            while (!isAccountInSystem(command)) {
                System.out.println("Name not found in system. Please enter new name:");
                command = scanner.next();
            }
            int i = 0;
            if (command.equals("ivanivanov")) {
                i = 1;
            } else if (command.equals("georgigeorgiev")) {
                i = 2;
            }
            System.out.println("Welcome " + accounts.get(i).getName() + "!");
            System.out.println("Please enter your PIN:");
            int tries = 3;
            command = scanner.next();
            while (!(command.equals(accounts.get(i).getCard().getPIN()))) {
                --tries;
                if (tries == 0) {
                    System.out.println("Entered wrong PIN 3 times. Card blocked. Exiting...");
                    return;
                }
                System.out.println("Wrong PIN. You have " + tries + " tries left");
                command = scanner.next();
            }
            System.out.print("Here are the available commands: ");
            System.out.println(commands);
            System.out.println("Enter command:");
            command = scanner.next();
            while (!commands.contains(command)) {
                System.out.println("Invalid command! Please enter a valid command:");
                command = scanner.next();
            }
            while (!command.equals("logout")) {
                if (command.equals("withdraw")) {
                    System.out.println("Enter the amount of money you want to withdraw:");
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    while(amount < 0){
                        System.out.println("You cant withdraw a negative amount! Please enter a positive amount:");
                        amount=scanner.nextDouble();
                    }
                    while (accounts.get(i).getCard().getBalance() < amount) {
                        System.out.println("You cant withdraw more than you have! Please enter new amount:");
                        amount = scanner.nextDouble();
                        scanner.nextLine();
                    }
                    accounts.get(i).getCard().withdraw(amount);
                    System.out.println("Successfully withdrew " + amount + " lv.");
                }
                if (command.equals("deposit")) {
                    System.out.println("Enter the amount of money you want to deposit:");
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    while(amount < 0){
                        System.out.println("You cant deposit a negative amount! Please enter a positive amount:");
                        amount=scanner.nextDouble();
                    }
                    accounts.get(i).getCard().deposit(amount);
                    System.out.println("Successfully deposited " + amount + " lv.");
                }
                if (command.equals("changepin")) {
                    System.out.println("Enter new PIN:");
                    command = scanner.next();
                    while(command.length() != 4 || !command.chars().allMatch(Character::isDigit)){
                        System.out.println("PIN must be 4-digits long! Example PIN: \"2304\". Please enter a valid input!");
                        command=scanner.next();
                    }
                    accounts.get(i).getCard().setPIN(command);
                    System.out.println("PIN changed successfully!");
                }
                if (command.equals("transfer")) {
                    System.out.println("Enter the name of the receiver:");
                    command = scanner.next();
                    while (!isAccountInSystem(command)) {
                        System.out.println("Name not found in system. Please enter new name:");
                        command = scanner.next();
                    }
                    int j = 0;
                    if (command.equals("ivanivanov")) {
                        j = 1;
                    } else if (command.equals("georgigeorgiev")) {
                        j = 2;
                    }
                    if (i != j) {
                        System.out.println("Enter the amount you want to send to " + accounts.get(j).getName());
                        amount = scanner.nextDouble();
                        scanner.nextLine();
                        while(amount < 0){
                            System.out.println("You cant send negative amount! Please enter a valid amount:");
                            amount = scanner.nextDouble();
                        }
                        if(accounts.get(i).getCard().getBalance() >= amount) {
                            accounts.get(j).getCard().deposit(amount);
                            accounts.get(i).getCard().withdraw(amount);
                            System.out.println("Successfully transferred " + amount + " lv. to " + accounts.get(j).getName() + ".");
                        }else{
                            System.out.println("The amount you want to send is more than your current balance! Transfer blocked.");
                        }
                    } else {
                        System.out.println("Method transfer must be applied to another account!");
                    }
                }
                if (command.equals("balance")) {
                    System.out.println("Current balance " + accounts.get(i).getCard().getBalance() + " lv.");
                }
                if (command.equals("exit")) {
                    System.out.println("Exiting ATM application...");
                    return;
                }
                System.out.println("Enter what would you like to do now:");
                command = scanner.next();
                while (!commands.contains(command)) {
                    System.out.println("Invalid command! Please enter a valid command:");
                    command = scanner.next();
                }
            }
        }
    }
}

