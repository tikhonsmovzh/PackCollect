package org.firstinspires.ftc.teamcode.Utils.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.firstinspires.ftc.teamcode.Utils.Configs.Configs;

public class Color {
    public static Color RED = new Color(255, 0, 0);
    public static Color BLUE = new Color(0, 0, 255);
    public static Color GREEN = new Color(0, 255, 0);
    public static Color GRAY = new Color(128, 128, 128);
    public static Color BLACK = new Color(0, 0, 0);
    public static Color WHITE = new Color(255, 255, 255);
    public static Color YELLOW = new Color(255, 255, 0);
    public static Color ORANGE = new Color(255, 128, 0);

    public int R, G, B;

    public Color(int r, int g, int b){
        R = r;
        G = g;
        B = b;
    }

    @NonNull
    @Override
    public String toString() {
        if (R > 255 || G > 255 || B > 255)
            throw new RuntimeException("color more 255");

        if (R < 0 || G < 0 || B < 0)
            throw new RuntimeException("color less 0");

        String rString = Integer.toString(R, 16).toUpperCase(), gString = Integer.toString(G, 16).toUpperCase(), bString = Integer.toString(B, 16).toUpperCase();

        if(rString.length() == 1)
            rString = "0" + rString;

        if(gString.length() == 1)
            gString = "0" + gString;

        if(bString.length() == 1)
            bString = "0" + bString;

        return "#" + rString + gString + bString;
    }

    @NonNull
    @Override
    public Color clone() {
        return new Color(R, G, B);
    }

    public static Color ofBytes(byte[] buf){
        if(buf.length != 6)
            throw new RuntimeException("error get color of " + buf.length + " bytes, request 6");

        return new Color(getInt(buf, 0), getInt(buf, 2), getInt(buf, 4));
    }

    public static int getInt(byte[] arr, int off) {
        return arr[off]<<8 &0xFF00 | arr[off+1]&0xFF;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return equals(obj, 0);
    }

    public boolean equals(@Nullable Object obj, int sensitivity) {
        if(!(obj instanceof Color))
            return false;

        Color color = (Color) obj;

        return Math.abs(color.R - Configs.Intake.RRedPuck) < sensitivity &&
                Math.abs(color.G - Configs.Intake.GRedPuck) < sensitivity &&
                Math.abs(color.B - Configs.Intake.BRedPuck) < sensitivity;
    }
}
