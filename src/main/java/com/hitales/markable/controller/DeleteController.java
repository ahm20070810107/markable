package com.hitales.markable.controller;

import com.hitales.markable.Exception.BadRequestException;
import com.hitales.markable.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping(value = "/{fileName}")
    public String deleteFile(@PathVariable String fileName){

        deleteService.deleteFileData(fileName);
        return String.format("Delete file Data %s OK!",fileName);
    }
}
