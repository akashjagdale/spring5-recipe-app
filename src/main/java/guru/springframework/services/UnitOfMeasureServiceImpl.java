package guru.springframework.services;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.repositories.UnitOfMeasureRepository;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

	private final UnitOfMeasureRepository unitOfMeasureRepo;
	private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToCommand;

	public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepo,
			UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToCommand) {
		this.unitOfMeasureRepo = unitOfMeasureRepo;
		this.unitOfMeasureToCommand = unitOfMeasureToCommand;
	}

	@Override
	public Set<UnitOfMeasureCommand> listAllUoms() {
		return StreamSupport.stream(unitOfMeasureRepo.findAll().spliterator(), false)
				.map(uom -> unitOfMeasureToCommand.convert(uom)).collect(Collectors.toSet());
	}

}
