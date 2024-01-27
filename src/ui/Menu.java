/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.util.ArrayList;
import util.Util;

/**
 *
 * @author hangaos
 */
public class Menu {
    
    private String title;
    private ArrayList<String> optionList = new ArrayList();

    public void title(String title) {
        this.title = title;
    }
    
    
    public void addNewOption(String newOption) {

        optionList.add(newOption);
    }

    public void printMenu() {
        if (optionList.isEmpty()) {
            System.out.println("No option in the list");
        }
        System.out.println("WELCOME TO " + title);
        for (String x : optionList) {
            System.out.println(x);
        }
    }

    public int getChoice() {
        int maxOption = optionList.size();
        // lựa chọn lớn nhất là số thứ tự ứng với số mục chọn
        String inputMsg = "Choose [1.." + maxOption + "]: ";
        String errorMsg = "You are required to choose the option 1.." + maxOption;
        return Util.getAnInteger(inputMsg, errorMsg, 1, maxOption);
        // in ra câu nhập: Choose[1..8]: , giả sử có 8 mục chọn trong
        // menu
    }
}
