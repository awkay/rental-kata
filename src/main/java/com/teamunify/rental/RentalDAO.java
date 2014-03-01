package com.teamunify.rental;

import java.util.Collection;

public interface RentalDAO {
  public void add(Rental r);
  public Collection<Rental> getRentals();
}
