package net.minheur.potoflux.terminal.commands;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandMakerHelpers {
    /**
     * A contant for the tabulation
     */
    public static final String tab = "    ";
    /**
     * A constant for a tabulation, preceded by a new line.
     */
    public static final String tabLine = "\n" + tab;
    /**
     * A constant for a tab, preceding an arrow (and space at the end)
     */
    public static final String tabArrow = tab + "→ ";

    /**
     * Checks for an amount of args, contained between a min and a max
     * @param min the minimum amount of args
     * @param max the maximum amount of args
     * @param args the args to check
     * @return if the args are good
     */
    @Contract(pure = true)
    public static boolean argAmountCheck(int min, int max, @NotNull List<String> args) {
        int actual = args.size();
        return actual < min || actual > max;
    }
    /**
     * Checks for an amount of args, being a fixed amount
     * @param amount the exact amount of args you want
     * @param args the args to check
     * @return if the args are good
     */
    @Contract(pure = true)
    public static boolean argAmountCheck(int amount, @NotNull List<String> args) {
        int actual = args.size();
        return actual != amount;
    }
    /**
     * Checks for an amount of args, being contained in a given list of allowed amounts
     * @param args the args to check
     * @param allowed the varargs of amount of args allowed
     * @return if the arg are good
     */
    @Contract(pure = true)
    public static boolean argAmountCheck(@NotNull List<String> args, int @NotNull ... allowed) {
        int actual = args.size();
        for (int a : allowed) if (a == actual) return false;
        return true;
    }
    /**
     * Checks if there are no args
     * @param args args to check
     * @return if there are no args
     */
    @Contract(pure = true)
    public static boolean checkNoArgs(@NotNull List<String> args) {
        return !args.isEmpty();
    }
}
