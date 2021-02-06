package guru.springframework.repositories;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import guru.springframework.domain.UnitOfMeasure;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasureRepositoryTest {

	@Autowired
	UnitOfMeasureRepository unitOfMajorRepo;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFindByDescription() {
		Optional<UnitOfMeasure> findByDescription = unitOfMajorRepo.findByDescription("Tablespoon");
		assertEquals("Tablespoon", findByDescription.get().getDescription());
	}
	
	@Test
	public void testFindByPinch() {
		Optional<UnitOfMeasure> pinch = unitOfMajorRepo.findByDescription("Pinch");
		assertEquals("Pinch", pinch.get().getDescription());
	}

}
