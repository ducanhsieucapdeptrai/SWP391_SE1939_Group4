package util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.Statistic;
import model.MaterialStatistic;
import java.text.SimpleDateFormat;

public class CsvExportUtil {

    private static String escapeCsvData(Object data) {
        if (data == null) {
            return "";
        }
        String stringData = data.toString();
        // Quote the data if it contains a comma, a quote, or a newline
        if (stringData.contains(",") || stringData.contains("\"") || stringData.contains("\n")) {
            // Escape quotes by doubling them
            stringData = stringData.replace("\"", "\"\"");
            return "\"" + stringData + "\"";
        }
        return stringData;
    }

    private static String convertToCsvRow(String[] data) {
        return Stream.of(data)
          .map(CsvExportUtil::escapeCsvData)
          .collect(Collectors.joining(","));
    }

    public static void exportInventoryToCsv(List<Statistic> statistics, PrintWriter writer) throws IOException {
        // Write UTF-8 BOM for Excel compatibility with Vietnamese characters
        writer.write('\uFEFF');

        // Header
        writer.println(convertToCsvRow(new String[]{"Mã vật tư", "Tên vật tư", "Tồn đầu kỳ", "Tổng nhập", "Tổng xuất", "Tồn cuối kỳ"}));

        // Data rows
        if (statistics != null) {
            for (Statistic stat : statistics) {
                String[] row = {
                    String.valueOf(stat.getMaterialId()),
                    stat.getMaterialName(),
                    String.valueOf(stat.getInitialStock()),
                    String.valueOf(stat.getTotalImported()),
                    String.valueOf(stat.getTotalExported()),
                    String.valueOf(stat.getFinalStock())
                };
                writer.println(convertToCsvRow(row));
            }
        }
    }

    public static void exportMaterialStatisticsToCsv(String reportType, List<MaterialStatistic> statistics, PrintWriter writer) throws IOException {
        // Write UTF-8 BOM
        writer.write('\uFEFF');

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // Header
        String[] header;
        if ("import".equals(reportType)) {
            header = new String[]{"Mã vật tư", "Tên vật tư", "Danh mục", "Danh mục con", "Ngày nhập", "Số lượng", "Người thực hiện", "Ghi chú"};
        } else { // export
            header = new String[]{"Mã vật tư", "Tên vật tư", "Danh mục", "Danh mục con", "Ngày xuất", "Số lượng", "Người thực hiện", "Ghi chú"};
        }
        writer.println(convertToCsvRow(header));

        // Data rows
        if (statistics != null) {
            for (MaterialStatistic stat : statistics) {
                String[] row = {
                    String.valueOf(stat.getMaterialId()),
                    stat.getMaterialName(),
                    stat.getCategoryName(),
                    stat.getSubCategoryName(),
                    stat.getTransactionDate() != null ? dateFormat.format(stat.getTransactionDate()) : "",
                    String.valueOf(stat.getQuantity()),
                    stat.getPerformedBy(),
                    stat.getNote() != null ? stat.getNote() : ""
                };
                writer.println(convertToCsvRow(row));
            }
        }
    }
}
