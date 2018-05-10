package com.hitales.markable.controller;

import com.hitales.markable.Exception.BadRequestException;
import com.hitales.markable.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.awt.SunHints;

/**
 * Created with IntelliJ IDEA
 * User:huangming
 * Date:2018/5/10
 * Time:上午11:37
 */
@RestController
@RequestMapping("/delete")
public class DeleteController {

    @Autowired
    private DeleteService deleteService;

    @DeleteMapping(value = "deleteFile")
    public String deleteFile(@RequestParam(value = "fileName") String fileName){

        deleteService.deleteFileData(fileName);
        return String.format("Delete file Data %s OK!",fileName);
    }
}
