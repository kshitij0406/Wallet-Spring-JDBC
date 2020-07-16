package com.org.vnit.training.ui;

import com.org.vnit.training.bean.Person;
import com.org.vnit.training.dao.Server;
import com.org.vnit.training.dao.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("appContext.xml");
        Server server = context.getBean("server", Server.class);
        Scanner scan = new Scanner(System.in);
        Integer choice, mobileNumber, option, amount;
        String name;
        Person p;
        while (true) {
            System.out.println(" 1. Signup \n 2. Login \n 3. EXIT");
            choice = scan.nextInt();

            if (choice == 1) {
                p = server.addPerson();
            } else if (choice == 3) {
                break;
            } else if (choice == 2) {
                System.out.println("Enter Mobile Number to login......");
                mobileNumber = scan.nextInt();
                if (!server.verifyUser(mobileNumber)) {
                    System.out.println("Could not verify user");
                    continue;
                }
                p = new Person(mobileNumber);

                while (true) {
                    System.out.println(" 1. Add Money to Wallet \n 2. Withdraw Money from Wallet \n " +
                            "3. View Balance \n 4. Change Name \n 5. Transaction History \n 6. View Details \n 7. LOGOUT");
                    option = scan.nextInt();
                    if (option == 1) {
                        System.out.println("Enter the amount you want to add.");
                        amount = scan.nextInt();
                        p.addMoney(amount, server);
                    } else if (option == 2) {
                        System.out.println("Enter the amount you want to withdraw.");
                        amount = scan.nextInt();
                        p.withdrawMoney(amount, server);
                    } else if (option == 3) {
                        System.out.println("Balance = " + p.viewBalance(server));
                    } else if (option == 4) {
                        scan = new Scanner(System.in);
                        System.out.println("Enter the new name");
                        name = scan.nextLine();
                        p.changeName(name, server);
                    } else if (option == 5) {
                        List<Transaction> l = p.transactionHistory(server);
                        for (Transaction transaction : l) {
                            System.out.println("Date Time: " + transaction.getDateTime() +
                                    "|| Details: " + transaction.getDetails() +
                                    "|| Balance: " + transaction.getBalance());
                        }

                    } else if (option == 6) {
                        p = server.viewDetails(p.getMobileNumber());
                        System.out.println("Mobile Number: " + p.getMobileNumber() +
                                "|| Name: " + p.getName() +
                                "|| Balance: " + p.getBalance());
                    } else if (option == 7) {

                        break;
                    } else {
                        System.out.println("Choose from above options");
                    }
                }

            } else {
                System.out.println("Enter correct Input");
            }

        }


    }

}
