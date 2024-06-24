import entity.RentalAgreement;
import entity.Tool;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import util.Util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

@Command(name = "ToolRental", mixinStandardHelpOptions = true, version = "1.0", description = "Tool Rental Application")
public class Application implements Runnable {

    @Option(names = {"-c", "--code"}, description = "Tool code", required = true)
    private static String toolCode;

    @Option(names = {"-r", "--rentalDays"}, description = "Number of rental days", required = true)
    private static int rentalDays;

    @Option(names = {"-d", "--discountPercent"}, description = "Discount percent", required = true)
    private static int discountPercent;

    @Option(names = {"-o", "--checkoutDate"}, description = "Checkout date (yyyy-MM-dd)", required = true)
    private static String checkoutDate;

    private static Map<String, Tool> tools = Util.inventoryMetadata();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter tool code:");
            toolCode = scanner.nextLine().trim();

            System.out.println("Enter rental days:");
            rentalDays = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Enter discount percent:");
            discountPercent = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Enter checkout date (yyyy-MM-dd):");
            checkoutDate = scanner.nextLine().trim();

            String[] arguments = new String[] {
                    "-c", toolCode,
                    "-r", String.valueOf(rentalDays),
                    "-d", String.valueOf(discountPercent),
                    "-o", checkoutDate
            };
            int exitCode = new CommandLine(new Application()).execute(arguments);
            if (exitCode != 0) {
                System.err.println("Execution failed with exit code " + exitCode);
            }
        }
    }

    @Override
    public void run() {
        try {
            validateInput();
            LocalDate parsedCheckoutDate = LocalDate.parse(checkoutDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Tool tool = tools.get(toolCode);
            RentalAgreement rentalAgreement = new RentalAgreement(tool, parsedCheckoutDate, rentalDays, discountPercent);
            rentalAgreement.printAgreement();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void validateInput() {
        if (!tools.containsKey(toolCode)) {
            throw new IllegalArgumentException("Invalid tool code. Available codes are: " + tools.keySet());
        }
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }
    }
}
