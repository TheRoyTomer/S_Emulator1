package Engine.Labels;

public enum FixedLabels implements LabelInterface
{
    EXIT {
        @Override
        public String getLabelRepresentation()
        {
            return "EXIT";
        }

    },
    EMPTY {
        @Override
        public String getLabelRepresentation()
        {
            return "  ";
        }

    }

}
