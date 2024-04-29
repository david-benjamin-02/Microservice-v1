package com.OpenApi.OpenApi;

import io.swagger.models.*;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.parser.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.models.properties.Property;
import io.swagger.models.HttpMethod;
import io.swagger.models.Path;
import io.swagger.models.Operation;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
public class OpenApiApplication implements CommandLineRunner {
    @Autowired
    private APIRepository apiRepository;

    public static void main(String[] args) {
        SpringApplication.run(OpenApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        int numberOfDuplicates =15;
        for (int i = 0; i < numberOfDuplicates; i++) {
            Swagger swagger = new SwaggerParser().read("/Api.json");
            if (swagger != null && swagger.getInfo() != null) {
                List<API> apiList = new ArrayList<>();
                processPath(swagger, apiList);
                apiRepository.saveAll(apiList);
            } else {
                System.out.println("Unable to parse OpenAPI file or getInfo() is null.");
            }
        }
    }

    private void processPath(Swagger swagger, List<API> apiList) {
        for (Map.Entry<String, Path> pathEntry : swagger.getPaths().entrySet()) {
            String path = pathEntry.getKey();
            Path pathObject = pathEntry.getValue();

            for (Map.Entry<HttpMethod, Operation> op : pathObject.getOperationMap().entrySet()) {
                API api = new API();
                Random random = new Random();

                api.setPath_url(path.substring(1));
                api.setMethod(op.getKey().toString());
                api.setFunction_name(op.getValue().getOperationId());
                api.setAvailability(String.valueOf(random.nextInt(100) + 1));
                api.setCost(String.valueOf(random.nextInt(100) + 1));
                api.setResponse_time(String.valueOf(random.nextInt(1000) + 1));
                api.setFun_description(op.getValue().getDescription());
                List<ParameterInfo> parameters = new ArrayList<>();

                for (Parameter p : op.getValue().getParameters()) {
                    System.out.println("Inside In Loop");
                    String name = p.getName();
                    Boolean required = p.getRequired();
                    String parameterIn = p.getIn();

//                    Property schema = p.getSchema(); // Get the schema
                    String type = null; // Retrieve type from the schema



                    System.out.println("parameterName" + name);
                    System.out.println("parameterRequired" + required);
                    System.out.println("parameterParameterIn" + parameterIn);
                    System.out.println("parameterType" + null);

                    ParameterInfo parameterInfo = new ParameterInfo(name, required, parameterIn, type);
                    parameters.add(parameterInfo);
                }

                api.setParameters(parameters);
                apiList.add(api);
            }
        }
    }
    }



