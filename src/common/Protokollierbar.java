package common;

import java.util.Stack;

public interface Protokollierbar {
    Stack<Spielzug> protokoll = new Stack<>();

    public abstract void protokolliere(Spielzug spielzug);

    public abstract void entferne();
}
