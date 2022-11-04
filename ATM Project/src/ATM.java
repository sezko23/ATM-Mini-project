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
        accounts.add(new Account("nikinikolov", new CreditCard("5678", 2000)));
    }

    public int isAccountInSystem(String name) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getName().equals(name)) return i;
        }
        return -1;
    }

    public void run() {
        initializeAccounts();
        initializeCommands();
        Scanner scanner = new Scanner(System.in);
        String command = "";
        String exitMsg = " or enter 0 to exit to the Main Menu:";
        double amount;
        while (true) {
            System.out.println("Welcome to the ATM application! Please enter your name or enter exit to quit the application: ");
            command = scanner.next();
            if(command.equals("exit")) {
                System.out.println("Exiting ATM application...");
                return;
            }
            while (isAccountInSystem(command) < 0) {
                System.out.println("Name not found in system. Please enter new name or enter exit to quit the application:");
                command = scanner.next();
                if(command.equals("exit")){
                    System.out.println("Exiting ATM application...");
                    return;
                }
            }
            int i = isAccountInSystem(command);
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
                    System.out.println("Enter the amount of money you want to withdraw" + exitMsg);
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    if(amount == 0) break;
                    while(amount < 0){
                        System.out.println("You cant withdraw a negative amount! Please enter a positive amount" + exitMsg);
                        amount=scanner.nextDouble();
                        scanner.nextLine();
                    }
                    if(amount == 0) break;
                    while (accounts.get(i).getCard().getBalance() < amount || amount < 0) {
                        System.out.println("You cant withdraw more than you have or negative amount! Please enter new amount" + exitMsg);
                        amount = scanner.nextDouble();
                        scanner.nextLine();
                    }
                    if(amount == 0) break;
                    accounts.get(i).getCard().withdraw(amount);
                    System.out.println("Successfully withdrew " + amount + " lv.");
                }
                if (command.equals("deposit")) {
                    System.out.println("Enter the amount of money you want to deposit" + exitMsg);
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    if(amount == 0) break;
                    while(amount < 0){
                        System.out.println("You cant deposit a negative amount! Please enter a positive amount" + exitMsg);
                        amount=scanner.nextDouble();
                        scanner.nextLine();
                    }
                    if(amount == 0) break;
                    accounts.get(i).getCard().deposit(amount);
                    System.out.println("Successfully deposited " + amount + " lv.");
                }
                if (command.equals("changepin")) {
                    System.out.println("Enter new PIN" + exitMsg);
                    command = scanner.next();
                    if(command.equals("0")) break;
                    while(command.length() != 4 || !command.chars().allMatch(Character::isDigit)){
                        System.out.println("PIN must be 4-digits long! Example PIN: \"2304\". Please enter a valid input" + exitMsg);
                        command=scanner.next();
                        if(command.equals("0")) break;
                    }
                    if(command.equals("0")) break;
                    accounts.get(i).getCard().setPIN(command);
                    System.out.println("PIN changed successfully!");
                }
                if (command.equals("transfer")) {
                    System.out.println("Enter the name of the receiver" + exitMsg);
                    command = scanner.next();
                    if(command.equals("0")) break;
                    while (isAccountInSystem(command) < 0) {
                        System.out.println("Name not found in system. Please enter new name" + exitMsg);
                        command = scanner.next();
                        if(command.equals("0")) break;
                    }
                    if(command.equals("0")) break;
                    int j = isAccountInSystem(command);
                    if (i != j) {
                        System.out.println("Enter the amount you want to send to " + accounts.get(j).getName());
                        amount = scanner.nextDouble();
                        scanner.nextLine();
                        while(amount <= 0){
                            System.out.println("You cant send negative amount! Please enter a positive amount:");
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
                System.out.println("Please enter what would you like to do now:");
                command = scanner.next();
                while (!commands.contains(command)) {
                    System.out.println("Invalid command! Please enter a valid command:");
                    command = scanner.next();
                }
            }
        }
    }
}

