package com.OpenApi.OpenApi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import java.util.List;

import javax.annotation.processing.Generated;

@Entity
@Getter
@Setter
@Table(name = "service_list")
public class
API {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native", parameters = @org.hibernate.annotations.Parameter(name = "hibernate.id.new_generator_mappings", value = "false"))
    @Column(name = "id")
    private Long id;

    private  String path_url;
    private  String method;
    private  String function_name;
    private  String required;
    private  String output;
    private  String availability;
    private  String cost;
    private  String response_time;
    private String fun_description;

    @Setter
    @ElementCollection
    private List<ParameterInfo> parameters;


    public void addAttribute(String apis, List<API> apiList) {
    }


}
