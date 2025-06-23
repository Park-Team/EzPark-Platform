package com.acme.ezpark.platform.schedule.interfaces.rest;

import com.acme.ezpark.platform.schedule.domain.model.queries.*;
import com.acme.ezpark.platform.schedule.domain.services.ScheduleCommandService;
import com.acme.ezpark.platform.schedule.domain.services.ScheduleQueryService;
import com.acme.ezpark.platform.schedule.interfaces.rest.resources.*;
import com.acme.ezpark.platform.schedule.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/schedules", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Schedule", description = "Schedule Management Endpoints")
public class SchedulesController {

    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleQueryService scheduleQueryService;

    public SchedulesController(ScheduleCommandService scheduleCommandService,
                              ScheduleQueryService scheduleQueryService) {
        this.scheduleCommandService = scheduleCommandService;
        this.scheduleQueryService = scheduleQueryService;
    }

    @PostMapping
    @Operation(summary = "Create schedule", description = "Create a new schedule for a parking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Schedule created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Parking not found")
    })
    public ResponseEntity<ScheduleResource> createSchedule(@RequestBody CreateScheduleResource resource) {
        var createScheduleCommand = CreateScheduleCommandFromResourceAssembler.toCommandFromResource(resource);
        var schedule = scheduleCommandService.handle(createScheduleCommand);
        if (schedule.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var scheduleResource = ScheduleResourceFromEntityAssembler.toResourceFromEntity(schedule.get());
        return new ResponseEntity<>(scheduleResource, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all schedules", description = "Get all active schedules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found")
    })
    public ResponseEntity<List<ScheduleResource>> getAllSchedules() {
        var getAllSchedulesQuery = new GetAllSchedulesQuery();
        var schedules = scheduleQueryService.handle(getAllSchedulesQuery);
        var scheduleResources = schedules.stream()
                .map(ScheduleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(scheduleResources);
    }

    @GetMapping("/{scheduleId}")
    @Operation(summary = "Get schedule by id", description = "Get schedule by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule found"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    public ResponseEntity<ScheduleResource> getScheduleById(@PathVariable Long scheduleId) {
        var getScheduleByIdQuery = new GetScheduleByIdQuery(scheduleId);
        var schedule = scheduleQueryService.handle(getScheduleByIdQuery);
        if (schedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var scheduleResource = ScheduleResourceFromEntityAssembler.toResourceFromEntity(schedule.get());
        return ResponseEntity.ok(scheduleResource);
    }

    @GetMapping("/parking/{parkingId}")
    @Operation(summary = "Get schedules by parking id", description = "Get all schedules for a specific parking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedules found"),
            @ApiResponse(responseCode = "404", description = "Parking not found")
    })
    public ResponseEntity<List<ScheduleResource>> getSchedulesByParkingId(@PathVariable Long parkingId) {
        var getSchedulesByParkingIdQuery = new GetSchedulesByParkingIdQuery(parkingId);
        var schedules = scheduleQueryService.handle(getSchedulesByParkingIdQuery);
        var scheduleResources = schedules.stream()
                .map(ScheduleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(scheduleResources);
    }

    @PutMapping("/{scheduleId}")
    @Operation(summary = "Update schedule", description = "Update schedule information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule updated"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    public ResponseEntity<ScheduleResource> updateSchedule(@PathVariable Long scheduleId,
                                                         @RequestBody UpdateScheduleResource resource) {
        var updateScheduleCommand = UpdateScheduleCommandFromResourceAssembler.toCommandFromResource(scheduleId, resource);
        var schedule = scheduleCommandService.handle(updateScheduleCommand);
        if (schedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var scheduleResource = ScheduleResourceFromEntityAssembler.toResourceFromEntity(schedule.get());
        return ResponseEntity.ok(scheduleResource);
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "Delete schedule", description = "Delete schedule by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule deleted"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    public ResponseEntity<?> deleteSchedule(@PathVariable Long scheduleId) {
        var getScheduleByIdQuery = new GetScheduleByIdQuery(scheduleId);
        var schedule = scheduleQueryService.handle(getScheduleByIdQuery);
        if (schedule.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        scheduleCommandService.deleteSchedule(scheduleId);
        return ResponseEntity.ok("Schedule with given id successfully deleted");
    }
}
