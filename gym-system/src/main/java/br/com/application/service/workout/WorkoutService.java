package br.com.application.service.workout;

import br.com.application.controllers.workout.WorkoutController;
import br.com.application.controllers.person.TestLogController;
import br.com.application.data.dto.WorkoutDTO;
import br.com.application.exception.RequiredObjectIsNullException;
import br.com.application.exception.ResourceNotFoundException;
import br.com.application.model.Workout;
import br.com.application.repository.WorkoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static br.com.application.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class WorkoutService {

    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());

    @Autowired
    WorkoutRepository repository;

    @Autowired
    private PagedResourcesAssembler<WorkoutDTO> assembler;

    public PagedModel<EntityModel<WorkoutDTO>> findAllWorkout(Pageable pageable) {
        logger.info("Finding all Workouts!");

        var workouts = repository.findAll(pageable);

        return buildPagedModel(pageable, workouts);

    }

    public WorkoutDTO findWorkoutById(Long id) {
        logger.info("Finding one Workout!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        var dto = parseObject(entity, WorkoutDTO.class);
        addHateoasLinks(dto);
        return dto;
    }


    public WorkoutDTO create(WorkoutDTO workoutDTO) {
        if (workoutDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Workout!");

        var entity = parseObject(workoutDTO, Workout.class);
        var dto = parseObject(repository.save(entity), WorkoutDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public WorkoutDTO update(WorkoutDTO workoutDTO) {
        if (workoutDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Workout!");

        Workout workout = repository.findById(workoutDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        workout.setExerciseName(workoutDTO.getExerciseName());
        workout.setMuscleGroup(workoutDTO.getMuscleGroup());
        workout.setEquipment(workoutDTO.getEquipment());
        workout.setDifficulty(workoutDTO.getDifficulty());

        var entity = repository.save(workout);
        var dto = parseObject(entity, WorkoutDTO.class); // convertendo em DTO, atualizando, e salvando a entidade
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Delete one Workout!");

        Workout entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        repository.delete(entity);
    }

    private PagedModel<EntityModel<WorkoutDTO>> buildPagedModel(Pageable pageable, Page<Workout> workouts) {
        var peopleWithLinks = workouts.map(workout -> {
            var dto = parseObject(workout, WorkoutDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo
                        (
                                WebMvcLinkBuilder.methodOn(WorkoutController.class)
                                        .findAllWorkouts(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))
                        )
                .withSelfRel();
        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private static void addHateoasLinks(WorkoutDTO dto) {
        dto.add(linkTo(methodOn(WorkoutController.class)
                .findWorkoutById(dto.getId()))
                .withSelfRel()
                .withType("GET"));

        dto.add(linkTo(methodOn(WorkoutController.class)
                .findAllWorkouts(0, 12, "asc"))
                .withRel("findAllWorkouts")
                .withType("GET"));

        dto.add(linkTo(methodOn(WorkoutController.class)
                .create(dto))
                .withRel("create")
                .withType("POST"));

        dto.add(linkTo(methodOn(WorkoutController.class)
                .update(dto))
                .withRel("update")
                .withType("PUT"));

        dto.add(linkTo(methodOn(WorkoutController.class)
                .delete(dto.getId()))
                .withRel("delete")
                .withType("DELETE"));
    }
}