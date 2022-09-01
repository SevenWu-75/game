//package com.simple.web.interceptor;
//
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.ModelAndView;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(SpeedBootException.class)
//    public ModelAndView handleException(SpeedBootException e){
//        ModelAndView modelAndView = new ModelAndView("error");
//        modelAndView.addObject("errorMessage", e.getCode().name());
//        return modelAndView;
//    }
//}
