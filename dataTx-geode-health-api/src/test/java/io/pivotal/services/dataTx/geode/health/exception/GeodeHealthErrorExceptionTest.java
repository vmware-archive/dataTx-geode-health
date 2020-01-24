package io.pivotal.services.dataTx.geode.health.exception;


import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class GeodeHealthErrorExceptionTest
{

	@Test
	public void test_HasCategory()
	{
		assertEquals("WARN", new GeodeHealthWarningException("code","message").getCategory());
		assertEquals("WARN", new GeodeHealthWarningException("code","sdsds",null).getCategory());
	}//------------------------------------------------
	@Test
	public void test_HasErrorCode()
	{
		assertEquals("code", new GeodeHealthWarningException("code","message").getCode());
	}//------------------------------------------------

}
