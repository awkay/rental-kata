package com.teamunify.rental;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.text.MessageFormat;
import java.util.Calendar;
import org.joda.time.DateTimeComparator;
import org.junit.Before;
import org.junit.Test;
import com.teamunify.time.Times;
import com.teamunify.util.Function0;

public class RentalSpec {

  private static final int fourDays = 4;
  private static final int twoDays = 2;
  
  @Before
  public void setup() {
    Identities.rentalIDFactory.resetToDefault();
  }

  @Test
  public void rentals_have_a_due_date_and_grace_period() {
    Calendar expectedDueDate = Calendar.getInstance();
    Rental rental = new Rental(expectedDueDate, fourDays);

    assertEquals(expectedDueDate, rental.getDueDate());
    assertEquals(fourDays, rental.getGracePeriod());
  }

  @Test
  public void rentals_get_their_identity_from_a_GlobalIDFunction() {
    Identities.rentalIDFactory.set(Function0.functionReturningValue(3L));
    
    Rental rental = new Rental(january(3, 2001), 4);
    
    assertEquals(3L, rental.getId());
  }
  
  @Test
  public void id_does_not_change_once_obtained_so_that_getId_is_idempotent() {
    Identities.rentalIDFactory.set(Function0.functionReturningValue(3L));
    
    Rental rental = new Rental(january(3, 2001), 4);
    
    assertEquals(3L, rental.getId());
    Identities.rentalIDFactory.set(Function0.functionReturningValue(4L));
    assertEquals(3L, rental.getId());
  }

  @Test
  public void hard_due_date_is_due_date_plus_grace_period_in_days() {
    Calendar dueDate = november(11, 2009);
    int gracePeriod = twoDays;
    Calendar hardDueDate = november(13, 2009);
    Rental rental = new Rental(dueDate, gracePeriod);

    Calendar actual = rental.getHardDueDate();

    assertSameDay(hardDueDate, actual);
  }

  @Test
  public void hard_due_date_is_idempotent() {
    Calendar dueDate = november(11, 2009);
    int gracePeriod = twoDays;
    Calendar hardDueDate = november(13, 2009);
    Rental rental = new Rental(dueDate, gracePeriod);

    rental.getHardDueDate();
    Calendar actual = rental.getHardDueDate();

    assertEquals(hardDueDate, actual);
  }

  @Test
  public void calculation_of_hard_due_date_tolerates_crossing_month_boundaries() {
    Calendar dueDate = november(30, 2009);
    int gracePeriod = twoDays;
    Calendar hardDueDate = december(2, 2009);
    Rental rental = new Rental(dueDate, gracePeriod);

    Calendar actual = rental.getHardDueDate();

    assertSameDay(hardDueDate, actual);
  }

  @Test
  public void calculation_of_hard_due_date_tolerates_crossing_year_boundaries() {
    Calendar dueDate = december(31, 2009);
    int gracePeriod = twoDays;
    Calendar hardDueDate = january(2, 2010);
    Rental rental = new Rental(dueDate, gracePeriod);

    Calendar actual = rental.getHardDueDate();

    assertSameDay(hardDueDate, actual);
  }

  @Test
  public void toString_shows_the_due_date_and_grace_period_for_diagnostic_purposes() {
    Rental rental = new Rental(january(5, 2010), twoDays);

    assertEquals("Rental is due on January 5, 2010 with a grace period of 2 days.", rental.toString());
  }

  @Test
  public void isPastDue_returns_false_on_any_day_up_to_and_including_the_hard_due_date() {
    final Calendar dateDue = january(12, 2000);
    final int graceDays = fourDays;
    final int lastDayToReturn = 12 + fourDays;
    final Rental rental = new Rental(dateDue, graceDays);

    for (int day = 5; day <= lastDayToReturn; day++) {
      setNowTo(january(day, 2000));
      assertFalse(rental.isPastDue());
    }
  }
  
  @Test
  public void isPastDue_returns_true_on_all_days_after_the_hard_due_date() {
    final Calendar dateDue = january(12, 2000);
    final int graceDays = fourDays;
    final Rental rental = new Rental(dateDue, graceDays);

    final int lastDayToReturn = 12 + fourDays;
    final int firstLateDay = lastDayToReturn + 1;

    for (int day = firstLateDay; day < firstLateDay + 5; day++) {
      setNowTo(january(day, 2000));
      assertTrue(rental.isPastDue());
    }
  }

  private void setNowTo(Calendar pointInTime) {
    Times.now.set(Function0.functionReturningValue(pointInTime));
  }

  private Calendar buildCalendar(int day, int month, int year) {
    Calendar rv = Calendar.getInstance();
    rv.set(Calendar.MONTH, month);
    rv.set(Calendar.DAY_OF_MONTH, day);
    rv.set(Calendar.YEAR, year);
    return rv;
  }

  private Calendar december(int day, int year) {
    return buildCalendar(day, Calendar.DECEMBER, year);
  }

  private Calendar november(int day, int year) {
    return buildCalendar(day, Calendar.NOVEMBER, year);
  }

  private Calendar january(int day, int year) {
    return buildCalendar(day, Calendar.JANUARY, year);
  }

  private void assertSameDay(Calendar day1, Calendar day2) {
    DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
    String message = MessageFormat.format("Expected {0, date, medium} vs {1, date, medium}", day1.getTime(),
                                          day2.getTime());

    assertTrue(message, comparator.compare(day1, day2) == 0);
  }

}
