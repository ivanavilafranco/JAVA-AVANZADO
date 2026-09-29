package tema1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkStats {

	public static void main(String[] args) throws IOException {
		
		String site = "https://www.xataka.com/";
		System.out.println("Nº de enlaces por host para el site " + site);

		URL pageUrl = new URL(site);
		HttpURLConnection conn = (HttpURLConnection) pageUrl.openConnection();
		
		//leer html:
		
		StringBuffer buffHtmlContent;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));) {
			
			buffHtmlContent = new StringBuffer();
			
			String strCurrentLine;
			while ((strCurrentLine = br.readLine()) != null) {
				buffHtmlContent.append(strCurrentLine);
				buffHtmlContent.append(" "); //sustituimos todos los retornos de carro por espacios para trabajar con expresiones regulares
			}
		}
        
        //quitamos html comentado:
        String htmlContent = buffHtmlContent.toString().replaceAll("<!--.*?-->", "");
        
        //buscamos todos los enlaces:
        Pattern linkPattern = Pattern.compile("<a (.+?)>"); 
        Matcher linkMatcher = linkPattern.matcher(htmlContent);
        
        Pattern hrefPattern = Pattern.compile(".*href=\"(.+?)\".*"); 
        
        ArrayList<URL> listaEnlacesComprobar = new ArrayList<URL>();
        
        while (linkMatcher.find()) {
        	String linkContent = linkMatcher.group(0);
        	//System.out.println(linkContent);
        	
        	Matcher hrefMatcher = hrefPattern.matcher(linkContent);
        	if (hrefMatcher.matches())
        	{
        		String href = hrefMatcher.group(1);
        		URL url = null;
        		if (href.startsWith("/") || href.startsWith("./") || href.startsWith("../")) {
        			//enlace absolutos o relativos al mismo portal, convertimos en urls completas.
        			url = new URL(pageUrl, href);
        		} else if (href.startsWith("http://") || href.startsWith("https://")) {
        			url = new URL(href);
        	    }
        		if (url != null) {
        			listaEnlacesComprobar.add(url);
        		}
        	}
        }
        
    	//Calculamos estadísticas por host:
        
        int numEnlaces = listaEnlacesComprobar.size();
        DecimalFormat formatter = new DecimalFormat("##0.00"); //2 decimales
        
        //Almacenamos el número de veces que aparece cada enlace en un Map (clave = url, valor = número de repeticiones).
        
        HashMap<String,Stat> map = new HashMap<String,Stat>();
        
        for (URL url : listaEnlacesComprobar)
        {
        	String key = url.getHost();
        	if (!map.containsKey(key)) {
        		map.put(key,new Stat(key));
        	} else {
        		Stat stat = map.get(key);
        		stat.increment();
        	}
        		
        }
        
         ArrayList<Stat> stats = new ArrayList<Stat>(map.values());

        //ordenamos descendentemente por el número de veces que aparece:
        Collections.sort(stats, (s1,s2) -> s2.getNumVeces() - s1.getNumVeces());
        
        //mostramos
        for (Stat stat : stats) {
        	System.out.println(
					stat.getHost() +
					" -> " + 
					stat.getNumVeces() +
					"/" + numEnlaces +
					" (" +
					formatter.format((stat.getNumVeces()/(double)numEnlaces)*100) +
					"%)");
        }
	}
	

	/**
	 * Almacena las estadísticas de un host
	 * 
	 * 
	 */
	private static class Stat {
		
		private String host;
		private int numVeces = 1;
		
		public String getHost() {
			return host;
		}

		public int getNumVeces() {
			return numVeces;
		}

		public Stat(String host) {
			this.host = host;
		}

		public void increment() {
			numVeces++;
		}
		
	}
	

}