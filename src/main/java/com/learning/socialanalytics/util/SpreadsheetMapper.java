package com.learning.socialanalytics.util;

import com.learning.socialanalytics.dto.MetricExportRow;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.usermodel.Row;

public final class SpreadsheetMapper {
  private static final List<Class<?>> ALLOWED_TYPES =
      List.of(String.class, long.class, Long.class, int.class, Integer.class);
  private static final Set<Class<?>> ALLOWED_MODELS = Set.of(MetricExportRow.class);

  private SpreadsheetMapper() {}

  public static void writeHeaders(Row row, Class<?> modelType) {
    List<Field> fields = exportableFields(modelType);
    for (int index = 0; index < fields.size(); index++) {
      row.createCell(index)
          .setCellValue(fields.get(index).getAnnotation(SpreadsheetColumn.class).value());
    }
  }

  public static void writeValues(Row row, Object model) {
    List<Field> fields = exportableFields(model.getClass());
    for (int index = 0; index < fields.size(); index++) {
      Field field = fields.get(index);
      try {
        field.setAccessible(true);
        Object value = field.get(model);
        if (value instanceof Number number)
          row.createCell(index).setCellValue(number.doubleValue());
        else row.createCell(index).setCellValue(value == null ? "" : value.toString());
      } catch (IllegalAccessException exception) {
        throw new IllegalArgumentException("Spreadsheet field could not be read", exception);
      }
    }
  }

  private static List<Field> exportableFields(Class<?> type) {
    if (!ALLOWED_MODELS.contains(type)) {
      throw new IllegalArgumentException("This model is not allow-listed for spreadsheet export");
    }
    return Arrays.stream(type.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(SpreadsheetColumn.class))
        .filter(field -> ALLOWED_TYPES.contains(field.getType()))
        .sorted(
            Comparator.comparingInt(field -> field.getAnnotation(SpreadsheetColumn.class).order()))
        .toList();
  }
}
