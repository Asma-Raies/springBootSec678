package com.asma.makeUp.controllers;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asma.makeUp.entities.MakeUp;
import com.asma.makeUp.entities.Marque;
import com.asma.makeUp.service.MakeUpService;

import jakarta.validation.Valid;

@Controller
public class MakeUpController {
	@Autowired
	MakeUpService makeUpService;
	@RequestMapping("/showCreate")
	public String showCreate(ModelMap modelMap)
	{
		List<Marque> cats = makeUpService.getAllMarques();
		Marque cat = new Marque();
		MakeUp make = new MakeUp();
		make.setMarque(cat);
		cat = cats.get(0);
	modelMap.addAttribute("makeUp", make);
	modelMap.addAttribute("mode", "new");
	modelMap.addAttribute("marque", cats);
	return "formMakeUp";
	}
	@RequestMapping("/saveMakeUp")
	public String saveMakeUp(@Valid MakeUp makeUp,BindingResult bindingResult)
	{
		
		if (bindingResult.hasErrors()) 
	return "formMakeUp";
		
		makeUpService.saveMakeUp(makeUp);
		return "formMakeUp";
		
	}
	@RequestMapping("/ListeMakeUp")
	public String listeMakeUp(ModelMap modelMap,
	@RequestParam (name="page",defaultValue = "0") int page,
	@RequestParam (name="size", defaultValue = "2") int size)
	{
	Page<MakeUp> prods =makeUpService.getAllMakeUpParPage(page, size);
	modelMap.addAttribute("makeUp", prods);
	 modelMap.addAttribute("pages", new int[prods.getTotalPages()]);
	modelMap.addAttribute("currentPage", page);
	return "listeMakeUp";
	}

	@RequestMapping("/supprimerMakeUp")
	public String supprimerMakeUp(@RequestParam("id") Long id,
	ModelMap modelMap,
	@RequestParam (name="page",defaultValue = "0") int page,
	@RequestParam (name="size", defaultValue = "2") int size)
	{
		makeUpService.deleteMakeUpById(id);
	Page<MakeUp> prods = makeUpService.getAllMakeUpParPage(page, 
	size);
	modelMap.addAttribute("makeUp", prods);
	modelMap.addAttribute("pages", new int[prods.getTotalPages()]);
	modelMap.addAttribute("currentPage", page);
	modelMap.addAttribute("size", size);
	return "listeMakeUp";
	}

	@RequestMapping("/modifierMakeUp")
	public String editerProduit(@RequestParam("id") Long id,ModelMap modelMap)
	{
		MakeUp p= makeUpService.getMakeUp(id);
		List<Marque> marq = makeUpService.getAllMarques();
	modelMap.addAttribute("makeUp", p);
	modelMap.addAttribute("mode", "edit");
	modelMap.addAttribute("marque", marq);
	return "formMakeUp";
	}

	@RequestMapping("/updateMakeUp")
	public String updateProduit(@ModelAttribute("produit") MakeUp makeUp,
	@RequestParam("date") String date,
	ModelMap modelMap) throws ParseException 
	{
	//conversion de la date 
	 SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	 Date dateCreation = dateformat.parse(String.valueOf(date));
	 makeUp.setDateCreation(dateCreation);
	 
	 makeUpService.updateMakeUp(makeUp);
	 List<MakeUp> prods = makeUpService.getAllMakeUp();
	 modelMap.addAttribute("makeUp", prods);
	return "listeMakeUp";
	}
}
