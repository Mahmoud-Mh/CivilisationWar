package io.github.civilisation.Units;

public class UnitFactory {
    public static Unit createUnit(String type, float x, float y, int attackdamage) {
        switch (type.toLowerCase()) {
            case "knight":
                return new Knight(x, y); // Knight constructor expects coordinates
            case "samurai":
                return new Samurai(x, y); // Samurai constructor expects coordinates
            case "wizard":
                return new Wizard(x, y); // Wizard now supports this constructor
            case "drake":
                return new Drake(x, y); // Drake constructor updated
            case "gorgon":
                return new Gorgon(x, y); // Gorgon constructor updated
            case "yokai":
                return new Yokai(x, y); // Yokai constructor updated
            default:
                throw new IllegalArgumentException("Unknown unit type: " + type);
        }
    }
}
