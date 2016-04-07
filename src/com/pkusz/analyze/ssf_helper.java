	package com.pkusz.analyze;


import java.io.UnsupportedEncodingException;

public  class ssf_helper {
    public static final  String EnCode_String(String Str_Word  , String StrKeyWord ) throws UnsupportedEncodingException {
        int i;
        byte[] Code_Byte;
        byte[] KeyWord_Byte;
        if(Str_Word.equals(""))
            return "";
        Code_Byte = Str_Word.getBytes("UnicodeLittleUnmarked");
        KeyWord_Byte = StrKeyWord.getBytes("UnicodeLittleUnmarked");
        for(i = 0 ;i< Code_Byte.length ;i++)
        {
            Code_Byte[i] = (byte) (Code_Byte[i] ^ KeyWord_Byte[i % KeyWord_Byte.length]);

        }
        String strResult="";
        for(i = 0 ;i< Code_Byte.length ;i++)
        {
            if(Code_Byte[i] <= 9)
                strResult =strResult+ "00" + Code_Byte[i];
            else if( Code_Byte[i] <= 99)
                strResult =strResult+  "0" + Code_Byte[i];
            else
                strResult =strResult+ Code_Byte[i];

        }
        return strResult;
    }

}
