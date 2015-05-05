package com.domain.pinyin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.helper.StringUtil;

import com.domain.constvalue.DomainConst;
import com.domain.constvalue.DomainUtil;
import com.domain.constvalue.FileProxy;
import com.domain.constvalue.Struct.DomainWhois;

public class pinyinHelper {
	final String pina [] = {"ang","an","ao","ai","a"};
	final String pinb [] = {"bang","beng","bing","bian","bai","ban","bao","bei","ben","bie","bin","bi","bo","bu","ba"};	
	final String pinc [] = {"chuang","chang","cheng","chong","chuan","chuai","ceng","cang","cong","chai","chan","chao","cuan","chen","chou","chua","chui","chun","chuo","chi","cou","chu","che","cha","cui","cun","cuo","can","cai","cen","cao","ca","ce","cu","ci"};
	final String pind [] = {"dang","deng","dian","diao","ding","dong","duan","dia","dai","dao","die","den","diu","dei","dou","dan","dui","dun","duo","di","de","da","du"};	
	final String pine [] = {"eng","en","ei","er","e"};
	final String pinf [] = {"fang","feng","fei","fen","fan","fou","fo","fa","fu"};
	final String ping [] = {"guang","geng","gong","guai","guan","gang","gan","gai","gou","gua","gao","gei","gen","gui","gun","guo","ga","gu","ge"};
	final String pinh [] = {"huang","heng","hong","huai","huan","hang","han","hai","hou","hua","hao","hei","hen","hui","hun","huo","ha","hu","he"};
	final String pini [] = {};
	final String pinj [] = {"jiang","jiong","jiao","jing","jian","juan","jia","jie","jiu","jin","jue","jun","ju","ji"};
	final String pink [] = {"kuang","keng","kong","kuai","kuan","kang","kai","kou","kua","kao","ken","kan","kui","kun","kuo","ke","ku","ka"};
	final String pinl [] = {"liang","leng","lian","lang","liao","ling","long","luan","lao","lei","lie","lin","lan","liu","lia","lou","lai","lue","l¨¹e","lun","luo","le","li","la","lu","l¨¹"};
	final String pinm [] = {"mang","meng","mian","miao","ming","men","man","mai","mao","mie","min","mei","miu","mou","ma","mi","mo","me","mu"};
	final String pinn [] = {"niang","neng","nian","nang","niao","ning","nong","nuan","nao","nei","nie","nin","nen","niu","nan","nai","n¨¹e","nuo","nun","nu","n¨¹","na","ni","ne"};
	final String pino [] = {"ou","o"};
	final String pinp [] = {"pang","peng","pian","piao","ping","pen","pan","pai","pao","pie","pin","pei","pou","pa","po","pi","pu"};
	final String pinq [] = {"qiang","qiong","qiao","qing","qian","quan","qia","qie","qiu","qin","que","qun","qu","qi"};
	final String pinr [] = {"rang","reng","rong","ruan","ran","rao","rou","ren","rui","run","ruo","ri","ru","re"};
	final String pins [] = {"shuang","sheng","shuai","shuan","shang","shao","shei","shen","seng","shou","shua","shai","shan","sang","shui","shun","shuo","song","suan","she","sha","sai","sao","shi","sen","shu","sou","san","sui","sun","suo","si","se","sa","su"};
	final String pint [] = {"tang","teng","tian","tiao","ting","tong","tuan","tie","tan","tai","tou","tao","tui","tun","tuo","te","ta","ti","tu"};
	final String pinu [] = {};
	final String pinv [] = {};
	final String pinw [] = {"wang","weng","wai","wei","wen","wan","wa","wo","wu"};
	final String pinx [] = {"xiang","xiong","xiao","xing","xian","xuan","xia","xie","xiu","xin","xue","xun","xu","xi"};
	final String piny [] = {"yang","ying","yong","yuan","yan","yao","you","yin","yue","yun","ya","yu","ye","yo","yi"};
	final String pinz [] = {"zhuang","zheng","zhong","zhuai","zhuan","zhang","zhei","zhen","zeng","zhai","zhou","zhua","zhan","zang","zhao","zhui","zhun","zhuo","zong","zuan","zen","zhe","zan","zha","zai","zhi","zao","zei","zou","zhu","zui","zun","zuo","zi","ze","za","zu"};

	
	int mindex = 0;
	Map<String, String []> pin = new HashMap<String, String []>();

	public void init() {
		pin.put("a", pina);
		pin.put("b", pinb);
		pin.put("c", pinc);
		pin.put("d", pind);
		pin.put("e", pine);
		pin.put("f", pinf);
		pin.put("g", ping);
		pin.put("h", pinh);
		pin.put("i", pini);
		pin.put("j", pinj);
		pin.put("k", pink);
		pin.put("l", pinl);
		pin.put("m", pinm);
		pin.put("n", pinn);
		pin.put("o", pino);
		pin.put("p", pinp);
		pin.put("q", pinq);
		pin.put("r", pinr);
		pin.put("s", pins);
		pin.put("t", pint);
		pin.put("u", pinu);
		pin.put("v", pinv);
		pin.put("w", pinw);
		pin.put("x", pinx);
		pin.put("y", piny);
		pin.put("z", pinz);
	}

	public boolean isPin(String str, List<String> p) {
		boolean res = true;
		int len = str.length();
		int index=0;
		int pincount = 0;
		while (index<len&&res) {
			String firstch = str.substring(index, index+1);
			String [] pl = pin.get(firstch);
			if (pl==null) {
				res = false;
				break;
			}
			String pvalue = getPinValue(pl, str.substring(index));
			p.add(pvalue);
			pincount++;
			res = pvalue.length()>0 && pincount<=3;
			index+=pvalue.length();
		}
		return res;
	}
	
	
	public String getHan(List<String> sp) {

		int i = sp.size();
		StringBuilder sb = new StringBuilder();
		String[] h0 = Han.han.get(sp.get(0));
		String[] j0 = Han.han.get(sp.get(1));
		
		
		for(String h:h0) {
			for (String j:j0) {
				sb.append(h);
				sb.append(j);
				if (i==3) {
					String[] q0 = Han.han.get(sp.get(2));
					for (String q:q0) {
						sb.append(q);
						sb.append(" ");
					}
				}
				else 
					sb.append(" ");
			}
		}

		return sb.toString();
	}
	
	private String getPinValue(String [] pl, String pre) {
		
		for (String pin : pl) {
			int len = pin.length();
			if (len <= pre.length()) {
				if (pre.substring(0, len).equals(pin)) 
					return pin;
			}
		}
		
		return "";
	}
	
	static public void main(String[] argu) {
		FileProxy fp = new FileProxy();
		List<String> dl = new ArrayList<String>();
		boolean fx = fp.init("./pendingdelete/7-24-2014.txt");
		fp.getNext(1, dl);
		pinyinHelper helper = new pinyinHelper();
		helper.init();

		for (String d : dl) {
			String str = DomainUtil.getpreDomain(d);
			String suffix = DomainUtil.getSuffDomin(d);
			List<String> sp = new ArrayList<String>();
			if (suffix.equals("com")/*||suffix.equals("net")*/) {
				
				boolean res = helper.isPin(str, sp);
				if (res) {
					String x ="";
					if (sp.size()<=2)
						x= helper.getHan(sp);
					System.out.println(d+":"+x);
				} else if (str.length()<=6 && StringUtil.isNumeric(str)) {
					System.out.println(d+":");
				}
				sp.clear();
			}
		}
	}
}
