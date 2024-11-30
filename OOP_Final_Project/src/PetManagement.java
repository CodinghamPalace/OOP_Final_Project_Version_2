abstract class Pet {
    private String name;
    private String breed;
    private String color;
    private String species;
    private String gender;
    private String healthStatus;

    public Pet(String name, String breed, String color, String species, String gender, String healthStatus) {
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.species = species;
        this.gender = gender;
        this.healthStatus = healthStatus;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getColor() {
        return color;
    }

    public String getSpecies() {
        return species;
    }

    public String getGender() {
        return gender;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public abstract String getPetInformation();
}

/* INHERITANCE */
public class PetManagement {
    private int count = 0;
    private Pet currentPet;

    public Pet addPet(String name, String breed, String color, String species, String gender, String healthStatus) {
        count++;

        currentPet = new Pet(name, breed, color, species, gender, healthStatus) {
            @Override
            public String getPetInformation() {
                return "Name: " + getName() + "\n" +
                        "Breed: " + getBreed() + "\n" +
                        "Color: " + getColor() + "\n" +
                        "Species: " + getSpecies() + "\n" +
                        "Gender: " + getGender() + "\n" +
                        "Health Status: " + getHealthStatus() + "\n";
            }
        };

        return currentPet;
    }

    public int getCount() {
        return count;
    }
}

