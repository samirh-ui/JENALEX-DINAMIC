package com.example.view;

import javax.swing.table.DefaultTableModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class PdfReportExporter {
    private static final DateTimeFormatter FECHA_REPORTE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private PdfReportExporter() {
    }

    static void exportar(Path destino, String titulo, Map<String, DefaultTableModel> tablas) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add(titulo);
        lineas.add("Generado: " + LocalDateTime.now().format(FECHA_REPORTE));
        lineas.add("");

        for (Map.Entry<String, DefaultTableModel> entry : tablas.entrySet()) {
            lineas.add(entry.getKey());
            agregarTabla(lineas, entry.getValue());
            lineas.add("");
        }

        Files.write(destino, crearPdf(lineas));
    }

    private static void agregarTabla(List<String> lineas, DefaultTableModel model) {
        if (model.getRowCount() == 0) {
            lineas.add("Sin datos cargados.");
            return;
        }

        StringBuilder columnas = new StringBuilder();
        for (int columna = 0; columna < model.getColumnCount(); columna++) {
            if (columna > 0) {
                columnas.append(" | ");
            }
            columnas.append(model.getColumnName(columna));
        }
        lineas.add(columnas.toString());

        for (int fila = 0; fila < model.getRowCount(); fila++) {
            StringBuilder valores = new StringBuilder();
            for (int columna = 0; columna < model.getColumnCount(); columna++) {
                if (columna > 0) {
                    valores.append(" | ");
                }
                valores.append(String.valueOf(model.getValueAt(fila, columna)));
            }
            lineas.add(valores.toString());
        }
    }

    private static byte[] crearPdf(List<String> lineas) throws IOException {
        List<String> paginas = crearContenidosPorPagina(lineas);
        List<byte[]> objetos = new ArrayList<>();
        int paginasObjeto = 2;
        int primeraPaginaObjeto = 3;
        int fuenteObjeto = primeraPaginaObjeto + (paginas.size() * 2);

        objetos.add("<< /Type /Catalog /Pages 2 0 R >>".getBytes(StandardCharsets.US_ASCII));

        StringBuilder kids = new StringBuilder();
        for (int i = 0; i < paginas.size(); i++) {
            if (i > 0) {
                kids.append(' ');
            }
            kids.append(primeraPaginaObjeto + (i * 2)).append(" 0 R");
        }
        objetos.add(("<< /Type /Pages /Kids [" + kids + "] /Count " + paginas.size() + " >>")
                .getBytes(StandardCharsets.US_ASCII));

        for (int i = 0; i < paginas.size(); i++) {
            int paginaObjeto = primeraPaginaObjeto + (i * 2);
            int contenidoObjeto = paginaObjeto + 1;
            String pagina = "<< /Type /Page /Parent " + paginasObjeto + " 0 R /MediaBox [0 0 612 792] "
                    + "/Resources << /Font << /F1 " + fuenteObjeto + " 0 R >> >> "
                    + "/Contents " + contenidoObjeto + " 0 R >>";
            objetos.add(pagina.getBytes(StandardCharsets.US_ASCII));

            byte[] contenido = paginas.get(i).getBytes(StandardCharsets.US_ASCII);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(("<< /Length " + contenido.length + " >>\nstream\n").getBytes(StandardCharsets.US_ASCII));
            stream.write(contenido);
            stream.write("\nendstream".getBytes(StandardCharsets.US_ASCII));
            objetos.add(stream.toByteArray());
        }

        objetos.add("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>".getBytes(StandardCharsets.US_ASCII));
        return ensamblarPdf(objetos);
    }

    private static List<String> crearContenidosPorPagina(List<String> lineas) {
        List<String> paginas = new ArrayList<>();
        StringBuilder pagina = new StringBuilder("BT\n/F1 11 Tf\n50 750 Td\n14 TL\n");
        int lineasEnPagina = 0;

        for (String linea : lineas) {
            for (String fragmento : partirLinea(normalizar(linea), 92)) {
                if (lineasEnPagina == 48) {
                    pagina.append("ET");
                    paginas.add(pagina.toString());
                    pagina = new StringBuilder("BT\n/F1 11 Tf\n50 750 Td\n14 TL\n");
                    lineasEnPagina = 0;
                }
                pagina.append('(').append(escapar(fragmento)).append(") Tj\nT*\n");
                lineasEnPagina++;
            }
        }

        pagina.append("ET");
        paginas.add(pagina.toString());
        return paginas;
    }

    private static List<String> partirLinea(String linea, int maximo) {
        List<String> partes = new ArrayList<>();
        if (linea.isEmpty()) {
            partes.add("");
            return partes;
        }

        int inicio = 0;
        while (inicio < linea.length()) {
            int fin = Math.min(inicio + maximo, linea.length());
            partes.add(linea.substring(inicio, fin));
            inicio = fin;
        }
        return partes;
    }

    private static String normalizar(String texto) {
        return texto == null ? "" : texto
                .replace('á', 'a')
                .replace('é', 'e')
                .replace('í', 'i')
                .replace('ó', 'o')
                .replace('ú', 'u')
                .replace('Á', 'A')
                .replace('É', 'E')
                .replace('Í', 'I')
                .replace('Ó', 'O')
                .replace('Ú', 'U')
                .replace('ñ', 'n')
                .replace('Ñ', 'N')
                .replaceAll("[^\\x20-\\x7E]", " ");
    }

    private static String escapar(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)");
    }

    private static byte[] ensamblarPdf(List<byte[]> objetos) throws IOException {
        ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        List<Integer> offsets = new ArrayList<>();
        pdf.write("%PDF-1.4\n".getBytes(StandardCharsets.US_ASCII));

        for (int i = 0; i < objetos.size(); i++) {
            offsets.add(pdf.size());
            pdf.write(((i + 1) + " 0 obj\n").getBytes(StandardCharsets.US_ASCII));
            pdf.write(objetos.get(i));
            pdf.write("\nendobj\n".getBytes(StandardCharsets.US_ASCII));
        }

        int xref = pdf.size();
        pdf.write(("xref\n0 " + (objetos.size() + 1) + "\n").getBytes(StandardCharsets.US_ASCII));
        pdf.write("0000000000 65535 f \n".getBytes(StandardCharsets.US_ASCII));
        for (int offset : offsets) {
            pdf.write(String.format("%010d 00000 n \n", offset).getBytes(StandardCharsets.US_ASCII));
        }
        pdf.write(("trailer\n<< /Size " + (objetos.size() + 1) + " /Root 1 0 R >>\nstartxref\n" + xref
                + "\n%%EOF\n").getBytes(StandardCharsets.US_ASCII));
        return pdf.toByteArray();
    }
}
