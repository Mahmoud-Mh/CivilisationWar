package io.github.civilisation.Units;

public class UnitFactory {
    public static Unit createUnit(String type, float x, float y, int attackdamage) {
        switch (type.toLowerCase()) {
            case "knight":
                return new Knight(x, y);
            case "samurai":
                return new Samurai(x, y);
            case "wizard":
                return new Wizard(x, y);
            case "drake":
                return new Drake(x, y);
            case "gorgon":
                return new Gorgon(x, y);
            case "yokai":
                return new Yokai(x, y);
            default:
                throw new IllegalArgumentException("Unknown unit type: " + type);
        }
    }
}
