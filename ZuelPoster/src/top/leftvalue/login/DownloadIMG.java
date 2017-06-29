package top.leftvalue.login;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 
 * @author Dick 2017年3月6日 下午3:15:21
 *
 *         TODO 下载验证码
 */
public class DownloadIMG
{
    /**
     * 测试
     * 
     * @param args
     */
    public static boolean getImg(String JSESSIONID)
    {
        String url = "http://202.114.234.160/jsxsd/verifycode.servlet";
        byte[] btImg = getImageFromNetByUrl(url, JSESSIONID);
        if (null != btImg && btImg.length > 0)
        {
            String fileName =  JSESSIONID + ".jpg";
            writeImage2(btImg, fileName);
            return true;
        } else
        {
            System.out.println("没有从该连接获得内容");
            return false;
        }
    }

    /**
     * 将图片写入到磁盘
     * 
     * @param img
     *            图片数据流
     * @param fileName
     *            文件保存时的名称
     */

    public static void writeImage2(byte[] img, String fileName)
    {
        try
        {
            File file = new File(fileName);
            if(file.exists()||file.isDirectory()){
            	file.delete();//同一JSESSIONID可能多次get验证码，所以需要删除本地先前的IMG
            }
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(img);
            fops.flush();
            fops.close();
            System.out.println("验证码加载完毕");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 根据地址获得数据的字节流
     * 
     * @param strUrl
     *            网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl, String JSESSIONID)
    {
        System.out.println("JSESSIONID " + JSESSIONID);
        try
        {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID);// 必须保证和登录的POST请求共享JSESSIONID
            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
            return btImg;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     * 
     * @param inStream
     *            输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}