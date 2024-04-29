package com.OpenApi.OpenApi;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import javax.mail.Service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

import java.util.stream.Collectors;
import javax.mail.Service;


@Controller
public class ApiController {

    private final APIRepository apiRepository;
    private final FileService fileService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private EntityManager entityManager;

    @Autowired
    public ApiController(APIRepository apiRepository, FileService fileService) {
        this.apiRepository = apiRepository;
        this.fileService = fileService;
    }

    @GetMapping("/apiList")
    public String showApis(Model model, String message) {
        List<API> apiList = apiRepository.findAll();
        System.out.println(apiList);
        model.addAttribute("index", apiList);
        model.addAttribute("message", message); // Add the additional message to the model
        return "table";
    }

    @PostMapping("/details")
    @ResponseBody
    public String showDetails(@RequestParam("selectedRowIds") List<Long> selectedRowIds, Model model) {
        List<API> recordDetailsList = new ArrayList<>();
        for (Long selectedRowId : selectedRowIds) {
            Optional<API> recordDetailsOptional = apiRepository.findById(selectedRowId);
            recordDetailsOptional.ifPresent(recordDetailsList::add);
        }
        model.addAttribute("recordDetailsList", recordDetailsList);
        return "details";
    }

    @GetMapping("/readFile")
    public ResponseEntity<String> readFile() {
        try {
            List<String> fileContents = fileService.readTextFile("ids.txt");
            StringBuilder response = new StringBuilder();
            for (String line : fileContents) {
                // Parse the ID from each line
                int id = Integer.parseInt(line.trim());
                // Fetch data from the database based on the ID
                Optional<API> serviceOptional = apiRepository.findById((long) id);
                if (serviceOptional.isPresent()) {
                    API service = serviceOptional.get();
                    // Append the service data to the response
                    response.append("ID: ").append(service.getId())
                            .append("\tName: ").append(service.getFunction_name())
                            .append("\tPath URL: ").append(service.getPath_url())
                            .append("\tMethod: ").append(service.getMethod())
                            .append("\tRequired: ").append(service.getRequired())
                            .append("\tOutput: ").append(service.getOutput())
                            .append("\tAvailability: ").append(service.getAvailability())
                            .append("\tCost: ").append(service.getCost())
                            .append("\tResponse Time: ").append(service.getResponse_time())
                            .append("\tFunction Description: ").append(service.getFun_description())
                            .append("\n")
                            .append("\n");

                } else {
                    response.append("No service found for ID: ").append(id).append("\n");
                }
            }
            return ResponseEntity.ok().body(response.toString());
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read file");
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Log the exceptio
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid ID format in file");
        }
    }

    @GetMapping("/")
    public String index() {

        return "Form";
    }

