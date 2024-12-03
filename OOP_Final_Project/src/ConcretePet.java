public class ConcretePet extends Pet {
    public ConcretePet(String name, String breed, String color, String species, String gender, String healthStatus) {
        super(name, breed, color, species, gender, healthStatus);
    }

    @Override
    public String getPetInformation() {
        return "Name: " + getName() + "\n" +
                "Breed: " + getBreed() + "\n" +
                "Color: " + getColor() + "\n" +
                "Species: " + getSpecies() + "\n" +
                "Gender: " + getGender() + "\n" +
                "Health Status: " + getHealthStatus() + "\n";
    }
}
