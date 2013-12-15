/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import interfaces.IValidator.NoValidatorException;
import java.util.Calendar;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jakoblautrupnysom
 */
public class ValidatorTest {
    
    Validator instance;
    
    public ValidatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new Validator();
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Tests the cprs for 10000 valid dates after the present one
     * @throws interfaces.IValidator.NoValidatorException 
     */
    @Test
    public void testValidCPRs() throws NoValidatorException {
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 10000; i++) {
            cal.roll(Calendar.DATE, true);
            String cpr = String.format("%02d%02d%s%04d", 
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.MONTH),
                    (""+cal.get(Calendar.YEAR)).substring(2),
                    i);
            assertEquals(true, instance.validate(PersonData.CPR, cpr));
        }
    }
    
    /**
     * Test to ensure that non-number characters are properly discarded
     * @throws interfaces.IValidator.NoValidatorException 
     */
    @Test
    public void testInvalidCPRCharacters() throws NoValidatorException {
        String cpr = "111111110000";
        String badChars = "abcdefghijklmnopqrstuvwxyzæøå'*@-_.:,; ¨^´`+?=()/&%€#!$§\"\\<>";
        for (int i = 0; i < cpr.length(); i++) {
            String testCPR;
            for (int j = 0; j < badChars.length(); j++) {
                String start = (i != 0)? cpr.substring(i-1, i) : "";
                String end = (i != (cpr.length()-1))? cpr.substring(i+1) : "";
                testCPR = start+badChars.charAt(j)+end;
                assertEquals(false, instance.validate(PersonData.CPR, testCPR));
            }
        }
    }
    
    /**
     * Test to ensure that CPRs of bad lengths are properly invalid
     * @throws interfaces.IValidator.NoValidatorException 
     */
    @Test
    public void testInvalidLengthCPRs() throws NoValidatorException {
        for (int i = 0; i < 1000; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j=0; j < i; j++) {
                sb.append("1");
            }
            if (i != 10) {
                assertEquals(false, instance.validate(PersonData.CPR, sb.toString()));
            } else { // Valid length => no test here
                assertEquals(true, instance.validate(PersonData.CPR, sb.toString()));
            }
        }
    }
    
    /**
     * Tests to ensure that all names too long for the system are rendered 
     * properly invalid by the validator
     * @throws interfaces.IValidator.NoValidatorException 
     */
    @Test
    public void testLongNames() throws NoValidatorException {
        for (int i = 0; i < 1000; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < i; j++) {
                sb.append("A");
            }
            if ((0 < i)&&(i < 51)) {
                assertEquals(true, instance.validate(PersonData.NAME, sb.toString()));
            } else {
                assertEquals(false, instance.validate(PersonData.NAME, sb.toString()));
            }
        }
    }
    
    /**
     * Same as above, since the validator isn't really suited to check this 
     * in a more thorough way. Who knows if people have numbered nationalities?
     * @throws interfaces.IValidator.NoValidatorException 
     */
    @Test
    public void testValidateNationalities() throws NoValidatorException {
        for (int i = 0; i < 1000; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < i; j++) {
                sb.append("B");
            }
            if ((0 < i)&&(i < 51)) {
                assertEquals(true, instance.validate(PersonData.NATIONALITY, sb.toString()));
            } else {
                assertEquals(false, instance.validate(PersonData.NATIONALITY, sb.toString()));
            }
        }
    }
    
    /**
     * Tests that the card number catches any numbers with a wrong length
     * @throws interfaces.IValidator.NoValidatorException 
     */
    @Test
    public void testValidateCardnumberLength() throws NoValidatorException {
        for (int i = 0; i < 1000; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < i; j++) {
                sb.append("1");
            }
            if ((0 < i)&&(i < 31)) {
                assertEquals(true, instance.validate(ReservationData.CARDNUMBER, sb.toString()));
            } else {
                assertEquals(false, instance.validate(ReservationData.CARDNUMBER, sb.toString()));
            }
        }
    }
    
    /**
     * Test to ensure that card numbers with non-number values are invalid
     * @throws interfaces.IValidator.NoValidatorException 
     */
    @Test
    public void testvalidateCardnumberCharacters() throws NoValidatorException {
        String cardnumber = "111111110000";
        String badChars = "abcdefghijklmnopqrstuvwxyzæøå'*@-_.:,; ¨^´`+?=()/&%€#!$§\"\\<>";
        for (int i = 0; i < cardnumber.length(); i++) {
            String testNumber;
            for (int j = 0; j < badChars.length(); j++) {
                String start = (i != 0)? cardnumber.substring(i-1, i) : "";
                String end = (i != (cardnumber.length()-1))? cardnumber.substring(i+1) : "";
                testNumber = start+badChars.charAt(j)+end;
                assertEquals(false, instance.validate(PersonData.CPR, testNumber));
            }
        }
    }
    
    /**
     * Test to ensure that if there are more than the starting plus in the 
     * phone number, it will fail.
     * @throws interfaces.IValidator.NoValidatorException 
     */
    @Test
    public void testMultiplePlussesInPhonenumber() throws NoValidatorException {
        String phone = "+45 112";
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(phone);
            int plusplus = rand.nextInt(10);
            for (int j = 0; j < plusplus; j++) {
                sb.append("+5"); //+5 phoniness
            }
            if (plusplus == 0) {
                assertEquals(true, instance.validate(ReservationData.PHONENUMBER, sb.toString()));
            } else {
                assertEquals(false, instance.validate(ReservationData.PHONENUMBER, sb.toString()));
            }
        }
    }
    
    /**
     * Test to ensure that the validator ignores all space in the phone number
     * @throws interfaces.IValidator.NoValidatorException 
     */
    @Test
    public void testPhonenumberIgnoresSpace() throws NoValidatorException {
        String phone = "+45 112";
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(phone);
            int spaces = (i != 0)? rand.nextInt(i): 0;
            for (int j = 0; j < spaces; j++) {
                sb.append(" ");
            }
            sb.append("2");
            assertEquals(true, instance.validate(ReservationData.PHONENUMBER, sb.toString()));
        }
    }
    
    @Test
    public void testPhoneNumberCharacters() throws NoValidatorException {
        String phoneNumber = "111111110000";
        String badChars = "abcdefghijklmnopqrstuvwxyzæøå'*@-_.:,; ¨^´`+?=()/&%€#!$§\"\\<>";
        for (int i = 0; i < phoneNumber.length(); i++) {
            String testNumber;
            for (int j = 0; j < badChars.length(); j++) {
                String start = (i != 0)? phoneNumber.substring(i-1, i) : "";
                String end = (i != (phoneNumber.length()-1))? phoneNumber.substring(i+1) : "";
                testNumber = start+badChars.charAt(j)+end;
                if (
                        ((""+badChars.charAt(j)).equals(" ")) || 
                        (((""+badChars.charAt(j)).equals("+")) && (i == 0))) {
                    assertEquals(true, instance.validate(ReservationData.PHONENUMBER, testNumber));
                } else {
                    assertEquals(false, instance.validate(ReservationData.PHONENUMBER, testNumber));
                }
            }
        }
    }
}
