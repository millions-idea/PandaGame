package com.panda.game.management.utils;

import net.sf.jmimemagic.*;


public class FileUtil {
    private static Magic parser = new Magic() ;

    public static String getFileMimeType(byte[] bytes){
        MagicMatch match = null;
        try {
            match = parser.getMagicMatch(bytes);
        } catch (MagicParseException e) {
            e.printStackTrace();
        } catch (MagicMatchNotFoundException e) {
            e.printStackTrace();
        } catch (MagicException e) {
            e.printStackTrace();
        }
        return match.getMimeType();
    }

}
