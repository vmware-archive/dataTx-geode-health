package io.pivotal.services.dataTx.geode.health.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeodeHealthWarningExceptionTest
{

	@Test
	public void test_HasCategory()
	{
		assertEquals("ERROR", new GeodeHealthErrorException("code","sdsds").getCategory());
		assertEquals("ERROR", new GeodeHealthErrorException("code","sdsds",null).getCategory());
	}//------------------------------------------------
	@Test
	public void test_HasErroCode()
	{
		assertEquals("code", new GeodeHealthErrorException("code","mssage").getCode());
	}//------------------------------------------------
}
