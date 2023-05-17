package com.ssafy.tourist.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.tourist.model.AttractionInfoDto;
import com.ssafy.tourist.model.GugunDto;
import com.ssafy.tourist.model.SidoDto;
import com.ssafy.tourist.model.service.TouristService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/tourist")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
@Api("여행지 컨트롤러  API V1")
public class TouristController {
	private static final Logger logger = LoggerFactory.getLogger(TouristController.class);

    private TouristService touristService;

    public TouristController(TouristService touristService) {
		super();
		this.touristService = touristService;
	}
    
//    @GetMapping("/search")
//    public String search(@RequestParam Map<String, Integer> param, Model model) throws SQLException {
//    	List<AttractionInfoDto> list = touristService.listTourist(param);
//    	
//    	model.addAttribute("attractions", list);
//    	
//    	return "tourist/region";
//    }
    
    @GetMapping("")
    public ResponseEntity<?> search() throws SQLException {
    	logger.debug("list sido");
    	
    	List<SidoDto> list = touristService.listSido();
    	return new ResponseEntity<List<SidoDto>>(list, HttpStatus.OK);
    }
    
    @GetMapping("/{sidoCode}")
    public ResponseEntity<?> search(@PathVariable("sidoCode") String sidoCode) throws SQLException {
    	logger.debug("list sido");
    	
    	List<GugunDto> list = touristService.listGugun(sidoCode);
    	return new ResponseEntity<List<GugunDto>>(list, HttpStatus.OK);
    }
    
    @GetMapping("/{sidoCode}/{gugunCode}/{contentTypeId}")
    @ResponseBody
    public ResponseEntity<?> search(@PathVariable("sidoCode") String sidoCode, @PathVariable("gugunCode") String gugunCode, @PathVariable("contentTypeId") String contentTypeId) throws SQLException {
    	logger.debug("search attraction : {}, {}, {}", sidoCode, gugunCode, contentTypeId);
    	
    	Map<String, String> param = new HashMap<String, String>();
    	param.put("sidoCode", sidoCode);
    	param.put("gugunCode", gugunCode);
    	param.put("contentTypeId", contentTypeId);
    	
    	List<AttractionInfoDto> list = touristService.listTourist(param);
    	return new ResponseEntity<List<AttractionInfoDto>>(list, HttpStatus.OK);
    }
    
    @GetMapping("/img/{id}")
    @ResponseBody
    public ResponseEntity<String> searchImgUrl(@PathVariable("id") int id) throws SQLException {
    	logger.debug("search img at : {}", id);
    	
    	String url = touristService.getImageUrl(id);
    	logger.debug("response: ", url);
    	return new ResponseEntity<String>(url, HttpStatus.OK);
    }
}
