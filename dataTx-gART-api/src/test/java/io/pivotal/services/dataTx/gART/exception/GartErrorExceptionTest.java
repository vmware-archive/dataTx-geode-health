package io.pivotal.services.dataTx.gART.exception;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GartErrorExceptionTest
{

	@Test
	public void test_HasCategory()
	{
		assertEquals("WARN", new GartWarningException("code","message").getCategory());
		assertEquals("WARN", new GartWarningException("code","sdsds",null).getCategory());
	}//------------------------------------------------
	@Test
	public void test_HasErrorCode()
	{
		assertEquals("code", new GartWarningException("code","message").getCode());
	}//------------------------------------------------

}
