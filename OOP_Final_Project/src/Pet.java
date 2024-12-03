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