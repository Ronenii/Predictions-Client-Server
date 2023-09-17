package simulation.objects.world.exception;

public class CrashException extends Exception{
    private final String errorMassage;

    public CrashException(String errorMassage) {
        this.errorMassage = String.format("Simulation crushed: %s",errorMassage);
    }

    public String getErrorMassage() {
        return errorMassage;
    }
}
