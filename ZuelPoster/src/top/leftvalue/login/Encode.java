package top.leftvalue.login;

/**
 * 
 * @author Dick 2017年3月6日 下午3:14:44
 *
 *         TODO 学号和密码的加密编码，
 */
public class Encode
{
    public static String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static String encodeInp(String input)
    {
        String output = "";
        int chr1, chr2, chr3 = 0;
        int enc1, enc2, enc3, enc4 = 0;
        int len = input.length();
        int last = 3 - len % 3;
        int i = 0;
        for (; i < len - 2;)
        {
            chr1 = (int) input.charAt(i++);
            chr2 = (int) input.charAt(i++);
            chr3 = (int) input.charAt(i++);
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            output += keyStr.charAt(enc1) + "" + keyStr.charAt(enc2) + "" + keyStr.charAt(enc3) + ""
                    + keyStr.charAt(enc4);
        }
        if (last != 3)
        {
            enc1 = enc2 = enc3 = enc4 = 0;
            chr1 = (int) input.charAt(i++);
            enc1 = chr1 >> 2;
            if (last == 1)
            {
                chr2 = (int) input.charAt(i++);
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc4 = 64;
            } else if (last == 2)
            {
                enc2 = ((chr1 & 3) << 4) | 0;// 这里我也没太明白，不过用js测出来如果enc2NAN，应该是0
                enc3 = enc4 = 64;
            }
            output += keyStr.charAt(enc1) + "" + keyStr.charAt(enc2) + "" + keyStr.charAt(enc3) + ""
                    + keyStr.charAt(enc4);
        }
        return output;
    }

    public static void main(String[] args)
    {
        System.out.println(encode("201504000110", "A19980520a"));
        System.out.println("MjAxNTA0MDAwMTEw%%%QTE5OTgwNTIwYQ==");
    }

    public static String encode(String xh, String passwrd)
    {
        return encodeInp(xh) + "%%%" + encodeInp(passwrd);
    }
}
