package org.kelvinizer.textbox;

@FunctionalInterface
public interface TextUpdateHandler {
    void update(CRectTextBox box, char c);
}
