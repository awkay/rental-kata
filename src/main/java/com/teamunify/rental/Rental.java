package com.teamunify.rental;

import java.text.MessageFormat;
import java.util.Calendar;
import org.joda.time.DateTimeComparator;
import com.teamunify.time.Times;

public class Rental {
  private final Calendar dueDate;
  private final int gracePeriodInDays;

  public Rental(Calendar dueDate, int gracePeriodInDays) {
    this.dueDate = dueDate;
    this.gracePeriodInDays = gracePeriodInDays;
  }

  public Calendar getDueDate() {
    return this.dueDate;
  }

  public int getGracePeriod() {
    return gracePeriodInDays;
  }

  public Calendar getHardDueDate() {
    Calendar hardDueDate = (Calendar) dueDate.clone();
    hardDueDate.add(Calendar.DAY_OF_MONTH, gracePeriodInDays);
    return hardDueDate;
  }

  @Override
  public String toString() {
    return MessageFormat.format("Rental is due on {0, date, long} with a grace period of {1} days.", dueDate.getTime(),
                                gracePeriodInDays);
  }

  public boolean isPastDue() {
    DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
    return comparator.compare(Times.now.make(), getHardDueDate()) > 0;
  }
}
