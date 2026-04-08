package com.parsons.controller;

public interface GuiConstants {
    // derived based on the two panel drag and drop
    // each panel is 40 cols of ~13px each (font size 10)
    // 40 cols is narrow but good enough for beginner level problems
    // maintain 16:9 ratio
    int NARROW_FRAME_WIDTH = 720;
    int FRAME_WIDTH = 1280;
    int FRAME_HEIGHT = 720;
    int BLOCK_PANEL_WIDTH = 624;
    int MAX_COLS = 40;
    int DIVIDER_LOCATION = FRAME_WIDTH / 2;
    int TIGHT_GAP = 5;   // between related elements
    int PANEL_PAD = 10;   // around panels
    int FRAME_MARGIN = 20;
}
