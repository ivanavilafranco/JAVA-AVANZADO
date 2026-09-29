package tema1;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
public class HttpExample {
public static void main(String[] args) throws Exception  {
URL url = new URL("https://ktvconstantina.es");
HttpURLConnection con = (HttpURLConnection) url.openConnection();
int responseCode = con.getResponseCode();
System.out.println("Response Code: " + responseCode);
System.out.println("=================");
System.out.println("Response Headers:");
System.out.println();
Map<String, List<String>> responseHeaders = con.getHeaderFields();
responseHeaders.entrySet().stream().forEach(
e -> System.out.println(e.getKey() + ": " + 
e.getValue().stream().findFirst().get()));
try(
BufferedReader in = new BufferedReader(new InputStreamReader(
con.getInputStream()));
){
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
//response.append(inputLine);
response.append(inputLine).append(System.lineSeparator());
}
System.out.println("=================");
System.out.println("Response:");
System.out.println();
System.out.println(response.toString());
}
}
}
