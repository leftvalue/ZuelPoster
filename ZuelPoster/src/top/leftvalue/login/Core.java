package top.leftvalue.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import com.alibaba.fastjson.JSONObject;

import org.jsoup.Connection.Response;

public class Core {
	private static int maxtime = 20000;
	private static String index = "http://202.114.234.160/jsxsd/";// 主页
	private static String loginurl = "http://202.114.234.160/jsxsd/xk/LoginToXk";// 登录验证请求地址
	private static String courseTable = "http://202.114.234.160/jsxsd/xskb/xskb_list.do";

	public static String getJsessionID() {
		Map<String, String> cookies = new HashMap<>();
		Response resp;
		try {
			resp = Jsoup.connect(index).timeout(maxtime).execute();
			cookies = resp.cookies();
			System.out.println(cookies.get("JSESSIONID"));
			return cookies.get("JSESSIONID");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("打开主页超时，获取jsessionid失败");
			return null;
		}

	}

	/**
	 * 
	 * 
	 * @param jsessionid
	 * @return TODO 根据传入的jsessionid获取验证码，存储到本地并以jsessionid命名
	 *
	 */
	public static String getIMG_Addr(String jsessionid) {
		if (DownloadIMG.getImg(jsessionid)) {
			return jsessionid;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	public static JSONObject login(String encoded, String xcode, String jsessionid) throws Exception {
		Document document = Jsoup.connect(loginurl).data("encoded", encoded).data("RANDOMCODE", xcode)
				.cookie("JSESSIONID", jsessionid).timeout(maxtime).post();
		String info = "";
		info = document.getElementById("Top1_divLoginName").html();
		System.out.println(info);
		Document courseTableDoc = Jsoup.connect(courseTable).cookie("JSESSIONID", jsessionid).timeout(maxtime).post();
		Element kb = courseTableDoc.getElementById("kbtable");
		/**
		 * 登录成功，重设jsessionid
		 */
		String ori_html_kb = Jsoup.clean(kb.html(), Whitelist.relaxed());
		JSONObject object = new JSONObject();
		object.put("info", info);
		object.put("data", "暂无");
		return object;
	}

}