package com.vdit;

import java.util.ArrayList;

class HotKeys {
    Cursor cursor = new Cursor();

    public int ctrlA = 1;
    public int ctrlC = 3;
    public int ctrlD = 4;
    public int ctrlE = 5;
    public int ctrlF = 6;
    public int ctrlG = 7;
    public int ctrlH = 8;
    public int ctrlI = 9;
    public int ctrlJ = 10;
    public int ctrlK = 11;
    public int ctrlL = 12;
    public int ctrlM = 13;
    public int ctrlN = 14;
    public int ctrlO = 15;
    public int ctrlP = 16;
    public int ctrlQ = 17;
    public int ctrlR = 18;
    public int ctrlS = 19;
    public int ctrlT = 20;
    public int ctrlU = 21;
    public int ctrlV = 22;
    public int ctrlW = 23;
    public int ctrlX = 24;
    public int ctrlY = 25;
    public int ctrlZ = 26;
    public int ctrlLeft = 27;
    public int ctrlRight = 28;
    public int ctrlUp = 29;
    public int ctrlDown = 30;

    public int altA = 65;
    public int altB = 66;
    public int altC = 67;
    public int altD = 68;
    public int altE = 69;
    public int altF = 70;
    public int altG = 71;
    public int altH = 72;
    public int altI = 73;
    public int altJ = 74;
    public int altK = 75;
    public int altL = 76;
    public int altM = 77;
    public int altN = 78;
    public int altO = 79;
    public int altP = 80;
    public int altQ = 81;
    public int altR = 82;
    public int altS = 83;
    public int altT = 84;
    public int altU = 85;
    public int altV = 86;
    public int altW = 87;
    public int altX = 88;
    public int altY = 89;
    public int altZ = 90;

    public int ctrlAltA = 65563;
    public int ctrlAltB = 65564;
    public int ctrlAltC = 65565;
    public int ctrlAltD = 65566;
    public int ctrlAltE = 65567;
    public int ctrlAltF = 65568;
    public int ctrlAltG = 65569;
    public int ctrlAltH = 65570;
    public int ctrlAltI = 65571;
    public int ctrlAltJ = 65572;
    public int ctrlAltK = 65573;
    public int ctrlAltL = 65574;
    public int ctrlAltM = 65575;
    public int ctrlAltN = 65576;
    public int ctrlAltO = 65577;
    public int ctrlAltP = 65578;
    public int ctrlAltQ = 65579;
    public int ctrlAltR = 65580;
    public int ctrlAltS = 65581;
    public int ctrlAltT = 65582;
    public int ctrlAltU = 65583;
    public int ctrlAltV = 65584;
    public int ctrlAltW = 65585;
    public int ctrlAltX = 65586;
    public int ctrlAltY = 65587;
    public int ctrlAltZ = 65588;
    public int ctrlAltF1 = 65589;
    public int ctrlAltF2 = 65590;
    public int ctrlAltF3 = 65591;
    public int ctrlAltF4 = 65592;
    public int ctrlAltF5 = 65593;
    public int ctrlAltF6 = 65594;
    public int ctrlAltF7 = 65595;
    public int ctrlAltF8 = 65596;
    public int ctrlAltF9 = 65597;
    public int ctrlAltF10 = 65598;
    public int ctrlAltF11 = 65599;
    public int ctrlAltF12 = 65600;
    public int ctrlAltInsert = 65609;
    public int ctrlAltDelete = 65610;
    public int ctrlAltHome = 65611;
    public int ctrlAltEnd = 65612;
    public int ctrlAltPageUp = 65613;
    public int ctrlAltPageDown = 65614;
    public int ctrlAltArrowUp = 65615;
    public int ctrlAltArrowDown = 65616;
    public int ctrlAltArrowLeft = 65617;
    public int ctrlAltArrowRight = 65618;

    public int ctrlShiftAltA = 65641;
    public int ctrlShiftAltB = 65642;
    public int ctrlShiftAltC = 65643;
    public int ctrlShiftAltD = 65644;
    public int ctrlShiftAltE = 65645;
    public int ctrlShiftAltF = 65646;
    public int ctrlShiftAltG = 65647;
    public int ctrlShiftAltH = 65648;
    public int ctrlShiftAltI = 65649;
    public int ctrlShiftAltJ = 65650;
    public int ctrlShiftAltK = 65651;
    public int ctrlShiftAltL = 65652;
    public int ctrlShiftAltM = 65653;
    public int ctrlShiftAltN = 65654;
    public int ctrlShiftAltO = 65655;
    public int ctrlShiftAltP = 65656;
    public int ctrlShiftAltQ = 65657;
    public int ctrlShiftAltR = 65658;
    public int ctrlShiftAltS = 65659;
    public int ctrlShiftAltT = 65660;
    public int ctrlShiftAltU = 65661;
    public int ctrlShiftAltV = 65662;
    public int ctrlShiftAltW = 65663;
    public int ctrlShiftAltX = 65664;
    public int ctrlShiftAltY = 65665;
    public int ctrlShiftAltZ = 65666;



    public void close(ArrayList<ArrayList<Character>> lines) {
        cursor.changeColorWhite();
        cursor.clearScreenAfterCursor();
        System.out.println("\nbye");

        for (int i = 0; i < lines.size(); i++) {
            lines.get(i).add('\n');
        }

        // Print lines.
        for (int i = 0; i < lines.size(); i++)
            for (int j = 0; j < lines.get(i).size(); j++)
                System.out.print(lines.get(i).get(j));
        
        System.out.println();
        System.exit(0);
    }
}
