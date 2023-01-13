/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws.FINAL_EXAM;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpEntity;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ws.FINAL_EXAM.model.Surat;
import ws.FINAL_EXAM.model.SuratJpaController;

/**
 *
 * @author ISA JAISYULLAH HAMID _ 20200140003
 */

@RestController
@CrossOrigin
public class MyController {
    
    Surat data = new Surat();
    SuratJpaController cont = new SuratJpaController();
    
    @GetMapping(value = "/GET", produces = APPLICATION_JSON_VALUE)
    public List<Surat> getData(){
        
        List<Surat> buffer = new ArrayList<>();
        buffer = cont.findSuratEntities();
        
        return buffer;
    }
    
    @PostMapping(value = "/POST", consumes = APPLICATION_JSON_VALUE )
    public String sendData(HttpEntity<String> datasend) throws JsonProcessingException{
        
        String feedback = "Do Nothing";
        
        ObjectMapper maper = new ObjectMapper();
        data = maper.readValue(datasend.getBody(), Surat.class);
        
        Timestamp ts = Timestamp.from(Instant.now());
        
        try {
            data.setTime(ts);
            cont.create(data);
            feedback = data.getJudul() + " Saved";
        } catch (Exception ex) {
            feedback = ex.getMessage();
        }
        return feedback;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
    try {
        byte[] fileBytes = file.getBytes();
        data.setId(10);
        data.setJudul("file");
            data.setFile(fileBytes);
        
        try {
            cont.create(data);
        } catch (Exception ex) {
            Logger.getLogger(MyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ResponseEntity.ok("Successfully uploaded - " + file.getOriginalFilename());
    } catch (IOException e) {
        return ResponseEntity.badRequest().body("Failed to upload - " + file.getOriginalFilename());
    }
}
    
     @PutMapping(value = "/PUT", consumes = APPLICATION_JSON_VALUE )
    public String editData(HttpEntity<String> datasend) throws JsonProcessingException{
        
        String feedback = "Do Nothing";
        
        ObjectMapper maper = new ObjectMapper();
        data = maper.readValue(datasend.getBody(), Surat.class);
        
        Timestamp ts = Timestamp.from(Instant.now());
        try {
            data.setTime(ts);
            cont.edit(data);
            feedback = data.getJudul() + " Edited";
        } catch (Exception ex) {
            feedback = ex.getMessage();
        }
        return feedback;
    }
    
    @DeleteMapping(value = "/DELETE", consumes = APPLICATION_JSON_VALUE )
    public String deleteData(HttpEntity<String> datasend) throws JsonProcessingException{
        
        String feedback = "Do Nothing";
        
        ObjectMapper maper = new ObjectMapper();
        data = maper.readValue(datasend.getBody(), Surat.class);
        
        try {
            cont.destroy(data.getId());
            feedback = data.getId() + " Deleted";
        } catch (Exception ex) {
            feedback = ex.getMessage();
        }
        return feedback;
    }
}
