package com.sg.flooring.ui;

public enum MenuSelection {

    NONE(0),
    DISPLAY_ORDERS(1),
    ADD_ORDER(2),
    EDIT_ORDER(3),
    REMOVE_ORDER(4),
    EXPORT_ALL_DATA(5),
    EXIT(6);

    private final int value;

    MenuSelection(int value) {
        this.value = value;
    }

    public static MenuSelection fromInt(int i) {
        final MenuSelection[] values = values();

        for (int j = 1; j < values.length; j++) {
            if (values[j].value == i) {
                return values[j];
            }
        }
        return MenuSelection.NONE;
    }
}
