package com.tax;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tax")
public class TaxController {
    @Autowired
    ObjectMapper mapper;

    @GetMapping
    public ResponseEntity<String> dummy() throws JsonProcessingException {
        Map<Long, TaxDTO> dummy = Map.of(
                1L, new TaxDTO(1.0, 1.0),
                2L, new TaxDTO(10.0, 20.0)
        );

        return ResponseEntity.ok(mapper.writeValueAsString(dummy));
    }
}