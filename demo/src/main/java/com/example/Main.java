package com.example;

import com.example.controller.HelpDeskController;
import com.example.service.HelpDeskSystem;
import com.example.view.HelpDeskView;

public class Main {
    public static void main(String[] args) {
        HelpDeskSystem model = new HelpDeskSystem();
        HelpDeskView view = new HelpDeskView();
        HelpDeskController controller = new HelpDeskController(model, view);
        controller.iniciar();
    }
}
