package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ATMProcessor {

    private String filePath;
    private static Scanner scanner = new Scanner(System.in);

    public ATMProcessor(String filePath) {
        this.filePath = filePath;
    }

    private void writeDataToFile(List<String> newData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : newData) {
                bw.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readDataFromFile() {
        List<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void updateBalance(String cardNumber, double amount, boolean isWithdrawal) {
        List<String> newData = new ArrayList<>();
        List<String> currentData = readDataFromFile();
        for (String line : currentData) {
            String[] parts = line.split(",");
            if (parts.length >= 2 && parts[0].equals(cardNumber)) {
                double currentBalance = Double.parseDouble(parts[1]);
                if (isWithdrawal && currentBalance < amount) {
                    System.out.println("Không đủ số dư để rút.");
                    newData.add(line);
                    continue;
                }
                currentBalance = isWithdrawal ? currentBalance - amount : currentBalance + amount;
                parts[1] = String.valueOf(currentBalance);
            }
            newData.add(String.join(",", parts));
        }
        writeDataToFile(newData);
    }

    public void withdrawFromAccount(String cardNumber, double amount, boolean isWithdrawal) {
        StringBuilder newAccountInfo = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(cardNumber)) {
                    double currentBalance = Double.parseDouble(parts[1]);
                    if (isWithdrawal && currentBalance < amount) {
                        System.out.println("Không đủ số dư để rút.");
                        newAccountInfo.append(line).append("\n");
                        continue;
                    }
                    currentBalance = isWithdrawal ? currentBalance - amount : currentBalance + amount;
                    parts[1] = String.valueOf(currentBalance);
                }
                newAccountInfo.append(String.join(",", parts)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(newAccountInfo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void depositToAccount(String cardNumber, double amount) {
        updateBalance(cardNumber, amount, false);
    }

    public void processWithdrawal() {
        System.out.print("Nhập số thẻ: ");
        String cardNumber = scanner.nextLine();

        try {
            if (isCardNumberExists(cardNumber)) {
                System.out.println("Thông tin số dư cho số thẻ " + cardNumber + ":");
                displayAccountInfo(cardNumber);

                double withdrawalAmount = inputValidWithdrawalAmount();

                // Kiểm tra xem có đủ số dư để rút không
                if (isSufficientBalance(cardNumber, withdrawalAmount)) {
                    // Thực hiện rút tiền và cập nhật số dư
                    withdrawFromAccount(cardNumber, withdrawalAmount, true);

                    // Hiển thị thông tin số dư sau khi rút
                    System.out.println("Thông tin số dư sau khi rút:");
                    displayAccountInfo(cardNumber);
                    System.exit(0);
                } else {
                    System.out.println("Số dư không đủ để rút số tiền này.");
                }
            } else {
                System.out.println("Số thẻ không tồn tại trong dữ liệu.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processDeposit() {
        System.out.print("Nhập số thẻ: ");
        String cardNumber = scanner.nextLine();

        try {
            if (isCardNumberExists(cardNumber)) {
                System.out.println("Thông tin số dư cho số thẻ " + cardNumber + ":");
                displayAccountInfo(cardNumber);

                double depositAmount = inputValidDepositAmount();

                // Thực hiện nạp tiền và cập nhật số dư
                depositToAccount(cardNumber, depositAmount);

                System.out.println("Thông tin số dư sau khi nạp tiền:");
                displayAccountInfo(cardNumber);
            } else {
                System.out.println("Số thẻ không tồn tại trong dữ liệu.");               
                return; 
                // Thêm return để kết thúc phương thức nếu có lỗi.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isSufficientBalance(String cardNumber, double withdrawalAmount) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(cardNumber)) {
                    double currentBalance = Double.parseDouble(parts[1]);
                    return currentBalance >= withdrawalAmount;
                }
            }
        }
        return false;
    }

    private double inputValidWithdrawalAmount() {
        double withdrawalAmount = 0.0;
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print("Nhập số tiền muốn rút: ");
            try {
                withdrawalAmount = Double.parseDouble(scanner.nextLine());
                validAmount = true;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
            }
        }
        return withdrawalAmount;
    }

    private double inputValidDepositAmount() {
        double depositAmount = 0.0;
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print("Nhập số tiền muốn nạp: ");
            try {
                depositAmount = Double.parseDouble(scanner.nextLine());
                validAmount = true;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
            }
        }
        return depositAmount;
    }

    private boolean isCardNumberExists(String cardNumber) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(cardNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void displayAccountInfo(String cardNumber) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(cardNumber)) {
                    System.out.println("Card Number: " + parts[0] + ", Balance: " + parts[1]);
                    return;
                }
            }
        }
        System.out.println("Không tìm thấy thông tin cho số thẻ " + cardNumber);
    }

    public void transferMoney(String sourceCardNumber, String targetCardNumber, double amount) {
        try {
            if (isCardNumberExists(sourceCardNumber) && isCardNumberExists(targetCardNumber)) {
                System.out.println("Thông tin số dư trước khi chuyển tiền:");
                displayAccountInfo(sourceCardNumber);
                displayAccountInfo(targetCardNumber);

                if (isSufficientBalance(sourceCardNumber, amount)) {
                    // Rút tiền từ số thẻ nguồn
                    withdrawFromAccount(sourceCardNumber, amount, true);

                    // Nạp tiền vào số thẻ đích
                    depositToAccount(targetCardNumber, amount);

                    System.out.println("Chuyển tiền thành công.");

                    System.out.println("Thông tin số dư sau khi chuyển tiền:");
                    displayAccountInfo(sourceCardNumber);
                    displayAccountInfo(targetCardNumber);
                } else {
                    System.out.println("Số dư không đủ để thực hiện chuyển tiền.");
                }
            } else {
                System.out.println("Số thẻ nguồn hoặc số thẻ đích không tồn tại trong dữ liệu.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processTransfer() {
        System.out.print("Nhập số thẻ nguồn: ");
        String sourceCardNumber = scanner.nextLine();

        try {
            if (isCardNumberExists(sourceCardNumber)) {
                System.out.print("Nhập số thẻ nhận tiền: ");
                String targetCardNumber = scanner.nextLine();

                // Kiểm tra xem số thẻ nhận tiền có tồn tại không
                if (isCardNumberExists(targetCardNumber)) {
                    System.out.println("Thông tin số dư trước khi chuyển tiền:");
                    displayAccountInfo(sourceCardNumber);
                    displayAccountInfo(targetCardNumber);

                    double transferAmount = inputValidTransferAmount();

                    // Thực hiện chuyển tiền và cập nhật số dư
                    transferMoney(sourceCardNumber, targetCardNumber, transferAmount);

                    System.out.println("Chuyển tiền thành công.");
                } else {
                    System.out.println("Số thẻ nhận tiền không tồn tại trong dữ liệu.");
                }
            } else {
                System.out.println("Số thẻ nguồn không tồn tại trong dữ liệu.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double inputValidTransferAmount() {
        double transferAmount = 0.0;
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print("Nhập số tiền muốn chuyển: ");
            try {
                transferAmount = Double.parseDouble(scanner.nextLine());
                validAmount = true;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
            }
        }
        return transferAmount;
    }

}
