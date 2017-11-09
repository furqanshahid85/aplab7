package lab7;

import static org.junit.Assert.*;

import org.junit.Test;

public class lab7test {

	@Test
	public void test() {
		
		int menuSelection=1;
		int itemPrice=300;
		String itemSelected="fries";
		int itemTime=3;
		
		assertEquals(1,menuSelection);
		assertEquals(300,itemPrice);
		
		assertNotNull(itemPrice);
		assertNotNull(itemTime);
		assertNotNull(itemSelected);
	}

}
