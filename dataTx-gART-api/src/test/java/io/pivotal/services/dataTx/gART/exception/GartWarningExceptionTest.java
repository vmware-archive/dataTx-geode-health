package io.pivotal.services.dataTx.gART.exception;

import static org.junit.Assert.*;

import org.junit.Test;

import io.pivotal.services.dataTx.gART.exception.GartErrorException;

public class GartWarningExceptionTest
{

	@Test
	public void test_HasCategory()
	{
		assertEquals("ERROR", new GartErrorException("code","sdsds").getCategory());
		assertEquals("ERROR", new GartErrorException("code","sdsds",null).getCategory());
	}//------------------------------------------------
	@Test
	public void test_HasErroCode()
	{
		assertEquals("code", new GartErrorException("code","mssage").getCode());
	}//------------------------------------------------
}
