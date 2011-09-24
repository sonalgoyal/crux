package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestUser {
@Test
public void testEquals(){
	User user = new User();
	user.setId(1);
	user.setName("user");
	
	User user1 = new User();
	user1.setId(1);
	user1.setName("user");
	
	assertTrue(user1.equals(user));
}

@Test
public void testEqualsForNull(){
	User user = null;
	
	User user1 = new User();
	user1.setId(1);
	user1.setName("user");
	
	assertTrue(!user1.equals(user));
}

@Test
public void testHashCode(){
	User user = new User();
	user.setId(1);
	user.setName("user");
	
	User user1 = new User();
	user1.setId(1);
	user1.setName("user");
	
	assertTrue(user1.hashCode()==user.hashCode());
}

@Test
public void testHashCodeForNullValue(){
	User user = new User();
	user.setId(1);
	user.setName(null);
	
	User user1 = new User();
	user1.setId(1);
	user1.setName(null);
	
	assertTrue(user1.hashCode()==user.hashCode());
}
}
