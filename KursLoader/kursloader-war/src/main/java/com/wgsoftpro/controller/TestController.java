package com.wgsoftpro.controller;

import com.wgsoftpro.DAO.BaseDAO;
import com.wgsoftpro.configuration.ApiVersion;
import com.wgsoftpro.configuration.MyApplicationContext;
import com.wgsoftpro.services.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.sling.commons.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Api(value = "KURSLOADER service", description = " Версия " + ApiVersion.version)
@RequestMapping({"/test"})
public class TestController {
  @Autowired
  TestService testService;

  @ApiOperation(value = "check", notes = "check", httpMethod = "GET")
  @RequestMapping(value = "/check", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String checkMessages(
  ) {
    //BaseDAO baseDAO = new BaseDAO();
    //return baseDAO.getTest();
    return testService.test();
  }

  @ApiOperation(value = "load", notes = "load", httpMethod = "GET")
  @RequestMapping(value = "/loadNBU/{d1}/{d2}", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String loadNBU(@PathVariable String d1, @PathVariable String d2) {
    //BaseDAO baseDAO = new BaseDAO();
    //return baseDAO.getTest();
    return testService.loadNBU(d1,d2);
  }
}
