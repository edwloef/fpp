package spiele.common;

import java.util.Stack;

public interface Protokollierbar {
    Stack<Spielzug> protokoll = new Stack<>();

    void protokolliere(Spielzug spielzug);

    void entferne();
}
