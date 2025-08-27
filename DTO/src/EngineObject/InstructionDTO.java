package EngineObject;

import java.util.Optional;

public record InstructionDTO(
        int lineIndex,
        boolean isSynthetic,
        String label,
        VariableDTO var,
        String name,
        InstructionDTO holder,
        int cycles,
        Optional<VariableDTO> arg,
        Optional<Long> constant,
        Optional<String> labelToJump)
{}


