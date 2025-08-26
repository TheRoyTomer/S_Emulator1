package EngineObject;

public record InstructionDTO(
        int lineIndex,
        boolean isSynthetic,
        String label,
        String commandBody,
        InstructionDTO holder,
        int cycles)
{
}


