/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App.MediFour.MediFour.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("dashboard")
    public String panelAdministrativo(){
        return "panel.html";
    }
}
