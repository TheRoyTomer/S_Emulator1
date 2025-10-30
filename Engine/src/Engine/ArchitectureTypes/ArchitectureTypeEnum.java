package Engine.ArchitectureTypes;

public enum ArchitectureTypeEnum implements ArchitectureInterface
{
    ONE {
        @Override
        public int getSerialRepresentation() {return 1;}

        @Override
        public int getCostArchitecture() {return 5;}
    },
    TWO {
        @Override
        public int getSerialRepresentation() {return 2;}

        @Override
        public int getCostArchitecture() {return 100;}
    },
    THREE {
        @Override
        public int getSerialRepresentation() {return 3;}

        @Override
        public int getCostArchitecture() {return 500;}
    },
    FOUR {
        @Override
        public int getSerialRepresentation() {return 4;}

        @Override
        public int getCostArchitecture() {return 1000;}
    }
}
