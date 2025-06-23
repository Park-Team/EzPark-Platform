package com.acme.ezpark.platform.schedule.application.internal;

import com.acme.ezpark.platform.schedule.domain.model.aggregates.Schedule;
import com.acme.ezpark.platform.schedule.domain.model.commands.*;
import com.acme.ezpark.platform.schedule.domain.services.ScheduleCommandService;
import com.acme.ezpark.platform.schedule.infrastructure.persistence.jpa.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScheduleCommandServiceImpl implements ScheduleCommandService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleCommandServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Optional<Schedule> handle(CreateScheduleCommand command) {
        var schedule = new Schedule(
                command.parkingId(),
                command.dayOfWeek(),
                command.startTime(),
                command.endTime()
        );
        
        try {
            scheduleRepository.save(schedule);
            return Optional.of(schedule);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Schedule> handle(UpdateScheduleCommand command) {
        return scheduleRepository.findById(command.scheduleId())
                .map(schedule -> {
                    schedule.updateSchedule(
                            command.dayOfWeek(),
                            command.startTime(),
                            command.endTime()
                    );
                    scheduleRepository.save(schedule);
                    return schedule;
                });
    }

    @Override
    public boolean handle(DeleteScheduleCommand command) {
        return scheduleRepository.findById(command.scheduleId())
                .map(schedule -> {
                    schedule.deactivate();
                    scheduleRepository.save(schedule);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }
}
