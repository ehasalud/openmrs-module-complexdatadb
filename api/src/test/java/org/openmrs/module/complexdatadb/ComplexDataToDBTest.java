package org.openmrs.module.complexdatadb;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Test;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class ComplexDataToDBTest extends BaseModuleContextSensitiveTest {

	@Test
	public void shouldEquality () {
		
		// Setup instance 1
		ComplexDataToDB cd1 = new ComplexDataToDB();
		cd1.setUuid(UUID.randomUUID().toString());
		
		// Setup instance 2
		ComplexDataToDB cd2 = new ComplexDataToDB();
		cd2.setUuid(cd1.getUuid());
		
		assertThat(cd1.getUuid(), equalTo(cd2.getUuid()));
		
		cd2.setUuid(UUID.randomUUID().toString());
		
		assertThat(cd1.getUuid(), not(equalTo(cd2.getUuid())));
	}
	
}
