package com.mycompany.project3task0;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import info.blockchain.api.blockexplorer.*;
import java.util.Scanner;
/**
 * This class is designed as a block chain client.
 * @author Jeremy
 */
public class BlockChainClientApp {
    public static void main(String[] args) throws Exception {
        // instantiate a block explorer
        BlockExplorer blockExplorer = new BlockExplorer();
        LatestBlock block = blockExplorer.getLatestBlock();
        
        while(true) {
            
            System.out.println("---------- Menu ---------");
            System.out.println("- 1. Get hash code of recent block");
            System.out.println("- 2. Get number of transactions of recent block");
            System.out.println("- 3. Quit");
            Scanner sc = new Scanner(System.in);
            
            int opt = Integer.parseInt(sc.nextLine());
            
            switch(opt) {
                case 1:
                    System.out.println("Block hash: ");
                    System.out.println(block.hashCode());
                    break;
                case 2:
                    System.out.println("Number of transactions: ");
                    System.out.println(block.getTransactionIndexes().size());
                    break;
                case 3:
                    return;
                default:
                    System.out.println("The option is invalid, please try again!");
            }
        }
    }
}
