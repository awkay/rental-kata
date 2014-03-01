package com.teamunify.rental;

import org.junit.Test;

public class DatabaseAccessSpec {

  @Test
  public void supports_access_to_rentalDAO() {
    DatabaseAccess.rentalDAO.make();
  }

}
