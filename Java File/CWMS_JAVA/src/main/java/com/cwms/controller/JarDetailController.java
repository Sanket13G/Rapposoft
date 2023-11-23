package com.cwms.controller;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cwms.entities.JarDetail;
import com.cwms.repository.JarDetailRepository;
import com.cwms.service.JarDetailService;
import com.cwms.service.ProcessNextIdService;

import jakarta.transaction.Transactional;

@CrossOrigin("*")
@RequestMapping("/jardetail")
@RestController
public class JarDetailController {

	@Autowired
	JarDetailService jarDetailService;

	@Autowired
	JarDetailRepository jarDetailRepository;

	@Autowired
	public ProcessNextIdService processSerrvice;

	@PostMapping(value = "/add")
	public JarDetail saveJarDetail(@RequestBody JarDetail entity) {
		entity.setStatus("N");
		entity.setCurrentDate();
		if (entity.getJarDtlId() == null) {
			entity = jarDetailService.saveJarDetail(entity);
			System.out.println(entity);
			return entity;
		} else
			return jarDetailService.saveJarDetail(entity);
	}
	 @PostMapping(value = "/addUpdateStatus")
	    public JarDetail saveJarDetailApp(@RequestBody JarDetail entity) {
	        if (entity.getStatus().equals("N")) {
	            entity.setStatus("U");
	        } else if (entity.getStatus().equals("A")) {
	            // If status is already "A", don't allow changing to "U"
	            entity.setStatus("A");
	        }

	        return jarDetailService.saveJarDetail(entity);
	    }

	@GetMapping(value = "/list")
	public List<JarDetail> getMethodName() {
		return jarDetailService.list();

	}

	@GetMapping(value = "/jarIdList/{jarId}/{cid}")
	public List<JarDetail> getjarDtl(@PathVariable("jarId") String jarId,@PathVariable("cid") String cid) {
		System.out.println(jarId);

		List<JarDetail> jarDetails = jarDetailService.listByJarId(jarId,cid);
		for (JarDetail j : jarDetails) {

			jarDetailRepository.save(j);
		}

		return jarDetailService.listByJarId(jarId,cid);
	}

    @GetMapping(value = "/jarIdListUStatus/{jarId}/{cid}")
    public List<JarDetail> getjarDtlUpdateStatus(@PathVariable String jarId,@PathVariable("cid") String cid) {
        System.out.println(jarId);

        List<JarDetail> jarDetails = jarDetailService.listByJarId(jarId,cid);
        System.out.println(jarDetails);
        for (JarDetail entity : jarDetails) {
            if (entity.getStatus() == null || entity.getStatus().isEmpty()) {
                entity.setStatus("N");
            } else {
                entity.setStatus("A");
            }
            jarDetailRepository.save(entity);
        }
        System.out.println(jarDetails);

        List<JarDetail> list = jarDetailService.listByJarId(jarId,cid);

        return list;
    }

	@DeleteMapping(value = "/delete/{id}")
	@Transactional
	public void getjaJarDetailList(@PathVariable("id") String param) {
		System.out.println(param);
		JarDetail entity = jarDetailService.findJarDetail(param);
		entity.setStatus("D");
		jarDetailRepository.save(entity);
	}
	
	
	@GetMapping("/dgdcstatus/{cid}")
	public List<JarDetail> getDGDCdata(@PathVariable("cid") String cid){
		return jarDetailRepository.findDataByID(cid);
	}

}