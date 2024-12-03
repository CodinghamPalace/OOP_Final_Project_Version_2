import java.util.ArrayList;
import java.util.List;

public class PetManagement {
    private List<Pet> pets = new ArrayList<>();

    public Pet addPet(String name, String breed, String color, String species, String gender, String healthStatus) {
        Pet newPet = new ConcretePet(name, breed, color, species, gender, healthStatus);
        pets.add(newPet);
        return newPet;
    }

    public int getCount() {
        return pets.size();
    }

    public Pet getPet(int index) {
        if (index >= 0 && index < pets.size()) {
            return pets.get(index);
        }
        return null;
    }
}
