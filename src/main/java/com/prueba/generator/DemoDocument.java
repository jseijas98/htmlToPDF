package com.prueba.generator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.prueba.document.DocumentGenerator;
import com.prueba.mapper.DataMapper;
import com.prueba.model.Employee;

@RestController
public class DemoDocument {

	@Autowired
	private DocumentGenerator documentGenerator;
	
	@Autowired
	private SpringTemplateEngine springTemplateEngine;
	
	@Autowired
	private DataMapper dataMapper;

	@PostMapping(value = "/generate/document")
	public ResponseEntity<byte[]> generateDocument(@RequestBody List<Employee> employeeList) {
		String finalHtml = null;
		String finalHeader = null;
		String finalfooter = null;

		Context dataContext = dataMapper.setData(employeeList);

		finalHtml = springTemplateEngine.process("template", dataContext);
		finalHeader = springTemplateEngine.process("headerHtml", dataContext);
		finalfooter = springTemplateEngine.process("footer", dataContext);

		byte[] pdfBytes = documentGenerator.htmlToPdf(finalHtml, finalHeader, finalfooter);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "document.pdf");

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}
}
