package it.uniroma3.vestiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.uniroma3.vestiti.service.TagliaService;

@Controller
public class TagliaController {

	@Autowired
	TagliaService tagliaService;
	
}
