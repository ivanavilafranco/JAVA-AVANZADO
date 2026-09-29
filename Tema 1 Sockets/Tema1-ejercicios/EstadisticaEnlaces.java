package tema1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EstadisticaEnlaces {

    public static void main(String[] args) {
        String direccion = "https://www.realbetisbalompie.es/";

        try {
            URL pagina = new URL(direccion);
            String html = descargarPagina(pagina);
            Map<String, Integer> enlacesPorHost = contarEnlaces(html, pagina);

            int total = 0;
            for (int cantidad : enlacesPorHost.values()) {
                total += cantidad;
            }

            System.out.println("Nº de enlaces por host para el site " + pagina);

            // Ordenamos por cantidad descendente y, en caso de empate, por host.
            List<String> hosts = new ArrayList<>(enlacesPorHost.keySet());
            Collections.sort(hosts, (host1, host2) -> {
                int comparacion = Integer.compare(
                        enlacesPorHost.get(host2),
                        enlacesPorHost.get(host1));

                return comparacion != 0
                        ? comparacion
                        : host1.compareTo(host2);
            });

            for (String host : hosts) {
                int cantidad = enlacesPorHost.get(host);
                double porcentaje = total == 0
                        ? 0
                        : cantidad * 100.0 / total;

                System.out.printf(
                        new Locale("es", "ES"),
                        "%s -> %d/%d (%.2f%%)%n",
                        host, cantidad, total, porcentaje);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String descargarPagina(URL pagina) throws Exception {
        StringBuilder html = new StringBuilder();

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(
                        pagina.openStream(),
                        StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = lector.readLine()) != null) {
                // Un espacio permite tratar etiquetas partidas en varias líneas.
                html.append(linea).append(' ');
            }
        }

        return html.toString();
    }

    private static Map<String, Integer> contarEnlaces(
            String html, URL pagina) {

        Map<String, Integer> resultado = new HashMap<>();

        // Busca href="..." o href='...' únicamente dentro de etiquetas <a>.
        Pattern patron = Pattern.compile(
                "<a\\b[^>]*?\\bhref\\s*=\\s*([\"'])(.*?)\\1[^>]*>",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = patron.matcher(html);

        while (matcher.find()) {
            String href = matcher.group(2).trim();

            boolean absoluta = href.startsWith("http://")
                    || href.startsWith("https://");

            boolean relativa = href.startsWith("/")
                    || href.startsWith("./")
                    || href.startsWith("../");

            if (!absoluta && !relativa) {
                continue;
            }

            try {
                // Si href es relativo, usa la URL de la página como base.
                URL destino = new URL(pagina, href);
                String host = destino.getHost().toLowerCase(Locale.ROOT);

                if (!host.isEmpty()) {
                    resultado.put(
                            host,
                            resultado.getOrDefault(host, 0) + 1);
                }
            } catch (Exception e) {
                // Ignoramos un enlace individual si su URL no es válida.
            }
        }

        return resultado;
    }
}