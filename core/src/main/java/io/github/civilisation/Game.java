package io.github.civilisation;

public class Game {
    private static final String Alliance = "Alliance";
    private static final String Horde = "Horde";

    private String playerCivilisation;
    private String aiCivilisation;

    public void startGame(String playerChoice) {
        if (playerChoice.equals(Alliance)) {
            playerCivilisation = Alliance;
            aiCivilisation = Horde;
        } else if (playerChoice.equals(Horde)) {
            playerCivilisation = Horde;
            aiCivilisation = Alliance;
        } else {
            return;
        }
    }

    public void playGame(){

    };
}
