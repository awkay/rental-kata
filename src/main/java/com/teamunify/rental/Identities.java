package com.teamunify.rental;

import com.teamunify.factory.Factory;
import com.teamunify.globalid.GlobalIDFunction;

public class Identities {
  public static final Factory<Long> rentalIDFactory = new Factory<Long>(new GlobalIDFunction());
}
