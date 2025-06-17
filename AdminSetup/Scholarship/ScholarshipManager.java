package AdminSetup.Scholarship;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//
public class ScholarshipManager {

    private static final String FILE_NAME = "allscholarships.txt";

    public static List<ScholarshipApplicationData> loadAllScholarships() {
        List<ScholarshipApplicationData> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length == 20) {
                    list.add(new ScholarshipApplicationData(parts));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
//public class ScholarshipManager {
//
//    private static final String FILE_NAME = "allscholarships.txt";
//
//    public static List<ScholarshipApplicationData> loadAllScholarships() {
//        List<ScholarshipApplicationData> list = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split("\\|", -1);
//                if (parts.length >= 20) {
//                    list.add(new ScholarshipApplicationData(parts));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//}


