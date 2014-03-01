package com.teamunify.rental;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import java.util.Calendar;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.teamunify.time.Times;
import com.teamunify.util.Function0;

public class IdentitiesSpec {
  private static boolean runIntegrationTests = false;

  @BeforeClass
  public static void initialize() {
    runIntegrationTests = System.getProperty("integration") != null;
  }

  @Before
  public void setup() {
    Times.now.resetToDefault();
    Identities.rentalIDFactory.resetToDefault();
  }

  @Test
  public void has_a_generator_for_rentals_that_makes_longs() {
    Object value = Identities.rentalIDFactory.make();
    assertTrue(value instanceof Long);
  }

  @Test
  public void actual_generator_used_is_a_global_identity_provider_based_on_time_ms() {
    assumeTrue(runIntegrationTests);
    Calendar testNow = Calendar.getInstance();
    Times.now.set(Function0.functionReturningValue(testNow));
    
    long id = Identities.rentalIDFactory.make();
    
    assertEquals(id, testNow.getTimeInMillis());
  }
}
