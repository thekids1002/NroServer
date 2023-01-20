package nro.main;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;
import java.text.Normalizer;

public class Util {

    private static final Random rand;
    private static final SimpleDateFormat dateFormat;

    static {
        rand = new Random();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static String powerToString(long power) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + " k";
        } else {
            return num.format(power);
        }
    }
    public static String strPercentPower(long power) {
        long powerNext;
        long powerPrev;
        if (power >= 300010000000L) {
            powerNext = 300010000000L;
            powerPrev = 300010000000L;
        } else if (power >= 280010000000L) {
            powerNext = 300010000000L;
            powerPrev = 280010000000L;
        } else if (power >= 230010000000L) {
            powerNext = 280010000000L;
            powerPrev = 230010000000L;
        } else if (power >= 180010000000L) {
            powerNext = 230010000000L;
            powerPrev = 180010000000L;
        } else if (power >= 130010000000L) {
            powerNext = 180010000000L;
            powerPrev = 130010000000L;
        } else if (power >= 100010000000L) {
            powerNext = 130010000000L;
            powerPrev = 100010000000L;
        } else if (power >= 81000000000L) {
            powerNext = 100010000000L;
            powerPrev = 81000000000L;
        } else if (power >= 70010000000L) {
            powerNext = 81000000000L;
            powerPrev = 70010000000L;
        } else if (power >= 60010000000L) {
            powerNext = 70010000000L;
            powerPrev = 60010000000L;
        } else if (power >= 50010000000L) {
            powerNext = 60010000000L;
            powerPrev = 50010000000L;
        } else if (power >= 40000000000L) {
            powerNext = 50010000000L;
            powerPrev = 40000000000L;
        } else if (power >= 10000000000L) {
            powerNext = 40000000000L;
            powerPrev = 10000000000L;
        } else if (power >= 5000000000L) {
            powerNext = 10000000000L;
            powerPrev = 5000000000L;
        } else if (power >= 1500000000) {
            powerNext = 5000000000L;
            powerPrev = 1500000000;
        } else if (power >= 150000000) {
            powerNext = 1500000000;
            powerPrev = 150000000;
        } else if (power >= 15000000) {
            powerNext = 150000000;
            powerPrev = 15000000;
        } else if (power >= 1500000) {
            powerNext = 15000000;
            powerPrev = 1500000;
        } else {
            powerNext = 1500000;
            powerPrev = 0;
        }
        return (Math.round(((double)(power - powerPrev)/(double)powerNext)*10000.0)/100.0) + "%";
    }
    public static boolean checkNumInt(String num) {
        return Pattern.compile("^[0-9]+$").matcher(num).find();
    }
    public static boolean isNumericInt(String inputString) {
        //check for null and empty string
        if (inputString == null || inputString.length() == 0) {
            return false;
        }
        try {
            Integer.parseInt(inputString);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
      }

    public static int UnsignedByte(byte b) {
        int ch = b;
        if (ch < 0) {
            return ch + 256;
        }
        return ch;
    }

    public static String parseString(String str, String wall) {
        return str.contains(wall) ? str.substring(str.indexOf(wall) + 1) : null;
    }

    public static boolean CheckString(String str, String c) {
        return Pattern.compile(c).matcher(str).find();
    }
    public static int nextInt(int from, int to) {
        return from + rand.nextInt(to - from);
    }

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }
    
    public static double getPercentDouble(int percent) {
        return ((double)Math.round(((double)percent / (double)100) * 100) / 100);
    }
    public static String validateString(String str) {
        str = str.toLowerCase();
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        str = pattern.matcher(nfdNormalizedString).replaceAll("");
        str = str.replaceAll("[^a-zA-Z0-9]", "");
        str = str.replaceAll("[-+^]*", "");
        str = str.replaceAll("[-+.^:,]","");
        str = str.replaceAll(" ","");
        return str;
    }
    public static String validateClan(String str) {
        str = str.toLowerCase();
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        str = pattern.matcher(nfdNormalizedString).replaceAll("");
        str = str.replaceAll("[^a-zA-Z0-9]", "");
        str = str.replaceAll("[-+^]*", "");
        str = str.replaceAll("[-+.^:,]","");
        return str;
    }
    public static int nextInt(int[] percen) {
        int next = nextInt(1000), i;
        for(i = 0; i < percen.length; i++) {
            if(next < percen[i])
                return i;
            next -= percen[i];
        }
        return i;
    }

    public static int currentTimeSec() {
        return (int) System.currentTimeMillis() / 1000;
    }

    public static String replace(String text, String regex, String replacement) {
        return text.replace(regex, replacement);
    }

    public static void log(String message) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        System.out.println("[" + strDate + "] " + message);
    }

    public static void debug(String message) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        System.out.println("[" + strDate + "] Debug: " + message);
    }

    public static int getToaDoXBROLY(int _idmap) {
        switch(_idmap) {
            case 5:
                return 1127;
            case 6:
                return 490;
            case 27:
                return 716;
            case 28:
                return 649;
            case 29:
                return 1026;
            case 30:
                return 911;
            case 10:
                return 690;
            case 13:
                return 1093;
            case 31:
                return 743;
            case 32:
                return 879;
            case 33:
                return 928;
            case 34:
                return 1098;
            case 19:
                return 718;
            case 20:
                return 594;
            case 35:
                return 728;
            case 36:
                return 554;
            case 37:
                return 1034;
            case 38:
                return 764;
        }
        return -1;
    }

    public static int getToaDoYBROLY(int _idmap) {
        switch(_idmap) {
            case 5:
                return 408;
            case 6:
                return 336;
            case 27:
                return 336;
            case 28:
                return 312;
            case 29:
                return 360;
            case 30:
                return 312;
            case 10:
                return 288;
            case 13:
                return 384;
            case 31:
                return 336;
            case 32:
                return 336;
            case 33:
                return 288;
            case 34:
                return 312;
            case 19:
                return 360;
            case 20:
                return 360;
            case 35:
                return 336;
            case 36:
                return 360;
            case 37:
                return 312;
            case 38:
                return 456;
        }
        return -1;
    }
}
