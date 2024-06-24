package entity;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import util.Util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RentalAppTest extends TestCase {

	private Map<String, Tool> tools;

	@Before
	public void setUp() {
		tools = Util.inventoryMetadata();
	}

	@Test
	public void testInvalidDiscount() {
		Tool tool = tools.get("JAKR");
		LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
		int rentalDays = 5;
		int discountPercent = 101;

		try {
			new RentalAgreement(tool, checkoutDate, rentalDays, discountPercent);
			fail("Expected IllegalArgumentException for discount over 100%");
		} catch (IllegalArgumentException e) {
			assertEquals("Discount percent must be between 0 and 100.", e.getMessage());
		}
	}

	@Test
	public void testLADWCheckout() {
		Tool tool = tools.get("LADW");
		LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
		int rentalDays = 3;
		int discountPercent = 10;

		RentalAgreement rentalAgreement = new RentalAgreement(tool, checkoutDate, rentalDays, discountPercent);
		rentalAgreement.printAgreement();

		assertEquals(3, rentalAgreement.getRentalDays());
		assertEquals("07/02/20", rentalAgreement.getCheckoutDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
		assertEquals("07/05/20", rentalAgreement.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
		assertEquals(2, rentalAgreement.getChargeDays());
		assertEquals(3.98, rentalAgreement.getPreDiscountCharge(), 0.01);
		assertEquals(0.40, rentalAgreement.getDiscountAmount(), 0.01);
		assertEquals(3.58, rentalAgreement.getFinalCharge(), 0.01);
	}

	@Test
	public void testCHNSCheckout() {
		Tool tool = tools.get("CHNS");
		LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
		int rentalDays = 5;
		int discountPercent = 25;

		RentalAgreement rentalAgreement = new RentalAgreement(tool, checkoutDate, rentalDays, discountPercent);
		rentalAgreement.printAgreement();

		assertEquals(5, rentalAgreement.getRentalDays());
		assertEquals("07/02/15", rentalAgreement.getCheckoutDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
		assertEquals("07/07/15", rentalAgreement.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
		assertEquals(3, rentalAgreement.getChargeDays());
		assertEquals(4.47, rentalAgreement.getPreDiscountCharge(), 0.01);
		assertEquals(1.12, rentalAgreement.getDiscountAmount(), 0.01);
		assertEquals(3.35, rentalAgreement.getFinalCharge(), 0.01);
	}

	@Test
	public void testJAKDCheckout() {
		Tool tool = tools.get("JAKD");
		LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
		int rentalDays = 6;
		int discountPercent = 0;

		RentalAgreement rentalAgreement = new RentalAgreement(tool, checkoutDate, rentalDays, discountPercent);
		rentalAgreement.printAgreement();

		assertEquals(6, rentalAgreement.getRentalDays());
		assertEquals("09/03/15", rentalAgreement.getCheckoutDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
		assertEquals("09/09/15", rentalAgreement.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
		assertEquals(3, rentalAgreement.getChargeDays());
		assertEquals(8.97, rentalAgreement.getPreDiscountCharge(), 0.01);
		assertEquals(0, rentalAgreement.getDiscountAmount(), 0.01);
		assertEquals(8.97, rentalAgreement.getFinalCharge(), 0.01);
	}

	@Test
	public void testJAKRCheckout9Days() {
		Tool tool = tools.get("JAKR");
		LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
		int rentalDays = 9;
		int discountPercent = 0;

		RentalAgreement rentalAgreement = new RentalAgreement(tool, checkoutDate, rentalDays, discountPercent);
		rentalAgreement.printAgreement();

		assertEquals(9, rentalAgreement.getRentalDays());
		assertEquals("07/02/15", rentalAgreement.getCheckoutDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
		assertEquals("07/11/15", rentalAgreement.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
		assertEquals(5, rentalAgreement.getChargeDays());
		assertEquals(14.95, rentalAgreement.getPreDiscountCharge(), 0.01);
		assertEquals(0.00, rentalAgreement.getDiscountAmount(), 0.01);
		assertEquals(14.95, rentalAgreement.getFinalCharge(), 0.01);
	}

	@Test
	public void testJAKRCheckoutDiscount() {
		Tool tool = tools.get("JAKR");
		LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
		int rentalDays = 4;
		int discountPercent = 50;

		RentalAgreement rentalAgreement = new RentalAgreement(tool, checkoutDate, rentalDays, discountPercent);
		rentalAgreement.printAgreement();

		assertEquals(4, rentalAgreement.getRentalDays());
		assertEquals("07/02/20", rentalAgreement.getCheckoutDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
		assertEquals("07/06/20", rentalAgreement.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
		assertEquals(1, rentalAgreement.getChargeDays());
		assertEquals(2.99, rentalAgreement.getPreDiscountCharge(), 0.01);
		assertEquals(1.50, rentalAgreement.getDiscountAmount(), 0.01);
		assertEquals(1.49, rentalAgreement.getFinalCharge(), 0.01);
	}
}
