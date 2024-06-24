package entity;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

@Getter
public class RentalAgreement {
    private Tool tool;
    private LocalDate checkoutDate;
    private int rentalDays;
    private int discountPercent;
    private LocalDate dueDate;
    private int chargeDays;
    private double preDiscountCharge;
    private double discountAmount;
    private double finalCharge;

    public RentalAgreement(Tool tool, LocalDate checkoutDate, int rentalDays, int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }
        this.tool = tool;
        this.checkoutDate = checkoutDate;
        this.rentalDays = rentalDays;
        this.discountPercent = discountPercent;
        this.dueDate = checkoutDate.plusDays(rentalDays);
        calculateCharges();
    }

    private void calculateCharges() {
        LocalDate currentDate = checkoutDate.plusDays(1);
        for (int i = 0; i < rentalDays; i++) {
            if (isChargeableDay(currentDate)) {
                chargeDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        preDiscountCharge = round(tool.getDailyCharge() * chargeDays, 2);
        discountAmount = round((discountPercent / 100.0) * preDiscountCharge, 2);
        finalCharge = round(preDiscountCharge - discountAmount, 2);
    }

    private boolean isChargeableDay(LocalDate date) {
        boolean isWeekend = date.getDayOfWeek().getValue() >= 6;
        boolean isHoliday = isHoliday(date);
        if (!tool.isWeekdayCharge() && !isWeekend && !isHoliday) return false;
        if (!tool.isWeekendCharge() && isWeekend) return false;
        if (!tool.isHolidayCharge() && isHoliday) return false;
        return true;
    }

    private boolean isHoliday(LocalDate date) {
        LocalDate independenceDay = LocalDate.of(date.getYear(), 7, 4);
        LocalDate laborDay = LocalDate.of(date.getYear(), 9, 1).with(TemporalAdjusters.firstInMonth(java.time.DayOfWeek.MONDAY));
        if (independenceDay.getDayOfWeek().getValue() == 6) independenceDay = independenceDay.minusDays(1);
        if (independenceDay.getDayOfWeek().getValue() == 7) independenceDay = independenceDay.plusDays(1);
        return date.equals(independenceDay) || date.equals(laborDay);
    }

    public void printAgreement() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy", Locale.US);
        System.out.println("Tool code: " + tool.getCode());
        System.out.println("Tool type: " + tool.getType());
        System.out.println("Tool brand: " + tool.getBrand());
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Checkout date: " + checkoutDate.format(dateFormatter));
        System.out.println("Due date: " + dueDate.format(dateFormatter));
        System.out.println("Daily rental charge: $" + String.format("%.2f", tool.getDailyCharge()));
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: $" + String.format("%.2f", preDiscountCharge));
        System.out.println("Discount percent: " + discountPercent + "%");
        System.out.println("Discount amount: $" + String.format("%.2f", discountAmount));
        System.out.println("Final charge: $" + String.format("%.2f", finalCharge));
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