    @GetMapping("/ServiceDetails")
    public String searchForAvailability(@RequestParam("availability") String availability,
                                        @RequestParam("service") String service,
                                        @RequestParam("cost") String cost, Model model) {

        long SqlstartTime = System.currentTimeMillis();
        int value = Integer.parseInt(availability);
        System.out.println("value: " + value);
        String serviceValue = service;
        System.out.println("serviceValue: " + serviceValue);
        int costValue = Integer.parseInt(cost);
        System.out.println("costValue: " + costValue);
        // without Indexing start
        List<API> retrievedServiceData = apiRepository.retrievedServiceData(serviceValue, value, costValue);
        long SqlendTime = System.currentTimeMillis();
        long SQlexecutionTime = SqlendTime - SqlstartTime;
        System.out.println("SQlkexecutionTime: " + SQlexecutionTime);
        model.addAttribute("withOutIndexTime", SQlexecutionTime);

        // without Indexing End
        // withIndexing Start
        long IndexPartOneExecutionTime = 0;
        long IndexPartTwoExecutionTime = 0;
        long indexPartOnestartTime = System.currentTimeMillis();
        List<Long> functionIds = apiRepository.findIdsByFunctionName(serviceValue);
        if (functionIds.isEmpty()) {
            return showApis(model, "Your Service is not found So choose one in the Below Table");
        }

        List<Long> availabilityIds = new ArrayList<>();
        List<Long> costIds = new ArrayList<>();
        List<API> availabilityDetails = new ArrayList<>();
        API[] costDetailArray = null;

        try (BufferedReader availabilityReader = new BufferedReader(new FileReader("availabilityId.txt"));
             BufferedReader costReader = new BufferedReader(new FileReader("CostId.txt"))) {
            int lowerRange = (value / 10) * 10;
            int upperRange = lowerRange + 9;
            String line;
            while ((line = availabilityReader.readLine()) != null) {
                String[] parts = line.split(":\\s+");
                String range = parts[0].trim();
                if (range.equals(lowerRange + "-" + upperRange)) {
                    String[] idStrings = parts[1].trim().split(",");
                    for (String idString : idStrings) {
                        Long id = Long.parseLong(idString);
                        if (functionIds.contains(id)) {
                            availabilityIds.add(id);
                            Optional<API> serviceOptional = apiRepository.findById(id);
                            serviceOptional.ifPresent(availabilityDetails::add);
                            System.out.println("availabilityDetails : " +availabilityDetails);
                        }
                    }
                    break;
                }
            }
            long indexPartOneEndTime = System.currentTimeMillis();
            IndexPartOneExecutionTime =   indexPartOneEndTime - indexPartOnestartTime;
            System.out.println("IndexPartOneExecutionTime: " + IndexPartOneExecutionTime);

            long indexPartTwoStartTime = System.currentTimeMillis();
            List<Long> costDetails = new ArrayList<>();
            costDetailArray = null;
            String ranges = getCostRange(costValue);
            while ((line = costReader.readLine()) != null) {
                String[] parts = line.split(":\\s+");
                String range = parts[0].trim();
                if (range.equals(ranges)) {
                    String[] idStrings = parts[1].trim().split(",");
                    for (String idString : idStrings) {
                        Long id = Long.parseLong(idString);
                        if (functionIds.contains(id)) {
                            costIds.add(id);
                            Optional<API> serviceOptional = apiRepository.findById(id);
                            costDetailArray = costDetails.toArray(new API[costDetails.size()]);
                            System.out.println("costDetailArray : " +costDetailArray);

                        }
                    }
                    break;
                }
            }
            long indexPartTwoEndTime = System.currentTimeMillis();
            IndexPartTwoExecutionTime = indexPartTwoEndTime - indexPartTwoStartTime;
            System.out.println("IndexPartTwoExecutionTime: " + IndexPartTwoExecutionTime);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        long IndexfinalexecutionTime =   IndexPartOneExecutionTime - IndexPartTwoExecutionTime;
        System.out.println("IndexfinalexecutionTime: " + IndexfinalexecutionTime);

        model.addAttribute("availabilityDetails", availabilityDetails);
        model.addAttribute("cost", costDetailArray);
        model.addAttribute("indexTime", IndexfinalexecutionTime);

        return "details";
    }


    // Note: The following code will not be executed, as the method returns within the if-else blocks
    // String htmlContent = generateHtmlTable(response.toString());
    // return ResponseEntity.ok().body(htmlContent);


    private String getCostRange(int costValue) {
        if (costValue >= 5 && costValue <= 14) {
            return "5-14";
        } else if (costValue >= 15 && costValue <= 24) {
            return "15-24";
        } else if (costValue >= 25 && costValue <= 34) {
            return "25-34";
        } else if (costValue >= 35 && costValue <= 44) {
            return "35-44";
        } else if (costValue >= 45 && costValue <= 54) {
            return "45-54";
        } else if (costValue >= 55 && costValue <= 64) {
            return "55-64";
        } else if (costValue >= 65 && costValue <= 74) {
            return "65-74";
        } else if (costValue >= 75 && costValue <= 84) {
            return "75-84";
        } else if (costValue >= 85 && costValue <= 94) {
            return "85-94";
        } else {
            return "";
        }
    }


// availability file Generator

    @GetMapping("/covertAvailabilityFile")
    @ResponseBody
    public void fetchDataAndSaveToFile() {
        Map<String, List<Long>> data = new TreeMap<>();
        data.put("50-59", availability(50, 59));
        data.put("60-69", availability(60, 69));
        data.put("70-79", availability(70, 79));
        data.put("80-89", availability(80, 89));
        data.put("90-99", availability(90, 99));

        saveIdsToFile(data);
    }

    private List<Long> availability(int min, int max) {
        String sql = "SELECT id FROM service_list WHERE availability >= ? AND availability <= ?";
        System.out.println("Executing SQL: " + sql + " for range " + min + " - " + max);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, min, max);
        List<Long> ids = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            ids.add((Long) row.get("id"));
        }
        return ids;
    }

    private void saveIdsToFile(Map<String, List<Long>> data) {
        try {
            FileWriter writer = new FileWriter("availabilityId.txt");
            for (Map.Entry<String, List<Long>> entry : data.entrySet()) {
                String availability = entry.getKey();
                List<Long> ids = entry.getValue();
                writer.write(availability + ":\t");
                for (Long id : ids) {
                    writer.write(id.toString() + ",");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Cost File Convert

    @GetMapping("/covertCostFile")
    @ResponseBody
    public void fetchCostWiseDatas(){
        Map<String, List<Long>> data = new TreeMap<>();
        data.put("5-14", Cost(5,14));
        data.put("15-24", Cost(15,24));
        data.put("25-34", Cost(25,34));
        data.put("35-44", Cost(35,44));
        data.put("45-54", Cost(45,54));
        data.put("55-64", Cost(55, 64));
        data.put("65-74", Cost(65, 74));
        data.put("75-84", Cost(75, 84));
        data.put("85-94", Cost(85,94));

        saveIdsToCostData(data);
    }

    private List<Long> Cost(int min, int max) {
        String sql = "SELECT id FROM service_list WHERE cost >= ? AND cost <= ?";
        System.out.println("Executing SQL: " + sql + " for range " + min + " - " + max);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, min, max);
        List<Long> ids = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            ids.add((Long) row.get("id"));
        }
        return ids;
    }

    private void saveIdsToCostData(Map<String, List<Long>> data) {
        try {
            FileWriter writer = new FileWriter("CostId.txt");
            for (Map.Entry<String, List<Long>> entry : data.entrySet()) {
                String availability = entry.getKey();
                List<Long> ids = entry.getValue();
                writer.write(availability + ":\t");
                for (Long id : ids) {
                    writer.write(id.toString() + ",");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}