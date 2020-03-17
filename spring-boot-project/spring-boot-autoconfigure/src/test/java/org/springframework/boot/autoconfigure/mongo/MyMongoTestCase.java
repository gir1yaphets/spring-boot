package org.springframework.boot.autoconfigure.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class MyMongoTestCase {

	@Test
	void databaseCanBeAccessed(){
		// Build a database
		MongoProperties properties = new MongoProperties();
		// Build a test database
		MongoProperties test = new MongoProperties();
		properties.setDatabase("W2020");
		properties.setUsername("user");
		properties.setPassword("secret".toCharArray());
		test.setDatabase(properties.getDatabase());
		test.setUsername(properties.getUsername());
		test.setPassword(properties.getPassword());
		MongoClient clientTest = createMongoClient(test);
		assertMongoCredential(getCredentials(clientTest).get(0), "user", "secret", "W2020");
	}


	@Test
	void gridFsDatabaseCanBeSet(){
		MongoProperties properties = new MongoProperties();
		properties.setGridFsDatabase("W2020");
		Assert.assertEquals("W2020", properties.getGridFsDatabase());
	}


	@Test
	void setStrategy(){
		MongoProperties test = new MongoProperties();
		test.setFieldNamingStrategy(TestHelper.class);
		Assert.assertEquals("org.springframework.boot.autoconfigure.mongo.MyMongoTestCase$TestHelper", test.getFieldNamingStrategy().getName());
	}



	private void assertMongoCredential(MongoCredential credentials, String expectedUsername, String expectedPassword,
									   String expectedSource) {
		assertThat(credentials.getUserName()).isEqualTo(expectedUsername);
		assertThat(credentials.getPassword()).isEqualTo(expectedPassword.toCharArray());
		assertThat(credentials.getSource()).isEqualTo(expectedSource);
	}

	private MongoClient createMongoClient(MongoProperties properties) {
		return createMongoClient(properties, null);
	}

	private MongoClient createMongoClient(MongoProperties properties, Environment environment) {
		return new MongoClientFactory(properties, environment).createMongoClient(null);
	}

	private List<MongoCredential> getCredentials(MongoClient client) {
		// At some point we'll probably need to use reflection to find the credentials but
		// for now, we can use the deprecated getCredentialsList method.
		return client.getCredentialsList();
	}

	public static class TestHelper {
		public static String test = "w2020";
	}
}
