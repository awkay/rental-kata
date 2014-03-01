package com.teamunify.rental;

import com.teamunify.factory.Factory;
import com.teamunify.util.Function0;

public class DatabaseAccess {
  public static final Factory<RentalDAO> rentalDAO = new Factory<RentalDAO>(Function0.functionReturningValue((RentalDAO)null));

}
