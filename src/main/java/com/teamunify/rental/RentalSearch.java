package com.teamunify.rental;

import java.util.ArrayList;
import java.util.Collection;

public class RentalSearch {

  public Collection<Rental> findOverdueItems() {
    RentalDAO rentalDB = DatabaseAccess.rentalDAO.make();
    ArrayList<Rental> rv = new ArrayList<Rental>();

    for (Rental r : rentalDB.getRentals())
      if (r.isPastDue()) rv.add(r);

    return rv;
  }

}
