package EngineObject;

import java.util.List;

public record StatisticDTO(
        int executeID,
        int degree,
        List<Long> inputs,
        long outPutVal,
        int totalCycles) {}
