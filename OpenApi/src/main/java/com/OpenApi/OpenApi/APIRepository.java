package com.OpenApi.OpenApi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



import java.util.List;
import java.util.Optional;

public interface APIRepository extends JpaRepository<API, Long> {

    @Query(value = "SELECT a.id FROM API a WHERE a.function_name = :functionName")
    List<Long> findIdsByFunctionName(@Param("functionName") String functionName);

    @Query(value = "SELECT a FROM API a WHERE a.function_name = :serviceValue AND a.availability < :availability AND a.cost < :cost")
    List<API> retrievedServiceData(@Param("serviceValue") String serviceValue, @Param("availability") int availability, @Param("cost") int cost);

}
