public class User {
    private String name;
    private float money;
    private boolean ifPotentialComputer;

    public User(String name, float money, boolean ifPotentialComputer) {
        this.name = name;
        this.money = money;
        this.ifPotentialComputer = ifPotentialComputer;
    }

    public void giveMoney(float value) {
        money += value;
    }

    public void takeMoney(float value) {
        money -= value;
    }

    public String getName() {
        return name;
    }

    public float getMoney() {
        return money;
    }

    public boolean getIfPotentialComputer() {
        return ifPotentialComputer;
    }
}
