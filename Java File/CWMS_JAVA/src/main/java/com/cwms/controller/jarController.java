package com.cwms.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cwms.entities.Jar;
import com.cwms.repository.JarRepository;
import com.cwms.service.JarServiceImpliment;
import com.cwms.service.ProcessNextIdService;

import jakarta.transaction.Transactional;

@CrossOrigin("*")
@RequestMapping("/jar")
@RestController
public class jarController {

	@Autowired
	JarServiceImpliment jarServiceImpliment;
	@Autowired
	JarRepository jarRepository;
	@Autowired
	public ProcessNextIdService processSerrvice;

	  @PostMapping("/addJar")
	    public void saveJar(@RequestBody Jar entity) {
	        if (entity.getStatus() == null || entity.getStatus().isEmpty()) {
	            entity.setStatus("N");
	        } else if (entity.getStatus().equals("N") && !entity.getStatus().equals("A")) {
	            entity.setStatus("U");
	        }

	        String nextJDId = processSerrvice.autoIncrementNextJarIdNext();

	        entity.setdate();
	        entity.setJarId(nextJDId);
	        System.out.println(entity);
	        jarServiceImpliment.saveJar(entity);
	        System.out.println(entity);
	    }

	@GetMapping("/list")
	public List<Jar> getlist() {

		return jarServiceImpliment.getlist();
	}

	@DeleteMapping(value = "/delete/{id}")
	@Transactional
	public String deleteJarbyId(@PathVariable("id") String id) {

		Jar jar = jarServiceImpliment.findByJarID(id);
		if (jar != null) {
			jarRepository.delete(jar);
			return "Jar delete suceesfully";
		}
		return "Jar  Not deleteted.";

	}

	@GetMapping(value = "/getJar/{id}")
	public Jar getJarbyId(@PathVariable("id") String id) {
		Jar jar = jarServiceImpliment.findByJarID(id);
		Jar jar1 =saveJarUpdate(jar);

		return jar;
	}
	
    @GetMapping(value = "/getJarForUpdate/{id}")
    public Jar getJarbyIdForUpdatestatus(@PathVariable("id") String id) {
        Jar jar = jarServiceImpliment.findByJarID(id);
        if (!jar.getStatus().equals("A")) {
            jar.setStatus("A");
            return jarServiceImpliment.saveJar(jar);
        } else {
            return jar;
        }
    }

    public Jar saveJarUpdate(Jar entity) {
        if (entity.getStatus() == null || entity.getStatus().isEmpty()) {
            entity.setStatus("N");
        } else if (!entity.getStatus().equals("A")) {
            entity.setStatus("U");
        }
        jarServiceImpliment.saveJar(entity);
        jarRepository.save(entity);
        System.out.println(entity);
        return jarServiceImpliment.saveJar(entity);
    }

}
//jar_Controller.