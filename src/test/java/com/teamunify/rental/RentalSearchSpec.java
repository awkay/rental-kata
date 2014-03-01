package com.teamunify.rental;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import com.teamunify.util.Function0;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RentalSearchSpec {
  private RentalDAO mockDatabase;
  private Rental overdueItem;
  private Rental item2;
  private Rental item3;
  
  @Before
  public void setup() {
    mockDatabase = mock(RentalDAO.class);
    overdueItem = mock(Rental.class);
    when(overdueItem.isPastDue()).thenReturn(true);
    item2 = mock(Rental.class);
    item3 = mock(Rental.class);
    when(item2.isPastDue()).thenReturn(false);
    when(item3.isPastDue()).thenReturn(false);
    
    ArrayList<Rental> storage = new ArrayList<Rental>();
    storage.add(overdueItem);
    storage.add(item2);
    storage.add(item3);
    
    when(mockDatabase.getRentals()).thenReturn(storage);

    DatabaseAccess.rentalDAO.set(Function0.functionReturningValue(mockDatabase));
  }

  @Test
  public void can_find_all_overdue_rentals() {
    RentalSearch search = new RentalSearch();
    
    Collection<Rental> overdueItems = search.findOverdueItems();
    
    assertEquals(1, overdueItems.size());
    assertEquals(overdueItem, overdueItems.iterator().next());
  }
}
